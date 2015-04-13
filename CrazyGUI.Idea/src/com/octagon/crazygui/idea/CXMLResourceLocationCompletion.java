package com.octagon.crazygui.idea;

import com.intellij.codeInsight.completion.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import com.octagon.crazygui.antlr.util.LogHelper;
import com.octagon.crazygui.idea.psi.CXMLTypes;
import org.jetbrains.annotations.NotNull;

public class CXMLResourceLocationCompletion extends CompletionContributor {
    private static Logger LOG = Logger.getInstance("CrazyGUI CXMLResourceLocationCompletion");

    public CXMLResourceLocationCompletion() {
        LOG.warn("test");
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(CXMLTypes.STRINGVALUE), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet resultSet) {
                String s = parameters.getPosition().getText();
                LogHelper.info(s);
            }
        });
    }
}
