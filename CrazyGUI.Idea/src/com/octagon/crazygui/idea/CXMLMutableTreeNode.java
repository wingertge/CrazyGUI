package com.octagon.crazygui.idea;

import javax.swing.tree.DefaultMutableTreeNode;

public class CXMLMutableTreeNode extends DefaultMutableTreeNode {
    private Object extraData = null;

    public CXMLMutableTreeNode(Object name, Object extraData) {
        super(name);
        this.extraData = extraData;
    }

    public Object getExtraData() {
        return extraData;
    }
}
