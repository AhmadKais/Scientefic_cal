package com.example.myapplication;

public class SplayTree {

    class Node {
        int data; // holds the key
        Node parent; // pointer to the parent
        Node left; // pointer to left child
        Node right; // pointer to right child
        String color; // for visualization purpose: "BLACK" or "RED"


        public Node(int data) {
            this.data = data;
            this.parent = null;
            this.left = null;
            this.right = null;
            this.color = "BLACK"; // default color

        }
    }

    private Node root;

    public SplayTree() {
        root = null;
    }


    private void printHelper(Node currPtr, StringBuilder result, String indent, boolean last) {
        if (currPtr != null) {
            result.append(indent);
            if (last) {
                result.append("R----");
                indent += "     ";
            } else {
                result.append("L----");
                indent += "|    ";
            }

            if (currPtr.color.equals("RED")) {
                result.append("[").append(currPtr.data).append("]").append("\n");
            } else {
                result.append(currPtr.data).append("\n");
            }

            printHelper(currPtr.left, result, indent, false);
            printHelper(currPtr.right, result, indent, true);
        }
    }


    public String prettyPrint() {
        StringBuilder result = new StringBuilder();
        printHelper(this.root, result, "", true);
        return result.toString();
    }

    // In-Order traversal
    public String inorder() {
        StringBuilder result = new StringBuilder();
        inOrderHelper(this.root, result);
        return result.toString();
    }

    private void inOrderHelper(Node node, StringBuilder result) {
        if (node != null) {
            inOrderHelper(node.left, result);
            result.append(node.data).append(", ");
            inOrderHelper(node.right, result);
        }
    }
    public Node getRoot() {
        return root;
    }

    // Pre-Order traversal
    public String preorder() {
        StringBuilder result = new StringBuilder();
        preOrderHelper(this.root, result);
        return result.toString();
    }

    private void preOrderHelper(Node node, StringBuilder result) {
        if (node != null) {
            result.append(node.data).append(", ");
            preOrderHelper(node.left, result);
            preOrderHelper(node.right, result);
        }
    }

    // Post-Order traversal
    public String postorder() {
        StringBuilder result = new StringBuilder();
        postOrderHelper(this.root, result);
        return result.toString();
    }

    private void postOrderHelper(Node node, StringBuilder result) {
        if (node != null) {
            postOrderHelper(node.left, result);
            postOrderHelper(node.right, result);
            result.append(node.data).append(", ");
        }
    }

    private void rightRotate(Node x) {
        Node y = x.left;
        if (y != null) {
            x.left = y.right;
            if (y.right != null) {
                y.right.parent = x;
            }
            y.parent = x.parent;
        }
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        if (y != null) {
            y.right = x;
        }
        x.parent = y;
    }

    private void leftRotate(Node x) {
        Node y = x.right;
        if (y != null) {
            x.right = y.left;
            if (y.left != null) {
                y.left.parent = x;
            }
            y.parent = x.parent;
        }
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        if (y != null) {
            y.left = x;
        }
        x.parent = y;
    }

    private void splay(Node x) {
        while (x.parent != null) {
            if (x.parent.parent == null) {
                if (x == x.parent.left) {
                    rightRotate(x.parent);
                } else {
                    leftRotate(x.parent);
                }
            } else if (x == x.parent.left && x.parent == x.parent.parent.left) {
                rightRotate(x.parent.parent);
                rightRotate(x.parent);
            } else if (x == x.parent.right && x.parent == x.parent.parent.right) {
                leftRotate(x.parent.parent);
                leftRotate(x.parent);
            } else if (x == x.parent.right && x.parent == x.parent.parent.left) {
                leftRotate(x.parent);
                rightRotate(x.parent);
            } else {
                rightRotate(x.parent);
                leftRotate(x.parent);
            }
        }
        if (x != null) {
            x.color = "RED";
            if (x.left != null) x.left.color = "BLACK";
            if (x.right != null) x.right.color = "BLACK";
        }
    }

    public void insert(int key) {
        Node z = root;
        Node p = null;

        while (z != null) {
            p = z;
            if (key < z.data) {
                z = z.left;
            } else if (key > z.data) {
                z = z.right;
            } else {
                splay(z);
                return;  // key already exists in the tree
            }
        }

        z = new Node(key);
        z.parent = p;

        if (p == null) {
            root = z;
        } else if (key < p.data) {
            p.left = z;
        } else {
            p.right = z;
        }

        splay(z);
    }

    public void deleteNode(int key) {
        Node z = searchTreeHelper(root, key);
        if (z == null) return;

        splay(z);

        if (z.left == null) {
            replace(z, z.right);
        } else if (z.right == null) {
            replace(z, z.left);
        } else {
            Node y = minimum(z.right);
            if (y.parent != z) {
                replace(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            replace(z, y);
            y.left = z.left;
            y.left.parent = y;
        }
    }

    private Node minimum(Node root) {
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }

    private void replace(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
    }

    private Node searchTreeHelper(Node root, int key) {
        if (root == null || key == root.data) {
            return root;
        }
        if (key < root.data) {
            return searchTreeHelper(root.left, key);
        }
        return searchTreeHelper(root.right, key);
    }

    @Override
    public String toString() {
        return inorder(); // just as an example
    }
}
