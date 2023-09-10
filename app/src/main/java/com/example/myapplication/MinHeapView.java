package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;

public class MinHeapView extends View {
    private ArrayList<Integer> heap = new ArrayList<>();
    private Paint paint, edgePaint;
    private static final int NODE_RADIUS = 50;

    public MinHeapView(Context context) {
        super(context);
        init();
    }

    public MinHeapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MinHeapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        edgePaint = new Paint();

        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);

        edgePaint.setAntiAlias(true);
        edgePaint.setColor(Color.GRAY);
        edgePaint.setStrokeWidth(5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!heap.isEmpty()) {
            drawHeap(canvas, 0, getWidth() / 2, 50);
        }
    }
    private void drawHeap(Canvas canvas, int index, int x, int y) {
        if (index >= heap.size()) return;

        // Draw the current node
        canvas.drawCircle(x, y, NODE_RADIUS, paint);
        float textWidth = paint.measureText(Integer.toString(heap.get(index)));
        canvas.drawText(Integer.toString(heap.get(index)), x - (textWidth / 2), y + 10, paint);

        int leftChildIndex = 2 * index + 1;
        int rightChildIndex = 2 * index + 2;

        int xOffset = 100;  // Adjust as needed for spacing

        // Recursively draw the left child
        if (leftChildIndex < heap.size()) {
            canvas.drawLine(x, y + NODE_RADIUS, x - xOffset, y + 3 * NODE_RADIUS, edgePaint);
            drawHeap(canvas, leftChildIndex, x - xOffset, y + 100);
        }

        // Recursively draw the right child
        if (rightChildIndex < heap.size()) {
            canvas.drawLine(x, y + NODE_RADIUS, x + xOffset, y + 3 * NODE_RADIUS, edgePaint);
            drawHeap(canvas, rightChildIndex, x + xOffset, y + 100);
        }
    }

    public void insert(int value) {
        heap.add(value);
        bubbleUp(heap.size() - 1);
        invalidate();
    }

    private void bubbleUp(int index) {
        if (index == 0) return;
        int parentIndex = (index - 1) / 2;
        if (heap.get(index) < heap.get(parentIndex)) {
            // Swap values
            int temp = heap.get(index);
            heap.set(index, heap.get(parentIndex));
            heap.set(parentIndex, temp);
            bubbleUp(parentIndex);
        }
    }

    public void extractMin() {
        if (heap.isEmpty()) return;
        if (heap.size() == 1) {
            heap.remove(0);
        } else {
            heap.set(0, heap.remove(heap.size() - 1));
            bubbleDown(0);
        }
        invalidate();
    }

    private void bubbleDown(int index) {
        int leftChildIndex = 2 * index + 1;
        int rightChildIndex = 2 * index + 2;

        int smallest = index;

        if (leftChildIndex < heap.size() && heap.get(leftChildIndex) < heap.get(smallest)) {
            smallest = leftChildIndex;
        }
        if (rightChildIndex < heap.size() && heap.get(rightChildIndex) < heap.get(smallest)) {
            smallest = rightChildIndex;
        }
        if (smallest != index) {
            // Swap values
            int temp = heap.get(index);
            heap.set(index, heap.get(smallest));
            heap.set(smallest, temp);
            bubbleDown(smallest);
        }
    }
}