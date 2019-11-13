package net.bis5.s1948;

import org.primefaces.model.*;

public class S1948App2 {

    private TreeNode rootNode;

    public S1948App2() {
        rootNode = new DefaultTreeNode("root node", null);
    }

    public TreeNode getRootNode() {
        return rootNode;
    }
}