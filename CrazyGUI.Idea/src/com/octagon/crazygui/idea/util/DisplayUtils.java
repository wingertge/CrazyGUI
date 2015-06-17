package com.octagon.crazygui.idea.util;

import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.util.containers.hash.HashMap;
import com.octagon.crazygui.antlr.ComponentAttribute;
import com.octagon.crazygui.antlr.ICustomPresentation;
import com.octagon.crazygui.idea.psi.CXMLAttribute;
import com.octagon.crazygui.idea.psi.CXMLNamedElement;
import com.octagon.crazygui.idea.psi.CXMLTagBase;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DisplayUtils {
    private static Map<String, ItemPresentation> presentationCache = new HashMap<>();

    public static ItemPresentation getCustomPresentation(CXMLNamedElement element, Project project) {
        if(element instanceof CXMLAttribute) {
            CXMLAttribute attribute = (CXMLAttribute)element;
            if(element.getParent() == null) return null;
            String parentName = ((CXMLTagBase)element.getParent()).getName();
            Map<String, List<ComponentAttribute>> attributesMap = ClassUtils.getComponentAttributeNames(project);
            if(!attributesMap.containsKey(parentName)) return null;
            List<ComponentAttribute> attributes = attributesMap.get(parentName);
            if(!attributes.stream().anyMatch(a -> a.getName().equals(attribute.getAttributeName()))) return null;
            ComponentAttribute attributeWrapper = attributes.stream().filter(a -> a.getName().equals(attribute.getAttributeName())).findFirst().orElse(null);
            if(attributeWrapper == null) return null;
            if(attributeWrapper.getParser() instanceof ICustomPresentation) return ((ICustomPresentation) attributeWrapper.getParser()).getPresentation(attributeWrapper.getName());
        }

        if(!(element instanceof CXMLTagBase)) return null;
        PsiClass elementClass = ClassUtils.getComponentClasses(project).containsKey(element.getName()) ? ClassUtils.getCachedComponentClasses().get(element.getName()) : null;
        if(elementClass == null) return null;

        if(Arrays.asList(elementClass.getInterfaces()).stream().anyMatch(a -> ICustomPresentation.class.getCanonicalName().equals(a.getQualifiedName()))) {
            PsiMethod method = Arrays.asList(elementClass.findMethodsByName("getPresentation", true)).stream().filter(a -> a.getParameterList().getParameters().length == 0).findFirst().orElse(null);
            if(method == null) return null;
            CompilerManager.getInstance(project).make((b, i, i1, compileContext) -> {
                try {
                    Class cls = Class.forName(elementClass.getQualifiedName());
                    Constructor constructor = cls.getConstructor(int.class, int.class);
                    if(constructor == null) return;
                    constructor.setAccessible(true);
                    Object instance = constructor.newInstance(0, 0);
                    if(!(instance instanceof ICustomPresentation)) return;
                    addPresentation(elementClass.getQualifiedName(), ((ICustomPresentation) instance).getPresentation(element.getName()));
                } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | NoSuchMethodException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        }

        if(presentationCache.containsKey(elementClass.getQualifiedName())) return presentationCache.get(elementClass.getQualifiedName());
        return null;
    }

    private static void addPresentation(String name, ItemPresentation presentation) {
        presentationCache.put(name, presentation);
    }
}
