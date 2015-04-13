package com.octagon.crazygui.idea;

import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.FileEditorStateLevel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.vfs.ReadonlyStatusHandler;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.octagon.crazygui.CrazyContainer;
import com.octagon.crazygui.CrazyGUIAttributeManager;
import com.octagon.crazygui.antlr.ComponentAttribute;
import com.octagon.crazygui.antlr.util.LogHelper;
import com.octagon.crazygui.idea.actions.ConfigCompilerDialogue;
import com.octagon.crazygui.idea.psi.CXMLFile;
import com.octagon.crazygui.idea.psi.CXMLTag;
import com.octagon.crazygui.idea.psi.CXMLTagBase;
import com.octagon.crazygui.idea.psi.CXMLTagName;
import com.octagon.crazygui.idea.util.ClassUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.stream.Collectors;

public class CXMLGuiEditor extends UserDataHolderBase implements FileEditor {
    private final VirtualFile file;
    private JPanel left;
    private JTree componentTree;
    private JTable attributes;
    private JPanel right;
    private JPanel mainPanel;
    private JPanel preview;
    private JTree palette;

    private CXMLFile psiDocument;
    private CXMLTagBase selectedTag = null;

    boolean[] slotIds = new boolean[128];

    public CXMLGuiEditor(Project project, VirtualFile file) {
        CrazyGUIAttributeManager.init();
        this.file = file;
        PsiFile psi = PsiManager.getInstance(project).findFile(file);
        if(psi instanceof CXMLFile) {
            this.psiDocument = (CXMLFile)psi;
            initComponents();
            setSelectedTag(project, (CXMLTagBase) Arrays.asList(psiDocument.getChildren()).stream().filter(a -> a instanceof CXMLTagBase).findFirst().orElse(null));
            recreateFileStructure();
            recreatePalette(project);
            componentTree.addTreeSelectionListener(e -> updateAttributes(e, project));
        }
    }

    private void updateAttributes(TreeSelectionEvent e, Project project) {
        CXMLMutableTreeNode node = (CXMLMutableTreeNode) componentTree.getLastSelectedPathComponent();
        if(node == null) return;

        Object obj = node.getExtraData();
        if(!(obj instanceof CXMLTagBase)) return;
        setSelectedTag(project, (CXMLTagBase) obj);
    }

    @NotNull
    @Override
    public JComponent getComponent() {
        return mainPanel;
    }

    public void setSelectedTag(@NotNull Project project, @Nullable CXMLTagBase tag) {
        this.selectedTag = tag;
        if(tag == null) {
            this.attributes.setModel(new TagAttributes(null, new HashMap<>()));
        } else {
            Map<String, List<ComponentAttribute>> attributes = ClassUtils.getComponentAttributeNames(project);
            if(!attributes.containsKey(selectedTag.getTagName() != null ? selectedTag.getTagName().getText() : "Root")) return;
            List<ComponentAttribute> tagAttributes = attributes.get(selectedTag.getTagName().getText());
            Map<String, String> definedAttributes = selectedTag.getAttributeList().stream().filter(a -> a.getValue() != null).collect(Collectors.toMap(a -> a.getFirstChild().getText(), a -> a.getValue().getText()));
            if(selectedTag.getTagName().getText().equalsIgnoreCase("slot")) {
                int slotId = 0;
                while(slotIds[slotId]) {
                    slotId++;
                }
                final int finalSlotId = slotId;
                tagAttributes.stream().filter(a -> a.getName().equals("id")).forEach(a -> a.setValue(finalSlotId));
                slotIds[finalSlotId] = true;
            }
            for(ComponentAttribute attribute : tagAttributes) {
                if(attribute.getName().equals("id")) continue;
                if(definedAttributes.containsKey(attribute.getName()))
                    attribute.setValueString(definedAttributes.get(attribute.getName()));
            }
            this.attributes.setModel(new TagAttributes(tag, tagAttributes.stream().collect(Collectors.toMap(ComponentAttribute::getName, a -> a))));
        }
    }

