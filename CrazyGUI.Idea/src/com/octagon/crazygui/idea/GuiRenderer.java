package com.octagon.crazygui.idea;

import com.octagon.crazygui.util.LogHelper;
import groovyjarjarasm.asm.ClassReader;
import groovyjarjarasm.asm.tree.ClassNode;
import groovyjarjarasm.asm.tree.MethodNode;
import groovyjarjarasm.asm.tree.ParameterNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class GuiRenderer {
    public void render() {
        InputStream classInputStream = getClass().getResourceAsStream("/com/octagon/crazygui/components/GuiComponentPanel.class");

        try {
            ClassReader classReader = new ClassReader(classInputStream);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);

            for(MethodNode method : (List<MethodNode>)classNode.methods) {
                String s = method.signature;
                LogHelper.info("asd");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
