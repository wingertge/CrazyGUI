package com.octagon.crazygui.idea;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import scala.actors.threadpool.Arrays;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class IdeaResourceManager implements IResourceManager {
    private Map<String, IResourceManager> domainMap = new HashMap<>();

    public IdeaResourceManager() {
        reloadResources();
    }

    private void reloadResources() {
        this.domainMap.clear();
        Map<String, Set<VirtualFile>> domainMap = new HashMap<>();
        for(Project project : ProjectManager.getInstance().getOpenProjects()) {
            VirtualFile file = project.getBaseDir().findFileByRelativePath("src/main/resources/assets");
            if(file == null || !file.isDirectory()) continue;
            Set<VirtualFile> fileRoots = domainMap.get(file.getName().toLowerCase());
            if(fileRoots == null) fileRoots = new HashSet<>();
            fileRoots.add(file);
            domainMap.put(file.getName().toLowerCase(), fileRoots);
        }

        for(Map.Entry<String, Set<VirtualFile>> entry : domainMap.entrySet()) {
            this.domainMap.put(entry.getKey(), new IdeaDomainResourceManager(entry.getValue()));
        }
    }

    @Override
    public Set getResourceDomains() {
        return domainMap.keySet();
    }

    @Override
    public IResource getResource(ResourceLocation location) throws IOException {
        if(!domainMap.containsKey(location.getResourceDomain().toLowerCase())) throw new FileNotFoundException(location.toString());
        return domainMap.get(location.getResourceDomain().toLowerCase()).getResource(location);
    }

    @Override
    public List getAllResources(ResourceLocation location) throws IOException {
        if(!domainMap.containsKey(location.getResourceDomain())) throw new IOException(location.toString());
        return domainMap.get(location.getResourceDomain()).getAllResources(location);
    }
}
