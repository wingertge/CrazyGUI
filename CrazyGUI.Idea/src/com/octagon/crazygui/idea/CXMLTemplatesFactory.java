package com.octagon.crazygui.idea;

import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.util.IncorrectOperationException;

public class CXMLTemplatesFactory implements FileTemplateGroupDescriptorFactory {
    /** File's content header. */
    //private static final String TEMPLATE_NOTE = IgnoreBundle.message("file.templateNote");

    /** Group descriptor. */
    private final FileTemplateGroupDescriptor templateGroup;
    private final String fileName;

    /** Builds a new instance of {@link CXMLTemplatesFactory}. */
    public CXMLTemplatesFactory(String fileName) {
        this.fileName = fileName;
        templateGroup = new FileTemplateGroupDescriptor("CXML", CrazyGUIIcons.FILE);
        templateGroup.addTemplate(".cxml");
    }

    /**
     * Returns group descriptor.
     *
     * @return descriptor
     */
    @Override
    public FileTemplateGroupDescriptor getFileTemplatesDescriptor() {
        return templateGroup;
    }

    /**
     * Creates new CXML file or uses an existing one.
     *
     * @param directory working directory
     * @return file
     * @throws IncorrectOperationException
     */
    public PsiFile createFromTemplate(final PsiDirectory directory) throws IncorrectOperationException {
        final PsiFileFactory factory = PsiFileFactory.getInstance(directory.getProject());
        final PsiFile file = factory.createFileFromText(fileName, CXMLFileType.INSTANCE, "");
        return (PsiFile) directory.add(file);
    }
}
