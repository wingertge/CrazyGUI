package com.octagon.crazygui.idea.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.octagon.crazygui.idea.CreateCXMLDialogue;

public class CreateGuiFileAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        CreateCXMLDialogue dialogue = new CreateCXMLDialogue(anActionEvent);
        dialogue.pack();
        dialogue.setVisible(true);
    }
}
