package com.octagon.crazygui.antlr.util;

import com.octagon.crazygui.antlr.proxy.IIDEProxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtil {
    private static IIDEProxy proxy = ProxyUtils.getProxy();

    public static Map<String, String> getModAssetDirs() {
        return proxy != null ? proxy.getModAssetDirs() : new HashMap<>();
    }

    public static List<String> listAssetDirContents(String path) {
        return proxy != null ? proxy.listAssetDirContents(path) : new ArrayList<>();
    }
}
