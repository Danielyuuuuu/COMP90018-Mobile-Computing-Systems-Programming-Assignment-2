package com.example.dansdistractor;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
/*
*Ring progress component
* reference from https://github.com/NewHuLe/RingProgressView/blob/master/app/src/main/java/cn/com/cesgroup/mygithub/RingProgressView.java
* author:NewHuLe
 */

public class RingProgressView extends View {

    //ring color
    private int ringColor = 0x0000FF;
    // progress color
    private int pColor = 0xFF03DAC5;
    //ring width
    private int ringWidth = 7;
    // font size
    private int textSize = 13;
    // font color
    private int textColor = 0xFF03DAC5;
    // current progress
    private int currentProgress = 0;
    // the target
    private int maxProgress = 100;
    // the whole width
    private int width;
    private Paint myPaint;
    private Context context;

    public RingProgressView(Context context) {
        this(context, null);
    }

    public RingProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    //set value and create paint
    public RingProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        // custom typed array
        TypedArray tyArray = context.obtainStyledAttributes(attrs, R.styleable.RingProgressView);
        ringColor = tyArray.getColor(R.styleable.RingProgressView_ringColor, ringColor);
        pColor = tyArray.getColor(R.styleable.RingProgressView_ringProgressColor, pColor);
        ringWidth = (int) tyArray.getDimension(R.styleable.RingProgressView_ringWidth, dip(10));
        textSize = (int) tyArray.getDimension(R.styleable.RingProgressView_textSize, dip(20));
        textColor = tyArray.getColor(R.styleable.RingProgressView_textColor, textColor);
        currentProgress = tyArray.getInt(R.styleable.RingProgressView_currentProgress, currentProgress);
        maxProgress = tyArray.getColor(R.styleable.RingProgressView_maxProgress, maxProgress);
        tyArray.recycle();

        myPaint = new Paint();
        myPaint.setAntiAlias(true);
    }


    // measure the width
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
    }

    // draw on the canvas
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // get radius
        float centerX = width / 2;
        float centerY = width / 2;
        float radius = width / 2 - ringWidth / 2;
        // draw circle
        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(ringWidth);
        myPaint.setColor(ringColor);
        canvas.drawCircle(centerX, centerY, radius, myPaint);
        // draw the circle arc on canvas
        RectF rectF = new RectF(ringWidth / 2, ringWidth / 2, width - ringWidth / 2, width - ringWidth / 2);
        myPaint.setColor(pColor);
        canvas.drawArc(rectF, 0, currentProgress * 360 / maxProgress, false, myPaint);
        // draw text
        String text = currentProgress * 100 / maxProgress + "%";
        myPaint.setColor(textColor);
        myPaint.setTextSize(textSize);
        // reset stroke width
        myPaint.setStrokeWidth(4);
        //create borders
        Rect borders = new Rect();
        myPaint.getTextBounds(text, 0, text.length(), borders);
        canvas.drawText(text, width / 2 - borders.width() / 2, width / 2 + borders.height() / 2, myPaint);
    }

    // set layout for canvas
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
    private int dip(int val) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (val * scale + 0.5f);
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }
}
