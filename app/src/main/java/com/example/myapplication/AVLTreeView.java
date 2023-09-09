package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class AVLTreeView extends View {
    private AVLTree tree;
    private Paint paint, edgePaint, recentlyMovedPaint;

    private static final int NODE_RADIUS = 50;

    public AVLTreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        tree = new AVLTree();
        paint = new Paint();
        edgePaint = new Paint();
        recentlyMovedPaint = new Paint();

        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);

        edgePaint.setAntiAlias(true);
        edgePaint.setColor(Color.GRAY);
        edgePaint.setStrokeWidth(5f);

        recentlyMovedPaint.setAntiAlias(true);
        recentlyMovedPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (tree.root != null) {
            drawTree(canvas, tree.root, getWidth() / 2, 50, getWidth() / 3.5, 0.4, 0);
        }
    }

    private void drawTree(Canvas canvas, Node node, int x, int y, double offset, double depthFactor, int depth) {
        paint.setTextSize(30);
        double newOffset = depth < 3 ? offset * depthFactor : offset;
        int adjustedRadius = NODE_RADIUS - (depth > 2 ? (depth - 2) * 15 : 0);
        adjustedRadius = Math.max(adjustedRadius, 20);

        // Decide the color based on the node's rotated status
        if (node.rotated) {
            paint.setColor(Color.RED); // Color for rotated nodes
        } else {
            paint.setColor(Color.BLACK); // Default color
        }

        canvas.drawCircle(x, y, adjustedRadius, paint);

        // For text, always use white color
        paint.setColor(Color.WHITE);
        float textWidth = paint.measureText(Integer.toString(node.value));
        canvas.drawText(Integer.toString(node.value), x - (textWidth / 2), y + 10, paint);

        if (node.left != null) {
            canvas.drawLine(x, y + adjustedRadius, (float) (x - offset), y + 100 + adjustedRadius, edgePaint);
            drawTree(canvas, node.left, (int) (x - offset), y + 100 + (2 * adjustedRadius), newOffset, depthFactor, depth + 1);
        }
        if (node.right != null) {
            canvas.drawLine(x, y + adjustedRadius, (float) (x + offset), y + 100 + adjustedRadius, edgePaint);
            drawTree(canvas, node.right, (int) (x + offset), y + 100 + (2 * adjustedRadius), newOffset, depthFactor, depth + 1);
        }

        // Reset the flag after drawing the node
        node.rotated = false;
    }

    private void resetRotatedFlags() {
        if (tree != null) {
            tree.resetRotatedFlags(tree.root);
        }
    }

    public void insert(int value) {
        tree.resetRotatedFlags(tree.root);  // Reset rotated flags before each insertion
        tree.insert(value);
        invalidate();
    }

    public void delete(int value) {
        tree.resetRotatedFlags(tree.root);  // Reset rotated flags before each deletion
        tree.delete(value);
        invalidate();
    }






    class Node {
        int value;
        Node left;
        Node right;
        int height;
        boolean rotated;  // This flag is to indicate a rotation

        Node(int value) {
            this.value = value;
            this.left = null;
            this.right = null;
            this.height = 1;
            this.rotated = false;
        }
    }


    class AVLTree {
        Node root;

        // ... (Rest of the AVLTree class as previously provided)

        Node delete(Node root, int value) {
            // ... (Delete method as previously provided)

            if (root == null) {
                return root;
            }

            root.height = 1 + max(height(root.left), height(root.right));

            int balance = getBalance(root);

            if (balance > 1 && getBalance(root.left) >= 0) {
                return rightRotate(root);
            }

            if (balance > 1 && getBalance(root.left) < 0) {
                root.left = leftRotate(root.left);
                return rightRotate(root);
            }

            if (balance < -1 && getBalance(root.right) <= 0) {
                return leftRotate(root);
            }

            if (balance < -1 && getBalance(root.right) > 0) {
                root.right = rightRotate(root.right);
                return leftRotate(root);
            }

            return root;
        }

        // ... (Rest of the methods of AVLTree class)

        private Node rightRotate(Node y) {
            Node x = y.left;
            Node T2 = x.right;

            // Rotate nodes
            x.right = y;
            y.left = T2;

            // Update heights
            y.height = 1 + max(height(y.left), height(y.right));
            x.height = 1 + max(height(x.left), height(x.right));

            // Mark nodes as rotated
            y.rotated = true;
            x.rotated = true;

            return x;  // Return the new root
        }

        private Node leftRotate(Node x) {
            Node y = x.right;
            Node T2 = y.left;

            // Rotate nodes
            y.left = x;
            x.right = T2;

            // Update heights
            x.height = 1 + max(height(x.left), height(x.right));
            y.height = 1 + max(height(y.left), height(y.right));

            // Mark nodes as rotated
            x.rotated = true;
            y.rotated = true;

            return y;  // Return new root
        }


        public void insert(int value) {
            root = insertRec(root, value);
        }

        private Node insertRec(Node node, int value) {
            if (node == null) {
                return new Node(value);
            }

            if (value < node.value) {
                node.left = insertRec(node.left, value);
            } else if (value > node.value) {
                node.right = insertRec(node.right, value);
            } else {
                return node; // Duplicate values not allowed
            }

            // Update height of current node
            node.height = 1 + max(height(node.left), height(node.right));

            int balance = getBalance(node);

            // Left heavy
            if (balance > 1 && value < node.left.value) {
                return rightRotate(node);
            }

            // Right heavy
            if (balance < -1 && value > node.right.value) {
                return leftRotate(node);
            }

            // Left-right heavy
            if (balance > 1 && value > node.left.value) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }

            // Right-left heavy
            if (balance < -1 && value < node.right.value) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }

            return node;
        }

        public void delete(int value) {
            root = deleteRec(root, value);
        }

        private Node deleteRec(Node root, int value) {
            if (root == null) {
                return root;
            }

            if (value < root.value) {
                root.left = deleteRec(root.left, value);
            } else if (value > root.value) {
                root.right = deleteRec(root.right, value);
            } else {
                if (root.left == null || root.right == null) {
                    Node temp = null;
                    if (temp == root.left) {
                        temp = root.right;
                    } else {
                        temp = root.left;
                    }

                    if (temp == null) {
                        temp = root;
                        root = null;
                    } else {
                        root = temp;
                    }
                } else {
                    root.value = minValue(root.right);
                    root.right = deleteRec(root.right, root.value);
                }
            }

            if (root == null) {
                return root;
            }

            root.height = 1 + max(height(root.left), height(root.right));

            int balance = getBalance(root);

            if (balance > 1 && getBalance(root.left) >= 0) {
                return rightRotate(root);
            }

            if (balance > 1 && getBalance(root.left) < 0) {
                root.left = leftRotate(root.left);
                return rightRotate(root);
            }

            if (balance < -1 && getBalance(root.right) <= 0) {
                return leftRotate(root);
            }

            if (balance < -1 && getBalance(root.right) > 0) {
                root.right = rightRotate(root.right);
                return leftRotate(root);
            }

            return root;
        }

        private int minValue(Node root) {
            int minValue = root.value;
            while (root.left != null) {
                minValue = root.left.value;
                root = root.left;
            }
            return minValue;
        }

        private int max(int a, int b) {
            return (a > b) ? a : b;
        }

        private int height(Node node) {
            if (node == null)
                return 0;
            return node.height;
        }

        private int getBalance(Node node) {
            if (node == null)
                return 0;
            return height(node.left) - height(node.right);
        }
        public void resetRotatedFlags(Node node) {
            if (node == null) return;

            node.rotated = false;

            resetRotatedFlags(node.left);
            resetRotatedFlags(node.right);
        }




    }
}
