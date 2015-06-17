package com.octagon.crazygui.idea.actions;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.octagon.crazygui.idea.RunCompilerOnXMLFile;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ForceCompileAction extends AnAction {
    public static final Logger LOG = Logger.getInstance("CrazyGUI ForceCompileAction");

    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        if ( project==null ) {
            LOG.error("actionPerformed no project for " + e);
            return; // whoa!
        }
        VirtualFile guiFile = getGuiFileFromEvent(e);
        LOG.info("actionPerformed "+(guiFile==null ? "NONE" : guiFile));
        if ( guiFile==null ) return;
        String title = "CrazyGUI Code Generation";

        // commit changes to PSI and file system
        PsiDocumentManager psiMgr = PsiDocumentManager.getInstance(project);
        FileDocumentManager docMgr = FileDocumentManager.getInstance();
        Document doc = docMgr.getDocument(guiFile);
        if ( doc==null ) return;

        boolean wasStale = !psiMgr.isCommitted(doc) || docMgr.isDocumentUnsaved(doc);
        if ( wasStale ) {
            // save event triggers CrazyGUI compiler run if autogen on
            psiMgr.commitDocument(doc);
            docMgr.saveDocument(doc);
        }

        RunCompilerOnXMLFile gen = new RunCompilerOnXMLFile(guiFile, project, title, true, true);
        boolean compileOnSave = ConfigCompilerDialogue.getBooleanProp(project, ConfigCompilerDialogue.PROP_COMPILE_ON_SAVE, false);

        if (!wasStale || !compileOnSave) {
            // if everything already saved (!stale) then run ANTLR
            // if had to be saved and autogen NOT on, then run ANTLR
            // Otherwise, the save file event will have or will run ANTLR.

            ProgressManager.getInstance().run(gen); //, "Generating", canBeCancelled, e.getData(PlatformDataKeys.PROJECT));

            // refresh from disk to see new files
            Set<File> generatedFiles = new HashSet<>();
            generatedFiles.add(new File(gen.getOutputDirName()));
            LocalFileSystem.getInstance().refreshIoFiles(generatedFiles, true, true, null);
            // pop up a notification
            Notification notification = new Notification(RunCompilerOnXMLFile.groupDisplayId, "class for " + guiFile.getName() + " generated", "to " + gen.getOutputDirName(), NotificationType.INFORMATION);
            Notifications.Bus.notify(notification, project);
        }
    }

    private static VirtualFile getGuiFileFromEvent(AnActionEvent e) {
        VirtualFile[] files = LangDataKeys.VIRTUAL_FILE_ARRAY.getData(e.getDataContext());
        if ( files==null || files.length==0 ) return null;
        VirtualFile vFile = files[0];
        if ( vFile!=null && vFile.getName().endsWith(".cxml") ) {
            return vFile;
        }
        return null;
    }
}
