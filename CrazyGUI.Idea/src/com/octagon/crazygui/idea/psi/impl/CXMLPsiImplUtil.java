package com.octagon.crazygui.idea.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.PsiElement;
import com.octagon.crazygui.antlr.ComponentAttribute;
import com.octagon.crazygui.antlr.CXMLItemPresentation;
import com.octagon.crazygui.idea.CXMLGuiEditor;
import com.octagon.crazygui.idea.icons.Icons;
import com.octagon.crazygui.idea.psi.*;
import com.octagon.crazygui.idea.util.ClassUtils;
import com.octagon.crazygui.idea.util.DisplayUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class CXMLPsiImplUtil {
    public static String getAttributeName(CXMLAttribute element) {
        ASTNode keyNode = element.getNode().findChildByType(CXMLTypes.IDENTIFIER);
        if (keyNode != null) {
            return keyNode.getText();
        } else {
            return null;
        }
    }

    public static String getName(CXMLAttribute element) {
        return getAttributeName(element);
    }

    public static String getName(CXMLTagBase element) {
        return element.getTagName() != null ? element.getTagName().getText() : "Root";
    }

    public static PsiElement setName(CXMLAttribute element, String newName) {
        ASTNode keyNode = element.getNode().findChildByType(CXMLTypes.IDENTIFIER);
        if (keyNode != null) {

            CXMLAttribute property = CXMLElementFactory.createAttribute(element.getProject(), newName, element.getValue() != null ? element.getValue().getText() : "");
            ASTNode newKeyNode = property.getFirstChild().getNode();
            element.getNode().replaceChild(keyNode, newKeyNode);
        }
        return element;
    }

    public static PsiElement setName(CXMLTagBase element, String newName) {
        ASTNode keyNode = element.getNode().findChildByType(CXMLTypes.TAG_NAME);
        if (keyNode != null) {
            Map<String, List<ComponentAttribute>> attributeMap = ClassUtils.getComponentAttributeNames(element.getProject());
            if(element.getTagName() != null && attributeMap.containsKey(element.getTagName().getText())) {
                List<ComponentAttribute> tagAttributes = attributeMap.get(element.getTagName().getText());

                CXMLTagBase tag = element instanceof CXMLSelfContainedTag ?
                        CXMLElementFactory.createSelfContainedTag(element.getProject(), newName, tagAttributes) :
                        CXMLElementFactory.createTag(element.getProject(), newName, tagAttributes);
                if(tag.getTagName() != null) {
                    ASTNode newKeyNode = tag.getTagName().getNode();
                    element.getNode().replaceChild(keyNode, newKeyNode);
                }
            }
        }
        return element;
    }

    public static PsiElement getNameIdentifier(CXMLAttribute element) {
        ASTNode keyNode = element.getNode().findChildByType(CXMLTypes.IDENTIFIER);
        if (keyNode != null) {
            return keyNode.getPsi();
        } else {
            return null;
        }
    }

    public static PsiElement getNameIdentifier(CXMLTagBase element) {
        ASTNode keyNode = element.getNode().findChildByType(CXMLTypes.TAG_NAME);
        CXMLTagName tagName = element.getTagName();
        if(tagName == null) {
            return element.getNode().findChildByType(CXMLTypes.ROOT).getPsi();
        }
        return tagName;
    }

    @Nullable
    public static ItemPresentation getPresentation(CXMLNamedElement element) {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        ItemPresentation customPresentation = null;
        for(Project project : projects) {
            customPresentation = DisplayUtils.getCustomPresentation(element, project);
            if(customPresentation != null) break;
        }
        Icons icon = null;
        if(element instanceof CXMLTag) {
            icon = ((CXMLTag)element).getTagList().size() + ((CXMLTag)element).getSelfContainedTagList().size() == 0 ? Icons.TAG_LEAF : Icons.TAG;
        } else if(element instanceof CXMLRootTag) {
            icon = Icons.ROOT_TAG;
        } else if(element instanceof CXMLAttribute) {
            icon = Icons.ATTRIBUTE;
        } else {
            icon = Icons.SELF_CONTAINED_TAG;
        }
        return customPresentation != null ? customPresentation : new CXMLItemPresentation(element.getName(), icon.getIcon());
    }

    public static void navigate(CXMLNamedElement element, boolean b) {
        Project project = element.getContainingFile().getProject();
        FileEditor[] editors = FileEditorManager.getInstance(project).getEditors(element.getContainingFile().getVirtualFile());
        if(editors.length == 0) {
            editors = FileEditorManager.getInstance(project).openFile(element.getContainingFile().getVirtualFile(), true);
        }
        FileEditor editor = editors[0];
        if(editor instanceof CXMLGuiEditor) ((CXMLGuiEditor) editor).navigateToElement(element);
        else if(editor instanceof TextEditor) ((TextEditor) editor).navigateTo(new OpenFileDescriptor(project, element.getContainingFile().getVirtualFile(), element.getTextOffset()));
    }

    public static boolean canNavigate(CXMLNamedElement element) {
        return true;
    }

    public static boolean canNavigateToSource(CXMLNamedElement element) {
        return true;
    }
}
