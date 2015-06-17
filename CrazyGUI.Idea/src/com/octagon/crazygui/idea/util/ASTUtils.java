package com.octagon.crazygui.idea.util;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ASTUtils {
    @NotNull
    public static List<ASTNode> findChildrenByType(ASTNode parent, IElementType type) {
        List<ASTNode> children = new ArrayList<>();
        ASTNode child = parent.getFirstChildNode();

        while (child != null) {
            if (child.getElementType() == type) {
                children.add(child);
            }
            child = child.getTreeNext();
        }

        return children;
    }
}
