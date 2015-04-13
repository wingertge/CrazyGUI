package com.octagon.crazygui.idea;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.octagon.crazygui.antlr.ComponentAttribute;
import com.octagon.crazygui.idea.psi.CXMLAttribute;
import com.octagon.crazygui.idea.psi.CXMLSelfContainedTag;
import com.octagon.crazygui.idea.psi.CXMLTag;
import com.octagon.crazygui.idea.psi.CXMLTagName;
import com.octagon.crazygui.idea.util.ClassUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CXMLAnnotator implements Annotator {
    @Override
    public void annotate(PsiElement element, AnnotationHolder holder) {
        if(element instanceof CXMLAttribute) {
            String name = element.getFirstChild().getText();
            Annotation annotation = holder.createInfoAnnotation(element.getFirstChild().getTextRange(), null);

            if(name.startsWith("x:"))
                annotation.setTextAttributes(CXMLSyntaxHighlighter.ATTRIBUTE_COMPILETIME_KEY);
            else annotation.setTextAttributes(CXMLSyntaxHighlighter.ATTRIBUTE_NAME);
            CXMLAttribute attribute = (CXMLAttribute)element;
            if(!(attribute.getParent() instanceof CXMLTag || attribute.getParent() instanceof CXMLSelfContainedTag)) return;
            String tagName;

            if(attribute.getParent() instanceof CXMLTag) {
                CXMLTag tag = (CXMLTag)attribute.getParent();
                tagName = tag.getTagNameList().size() != 0 ? tag.getTagNameList().get(0).getText() : "";
            } else {
                CXMLSelfContainedTag tag = (CXMLSelfContainedTag)attribute.getParent();
                tagName = tag.getTagName().getText();
            }
            if(tagName.isEmpty()) return;

            if(tagName.equalsIgnoreCase("slot") && name.equalsIgnoreCase("id")) return;
            if(name.equalsIgnoreCase("x:name") || name.equalsIgnoreCase("x:class") || name.equalsIgnoreCase("xPos") || name.equalsIgnoreCase("yPos"))
                return;
            else {
                Map<String, List<ComponentAttribute>> componentAttributeNames = ClassUtils.getComponentAttributeNames(element.getProject());
                if(componentAttributeNames.containsKey(tagName)) {
                    List<ComponentAttribute> attributeNames = componentAttributeNames.get(tagName);
                    if(ComponentAttribute.get(attributeNames, decapitalize(name)) != null)
                        return;
                }
            }
            holder.createErrorAnnotation(element.getTextRange(), "Unresolved component setter");
        } else if(element instanceof CXMLTagName) {
            String text = element.getText();
            if(!text.equalsIgnoreCase("slot") && !text.equalsIgnoreCase("playerInventory") && !ClassUtils.getComponentClasses(element.getProject()).keySet().contains(text)) {
                holder.createErrorAnnotation(element.getTextRange(), "Unresolved component");
            }

            if(element.getParent() instanceof CXMLTag) {
                CXMLTag tag = (CXMLTag) element.getParent();
                if(tag.getTagNameList().size() != 2 || !Objects.equals(tag.getTagNameList().get(0).getText(), tag.getTagNameList().get(1).getText()))
                    holder.createErrorAnnotation(tag.getTagNameList().get(1).getTextRange(), "Mismatched closing tag");
            }
        }
    }



    private String decapitalize(String s) {
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }
}
