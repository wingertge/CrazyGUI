package com.octagon.crazygui.antlr.proxy;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IdeaProxy implements IIDEProxy {
    private List<String> assetDirs = null;

    @Override
    public Map<String, String> getModAssetDirs() {
        if(assetDirs == null) {
            assetDirs = new ArrayList<>();
            walkDirs("");
        }
        return assetDirs.stream().filter(a -> VirtualFileManager.getInstance().findFileByUrl(a) != null).collect(Collectors.toMap(a -> VirtualFileManager.getInstance().findFileByUrl(a).getName(), a -> a));
    }

    public List<String> getModAssetUrls() {
        return assetDirs;
    }

    private void walkDirs(String path) {
        for(Project p : ProjectManager.getInstance().getOpenProjects()) {
            VirtualFile file = p.getBaseDir().findFileByRelativePath(path);
            if(file != null && file.isDirectory()) {
                if(file.getName().equalsIgnoreCase("assets")) {
                    for(VirtualFile subDir : file.getChildren()) {
                        if(subDir.isDirectory()) assetDirs.add(subDir.getUrl());
                    }
                } else {
                    walkDirs(file.getPath());
                }
            }
        }
    }

    @Override
    public List<String> listAssetDirContents(String path) {
        List<String> contents = new ArrayList<>();
        for(String url : getModAssetDirs().values()) {
            VirtualFile dir = VirtualFileManager.getInstance().findFileByUrl(url);
            if(dir == null || !dir.isDirectory()) continue;
            VirtualFile subDir = dir.findFileByRelativePath(path);
            if(subDir == null || !dir.isDirectory()) continue;
            for(VirtualFile file : subDir.getChildren()) {
                contents.add(dir.getName() + ":" + file.getName());
            }
        }
        return contents;
    }
}
