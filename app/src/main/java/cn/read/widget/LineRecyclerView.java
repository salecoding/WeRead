package cn.read.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import cn.read.R;

/**
 * Created by Administrator on 2017-03-22.
 */

public class LineRecyclerView extends RecyclerView {
    private boolean isDrawLine;

    public boolean isDrawLine() {
        return isDrawLine;
    }

    public void setDrawLine(boolean drawLine) {
        isDrawLine = drawLine;
    }

    public LineRecyclerView(Context context) {
        super(context);
    }

    public LineRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray types = context.obtainStyledAttributes(attrs, R.styleable.LineRecyclerView);
        isDrawLine = types.getBoolean(R.styleable.LineRecyclerView_isDrawLine, false);
        types.recycle();
    }

    public LineRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (!isDrawLine) return;
        View localView1 = getChildAt(0);
        if (localView1 == null) return;
        int column = getWidth() / localView1.getWidth();
        int childCount = getChildCount();
        Paint localPaint;
        localPaint = new Paint();
        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setStrokeWidth(3F);
        localPaint.setColor(getResources().getColor(R.color.lightgray));
        for (int i = 0; i < childCount; i++) {
            View cellView = getChildAt(i);
            if ((i + 1) % column == 0) {
                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
            } else if ((i + 1) > (childCount - (childCount % column))) {
                canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
            } else {
                canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
            }
        }
        /*if (childCount % column != 0) {
            for (int j = 0; j < (column - childCount % column); j++) {
                View lastView = getChildAt(childCount - 1);
                canvas.drawLine(lastView.getRight() + lastView.getWidth() * j, lastView.getTop(), lastView.getRight() + lastView.getWidth() * j, lastView.getBottom(), localPaint);
            }
        }*/
    }
}
