package com.octagon.crazygui.idea.util;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.octagon.crazygui.antlr.AttributeManager;
import com.octagon.crazygui.antlr.ICXMLSerializable;
import com.octagon.crazygui.antlr.ComponentAttribute;
import com.octagon.crazygui.antlr.util.LogHelper;
import com.octagon.crazygui.idea.actions.ConfigCompilerDialogue;
import org.apache.commons.lang.ArrayUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.octagon.crazygui.antlr.AttributeManager.getClassParser;
import static com.octagon.crazygui.antlr.AttributeManager.getEnumParser;

public class ClassUtils {
    private static Map<String, PsiClass> componentNames;
    private static Map<String, List<ComponentAttribute>> componentAttributeNames;

    public static Map<String, String> getComponentList(Project project, String package_) {
        PsiPackage psiPackage = JavaPsiFacade.getInstance(project).findPackage(package_);
        if(psiPackage == null) return new HashMap<>();
        return Arrays.asList(psiPackage.getClasses()).stream().filter(a -> a.getName().startsWith("GuiComponent")).collect(Collectors.toMap(a -> a.getName().replace("GuiComponent", ""), PsiClass::getQualifiedName));
    }

    public static Map<String, PsiClass> getComponentClasses(Project project) {
        PsiPackage vendorPackage = JavaPsiFacade.getInstance(project).findPackage("com.octagon.crazygui.components");
        PsiPackage userPackage = JavaPsiFacade.getInstance(project).findPackage(ConfigCompilerDialogue.getProp(project, ConfigCompilerDialogue.PROP_GUI_PACKAGE, "com.octagon.airships.client.gui") + ".components");
        PsiClass[] vendorClasses = new PsiClass[0];
        PsiClass[] userClasses = new PsiClass[0];
        if(vendorPackage != null) vendorClasses = vendorPackage.getClasses();
        if(userPackage != null) userClasses = userPackage.getClasses();
        PsiClass[] allClasses = (PsiClass[]) ArrayUtils.addAll(vendorClasses, userClasses);
        return componentNames = Arrays.asList(allClasses).stream().filter(a -> a.getName().startsWith("GuiComponent")).collect(Collectors.toMap(a -> a.getName().replace("GuiComponent", ""), a -> a));
    }

    public static Map<String, List<ComponentAttribute>> getComponentAttributeNames(Project project) {
        Collection<String> classNames = getComponentClasses(project).values().stream().map(PsiClass::getQualifiedName).collect(Collectors.toList());
        Map<String, List<ComponentAttribute>> componentAttributeNames = new HashMap<>();
        for(String clazz : classNames) {
            List<ComponentAttribute> attributeNames = new ArrayList<>();
            PsiClass file = JavaPsiFacade.getInstance(project).findClass(clazz, GlobalSearchScope.allScope(project));
            if(file == null) continue;
            PsiMethod[] methods = file.getAllMethods();
            for(PsiMethod method : methods) {
                if(method.getName().startsWith("set") && method.getParameterList().getParametersCount() == 1) {
                    String attributeName = method.getName().replace("set", "");
                    if(attributeName.equalsIgnoreCase("Listener") || attributeName.equalsIgnoreCase("x") || attributeName.equalsIgnoreCase("y")) continue;
                    attributeName = Character.toLowerCase(attributeName.charAt(0)) + attributeName.substring(1);
                    PsiType parameterType = method.getParameterList().getParameters()[0].getType();
                    ICXMLSerializable parser = null;
                    String qualClassName = parameterType.getCanonicalText();
                    if(qualClassName.contains("<")) qualClassName = qualClassName.substring(0, qualClassName.indexOf('<'));
                    if(parameterType instanceof PsiClassType && JavaPsiFacade.getInstance(project).findClass(qualClassName, GlobalSearchScope.allScope(project)).isEnum()) {
                        parser = getEnumParser(parameterType.getCanonicalText());
                    } else if(parameterType instanceof PsiPrimitiveType) {
                        parser = getClassParser(((PsiPrimitiveType) parameterType).getBoxedTypeName());
                    } else if(parameterType instanceof PsiClassType) {
                        parser = getClassParser(parameterType.getCanonicalText());
                    }
                    if(parser == null) parser = AttributeManager.getCodeParser();
                    Object defaultValue = "";
                    for(PsiField field : file.getAllFields()) {
                        if(field.getName().equals(attributeName)) {
                            if(parser instanceof AttributeManager.CodeParser) {
                                defaultValue = field.getInitializer() != null ? field.getInitializer().getText() : "null";
                            } else {
                                Object obj = JavaPsiFacade.getInstance(project).getConstantEvaluationHelper().computeConstantExpression(field.getInitializer());
                                defaultValue = obj != null ? obj : DefaultUtils.getDefaultValue(parameterType);
                            }
                            break;
                        }
                    }
                    if(defaultValue == null || defaultValue.equals("")) defaultValue = DefaultUtils.getDefaultValue(parameterType);
                    attributeNames.add(new ComponentAttribute(defaultValue, parser, attributeName));
                }
            }
            componentAttributeNames.put(file.getName().replace("GuiComponent", ""), attributeNames);
        }
        List<ComponentAttribute> slotAttributes = new ArrayList<>();
        slotAttributes.add(new ComponentAttribute(0, getClassParser("java.lang.Integer"), "id"));
        slotAttributes.add(new ComponentAttribute(0, getClassParser("java.lang.Integer"), "xPos"));
        slotAttributes.add(new ComponentAttribute(0, getClassParser("java.lang.Integer"), "yPos"));
        componentAttributeNames.put("Slot", slotAttributes);
        List<ComponentAttribute> playerInventoryAttributes = new ArrayList<>();
        playerInventoryAttributes.add(new ComponentAttribute(0, getClassParser("java.lang.Integer"), "xPos"));
        playerInventoryAttributes.add(new ComponentAttribute(0, getClassParser("java.lang.Integer"), "yPos"));
        componentAttributeNames.put("PlayerInventory", playerInventoryAttributes);
        return ClassUtils.componentAttributeNames = componentAttributeNames;
    }

    public static Map<String, PsiClass> getCachedComponentClasses() {
        return componentNames;
    }
    public static Map<String, List<ComponentAttribute>> getCachedComponentAttributeNames() { return componentAttributeNames; }
}
