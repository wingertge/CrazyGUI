package com.octagon.crazygui;

import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.octagon.crazygui.antlr.*;
import com.octagon.crazygui.antlr.util.FileUtil;
import com.octagon.crazygui.reference.Reference;
import com.octagon.crazygui.util.ResourceLocationHelper;
import net.minecraft.util.ResourceLocation;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.octagon.crazygui.antlr.AttributeManager.registerCXMLSerializer;

public class CrazyGUIAttributeManager {
    public static void init() {
        registerCXMLSerializer("net.minecraft.util.ResourceLocation", new ResourceLocationSerializer());
    }

    public static class ResourceLocationSerializer implements ICXMLSerializable<ResourceLocation>, IAutoCompleteProvider, IGuiEditorProvider {

        @Override
        public List<String> getAutoCompleteOptions(String existingText) {
            if(!existingText.contains(":")) {
                return new ArrayList<>(FileUtil.getModAssetDirs().keySet());
            } else existingText = existingText.substring(existingText.lastIndexOf(':') + 1);
            if(existingText.contains("/")) existingText = existingText.substring(0, existingText.lastIndexOf("/"));
            else existingText = "";
            return FileUtil.listAssetDirContents(existingText);
        }

        @Override
        public ResourceLocation deserialize(String string) {
            return string.contains(":") ? ResourceLocationHelper.getResourceLocation(string.split(":")[0], string.split(":")[1]) : ResourceLocationHelper.getResourceLocation(string);
        }

        @Override
        public String serialize(ResourceLocation value) {
            if(value.getResourcePath().contains(":") && value.getResourcePath().split(":")[0].equalsIgnoreCase(Reference.MOD_ID))
                return value.getResourcePath().split(":")[1];
            return value.getResourcePath();
        }

        @Override
        public JComponent getSwingEditor(ComponentAttribute attribute) {
            String currentText = attribute.getValueString();
            TextFieldWithBrowseButton browseField = new TextFieldWithBrowseButton();
            Map<String, String> assetDirs = FileUtil.getModAssetDirs();
            if(currentText.isEmpty()) return browseField;
            if(!currentText.contains(":")) currentText = Reference.MOD_ID.toLowerCase() + ":" + currentText;
            if(!assetDirs.containsKey(currentText.split(":")[0])) return browseField;
            String url = assetDirs.get(currentText.split(":")[0]);
            url += "/" + currentText.split(":")[1];
            browseField.setText(url);
            return browseField;
        }

        @Override
        public JComponent getSwingDisplay(ComponentAttribute attribute) {
            String currentText = attribute.getValueString();
            TextFieldWithBrowseButton browseField = new TextFieldWithBrowseButton();
            Map<String, String> assetDirs = FileUtil.getModAssetDirs();
            if(currentText.isEmpty()) return browseField;
            if(!currentText.contains(":")) currentText = Reference.MOD_ID.toLowerCase() + ":" + currentText;
            if(!assetDirs.containsKey(currentText.split(":")[0])) return browseField;
            String url = assetDirs.get(currentText.split(":")[0]);
            url += "/" + currentText.split(":")[1];
            browseField.setText(url);
            return browseField;
        }

        @Override
        public void registerValueChangedListener(JComponent component, IValueChangedListener listener, TableCellEditor editor) {
            TextFieldWithBrowseButton textBox = (TextFieldWithBrowseButton)component;
            textBox.getTextField().addActionListener(a -> {
                JTextField textField = (JTextField) a.getSource();
                String currentText = textField.getText();
                Map<String, String> assetDirs = FileUtil.getModAssetDirs();
                String modIdentifier = Reference.MOD_ID.toLowerCase();
                if(assetDirs.values().stream().anyMatch(b -> b.contains(currentText))) modIdentifier = assetDirs.entrySet().stream().filter(b -> b.getValue().contains(currentText)).map(b -> b.getKey()).findFirst().orElse("");
                if(!assetDirs.containsKey(modIdentifier)) {
                    listener.updateValue(deserialize(""));
                    return;
                }
                String relativePath = currentText.replace(assetDirs.get(modIdentifier), "");
                String totalPath = modIdentifier + ":" + relativePath;
                listener.updateValue(deserialize(totalPath));
            });
        }
    }
}
