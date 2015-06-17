package com.octagon.crazygui.idea.psi.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;
import com.octagon.crazygui.antlr.AttributeManager;
import com.octagon.crazygui.antlr.ComponentAttribute;
import com.octagon.crazygui.antlr.ICXMLSerializable;
import com.octagon.crazygui.idea.CXMLFileType;
import com.octagon.crazygui.idea.psi.*;

import java.util.List;

public class CXMLElementFactory {
    public static CXMLAttribute createAttribute(Project project, String name, String value) {
        final CXMLFile file = createFile(project, "<Root " + name + "=" + value + "></Root>");
        return ((CXMLRootTag)file.getFirstChild()).getAttributeList().get(0);
    }

    public static CXMLTag createTag(Project project, String tagName, List<ComponentAttribute> attributes) {
        String fileContent = String.format("<Root><%s %s></%s></Root>", tagName,
                attributes.stream().map(a -> String.format("%s=%s", a.getName(), getValueString(a.getValueString(), a.getParser()))).reduce((a, b) -> a + " " + b).orElse(""),
                tagName);
        final CXMLFile file = createFile(project, fileContent);
        return file.findChildByClass(CXMLRootTag.class).getTagList().get(0);
    }

    public static CXMLSelfContainedTag createSelfContainedTag(Project project, String tagName, List<ComponentAttribute> attributes) {
        String fileContent = String.format("<Root><%s %s /></Root>", tagName,
                attributes.stream().map(a -> String.format("%s=%s", a.getName(), getValueString(a.getValueString(), a.getParser()))).reduce((a, b) -> a + " " + b));
        final CXMLFile file = createFile(project, fileContent);
        return ((CXMLRootTag)file.getFirstChild()).getSelfContainedTagList().get(0);
    }

    private static String getValueString(String value, ICXMLSerializable parser) {
        return parser instanceof AttributeManager.PrimitiveParser && !(parser instanceof AttributeManager.BooleanParser) ? value : "\"" + value + "\"";
    }

    public static CXMLFile createFile(Project project, String text) {
        String name = "dummy.cxml";
        return (CXMLFile) PsiFileFactory.getInstance(project).
                createFileFromText(name, CXMLFileType.INSTANCE, text);
    }
}
