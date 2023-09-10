package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class SplayTreeView extends View {
    private Paint mPaint;
    private SplayTree mTree;

    private static final int NODE_RADIUS = 30;
    private static final int NODE_SPACING = 80;
    private static final int LEVEL_SPACING = 120;

    public SplayTreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mTree = new SplayTree();
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(3);
        mPaint.setTextSize(30);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true);
    }


    public SplayTreeView(Context context, SplayTree tree) {
        super(context);
        this.mTree = tree;
        init();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTree.getRoot() != null) {
            drawNode(canvas, mTree.getRoot(), getWidth() / 2, NODE_RADIUS * 2, getWidth() / 2);
        }
    }

    private void drawNode(Canvas canvas, SplayTree.Node node, int x, int y, int parentX) {
        if (node == null) return;

        // Set color to draw the line connecting to parent (assuming you want the edges in black)
        mPaint.setColor(Color.BLACK);

        // Draw the line connecting to parent
        if (node.parent != null) {
            canvas.drawLine(x, y, parentX, y - LEVEL_SPACING + NODE_RADIUS, mPaint);
        }

        // Draw the node (circle) in black color
        canvas.drawCircle(x, y, NODE_RADIUS, mPaint);

        // Set the text color to WHITE for drawing the node's data
        mPaint.setColor(Color.WHITE);

        // Center the text vertically inside the node
        canvas.drawText(String.valueOf(node.data), x, y + (mPaint.getTextSize() / 2) - 10, mPaint);

        // Recursively draw children
        if (node.left != null) {
            drawNode(canvas, node.left, x - NODE_SPACING, y + LEVEL_SPACING, x);
        }
        if (node.right != null) {
            drawNode(canvas, node.right, x + NODE_SPACING, y + LEVEL_SPACING, x);
        }
    }

    public void insert(int value) {
        mTree.insert(value);
        invalidate();  // Redraw the view
    }

    public void delete(int value) {
        mTree.deleteNode(value);
        invalidate();  // Redraw the view
    }

    public void setTree(SplayTree tree) {
        this.mTree = tree;
        invalidate();  // Cause the view to redraw
    }
}

