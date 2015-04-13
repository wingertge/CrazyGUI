package com.octagon.crazygui.idea.actions;

import com.intellij.ide.IdeView;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;

@SuppressWarnings("ComponentNotRegistered")
public class CreateCXMLAction extends AnAction {
    private final String fileName;

    public CreateCXMLAction(String fileName) {
        if(!fileName.endsWith(".cxml")) fileName += ".cxml";
        this.fileName = fileName;
    }

    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        final IdeView view = e.getRequiredData(LangDataKeys.IDE_VIEW);

        final PsiDirectory directory = view.getOrChooseDirectory();
        if (directory == null) {
            return;
        }

        PsiFile file = directory.findFile(fileName);
        if (file == null)
            file = new CreateFileCommandAction(project, directory, fileName).execute().getResultObject();

        FileEditorManager.getInstance(project).openFile(file.getVirtualFile(), true);
    }

    @Override
    public void update(AnActionEvent e) {
        final Project project = e.getData(CommonDataKeys.PROJECT);
        final IdeView view = e.getData(LangDataKeys.IDE_VIEW);

        final PsiDirectory[] directory = view != null ? view.getDirectories() : null;
        if (directory == null || directory.length == 0 || project == null) {
            e.getPresentation().setVisible(false);
        }
    }
}
