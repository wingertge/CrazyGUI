package com.octagon.crazygui.idea.actions;

import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.octagon.crazygui.idea.CXMLTemplatesFactory;
import org.jetbrains.annotations.NotNull;

public class CreateFileCommandAction extends WriteCommandAction<PsiFile> {
    /** Working directory. */
    private final PsiDirectory directory;
    private final String fileName;

    /**
     * Builds a new instance of {@link CreateFileCommandAction}.
     *
     * @param project   current project
     * @param directory working directory
     */
    public CreateFileCommandAction(@NotNull Project project, @NotNull PsiDirectory directory, String fileName) {
        super(project);
        this.directory = directory;
        this.fileName = fileName;
    }

    /**
     * Creates a new file using {@link CXMLTemplatesFactory#createFromTemplate(PsiDirectory)} to fill it with content.
     * @param result command result
     * @throws Throwable
     */
    @Override
    protected void run(@NotNull Result<PsiFile> result) throws Throwable {
        CXMLTemplatesFactory factory = new CXMLTemplatesFactory(fileName);
        result.setResult(factory.createFromTemplate(directory));
    }
}
