package com.octagon.crazygui.idea;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManagerAdapter;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileAdapter;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.util.messages.MessageBusConnection;
import com.octagon.crazygui.idea.actions.ConfigCompilerDialogue;
import com.octagon.crazygui.idea.util.PsiFiles;
import org.jetbrains.annotations.NotNull;

public class MainComponent implements ProjectComponent {
    public boolean projectIsClosed = false;
    public static final Logger LOG = Logger.getInstance("CrazyGUI MainComponent");
    public Project project;
    private MyVirtualFileAdapter myVirtualFileAdapter = new MyVirtualFileAdapter();

    public MainComponent(Project project) {
        this.project = project;
    }

    public static MainComponent getInstance(Project project) {
        return project.getComponent(MainComponent.class);
    }

    @Override
    public void projectOpened() {
        installListeners();
        new CXMLTagNameCompletion();
    }

    @Override
    public void projectClosed() {
        LOG.info("projectClosed " + project.getName());
        //synchronized ( shutdownLock ) { // They should be called from EDT only so no lock
        projectIsClosed = true;
        uninstallListeners();
        project = null;
    }

    public void uninstallListeners() {
        VirtualFileManager.getInstance().removeVirtualFileListener(myVirtualFileAdapter);
        MessageBusConnection msgBus = project.getMessageBus().connect(project);
        msgBus.disconnect();
    }

    public void installListeners() {
        LOG.info("installListeners " + project.getName());
        // Listen for .cxml file saves
        VirtualFileManager.getInstance().addVirtualFileListener(myVirtualFileAdapter);
    }

    public void initComponent() {
    }

    public void guiFileSavedEvent(VirtualFile guiFile) {
        LOG.info("guiFileSavedEvent " + guiFile.getPath() + " " + project.getName());

        runCXMLCompiler(guiFile);
    }

    public void runCXMLCompiler(final VirtualFile grammarFile) {
        String title = "CrazyGUI Code Generation";
        Task gen = new RunCompilerOnXMLFile(grammarFile, project, title, true, false);
        ProgressManager.getInstance().run(gen);
    }

    private FileDocumentManagerAdapter getFileDocumentManagerAdapter() {
        return new FileDocumentManagerAdapter() {
            @Override
            public void beforeFileContentReload(VirtualFile file, @NotNull Document document) {
                for (Project project : ProjectManager.getInstance().getOpenProjects()) {
                    if(!ConfigCompilerDialogue.getBooleanProp(project, ConfigCompilerDialogue.PROP_COMPILE_ON_SAVE, false)) continue;
                    PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
                    /*
                     * The psi files seems to be shared between projects, so we need to check if the file is physically
                     * in that project before reformating, or else the file is formatted twice and intellij will ask to
                     * confirm unlocking of non-project file in the other project (very annoying).
                     */
                    if (null != psiFile && PsiFiles.isPsiFilePhysicallyInProject(project, psiFile)) {
                        RunCompilerOnXMLFile processor = new RunCompilerOnXMLFile(psiFile.getVirtualFile(), project, "CrazyGUI code compiler", true, false);
                        ProgressManager.getInstance().run(processor);
                    }
                }
            }
        };
    }

    public void disposeComponent() { }

    @NotNull
    public String getComponentName() {
        return "CrazyGUI.Main";
    }

    private class MyVirtualFileAdapter extends VirtualFileAdapter {
        @Override
        public void contentsChanged(VirtualFileEvent event) {
            final VirtualFile vFile = event.getFile();
            if ( !vFile.getName().endsWith(".cxml") ) return;
            if ( !projectIsClosed ) guiFileSavedEvent(vFile);
        }
    }
}
