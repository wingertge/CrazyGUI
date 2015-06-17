package com.octagon.crazygui.antlr;

import com.intellij.navigation.ItemPresentation;

public interface ICustomPresentation {
    ItemPresentation getPresentation(String name);
}
