package com.octagon.crazygui.idea;

import com.octagon.crazygui.antlr.ComponentAttribute;
import com.octagon.crazygui.idea.psi.CXMLAttribute;
import com.octagon.crazygui.idea.psi.CXMLTagBase;
import com.octagon.crazygui.idea.util.ClassUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class ComponentAttributeFactory {
    @Nullable
    public static ComponentAttribute create(CXMLAttribute attribute) {
        String attributeName = attribute.getText().substring(0, attribute.getText().indexOf('=') != -1 ? attribute.getText().indexOf('=') : attribute.getText().length() - 1);
        String tagName = ((CXMLTagBase)attribute.getParent()).getTagName().getText();
        Map<String, List<ComponentAttribute>> attributes = ClassUtils.getCachedComponentAttributeNames();
        return attributes.containsKey(tagName) ? attributes.get(tagName).stream().filter(a -> a.getName().equals(decapitalize(attributeName))).findFirst().orElse(null) : null;
    }

    private static String decapitalize(String s) {
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }
}
