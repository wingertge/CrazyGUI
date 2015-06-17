package com.octagon.crazygui.idea;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import com.octagon.crazygui.idea.psi.CXMLTypes;
import com.octagon.crazygui.idea.util.ASTUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CXMLTagBlock extends AbstractBlock {
    private final SpacingBuilder spacingBuilder;
    private final Wrap wrap;

    protected CXMLTagBlock(@NotNull ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment,
                           SpacingBuilder spacingBuilder) {
        super(node, wrap, alignment);
        this.spacingBuilder = spacingBuilder;
        this.wrap = wrap;
    }

    @Override
    protected List<Block> buildChildren() {
        List<Block> children = new ArrayList<>();
        ASTNode child = myNode.getFirstChildNode();
        ASTNode previousChild = null;
        while (child != null) {
            if (child.getElementType() != TokenType.WHITE_SPACE &&
                    (previousChild == null || previousChild.getElementType() != CXMLTypes.CRLF ||
                            child.getElementType() != CXMLTypes.CRLF)) {
                Block block = new CXMLTagBlock(child, Wrap.createWrap(WrapType.NONE, false), Alignment.createAlignment(),
                        spacingBuilder);
                children.add(block);
            }
            previousChild = child;
            child = child.getTreeNext();
        }
        return children;
    }

    @Nullable
    @Override
    public Spacing getSpacing(Block block, Block block1) {
        return spacingBuilder.getSpacing(this, block1, block1);
    }

    @Override
    public Indent getIndent() {
        return myNode.getElementType() == CXMLTypes.SELF_CONTAINED_TAG || myNode.getElementType() == CXMLTypes.TAG ? Indent.getNormalIndent() : Indent.getNoneIndent();
    }

    @Override
    public boolean isLeaf() {
        return myNode.getFirstChildNode() == null;
    }
}
