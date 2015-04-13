package com.octagon.crazygui.antlr.util;

import com.octagon.crazygui.antlr.proxy.IIDEProxy;
import com.octagon.crazygui.antlr.proxy.IdeaProxy;

public class ProxyUtils {
    public static IIDEProxy getProxy() {
        if(isIdea())
            return new IdeaProxy();
        else return null;
    }

    private static boolean isIdea() {
        try {
            Class.forName("com.intellij.openapi.vfs.VirtualFile");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
