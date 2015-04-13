package com.octagon.crazygui.idea;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import com.octagon.crazygui.idea.psi.CXMLTagName;
import com.octagon.crazygui.idea.psi.CXMLTypes;
import com.octagon.crazygui.idea.util.ClassUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class CXMLTagNameCompletion extends CompletionContributor {
    private static Logger LOG = Logger.getInstance("CrazyGUI CXMLTagNameCompletion");

    public CXMLTagNameCompletion() {
        LOG.warn("test");
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(CXMLTypes.IDENTIFIER).withParent(CXMLTagName.class), new CompletionProvider<CompletionParameters>() {
            public void addCompletions(@NotNull CompletionParameters parameters,
                                       ProcessingContext context,
                                       @NotNull CompletionResultSet resultSet) {
                resultSet.addElement(LookupElementBuilder.create("Slot"));
                resultSet.addElement(LookupElementBuilder.create("PlayerInventory"));
                Collection<String> componentNames = ClassUtils.getCachedComponentClasses().keySet();
                for (String s : componentNames) {
                    resultSet.addElement(LookupElementBuilder.create(s));
                }
            }
        });
    }
}
