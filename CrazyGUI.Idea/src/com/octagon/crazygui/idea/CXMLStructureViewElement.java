package com.octagon.crazygui.idea;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.octagon.crazygui.antlr.CXMLItemPresentation;
import com.octagon.crazygui.idea.psi.CXMLAttribute;
import com.octagon.crazygui.idea.psi.CXMLFile;
import com.octagon.crazygui.idea.psi.CXMLRootTag;
import com.octagon.crazygui.idea.psi.CXMLTagBase;
import org.jetbrains.annotations.NotNull;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CXMLStructureViewElement implements StructureViewTreeElement, SortableTreeElement {
    private PsiElement element;

    public CXMLStructureViewElement(PsiElement element) {
        this.element = element;
    }

    @Override
    public Object getValue() {
        return element;
    }

    @Override
    public void navigate(boolean requestFocus) {
        if (element instanceof NavigationItem) {
            ((NavigationItem) element).navigate(requestFocus);
        }
    }

    @Override
    public boolean canNavigate() {
        return true;
    }

    @Override
    public boolean canNavigateToSource() {
        return true;
    }

    @NotNull
    @Override
    public String getAlphaSortKey() {
        return element instanceof PsiNamedElement ? ((PsiNamedElement) element).getName() : null;
    }

    @Override
    public ItemPresentation getPresentation() {
        if(element instanceof CXMLFile) return new CXMLItemPresentation(((CXMLFile) element).getName(), CrazyGUIIcons.FILE);
        return element instanceof NavigationItem ?
                ((NavigationItem) element).getPresentation() : null;
    }

    @Override
    public TreeElement[] getChildren() {
        if (element instanceof CXMLTagBase) {
            return getTagChildren((CXMLTagBase) element);
        } else if(element instanceof CXMLFile) {
            return getTagChildren(PsiTreeUtil.getChildOfType(element, CXMLRootTag.class));
        } else {
            return EMPTY_ARRAY;
        }
    }

    private TreeElement[] getTagChildren(CXMLTagBase tag) {
        CXMLAttribute[] attributes = PsiTreeUtil.getChildrenOfType(tag, CXMLAttribute.class);
        CXMLTagBase[] childTags = PsiTreeUtil.getChildrenOfType(tag, CXMLTagBase.class);
        if (attributes == null) attributes = new CXMLAttribute[0];
        if (childTags == null) childTags = new CXMLTagBase[0];
        List<TreeElement> treeElements = new ArrayList<>(attributes.length + childTags.length);
        treeElements.addAll(((List<CXMLAttribute>) Arrays.asList(attributes)).stream().map(CXMLStructureViewElement::new).collect(Collectors.toList()));
        treeElements.addAll(((List<CXMLTagBase>) Arrays.asList(childTags)).stream().map(CXMLStructureViewElement::new).collect(Collectors.toList()));
        return treeElements.toArray(new TreeElement[treeElements.size()]);
    }
}
