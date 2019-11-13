package net.bis5.s1948;

import org.primefaces.model.*;
import java.io.*;

public class S1948App2 implements Serializable {

    private TreeNode rootNode = new DefaultTreeNode("root node", null);

    private final TreeNode finalRootNode = new DefaultTreeNode("root node", null);

    public TreeNode getRootNode() {
        return rootNode;
    }

    public TreeNode getFinalRootNode() {
        return finalRootNode;
    }
}