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


public class RingProgressView extends View {

    //ring color
    private int ringColor = 0x0000FF;
    // progress color
    private int ringProgressColor = 0xFF03DAC5;
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
    private Paint paint;
    private Context context;

    public RingProgressView(Context context) {
        this(context, null);
    }

    public RingProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        // custom typed array
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RingProgressView);
        ringColor = typedArray.getColor(R.styleable.RingProgressView_ringColor, ringColor);
        ringProgressColor = typedArray.getColor(R.styleable.RingProgressView_ringProgressColor, ringProgressColor);
        ringWidth = (int) typedArray.getDimension(R.styleable.RingProgressView_ringWidth, dip2px(10));
        textSize = (int) typedArray.getDimension(R.styleable.RingProgressView_textSize, dip2px(20));
        textColor = typedArray.getColor(R.styleable.RingProgressView_textColor, textColor);
        currentProgress = typedArray.getInt(R.styleable.RingProgressView_currentProgress, currentProgress);
        maxProgress = typedArray.getColor(R.styleable.RingProgressView_maxProgress, maxProgress);
        typedArray.recycle();

        paint = new Paint();
        paint.setAntiAlias(true);
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
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(ringWidth);
        paint.setColor(ringColor);
        canvas.drawCircle(centerX, centerY, radius, paint);


        // draw the circle arc
        RectF rectF = new RectF(ringWidth / 2, ringWidth / 2, width - ringWidth / 2, width - ringWidth / 2);
        paint.setColor(ringProgressColor);
        canvas.drawArc(rectF, 0, currentProgress * 360 / maxProgress, false, paint);

        // draw text
        String text = currentProgress * 100 / maxProgress + "%";
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        // reset stroke width
        paint.setStrokeWidth(4);

        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        canvas.drawText(text, width / 2 - bounds.width() / 2, width / 2 + bounds.height() / 2, paint);
    }

    // set layout for canvas
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * change dp to px
     *
     * @param dipValue
     * @return
     */
    private int dip2px(int dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
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
