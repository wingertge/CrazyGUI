package com.octagon.crazygui.idea;

import com.octagon.crazygui.antlr.ComponentAttribute;
import com.octagon.crazygui.antlr.IGuiEditorProvider;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Map;

public class MyTableCellRenderer extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    Object value;
    ComponentAttribute attribute;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(column == 0) return new JLabel((String) value);
        String name = (String) table.getModel().getValueAt(row, 0);
        Map<String, ComponentAttribute> attributes = ((TagAttributes)table.getModel()).attributes;
        if(attributes.containsKey(name)) {
            this.value = attributes.get(name).getValue();
            this.attribute = attributes.get(name);
        }
        if(!attributes.containsKey(name) || !(attributes.get(name).getParser() instanceof IGuiEditorProvider)) {
            table.setRowHeight(row, Math.max((int) new JTextField((String)value).getPreferredSize().getHeight(), table.getRowHeight(row)));
            return new JLabel((String) value);
        }
        JComponent editor = ((IGuiEditorProvider) attributes.get(name).getParser()).getSwingEditor(this.attribute);
        JComponent renderer = ((IGuiEditorProvider) attributes.get(name).getParser()).getSwingDisplay(this.attribute);
        int height = Math.max(table.getRowHeight(row), Math.max((int) editor.getPreferredSize().getHeight(), (int)renderer.getPreferredSize().getHeight()));
        table.setRowHeight(row, height);
        return renderer;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if(column == 0) return new JLabel((String) value);
        String name = (String) table.getModel().getValueAt(row, 0);
        Map<String, ComponentAttribute> attributes = ((TagAttributes)table.getModel()).attributes;
        if(attributes.containsKey(name)) {
            this.value = attributes.get(name).getValue();
            this.attribute = attributes.get(name);
        }
        if(!attributes.containsKey(name) || !(attributes.get(name).getParser() instanceof IGuiEditorProvider)) {
            table.setRowHeight(row, Math.max((int)new JTextField((String)value).getPreferredSize().getHeight(), table.getRowHeight(row)));
            JTextField textField = new JTextField((String)value);
            textField.addActionListener(a -> {
                updateValue(this.attribute.getParser().deserialize(((JTextField) a.getSource()).getText()));
                stopCellEditing();
            });
            return textField;
        }
        JComponent editor = ((IGuiEditorProvider) attributes.get(name).getParser()).getSwingEditor(this.attribute);
        JComponent renderer = ((IGuiEditorProvider) attributes.get(name).getParser()).getSwingDisplay(this.attribute);
        ((IGuiEditorProvider) attributes.get(name).getParser()).registerValueChangedListener(editor, this::updateValue, this);
        int height = Math.max(table.getRowHeight(row), Math.max(editor.getHeight(), renderer.getHeight()));
        table.setRowHeight(row, height);
        return editor;
    }

    @Override
    public Object getCellEditorValue() {
        return value;
    }

    private void updateValue(Object aValue) {
        this.value = aValue;
        this.attribute.setValue(aValue);
    }
}
