package com.octagon.crazygui;

import com.octagon.crazygui.reference.Reference;

public class CrazyGUI {
    public static void init(String modId) {
        Reference.MOD_ID = modId;
        CrazyGUIAttributeManager.init();
    }
}
