package com.octagon.crazygui.idea.icons;

import com.octagon.crazygui.idea.CrazyGUIIcons;

import javax.swing.*;

public enum Icons {
    SELF_CONTAINED_TAG(CrazyGUIIcons.FILE),
    TAG(CrazyGUIIcons.CONTAINER),
    ROOT_TAG(CrazyGUIIcons.FILE),
    TAG_LEAF(CrazyGUIIcons.FILE),
    ATTRIBUTE(CrazyGUIIcons.STRING_VALUE);

    private Icon icon;

    Icons(Icon file) {
        this.icon = file;
    }

    public Icon getIcon() {
        return icon;
    }
}
