package com.octagon.crazygui.idea;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.ReadonlyStatusHandler;
import com.intellij.psi.PsiDocumentManager;
import com.octagon.crazygui.antlr.AttributeManager;
import com.octagon.crazygui.antlr.ComponentAttribute;
import com.octagon.crazygui.antlr.util.LogHelper;
import com.octagon.crazygui.idea.psi.CXMLAttribute;
import com.octagon.crazygui.idea.psi.CXMLTagBase;

import javax.swing.table.AbstractTableModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TagAttributes extends AbstractTableModel {
    private CXMLTagBase tag;
    public Map<String, ComponentAttribute> attributes;
    public Map<Integer, String> keyMap;

    public TagAttributes(CXMLTagBase tag, Map<String, ComponentAttribute> attributes) {
        this.tag = tag;
        this.attributes = attributes;
        keyMap = new HashMap<>();
        final int[] i = {0};
        this.attributes.entrySet().stream().forEach(a -> {
            keyMap.put(i[0], a.getKey());
            i[0]++;
        });
    }

    public void setAttribute(String name, ComponentAttribute value) {
        attributes.put(name, value);
    }

    public Object getAttribute(String name) {
        return attributes.containsKey(name) ? attributes.get(name) : null;
    }

    public Map<String, ComponentAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, ComponentAttribute> attributes) {
        this.attributes = attributes;
        keyMap.clear();
        final int[] i = {0};
        this.attributes.entrySet().stream().forEach(a -> {
            keyMap.put(i[0], a.getKey());
            i[0]++;
        });
    }

    @Override
    public int getRowCount() {
        return attributes.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex == 0) return keyMap.get(rowIndex);
        ComponentAttribute attribute = attributes.get(keyMap.get(rowIndex));
        if(attribute.getValue() == null)
            LogHelper.info("stuff");
        return attribute.getValueString();
    }

    @Override
    public String getColumnName(int column) {
        return column == 0 ? "Name" : "Value";
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(columnIndex == 0) return;
        ComponentAttribute attribute = attributes.get(keyMap.get(rowIndex));
        attribute.setValue(aValue);
        attribute.markDirty();
        updateAttributes();
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    private void updateAttributes() {
        List<ComponentAttribute> changed = attributes.values().stream().filter(ComponentAttribute::isDirty).collect(Collectors.toList());
        Document document = null;
        Project project = null;
        for(Project p : ProjectManager.getInstance().getOpenProjects()) {
            Document d = PsiDocumentManager.getInstance(p).getDocument(tag.getContainingFile());
            if(d != null) {
                document = d;
                project = p;
                break;
            }
        }
        if(document == null) return;
        final String[] tagText = {tag.getText()};
        for(ComponentAttribute a : changed) {
            boolean matched = false;
            for(CXMLAttribute ignored : tag.getAttributeList().stream().filter(b -> b.getName().equals(a.getName())).collect(Collectors.toList())) {
                if (tagText[0].matches("(.*)" + a.getName() + "(\\s+)?(=)?(\\s+)?(([0-9]+)|(\"[a-zA-Z0-9\\.\\(\\)\\s]*\")|(\"\\{[a-zA-Z0-9\\.\\(\\)]*\\}\"))?(.*)")) {
                    tagText[0] = tagText[0].replaceFirst(a.getName() + "(\\s+)?(=)?(\\s+)?(([0-9]+)|(\"[a-zA-Z0-9\\.\\(\\)\\s]*\")|(\"\\{[a-zA-Z0-9\\.\\(\\)]*\\}\"))?",
                            a.getParser() instanceof AttributeManager.PrimitiveParser && !(a.getParser() instanceof AttributeManager.BooleanParser) ?
                                    a.getName() + "=" + a.getValueString() : a.getName() + "=\"" + a.getValueString() + "\"");
                    matched = true;
                    break;
                }
            }
            if(matched) continue;
            if (tagText[0].endsWith("/>")) {
                tagText[0] = tagText[0].split("/>")[0];
                tagText[0] += " " + a.getName() + "=" + a.getValueString();
                tagText[0] += "/>";
            } else if (tagText[0].contains(">")) {
                String tempText = tagText[0].split(">")[0];
                tempText += " " + a.getName() + "=" + a.getValueString();
                tempText += ">";
                tagText[0] = tempText + tagText[0].substring(tagText[0].indexOf(">"));
            }
            attributes.values().stream().filter(b -> b.getName().equals(a.getName())).forEach(ComponentAttribute::markClean);
        }

        ReadonlyStatusHandler.getInstance(project).ensureFilesWritable(tag.getContainingFile().getVirtualFile());
        final Document finalDocument = document;
        final Project finalProject = project;
        ApplicationManager.getApplication().runWriteAction(() -> {
            CommandProcessor.getInstance().executeCommand(finalProject, () ->
                    finalDocument.replaceString(tag.getTextRange().getStartOffset(), tag.getTextRange().getEndOffset(), tagText[0]), "CrazyGUI.UpdateTagAttributes", null);
        });
    }
}