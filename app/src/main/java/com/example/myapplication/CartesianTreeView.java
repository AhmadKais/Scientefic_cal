package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CartesianTreeView extends View {
    private List<Integer> sequence = new ArrayList<>();
    private CartesianTreeNode root;
    private Paint nodePaint, textPaint, edgePaint;
    private static final int NODE_RADIUS = 40;

    private static final int BASE_NODE_RADIUS = 40;

    public CartesianTreeView(Context context) {
        super(context);
        init();
    }

    public CartesianTreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CartesianTreeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        nodePaint = new Paint();
        textPaint = new Paint();
        edgePaint = new Paint();

        nodePaint.setAntiAlias(true);
        nodePaint.setColor(Color.BLACK);

        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(30f);

        edgePaint.setAntiAlias(true);
        edgePaint.setColor(Color.BLACK);
        edgePaint.setStrokeWidth(4f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (root != null) {
            drawTree(canvas, root, getWidth() / 2, 100, getWidth() / 3.5, 0.4, 0);
        }
    }

    private void drawTree(Canvas canvas, CartesianTreeNode node, int x, int y, double offset, double depthFactor, int depth) {
        double newOffset = depth < 3 ? offset * depthFactor : offset;
        int adjustedRadius = BASE_NODE_RADIUS - (depth > 2 ? (depth - 2) * 15 : 0);
        adjustedRadius = Math.max(adjustedRadius, 20);

        canvas.drawCircle(x, y, adjustedRadius, nodePaint);

        float textWidth = textPaint.measureText(Integer.toString(node.value));
        canvas.drawText(Integer.toString(node.value), x - (textWidth / 2), y + 10, textPaint);

        int newY = y + 100 + (2 * adjustedRadius);

        if (node.left != null) {
            canvas.drawLine(x, y + adjustedRadius, (float) (x - offset), y + 100 + adjustedRadius, edgePaint);
            drawTree(canvas, node.left, (int) (x - offset), newY, newOffset, depthFactor, depth + 1);
        }
        if (node.right != null) {
            canvas.drawLine(x, y + adjustedRadius, (float) (x + offset), y + 100 + adjustedRadius, edgePaint);
            drawTree(canvas, node.right, (int) (x + offset), newY, newOffset, depthFactor, depth + 1);
        }
    }



    public void insert(int value) {
        sequence.add(value);
        root = buildTree(sequence);
        invalidate();
    }

    private CartesianTreeNode buildTree(List<Integer> sequence) {
        if (sequence == null || sequence.isEmpty()) {
            return null;
        }

        return buildTreeRecursive(sequence, 0, sequence.size() - 1);
    }

    private CartesianTreeNode buildTreeRecursive(List<Integer> sequence, int start, int end) {
        if (start > end) {
            return null;
        }

        int minIndex = start;
        for (int i = start + 1; i <= end; i++) {
            if (sequence.get(i) < sequence.get(minIndex)) {
                minIndex = i;
            }
        }

        CartesianTreeNode node = new CartesianTreeNode(sequence.get(minIndex));
        node.left = buildTreeRecursive(sequence, start, minIndex - 1);
        node.right = buildTreeRecursive(sequence, minIndex + 1, end);
        return node;
    }
}
