package com.octagon.crazygui.idea.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.octagon.crazygui.antlr.ComponentAttribute;
import com.octagon.crazygui.idea.psi.CXMLAttribute;
import com.octagon.crazygui.idea.psi.CXMLSelfContainedTag;
import com.octagon.crazygui.idea.psi.CXMLTagBase;
import com.octagon.crazygui.idea.psi.CXMLTypes;
import com.octagon.crazygui.idea.util.ClassUtils;

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
        return element.getTagName() != null ? element.getTagName().getText() : "";
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
        if (keyNode != null) {
            return keyNode.getPsi();
        } else {
            return null;
        }
    }
}
