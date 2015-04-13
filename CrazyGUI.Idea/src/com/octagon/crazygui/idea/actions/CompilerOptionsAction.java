package com.octagon.crazygui.idea.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbAware;

public class CompilerOptionsAction extends AnAction implements DumbAware {
    public static final Logger LOG = Logger.getInstance("CrazyGUI CompilerOptionsAction");

    public void actionPerformed(AnActionEvent e) {
        if ( e.getProject()==null ) {
            LOG.error("actionPerformed no project for "+e);
            return; // whoa!
        }

        ConfigCompilerDialogue configDialogue = new ConfigCompilerDialogue(e.getProject());
        configDialogue.pack();
        configDialogue.setVisible(true);
    }
}
