package com.octagon.crazygui.idea;

import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileAdapter;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.util.messages.MessageBusConnection;
import com.octagon.crazygui.idea.actions.ConfigCompilerDialogue;
import com.octagon.crazygui.idea.util.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
        new GuiRenderer().render();
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

        runFormatter(guiFile);
        reloadEditor(guiFile);
        runCXMLCompiler(guiFile);
    }

    private void reloadEditor(VirtualFile file) {
        FileEditor[] editors = FileEditorManager.getInstance(project).getEditors(file);
        for(FileEditor editor : editors) {
            if(editor instanceof CXMLGuiEditor)
                ((CXMLGuiEditor) editor).reload();
        }
    }

    private void runFormatter(VirtualFile file) {
        ReformatCodeProcessor formattingProcessor = new ReformatCodeProcessor(project, PsiManager.getInstance(project).findFile(file), null, false);
        formattingProcessor.run();
    }

    public void runCXMLCompiler(final VirtualFile grammarFile) {
        String title = "CrazyGUI Code Generation";
        Task gen = new RunCompilerOnXMLFile(grammarFile, project, title, true, false);
        ProgressManager.getInstance().run(gen);
    }

    private VirtualFileAdapter getFileDocumentManagerAdapter() {
        return new VirtualFileAdapter() {
            @Override
            public void contentsChanged(@NotNull VirtualFileEvent event) {
                for (Project project : ProjectManager.getInstance().getOpenProjects()) {
                    PsiFile psiFile = PsiManager.getInstance(project).findFile(event.getFile());
                    /*
                     * The psi files seems to be shared between projects, so we need to check if the file is physically
                     * in that project before reformating, or else the file is formatted twice and intellij will ask to
                     * confirm unlocking of non-project file in the other project (very annoying).
                     */
                    if (null != psiFile && FileUtils.isPsiFilePhysicallyInProject(project, psiFile)) {
                        ReformatCodeProcessor formattingProcessor = new ReformatCodeProcessor(project, psiFile, null, false);
                        formattingProcessor.run();

                        FileEditor[] editors = FileEditorManager.getInstance(project).getEditors(psiFile.getVirtualFile());
                        for(FileEditor editor : editors) {
                            if(editor instanceof CXMLGuiEditor)
                                ((CXMLGuiEditor) editor).reload();
                        }

                        if(!ConfigCompilerDialogue.getBooleanProp(project, ConfigCompilerDialogue.PROP_COMPILE_ON_SAVE, false)) continue;
                        RunCompilerOnXMLFile processor = new RunCompilerOnXMLFile(psiFile.getVirtualFile(), project, "CrazyGUI code compiler", true, false);
                        ProgressManager.getInstance().run(processor);
                    }
                }
            }


            public void beforeFileContentReload(VirtualFile file, @NotNull Document document) {
                for (Project project : ProjectManager.getInstance().getOpenProjects()) {
                    PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
                    /*
                     * The psi files seems to be shared between projects, so we need to check if the file is physically
                     * in that project before reformating, or else the file is formatted twice and intellij will ask to
                     * confirm unlocking of non-project file in the other project (very annoying).
                     */
                    if (null != psiFile && FileUtils.isPsiFilePhysicallyInProject(project, psiFile)) {
                        ReformatCodeProcessor formattingProcessor = new ReformatCodeProcessor(project, psiFile, null, false);
                        formattingProcessor.run();

                        FileEditor[] editors = FileEditorManager.getInstance(project).getEditors(psiFile.getVirtualFile());
                        for(FileEditor editor : editors) {
                            if(editor instanceof CXMLGuiEditor)
                                ((CXMLGuiEditor) editor).reload();
                        }

                        if(!ConfigCompilerDialogue.getBooleanProp(project, ConfigCompilerDialogue.PROP_COMPILE_ON_SAVE, false)) continue;
                        RunCompilerOnXMLFile processor = new RunCompilerOnXMLFile(psiFile.getVirtualFile(), project, "CrazyGUI code compiler", true, false);
                        ProgressManager.getInstance().run(processor);
                    }
                }
            }


            public void fileContentReloaded(@NotNull VirtualFile file, @NotNull Document document) {
                for(Project project : ProjectManager.getInstance().getOpenProjects()) {
                    PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);

                    if(psiFile != null && FileUtils.isPsiFilePhysicallyInProject(project, psiFile)) {
                        ReformatCodeProcessor formattingProcessor = new ReformatCodeProcessor(project, psiFile, null, false);
                        formattingProcessor.run();

                        FileEditor[] editors = FileEditorManager.getInstance(project).getEditors(psiFile.getVirtualFile());
                        for(FileEditor editor : editors) {
                            if(editor instanceof CXMLGuiEditor)
                                ((CXMLGuiEditor) editor).reload();
                        }
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
