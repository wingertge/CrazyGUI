package com.octagon.crazygui.idea;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

public class CXMLEditorProvider implements ApplicationComponent, FileEditorProvider {
    @Override
    public boolean accept(Project project, VirtualFile virtualFile) {
        return virtualFile.getFileType().equals(CXMLFileType.INSTANCE);
    }

    @NotNull
    @Override
    public FileEditor createEditor(Project project, VirtualFile virtualFile) {
        return new CXMLGuiEditor(project, virtualFile);
    }

    @Override
    public void disposeEditor(@NotNull FileEditor fileEditor) {

    }

    @NotNull
    @Override
    public FileEditorState readState(@NotNull Element element, @NotNull Project project, @NotNull VirtualFile virtualFile) {
        return (otherState, level) -> false;
    }

    @Override
    public void writeState(@NotNull FileEditorState fileEditorState, @NotNull Project project, @NotNull Element element) {

    }

    @NotNull
    @Override
    public String getEditorTypeId() {
        return "crazygui.GuiEditor";
    }

    @NotNull
    @Override
    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.PLACE_AFTER_DEFAULT_EDITOR;
    }

    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "crazygui.GuiEditorComponent";
    }
}