    private void initComponents() {
        attributes.setDefaultRenderer(Object.class, new MyTableCellRenderer());
        attributes.setDefaultEditor(Object.class, new MyTableCellRenderer());

        palette.setRootVisible(false);

        {
            DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) palette.getCellRenderer();
            renderer.setLeafIcon(null);
            renderer.setOpenIcon(null);
            renderer.setClosedIcon(null);
        }

        {
            DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) componentTree.getCellRenderer();
            renderer.setLeafIcon(null);
            renderer.setOpenIcon(null);
            renderer.setClosedIcon(null);
        }

        componentTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        componentTree.setDragEnabled(true);
        componentTree.setDropMode(DropMode.ON_OR_INSERT);
        componentTree.setTransferHandler(new ComponentTreeTransferHandler());
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return mainPanel;
    }

    @NotNull
    @Override
    public String getName() {
        return "crazygui.GuiEditor";
    }

    @NotNull
    @Override
    public FileEditorState getState(@NotNull FileEditorStateLevel fileEditorStateLevel) {
        return (fileEditorState, fileEditorStateLevel1) -> false;
    }

    @Override
    public void setState(@NotNull FileEditorState fileEditorState) {

    }

    @Override
    public boolean isModified() {
        return ((TagAttributes)attributes.getModel()).getAttributes().values().stream().anyMatch(ComponentAttribute::isDirty);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void selectNotify() {

    }

    @Override
    public void deselectNotify() {

    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener propertyChangeListener) {

    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener propertyChangeListener) {

    }

    @Nullable
    @Override
    public BackgroundEditorHighlighter getBackgroundHighlighter() {
        return null;
    }

    @Nullable
    @Override
    public FileEditorLocation getCurrentLocation() {
        return null;
    }

    @Nullable
    @Override
    public StructureViewBuilder getStructureViewBuilder() {
        return null;
    }

    @Override
    public void dispose() {

    }

    @Nullable
    @Override
    public <T> T getUserData(@NotNull Key<T> key) {
        return null;
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> key, @Nullable T t) {

    }

    private void recreateFileStructure() {
        CXMLMutableTreeNode root = new CXMLMutableTreeNode("Root", psiDocument.getFirstChild());
        walkTree(psiDocument.getFirstChild(), root);
        componentTree.setModel(new DefaultTreeModel(root));
    }

    private void walkTree(PsiElement base, DefaultMutableTreeNode parent) {
        for(PsiElement element : base.getChildren()) {
            if(element instanceof CXMLTagBase) {
                CXMLTagName tagName = ((CXMLTagBase) element).getTagName();
                DefaultMutableTreeNode node = new CXMLMutableTreeNode(tagName != null ? tagName.getText() : "Root", element);
                if(element instanceof CXMLTag) walkTree(element, node);
                parent.add(node);
            }
        }
    }

    private void recreatePalette(Project project) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        DefaultMutableTreeNode vendorComponents = new DefaultMutableTreeNode("CrazyGUI");
        DefaultMutableTreeNode userComponents = new DefaultMutableTreeNode("User Defined");
        Map<String, String> vendorComponentList = ClassUtils.getComponentList(project, "com.octagon.crazygui.components");
        Map<String, String> userComponentList = ClassUtils.getComponentList(project, ConfigCompilerDialogue.getProp(project, ConfigCompilerDialogue.PROP_GUI_PACKAGE, "com.octagon.airships.gui") + ".components");

        for(Map.Entry<String, String> entry : vendorComponentList.entrySet()) vendorComponents.add(new CXMLMutableTreeNode(entry.getKey(), entry.getValue()));
        for(Map.Entry<String, String> entry : userComponentList.entrySet()) userComponents.add(new CXMLMutableTreeNode(entry.getKey(), entry.getValue()));

        vendorComponents.add(new CXMLMutableTreeNode("Slot", "com.octagon.crazygui.editor.EditorSlot"));
        vendorComponents.add(new CXMLMutableTreeNode("PlayerInventory", "com.octagon.crazygui.editor.EditorPlayerInventory"));

        root.add(vendorComponents);
        root.add(userComponents);
        palette.setModel(new PaletteTreeModel(root));
    }

    private void moveTagTo(CXMLTagBase tagToMove, int insertAt) {
        int currentPos = -1;
        List<CXMLTagBase> tags = Arrays.asList(tagToMove.getParent().getChildren()).stream().filter(a -> a instanceof CXMLTagBase).map(a -> (CXMLTagBase)a).collect(Collectors.toList());
        for(int i=0; i < tags.size(); i++) {
            if(tags.get(i).equals(tagToMove)) currentPos = i;
        }
        if(currentPos == -1 || currentPos == insertAt) return;

        Document document = null;
        Project project = null;
        for(Project p : ProjectManager.getInstance().getOpenProjects()) {
            if(PsiDocumentManager.getInstance(p).getCachedDocument(tagToMove.getContainingFile()) != null) {
                project = p;
                document = PsiDocumentManager.getInstance(p).getCachedDocument(tagToMove.getContainingFile());
                break;
            }
        }
        if(document == null) return;
        ReadonlyStatusHandler.getInstance(project).ensureFilesWritable(tagToMove.getContainingFile().getVirtualFile());
        PsiElement copy = tagToMove.copy();
        final Project finalProject = project;
        ApplicationManager.getApplication().runWriteAction(() -> {
            CommandProcessor.getInstance().executeCommand(finalProject, () -> {
                CommandProcessor.getInstance().executeCommand(finalProject, () -> {
                    if (insertAt == 0) tagToMove.getParent().addBefore(copy, tags.get(0));
                    else tagToMove.getParent().addAfter(copy, tags.get(insertAt - 1));
                }, "CrazyGUI.InsertTag", null);

                LogHelper.info(psiDocument);
                CommandProcessor.getInstance().executeCommand(finalProject, tagToMove::delete, "CrazyGUI.RemoveTag", null);
                LogHelper.info(psiDocument);
            }, "CrazyGui.MoveTagTo", null);
        });

        LogHelper.info(psiDocument);
    }

    private class ComponentTreeTransferHandler extends TransferHandler {
        DataFlavor nodesFlavor;
        DataFlavor[] flavors = new DataFlavor[1];
        DefaultMutableTreeNode[] nodesToRemove;

        public ComponentTreeTransferHandler() {
            try {
                String mimeType = DataFlavor.javaJVMLocalObjectMimeType +
                        ";class=\"" +
                        javax.swing.tree.DefaultMutableTreeNode[].class.getName() +
                        "\"";
                nodesFlavor = new DataFlavor(mimeType);
                flavors[0] = nodesFlavor;
            } catch(ClassNotFoundException e) {
                System.out.println("ClassNotFound: " + e.getMessage());
            }
        }

        @Override
        public boolean canImport(TransferHandler.TransferSupport support) {
            if(!support.isDrop()) {
                return false;
            }
            support.setShowDropLocation(true);
            if(!support.isDataFlavorSupported(nodesFlavor)) {
                return false;
            }
            // Do not allow a drop on the drag source selections.
            JTree.DropLocation dropLocation = (JTree.DropLocation)support.getDropLocation();
            JTree tree = (JTree)support.getComponent();
            int dropRow = tree.getRowForPath(dropLocation.getPath());
            int[] selRows = tree.getSelectionRows();
            for (int selRow : selRows) {
                if (selRow == dropRow) {
                    return false;
                }
            }

            if (dropLocation.getChildIndex() == -1) { //DropMode.ON
                CXMLMutableTreeNode targetNode = (CXMLMutableTreeNode) dropLocation.getPath().getLastPathComponent();
                String componentName = ((CXMLTagBase) targetNode.getExtraData()).getTagName().getText();
                PsiClass componentClass = ClassUtils.getCachedComponentClasses().containsKey(componentName) ? ClassUtils.getCachedComponentClasses().get(componentName) : null;
                if (componentClass == null) return false;
                if (!Arrays.asList(componentClass.getSupers()).stream().anyMatch(a -> a.getQualifiedName().equals(CrazyContainer.class.getCanonicalName())))
                    return false;
            }

            return true;
        }

        protected Transferable createTransferable(JComponent c) {
            JTree tree = (JTree)c;
            TreePath[] paths = tree.getSelectionPaths();
            if(paths != null) {
                // Make up a node array of copies for transfer and
                // another for/of the nodes that will be removed in
                // exportDone after a successful drop.
                List<CXMLMutableTreeNode> copies = new ArrayList<>();
                List<CXMLMutableTreeNode> toRemove = new ArrayList<>();
                CXMLMutableTreeNode node = (CXMLMutableTreeNode)paths[0].getLastPathComponent();
                CXMLMutableTreeNode copy = copy(node);
                copies.add(copy);
                toRemove.add(node);
                for(int i = 1; i < paths.length; i++) {
                    CXMLMutableTreeNode next = (CXMLMutableTreeNode)paths[i].getLastPathComponent();
                    // Do not allow higher level nodes to be added to list.
                    if(next.getLevel() < node.getLevel()) {
                        break;
                    } else if(next.getLevel() > node.getLevel()) {  // child node
                        copy.add(copy(next));
                        // node already contains child
                    } else {                                        // sibling
                        copies.add(copy(next));
                        toRemove.add(next);
                    }
                }
                CXMLMutableTreeNode[] nodes = copies.toArray(new CXMLMutableTreeNode[copies.size()]);
                nodesToRemove = toRemove.toArray(new DefaultMutableTreeNode[toRemove.size()]);
                return new NodesTransferable(nodes);
            }
            return null;
        }

        /** Defensive copy used in createTransferable. */
        private CXMLMutableTreeNode copy(CXMLMutableTreeNode node) {
            return new CXMLMutableTreeNode(node.getUserObject(), node.getExtraData());
        }

        protected void exportDone(JComponent source, Transferable data, int action) {
            if((action & MOVE) == MOVE) {
                JTree tree = (JTree)source;
                DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
                if(model instanceof PaletteTreeModel) return;
                // Remove nodes saved in nodesToRemove in createTransferable.
                for (DefaultMutableTreeNode node : nodesToRemove) {
                    model.removeNodeFromParent(node);
                }
            }
        }

        public int getSourceActions(JComponent c) {
            return COPY_OR_MOVE;
        }

        public boolean importData(TransferHandler.TransferSupport support) {
            if(!canImport(support)) {
                return false;
            }
            // Extract transfer data.
            CXMLMutableTreeNode[] nodes = null;
            try {
                Transferable t = support.getTransferable();
                nodes = (CXMLMutableTreeNode[])t.getTransferData(nodesFlavor);
            } catch(UnsupportedFlavorException | java.io.IOException e) {
                e.printStackTrace();
            }
            // Get drop location info.
            JTree.DropLocation dl =
                    (JTree.DropLocation)support.getDropLocation();
            int childIndex = dl.getChildIndex();
            TreePath dest = dl.getPath();
            CXMLMutableTreeNode parent = (CXMLMutableTreeNode)dest.getLastPathComponent();
            JTree tree = (JTree)support.getComponent();
            DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
            // Configure for drop mode.
            int index = childIndex;    // DropMode.INSERT
            if(childIndex == -1) {     // DropMode.ON
                String componentName = ((CXMLTagBase)parent.getExtraData()).getTagName().getText();
                PsiClass componentClass = ClassUtils.getCachedComponentClasses().containsKey(componentName) ? ClassUtils.getCachedComponentClasses().get(componentName) : null;
                if(componentClass == null) return false;
                if(!Arrays.asList(componentClass.getSupers()).stream().anyMatch(a -> a.getQualifiedName().equals(CrazyContainer.class.getCanonicalName()))) return false;
                index = parent.getChildCount();
            }
            // Add data to model.
            for (DefaultMutableTreeNode node : nodes) {
                model.insertNodeInto(node, parent, index++);
            }
            if(support.getDropAction() == MOVE && childIndex != -1) moveTagTo(((CXMLTagBase)nodes[0].getExtraData()), index);
            return true;
        }

        public class NodesTransferable implements Transferable {
            CXMLMutableTreeNode[] nodes;

            public NodesTransferable(CXMLMutableTreeNode[] nodes) {
                this.nodes = nodes;
            }

            public Object getTransferData(DataFlavor flavor)
                    throws UnsupportedFlavorException {
                if(!isDataFlavorSupported(flavor))
                    throw new UnsupportedFlavorException(flavor);
                return nodes;
            }

            public DataFlavor[] getTransferDataFlavors() {
                return flavors;
            }

            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return nodesFlavor.equals(flavor);
            }
        }
    }

    private class PaletteTransferHandler extends TransferHandler {
        DataFlavor nodesFlavor;
        DataFlavor[] flavors = new DataFlavor[1];

        public PaletteTransferHandler() {
            try {
                String mimeType = DataFlavor.javaJVMLocalObjectMimeType +
                        ";class=\"" +
                        com.octagon.crazygui.idea.CXMLMutableTreeNode[].class.getName() +
                        "\"";
                nodesFlavor = new DataFlavor(mimeType);
                flavors[0] = nodesFlavor;
            } catch(ClassNotFoundException e) {
                System.out.println("ClassNotFound: " + e.getMessage());
            }
        }

        @Override
        public boolean canImport(TransferHandler.TransferSupport support) {
            return false;
        }

        protected Transferable createTransferable(JComponent c) {
            JTree tree = (JTree)c;
            TreePath[] paths = tree.getSelectionPaths();
            if(paths != null) {
                // Make up a node array of copies for transfer and
                // another for/of the nodes that will be removed in
                // exportDone after a successful drop.
                List<DefaultMutableTreeNode> copies = new ArrayList<>();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)paths[0].getLastPathComponent();
                DefaultMutableTreeNode copy = copy(node);
                copies.add(copy);
                for(int i = 1; i < paths.length; i++) {
                    DefaultMutableTreeNode next = (DefaultMutableTreeNode)paths[i].getLastPathComponent();
                    // Do not allow higher level nodes to be added to list.
                    if(next.getLevel() < node.getLevel()) {
                        break;
                    } else if(next.getLevel() > node.getLevel()) {  // child node
                        copy.add(copy(next));
                        // node already contains child
                    } else {                                        // sibling
                        copies.add(copy(next));
                    }
                }
                DefaultMutableTreeNode[] nodes = copies.toArray(new DefaultMutableTreeNode[copies.size()]);
                return new NodesTransferable(nodes);
            }
            return null;
        }

        /** Defensive copy used in createTransferable. */
        private DefaultMutableTreeNode copy(TreeNode node) {
            return new DefaultMutableTreeNode(node);
        }

        public int getSourceActions(JComponent c) {
            return COPY;
        }

        public class NodesTransferable implements Transferable {
            DefaultMutableTreeNode[] nodes;

            public NodesTransferable(DefaultMutableTreeNode[] nodes) {
                this.nodes = nodes;
            }

            public Object getTransferData(DataFlavor flavor)
                    throws UnsupportedFlavorException {
                if(!isDataFlavorSupported(flavor))
                    throw new UnsupportedFlavorException(flavor);
                return nodes;
            }

            public DataFlavor[] getTransferDataFlavors() {
                return flavors;
            }

            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return nodesFlavor.equals(flavor);
            }
        }
    }

    /** Dummy to check in DnD */
    private class PaletteTreeModel extends DefaultTreeModel {

        public PaletteTreeModel(TreeNode root) {
            super(root);
        }

        public PaletteTreeModel(TreeNode root, boolean asksAllowsChildren) {
            super(root, asksAllowsChildren);
        }
    }
}
