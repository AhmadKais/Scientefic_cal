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
    private Paint nodePaint, textPaint, edgePaint;
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
        nodePaint = new Paint();
        textPaint = new Paint();
        edgePaint = new Paint();

        nodePaint.setAntiAlias(true);
        nodePaint.setColor(Color.BLACK);

        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(30);

        edgePaint.setAntiAlias(true);
        edgePaint.setColor(Color.GRAY);
        edgePaint.setStrokeWidth(5f);
    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!heap.isEmpty()) {
            drawHeap(canvas, 0, getWidth() / 2, 50, getWidth() / 3.5, 0.4, 0);
        }
    }

    private void drawHeap(Canvas canvas, int index, int x, int y, double offset, double depthFactor, int depth) {
        int adjustedRadius = NODE_RADIUS - (depth > 2 ? (depth - 2) * 15 : 0);
        adjustedRadius = Math.max(adjustedRadius, 20);

        double newOffset = depth < 3 ? offset * depthFactor : offset;

        canvas.drawCircle(x, y, adjustedRadius, nodePaint);
        float textWidth = textPaint.measureText(Integer.toString(heap.get(index)));
        canvas.drawText(Integer.toString(heap.get(index)), x - (textWidth / 2), y + 10, textPaint);

        int leftChildIndex = 2 * index + 1;
        int rightChildIndex = 2 * index + 2;

        if (leftChildIndex < heap.size()) {
            canvas.drawLine(x, y + adjustedRadius, (float) (x - offset), y + 100 + adjustedRadius, edgePaint);
            drawHeap(canvas, leftChildIndex, (int) (x - offset), y + 100 + (2 * adjustedRadius), newOffset, depthFactor, depth + 1);
        }

        if (rightChildIndex < heap.size()) {
            canvas.drawLine(x, y + adjustedRadius, (float) (x + offset), y + 100 + adjustedRadius, edgePaint);
            drawHeap(canvas, rightChildIndex, (int) (x + offset), y + 100 + (2 * adjustedRadius), newOffset, depthFactor, depth + 1);
        }
    }




    public void insert(int value) {
        if (heap.contains(value)) {
            // Value already exists, you can either return or display a message
            return;
        }
        heap.add(value);
        bubbleUp(heap.size() - 1);
        invalidate();  // Redraw the view.
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
        invalidate();  // Redraw the view.
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
