package com.octagon.crazygui.idea;

import com.intellij.openapi.vfs.VirtualFile;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.SimpleResource;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IdeaDomainResourceManager implements IResourceManager {
    private final Set<VirtualFile> fileRoots;

    public IdeaDomainResourceManager(Set<VirtualFile> fileRoots) {
        this.fileRoots = fileRoots;
    }

    @Override
    public Set getResourceDomains() {
        return null;
    }

    @Override
    public IResource getResource(ResourceLocation location) throws IOException {
        for(VirtualFile root : fileRoots) {
            VirtualFile file = root.findFileByRelativePath(location.getResourcePath());
            if(file == null) continue;
            InputStream stream = file.getInputStream();
            return new SimpleResource(location, stream, null, new IMetadataSerializer());
        }
        throw new FileNotFoundException(location.toString());
    }

    @Override
    public List getAllResources(ResourceLocation location) throws IOException {
        List<IResource> resources = new ArrayList<>();
        resources.add(getResource(location));
        return resources;
    }
}
