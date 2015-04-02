package com.appacitive.khelkund.infra;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by sathley on 4/2/2015.
 */
public class CircleDrawable extends Drawable {

    private final BitmapShader mBitmapShader;
    private final Paint mPaint;
    private Paint mWhitePaint;
    int circleCenterX;
    int circleCenterY;
    int mRadius;
    private boolean mUseStroke = false;
    private int mStrokePadding = 0;

    public CircleDrawable(Bitmap bitmap) {

        mBitmapShader = new BitmapShader(bitmap,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(mBitmapShader);

    }

    public CircleDrawable(Bitmap bitmap, boolean mUseStroke) {
        this(bitmap);

        if (mUseStroke) {
            this.mUseStroke = true;
            mStrokePadding = 4;
            mWhitePaint = new Paint();
            mWhitePaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mWhitePaint.setStrokeWidth(0.75f);
            mWhitePaint.setColor(Color.WHITE);
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        circleCenterX = bounds.width() / 2;
        circleCenterY = bounds.height() / 2;

        if (bounds.width() >= bounds.height())
            mRadius = bounds.width() / 2;
        else
            mRadius = bounds.height() / 2;
    }

    @Override
    public void draw(Canvas canvas) {
        if (mUseStroke) {
            canvas.drawCircle(circleCenterX, circleCenterY, mRadius, mWhitePaint);
        }
        canvas.drawCircle(circleCenterX, circleCenterY, mRadius - mStrokePadding, mPaint);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    public boolean ismUseStroke() {
        return mUseStroke;
    }

    public void setmUseStroke(boolean mUseStroke) {
        this.mUseStroke = mUseStroke;
    }

}
