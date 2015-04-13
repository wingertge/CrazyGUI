package com.octagon.crazygui.config;

import com.octagon.crazygui.CrazyGUICmd;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigHandler {
    private static Properties config;

    public static String guiDir = "src/main/resources/assets/masseffectships/gui";
    public static String javaDir = "src/main/java";
    public static String guiPackage = "com.octagon.airships.client.gui";
    public static String tileEntitiesPackage = "com.octagon.airships.block.tileentity";
    public static String generatedPackage = "com.octagon.airships.client.gui.test";

    public static void init(File configFile) {
        InputStream input = null;
        OutputStream output = null;

        try {
            config = new Properties();
            InputStream is = null;

            // First try loading from the current directory
            try {
                File f = new File("CrazyGUI.properties");
                is = new FileInputStream( f );
            }
            catch ( Exception e ) { is = null; }

            try {
                if ( is == null ) {
                    if(!Files.exists(Paths.get("CrazyGUI.properties")))
                        Files.createFile(Paths.get("CrazyGUI.properties"));

                    // Try loading from classpath
                    is = CrazyGUICmd.class.getResourceAsStream("CrazyGUI.properties");
                }

                // Try loading properties from the file (if found)
                config.load(is);
                output = new FileOutputStream("CrazyGUI.properties");
            }
            catch ( Exception e ) { }

            guiDir = config.getProperty("guiDir", guiDir);
            javaDir = config.getProperty("javaDir", javaDir);
            guiPackage = config.getProperty("guiPackage", guiPackage);
            tileEntitiesPackage = config.getProperty("tileEntitiesPackage", tileEntitiesPackage);
            generatedPackage = config.getProperty("generatedPackage", generatedPackage);

            if(output != null)
                config.store(output, "");
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
