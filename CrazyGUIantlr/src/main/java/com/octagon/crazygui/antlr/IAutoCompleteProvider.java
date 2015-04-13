package com.octagon.crazygui.antlr;

import java.util.List;

public interface IAutoCompleteProvider {
    List<String> getAutoCompleteOptions(String existingText);
}
