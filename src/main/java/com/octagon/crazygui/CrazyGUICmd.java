package com.octagon.crazygui;

import com.octagon.crazygui.antlr.CrazyGUILexer;
import com.octagon.crazygui.antlr.CrazyGUIParser;
import com.octagon.crazygui.antlr.GuiVisitor;
import com.octagon.crazygui.config.ConfigHandler;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.Arrays;
import java.util.BitSet;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class CrazyGUICmd {
    public static void main(String[] args) throws InterruptedException {
        try {
            File configFile = new File("CrazyGUICmd.cfg");
            ConfigHandler.init(configFile);

            Files.walk(Paths.get(ConfigHandler.guiDir)).filter(a -> a.toString().toLowerCase().endsWith(".xml")).forEach(a -> {
                try {
                    CrazyGUILexer guiLexer = new CrazyGUILexer(new ANTLRInputStream(Files.newInputStream(a)));
                    CrazyGUIParser guiParser = new CrazyGUIParser(new CommonTokenStream(guiLexer));
                    guiParser.addErrorListener(new ANTLRErrorListener() {
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

                    ParseTree tree = guiParser.tag();
                    GuiVisitor visitor = new GuiVisitor(a.getFileName().toString().split("\\.")[0], ConfigHandler.guiPackage, ConfigHandler.generatedPackage, ConfigHandler.tileEntitiesPackage);
                    String result = visitor.visit(tree);
                    String filename = a.getFileName().toString().split("\\.")[0];
                    filename = Character.toUpperCase(filename.charAt(0)) + filename.substring(1) + ".java";
                    filename = "AbstractGui" + filename;
                    Path path = Paths.get(ConfigHandler.javaDir, ConfigHandler.generatedPackage.replace(".", "/"), filename);
                    if (!Files.exists(path))
                        Files.createFile(path);
                    Files.write(path, Arrays.asList(result.split("\\n")), Charset.defaultCharset());
                } catch (IllegalStateException e) {

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            Path guiDir = Paths.get(ConfigHandler.guiDir);
            WatchService watcher = guiDir.getFileSystem().newWatchService();
            MonitorDirectoryThread handler = new MonitorDirectoryThread(watcher);
            Thread thread = new Thread(handler, "CrazyGUIFileWatcher");
            thread.start();

            // register a file
            guiDir.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
            thread.join();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class MonitorDirectoryThread implements Runnable {
        private final WatchService watcher;

        MonitorDirectoryThread(WatchService watcher) {

            this.watcher = watcher;
        }

        @Override
        public void run() {
            try {
                // get the first event before looping
                WatchKey key = watcher.take();
                while(key != null) {
                    // we have a polled event, now we traverse it and
                    // receive all the states from it
                    for (WatchEvent event : key.pollEvents()) {
                        try {
                            Path file = Paths.get(ConfigHandler.guiDir, event.context().toString());
                            if (!Files.exists(file)) continue;
                            CrazyGUILexer guiLexer = new CrazyGUILexer(new ANTLRInputStream(Files.newInputStream(file)));
                            CrazyGUIParser guiParser = new CrazyGUIParser(new CommonTokenStream(guiLexer));
                            guiParser.removeErrorListeners();
                            guiParser.addErrorListener(new ANTLRErrorListener() {
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

                            ParseTree tree = guiParser.tag();
                            GuiVisitor visitor = new GuiVisitor(file.getFileName().toString().split("\\.")[0], ConfigHandler.guiPackage, ConfigHandler.generatedPackage, ConfigHandler.tileEntitiesPackage);
                            String result = visitor.visit(tree);
                            String filename = file.getFileName().toString().split("\\.")[0];
                            filename = Character.toUpperCase(filename.charAt(0)) + filename.substring(1) + ".java";
                            filename = "AbstractGui" + filename;
                            Path path = Paths.get(ConfigHandler.javaDir, ConfigHandler.generatedPackage.replace(".", "/"), filename);
                            if (!Files.exists(path))
                                Files.createFile(path);
                            Files.write(path, Arrays.asList(result.split("\\n")), Charset.defaultCharset());
                        } catch (IllegalStateException e) {

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    key.reset();
                    key = watcher.take();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Stopping thread");
        }
    }
}
