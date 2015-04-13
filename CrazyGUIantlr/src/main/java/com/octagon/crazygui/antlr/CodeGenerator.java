package com.octagon.crazygui.antlr;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

public class CodeGenerator {
    private final String filePath;

    public CodeGenerator(String filePath) {
        this.filePath = filePath;
    }

    public String getOutputFileName() {
        String strippedName = filePath.substring(filePath.lastIndexOf(File.separatorChar) + 1, filePath.lastIndexOf('.'));
        return "AbstractGui" + Character.toUpperCase(strippedName.charAt(0)) + strippedName.substring(1) + ".java";
    }

    public List<String> generateCode(String guiPackage, String generatedPackage, String tileEntitiesPackage) throws IOException {
        Path xmlPath = Paths.get(filePath);
        if(!Files.exists(xmlPath)) return new ArrayList<>();

        CrazyGUILexer lexer = new CrazyGUILexer(new ANTLRInputStream(Files.newInputStream(xmlPath)));
        CrazyGUIParser parser = new CrazyGUIParser(new CommonTokenStream(lexer));

        try {
            parser.removeErrorListeners();
            parser.addErrorListener(new ANTLRErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                    throw new IllegalStateException();
                }

                @Override
                public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {

                }

                @Override
                public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {

                }

                @Override
                public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {

                }
            });

            ParseTree tree = parser.tag();
            GuiVisitor visitor = new GuiVisitor(xmlPath.getFileName().toString().split("\\.")[0], guiPackage, generatedPackage, tileEntitiesPackage);
            String result = visitor.visit(tree);
            return Arrays.asList(result.split("\\n"));
        } catch (IllegalStateException e) {
            return new ArrayList<>();
        }
    }
}
