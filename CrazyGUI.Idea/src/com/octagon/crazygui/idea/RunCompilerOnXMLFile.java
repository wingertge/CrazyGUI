package com.octagon.crazygui.idea;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.octagon.crazygui.antlr.CodeGenerator;
import com.octagon.crazygui.idea.actions.ConfigCompilerDialogue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class RunCompilerOnXMLFile extends Task.Modal {
    public static final Logger LOG = Logger.getInstance("RunCompilerOnXMLFile");
    public static String OUTPUT_DIR_NAME = "src/main/java";
    public static final String groupDisplayId = "CrazyGUI Code Generation";
    private final VirtualFile xmlFile;
    private final Project project;
    private final boolean forceGeneration;

    public RunCompilerOnXMLFile(VirtualFile xmlFile,
                                @Nullable final Project project,
                                @NotNull final String title,
                                final boolean canBeCancelled,
                                boolean forceGeneration) {
        super(project, title, canBeCancelled);
        this.xmlFile = xmlFile;
        this.project = project;
        this.forceGeneration = forceGeneration;
    }

    @Override
    public void run(ProgressIndicator indicator) {
        indicator.setIndeterminate(true);
        boolean autoGen = ConfigCompilerDialogue.getBooleanProp(project, ConfigCompilerDialogue.PROP_COMPILE_ON_SAVE, false);

        if ( forceGeneration || (autoGen && isXMLStale()) ) {
            compile(xmlFile);
        }
    }

    public boolean isXMLStale() {
        String qualFileName = xmlFile.getPath();
        String sourcePath = ConfigCompilerDialogue.getParentDir(xmlFile);
        String fullyQualifiedInputFileName = sourcePath + File.separator + xmlFile.getName();

        CodeGenerator generator = new CodeGenerator(xmlFile.getPath());
        String outputFileName = generator.getOutputFileName();

        VirtualFile contentRoot = ConfigCompilerDialogue.getContentRoot(project, xmlFile);
        String package_ = ConfigCompilerDialogue.getProp(project, ConfigCompilerDialogue.PROP_OUTPUT_PACKAGE, "");
        String outputDirName = ConfigCompilerDialogue.getOutputDirName(project, contentRoot, package_);
        String fullyQualifiedOutputFileName = outputDirName + File.separator + outputFileName;

        File inF = new File(fullyQualifiedInputFileName);
        File outF = new File(fullyQualifiedOutputFileName);
        boolean stale = inF.lastModified() > outF.lastModified();
        LOG.info((!stale ? "not" : "") + "stale: " + fullyQualifiedInputFileName + " -> " + fullyQualifiedOutputFileName);
        return stale;
    }

    public void compile(VirtualFile vFile) {
        if ( vFile==null ) return;

        LOG.info("compile(\""+vFile.getPath()+"\")");

        String sourcePath = ConfigCompilerDialogue.getParentDir(vFile);
        String fullyQualifiedInputFileName = sourcePath + File.separator + vFile.getName();

        VirtualFile contentRoot = ConfigCompilerDialogue.getContentRoot(project, xmlFile);
        String package_ = ConfigCompilerDialogue.getProp(project, ConfigCompilerDialogue.PROP_OUTPUT_PACKAGE, "com.octagon.airships.client.gui.test");
        String outputDirName = ConfigCompilerDialogue.getOutputDirName(project, contentRoot, package_);

        CodeGenerator generator = new CodeGenerator(fullyQualifiedInputFileName);
        String fullyQualifiedOutputFileName = outputDirName + File.separator + generator.getOutputFileName();
        String guiPackage = ConfigCompilerDialogue.getProp(project, ConfigCompilerDialogue.PROP_GUI_PACKAGE, "com.octagon.airships.client.gui");
        String tileEntitiesPackage = ConfigCompilerDialogue.getProp(project, ConfigCompilerDialogue.PROP_TILEENTITIES_PACKAGE, "com.octagon.airships.block.tileentity");

        try {
            List<String> lines = generator.generateCode(guiPackage, package_, tileEntitiesPackage);
            if(lines.size() == 0) return;

            if(!Files.exists(Paths.get(fullyQualifiedOutputFileName))) {
                Files.createDirectories(Paths.get(fullyQualifiedOutputFileName).getParent());
                Files.createFile(Paths.get(fullyQualifiedOutputFileName));
            }
            Files.write(Paths.get(fullyQualifiedOutputFileName), lines);

            String vPath = ConfigCompilerDialogue.getProp(project, ConfigCompilerDialogue.PROP_JAVA_DIR, outputDirName);
            if(!package_.isEmpty()) vPath += File.separatorChar + package_.replace('.', File.separatorChar);
            vPath += File.separatorChar + generator.getOutputFileName();
            vPath = vPath.substring(project.getBaseDir().getPath().length());
            VirtualFile file = project.getBaseDir().findFileByRelativePath(vPath.replace('\\', '/'));
            if(file != null) {
                file.refresh(true, false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getOutputDirName() {
        VirtualFile contentRoot = ConfigCompilerDialogue.getContentRoot(project, xmlFile);
        String package_ = ConfigCompilerDialogue.getProp(project, ConfigCompilerDialogue.PROP_OUTPUT_PACKAGE, "com.octagon.airships.client.gui.test");
        if ( package_==null ) {
            package_ = "com.octagon.airships.client.gui.test";
        }
        return ConfigCompilerDialogue.getOutputDirName(project, contentRoot, package_);
    }
}
