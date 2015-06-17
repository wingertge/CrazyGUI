package com.octagon.crazygui.idea;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.octagon.crazygui.idea.psi.CXMLTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CXMLFormattingModelBuilder implements FormattingModelBuilder {
    @NotNull
    @Override
    public FormattingModel createModel(PsiElement element, CodeStyleSettings settings) {
        return FormattingModelProvider.createFormattingModelForPsiFile(element.getContainingFile(),
                new CXMLTagBlock(element.getNode(), Wrap.createWrap(WrapType.NONE, false),
                        Alignment.createAlignment(), createSpaceBuilder(settings)), settings);
    }

    private static SpacingBuilder createSpaceBuilder(CodeStyleSettings settings) {
        return new SpacingBuilder(settings, CXMLLanguage.INSTANCE).
                around(CXMLTypes.ATTRIBUTE).spaces(1)
                .beforeInside(CXMLTypes.TAG, CXMLTypes.ROOT_TAG).lineBreakInCode()
                .beforeInside(CXMLTypes.SELF_CONTAINED_TAG, CXMLTypes.ROOT_TAG).lineBreakInCode()
                .beforeInside(CXMLTypes.TAG, CXMLTypes.TAG).lineBreakInCode()
                .beforeInside(CXMLTypes.SELF_CONTAINED_TAG, CXMLTypes.TAG).lineBreakInCode();
    }

    private static Block createBlock(ASTNode node, Wrap wrap, Alignment alignment, SpacingBuilder spacingBuilder) {
        return new AbstractBlock(node, wrap, alignment) {
            @Override
            protected List<Block> buildChildren() {
                List<Block> children = new ArrayList<>();
                ASTNode root = myNode.findChildByType(CXMLTypes.ROOT_TAG);
                if(root != null) {
                    children.add(new CXMLTagBlock(root, wrap, alignment, spacingBuilder));
                }
                return children;
            }

            @Nullable
            @Override
            public Spacing getSpacing(@Nullable Block block, @NotNull Block block1) {
                return spacingBuilder.getSpacing(this, block, block1);
            }

            @Override
            public boolean isLeaf() {
                return myNode.findChildByType(CXMLTypes.ROOT_TAG) == null;
            }
        };
    }

    @Nullable
    @Override
    public TextRange getRangeAffectingIndent(PsiFile psiFile, int i, ASTNode astNode) {
        return null;
    }
}
