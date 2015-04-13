package com.octagon.crazygui.idea;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiFile;
import com.octagon.crazygui.idea.psi.CXMLAttribute;
import com.octagon.crazygui.idea.psi.CXMLTagBase;
import org.jetbrains.annotations.NotNull;

public class CXMLStructureViewModel extends StructureViewModelBase implements StructureViewModel.ElementInfoProvider {
    public CXMLStructureViewModel(PsiFile psiFile) {
        super(psiFile, new CXMLStructureViewElement(psiFile));
    }

    @NotNull
    public Sorter[] getSorters() {
        return new Sorter[] {Sorter.ALPHA_SORTER};
    }


    @Override
    public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
        return element instanceof CXMLTagBase;
    }

    @Override
    public boolean isAlwaysLeaf(StructureViewTreeElement element) {
        return element instanceof CXMLAttribute;
    }
}
