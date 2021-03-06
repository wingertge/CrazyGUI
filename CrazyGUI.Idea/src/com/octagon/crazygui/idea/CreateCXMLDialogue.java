package com.octagon.crazygui.idea;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.octagon.crazygui.idea.actions.CreateCXMLAction;

import javax.swing.*;
import java.awt.event.*;

public class CreateCXMLDialogue extends JDialog {
    private AnActionEvent event;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fileNameField;
    private JComboBox template;

    public CreateCXMLDialogue(AnActionEvent event) {
        this.event = event;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        if(!fileNameField.getText().isEmpty())
            new CreateCXMLAction(fileNameField.getText()).actionPerformed(event);
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
