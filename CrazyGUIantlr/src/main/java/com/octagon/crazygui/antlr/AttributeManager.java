package com.octagon.crazygui.antlr;

import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.util.IconLoader;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributeManager {
    private static Map<String, ICXMLSerializable> serializers = new HashMap<>();

    public static void registerCXMLSerializer(String qualifiedClassName, ICXMLSerializable serializer) {
        serializers.put(qualifiedClassName, serializer);
    }

    public static ICXMLSerializable getCodeParser() {
        return new CodeParser();
    }

    public static ICXMLSerializable<Enum> getEnumParser(String qualifiedClassName) {
        return new EnumParser(qualifiedClassName);
    }

    public static ICXMLSerializable getClassParser(String qualifiedClassName) {
        return serializers.containsKey(qualifiedClassName) ? serializers.get(qualifiedClassName) : null;
    }

    static {
        registerCXMLSerializer("java.lang.Byte", new ByteParser());
        registerCXMLSerializer("java.lang.Short", new ShortParser());
        registerCXMLSerializer("java.lang.Integer", new IntegerParser());
        registerCXMLSerializer("java.lang.Long", new LongParser());
        registerCXMLSerializer("java.lang.Boolean", new BooleanParser());
        registerCXMLSerializer("java.lang.Float", new FloatParser());
        registerCXMLSerializer("java.lang.Double", new DoubleParser());
    }

    public static class CodeParser implements ICXMLSerializable<String> {
        @Override
        public String deserialize(String string) {
            return string.replace("{", "").replace("}", "");
        }

        @Override
        public String serialize(String value) {
            return "{" + value + "}";
        }
    }

    public static class EnumParser implements ICXMLSerializable<Enum>, IAutoCompleteProvider, IGuiEditorProvider {
        private Class<? extends Enum> enumClass;

        public EnumParser(String enumClass) {
            try {
                this.enumClass = (Class<? extends Enum>) Class.forName(enumClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Enum deserialize(String string) {
            return Enum.valueOf(enumClass, string);
        }

        @Override
        public String serialize(Enum value) {
            return value.name();
        }

        @Override
        public List<String> getAutoCompleteOptions(String existingText) {
            return new ArrayList<>();
        }

        @Override
        public JComponent getSwingEditor(ComponentAttribute attribute) {
            ComboBox comboBox = new ComboBox();
            Enum[] enums = new Enum[0];
            try {
                Method method = enumClass.getDeclaredMethod("values");
                enums = (Enum[]) method.invoke(null);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }

            for(Enum e : enums) {
                comboBox.addItem(e.name());
            }
            return comboBox;
        }

        @Override
        public JComponent getSwingDisplay(ComponentAttribute attribute) {
            ComboBox comboBox = new ComboBox();
            Enum[] enums = new Enum[0];
            try {
                Method method = enumClass.getDeclaredMethod("values");
                enums = (Enum[]) method.invoke(null);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }

            for(Enum e : enums) {
                comboBox.addItem(e.name());
            }
            comboBox.setSelectedItem(((Enum)attribute.getValue()).name());
            return comboBox;
        }

        @Override
        public void registerValueChangedListener(JComponent component, IValueChangedListener listener, TableCellEditor editor) {
            ComboBox box = (ComboBox)component;
            box.addActionListener(a -> {
                listener.updateValue(deserialize((String) ((ComboBox) a.getSource()).getSelectedItem()));
                editor.stopCellEditing();
            });
        }
    }

    public abstract static class PrimitiveParser<T> implements ICXMLSerializable<T>, IGuiEditorProvider, ICustomPresentation {
        private static final Icon numericIcon = IconLoader.getIcon("/com/octagon/crazygui/idea/icons/numericValueIcon.png");

        @Override
        public String serialize(T value) {
            return value.toString();
        }

        @Override
        public JComponent getSwingEditor(ComponentAttribute attribute) {
            return new JTextField(attribute.getValueString());
        }

        @Override
        public JComponent getSwingDisplay(ComponentAttribute attribute) {
            return new JLabel(attribute.getValueString());
        }

        @Override
        public void registerValueChangedListener(JComponent component, IValueChangedListener listener, TableCellEditor editor) {
            JTextField textField = (JTextField)component;
            textField.addActionListener(a -> {
                listener.updateValue(deserialize(((JTextField) a.getSource()).getText()));
                editor.stopCellEditing();
            });
        }

        @Override
        public ItemPresentation getPresentation(String name) {
            return new CXMLItemPresentation(name, numericIcon);
        }
    }

    public static class ByteParser extends PrimitiveParser<Byte> {

        @Override
        public Byte deserialize(String string) {
            return Byte.parseByte(string);
        }
    }

    public static class ShortParser extends PrimitiveParser<Short> {

        @Override
        public Short deserialize(String string) {
            return Short.parseShort(string);
        }
    }

    public static class IntegerParser extends PrimitiveParser<Integer> {

        @Override
        public Integer deserialize(String string) {
            return Integer.parseInt(string);
        }
    }

    public static class LongParser extends PrimitiveParser<Long> {

        @Override
        public Long deserialize(String string) {
            return Long.parseLong(string);
        }
    }

    public static class BooleanParser extends PrimitiveParser<Boolean> {
        private static final Icon booleanIcon = IconLoader.getIcon("/com/octagon/crazygui/idea/icons/booleanValueIcon.png");

        @Override
        public Boolean deserialize(String string) {
            return Boolean.parseBoolean(string);
        }

        @Override
        public JComponent getSwingEditor(ComponentAttribute attribute) {
            return new JCheckBox("", (Boolean) attribute.getValue());
        }

        @Override
        public JComponent getSwingDisplay(ComponentAttribute attribute) {
            return new JCheckBox("", (Boolean) attribute.getValue());
        }

        @Override
        public void registerValueChangedListener(JComponent component, IValueChangedListener listener, TableCellEditor editor) {
            JCheckBox checkBox = (JCheckBox)component;
            checkBox.addActionListener(a -> {
                listener.updateValue(checkBox.isSelected());
                editor.stopCellEditing();
            });
        }

        @Override
        public ItemPresentation getPresentation(String name) {
            return new CXMLItemPresentation(name, booleanIcon);
        }
    }

    public static class FloatParser extends PrimitiveParser<Float> {

        @Override
        public Float deserialize(String string) {
            return Float.parseFloat(string);
        }
    }

    public static class DoubleParser extends PrimitiveParser<Double> {

        @Override
        public Double deserialize(String string) {
            return Double.parseDouble(string);
        }
    }
}
