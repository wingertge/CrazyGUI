package com.octagon.crazygui.idea;

import com.intellij.psi.PsiElement;
import com.octagon.crazygui.idea.psi.CXMLFile;
import com.octagon.crazygui.idea.psi.CXMLTagBase;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class GuiStructureModel implements TreeModel {
    private CXMLFile root;

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        return ((PsiElement)parent).getChildren()[index];
    }

    @Override
    public int getChildCount(Object parent) {
        return ((PsiElement)parent).getChildren().length;
    }

    @Override
    public boolean isLeaf(Object node) {
        return node instanceof CXMLTagBase;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        PsiElement element = ((PsiElement)path.getLastPathComponent());
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        PsiElement[] children = ((PsiElement)parent).getChildren();
        for(int i = 0; i < children.length; i++) {
            if(children[i].equals(child)) return i;
        }
        return -1;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {

    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {

    }
}
