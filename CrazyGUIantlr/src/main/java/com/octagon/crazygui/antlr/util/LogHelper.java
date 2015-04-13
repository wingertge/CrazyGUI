package com.octagon.crazygui.antlr.util;

import java.util.logging.Logger;

public class LogHelper {
    private static Logger log = Logger.getLogger("CrazyGUI");

    public static void info(Object o) { log.info(o.toString()); }
    public static void error(Object o) { log.severe(o.toString()); }
}
