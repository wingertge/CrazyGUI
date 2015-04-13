package com.octagon.crazygui.antlr.proxy;

import java.util.List;
import java.util.Map;

public interface IIDEProxy {
    Map<String, String> getModAssetDirs();
    List<String> listAssetDirContents(String path);
}
