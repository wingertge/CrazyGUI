package com.octagon.crazygui.idea.actions;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFile;
import com.octagon.crazygui.idea.RunCompilerOnXMLFile;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class ConfigCompilerDialogue extends JDialog {
    public static String PROP_JAVA_DIR = "javaDir";
    public static String PROP_GUI_DIR = "guiDir";
    public static String PROP_GUI_PACKAGE = "guiPackage";
    public static String PROP_TILEENTITIES_PACKAGE = "tileEntitiesPackage";
    public static String PROP_OUTPUT_PACKAGE = "outputPackage";
    public static String PROP_COMPILE_ON_SAVE = "compileOnSave";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private TextFieldWithBrowseButton guiDirText;
    private TextFieldWithBrowseButton javaDirText;
    private JTextField guiPackageText;
    private JTextField tileEntitiesPackageText;
    private JTextField generatedPackageText;
    private JCheckBox autoCompileCheckBox;
    private JButton applyButton;

    private Project project;

    public ConfigCompilerDialogue(Project project) {
        this.project = project;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        FileChooserDescriptor guiDirChooser = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        guiDirText.addBrowseFolderListener("Select gui xml directory", null, project, guiDirChooser);
        guiDirText.setTextFieldPreferredWidth(50);

        FileChooserDescriptor javaDirChooser = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        javaDirText.addBrowseFolderListener("Select Java Source Directory", null, project, javaDirChooser);
        javaDirText.setTextFieldPreferredWidth(50);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
        applyButton.addActionListener(a -> onApply());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        loadValues(project);
    }

    private void onOK() {
        // add your code here
        onApply();
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void onApply() {
        saveValues(project);
    }

    public static String getParentDir(VirtualFile vfile) {
        return vfile.getParent().getPath();
    }

    public static VirtualFile getContentRoot(Project project, VirtualFile vfile) {
        VirtualFile root = ProjectRootManager.getInstance(project).getFileIndex().getContentRootForFile(vfile);
        if ( root!=null ) return root;
        return vfile.getParent();
    }

    public static String getOutputDirName(Project project, VirtualFile contentRoot, String package_) {
        String outputDirName = contentRoot.getPath() + File.separator + RunCompilerOnXMLFile.OUTPUT_DIR_NAME;
        outputDirName = getProp(project, PROP_JAVA_DIR, outputDirName);
        if ( !package_.isEmpty() ) {
            outputDirName += File.separator+package_.replace('.', File.separatorChar);
        }
        return outputDirName;
    }

    public void loadValues(Project project) {
        PropertiesComponent props = PropertiesComponent.getInstance(project);
        String s;
        boolean b;
        b = props.getBoolean(PROP_COMPILE_ON_SAVE, false);
        autoCompileCheckBox.setSelected(b);

        s = props.getValue(PROP_JAVA_DIR, "");
        javaDirText.setText(s);
        s = props.getValue(PROP_GUI_DIR, "");
        guiDirText.setText(s);
        s = props.getValue(PROP_GUI_PACKAGE, "");
        guiPackageText.setText(s);
        s = props.getValue(PROP_TILEENTITIES_PACKAGE, "");
        tileEntitiesPackageText.setText(s);
        s = props.getValue(PROP_OUTPUT_PACKAGE, "");
        generatedPackageText.setText(s);
    }

    public void saveValues(Project project) {
        String v;
        PropertiesComponent props = PropertiesComponent.getInstance(project);

        props.setValue(PROP_COMPILE_ON_SAVE,
                String.valueOf(autoCompileCheckBox.isSelected()));

        v = javaDirText.getText();
        if ( v.trim().length()>0 ) {
            props.setValue(PROP_JAVA_DIR, v);
        }
        else {
            props.unsetValue(PROP_JAVA_DIR);
        }

        v = guiDirText.getText();
        if ( v.trim().length()>0 ) {
            props.setValue(PROP_GUI_DIR, v);
        }
        else {
            props.unsetValue(PROP_GUI_DIR);
        }

        v = guiPackageText.getText();
        if ( v.trim().length()>0 ) {
            props.setValue(PROP_GUI_PACKAGE, v);
        }
        else {
            props.unsetValue(PROP_GUI_PACKAGE);
        }

        v = tileEntitiesPackageText.getText();
        if ( v.trim().length()>0 ) {
            props.setValue(PROP_TILEENTITIES_PACKAGE, v);
        }
        else {
            props.unsetValue(PROP_TILEENTITIES_PACKAGE);
        }

        v = generatedPackageText.getText();
        if ( v.trim().length()>0 ) {
            props.setValue(PROP_OUTPUT_PACKAGE, v);
        }
        else {
            props.unsetValue(PROP_OUTPUT_PACKAGE);
        }
    }

    public static String getProp(Project project, String name, String defaultValue) {
        PropertiesComponent props = PropertiesComponent.getInstance(project);
        String v = props.getValue(name);
        if ( v==null || v.trim().length()==0 ) return defaultValue;
        return v;
    }

    public static boolean getBooleanProp(Project project, String name, boolean defaultValue) {
        PropertiesComponent props = PropertiesComponent.getInstance(project);
        return props.getBoolean(name, defaultValue);
    }
}
