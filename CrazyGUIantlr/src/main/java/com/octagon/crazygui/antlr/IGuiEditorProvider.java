package com.octagon.crazygui.antlr;

import javax.swing.*;
import javax.swing.table.TableCellEditor;

public interface IGuiEditorProvider {
    JComponent getSwingEditor(ComponentAttribute attribute);
    JComponent getSwingDisplay(ComponentAttribute attribute);
    void registerValueChangedListener(JComponent component, IValueChangedListener listener, TableCellEditor editor);
}
