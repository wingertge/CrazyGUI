package com.octagon.crazygui.idea;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import com.octagon.crazygui.antlr.AttributeManager;
import com.octagon.crazygui.antlr.ComponentAttribute;
import com.octagon.crazygui.idea.psi.CXMLAttribute;
import com.octagon.crazygui.idea.psi.CXMLSelfContainedTag;
import com.octagon.crazygui.idea.psi.CXMLTag;
import com.octagon.crazygui.idea.psi.CXMLTypes;
import com.octagon.crazygui.idea.util.ClassUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CXMLAttributeNameCompletion extends CompletionContributor {
    private static Logger LOG = Logger.getInstance("CrazyGUI CXMLAttributeNameCompletion");

    public CXMLAttributeNameCompletion() {
        LOG.warn("test");
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(CXMLTypes.IDENTIFIER).withParent(CXMLAttribute.class), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          ProcessingContext context,
                                          @NotNull CompletionResultSet resultSet) {
                resultSet.addElement(LookupElementBuilder.create("xPos").setInsertHandler(new AttributeInsertionHandler(false)));
                resultSet.addElement(LookupElementBuilder.create("yPos").setInsertHandler(new AttributeInsertionHandler(false)));
                resultSet.addElement(LookupElementBuilder.create("x:name").setInsertHandler(new AttributeInsertionHandler(true)));
                resultSet.addElement(LookupElementBuilder.create("x:class").setInsertHandler(new AttributeInsertionHandler(true)));
                CXMLAttribute attribute = (CXMLAttribute) parameters.getPosition().getParent();
                Object parent = attribute.getParent();
                if (!(parent instanceof CXMLTag || parent instanceof CXMLSelfContainedTag)) return;

                String tagName;
                List<CXMLAttribute> definedAttributes;
                if (parent instanceof CXMLTag) {
                    CXMLTag tag = (CXMLTag) parent;
                    tagName = tag.getTagNameList().size() != 0 ? tag.getTagNameList().get(0).getText() : "";
                    definedAttributes = Arrays.asList(tag.getChildren()).stream().filter(a -> a instanceof CXMLAttribute).map(a -> ((CXMLAttribute) a)).collect(Collectors.toList());
                } else {
                    CXMLSelfContainedTag tag = (CXMLSelfContainedTag) parent;
                    tagName = tag.getTagName().getText();
                    definedAttributes = Arrays.asList(tag.getChildren()).stream().filter(a -> a instanceof CXMLAttribute).map(a -> ((CXMLAttribute) a)).collect(Collectors.toList());
                }
                if (tagName.isEmpty()) return;
                if (tagName.equalsIgnoreCase("slot")) {
                    resultSet.addElement(LookupElementBuilder.create("id").setInsertHandler(new AttributeInsertionHandler(false)));
                    return;
                }

                Map<String, List<ComponentAttribute>> componentAttributeNames = ClassUtils.getCachedComponentAttributeNames();
                if (componentAttributeNames.containsKey(tagName)) {
                    List<ComponentAttribute> attributeNames = componentAttributeNames.get(tagName);
                    definedAttributes.stream().filter(definedAttribute -> attributeNames.contains(decapitalize(definedAttribute.getFirstChild().getText())))
                            .forEach(definedAttribute -> attributeNames.remove(decapitalize(definedAttribute.getFirstChild().getText())));
                    for (ComponentAttribute componentAttribute : attributeNames) {
                        resultSet.addElement(LookupElementBuilder.create(componentAttribute.getName()).setInsertHandler(new AttributeInsertionHandler(!(componentAttribute.getParser() instanceof AttributeManager.PrimitiveParser))));
                    }
                }
            }
        });
    }

    private String decapitalize(String s) {
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }

    private class AttributeInsertionHandler implements InsertHandler<LookupElement> {
        private boolean addQuotes;

        public AttributeInsertionHandler(boolean addQuotes) {

            this.addQuotes = addQuotes;
        }

        @Override
        public void handleInsert(InsertionContext insertionContext, LookupElement lookupElement) {
            insertionContext.getDocument().insertString(insertionContext.getTailOffset(), "=");
            if(addQuotes) insertionContext.getDocument().insertString(insertionContext.getTailOffset(), "\"\"");
            insertionContext.getEditor().getCaretModel().moveToOffset(insertionContext.getTailOffset() - 1);
        }
    }
}
