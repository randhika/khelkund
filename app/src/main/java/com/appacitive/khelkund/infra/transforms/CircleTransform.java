package com.appacitive.khelkund.infra.transforms;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;

import com.squareup.picasso.Transformation;

/**
 * Created by sathley on 4/4/2015.
 */
public class CircleTransform implements Transformation {

    private int mBackgroundColor;
    private int mBorderColor;

    public CircleTransform(int backgroundColor, int borderColor)
    {
        this.mBackgroundColor = backgroundColor;
        this.mBorderColor = borderColor;
    }

    public CircleTransform(int backgroundColor)
    {
        this.mBackgroundColor = backgroundColor;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        Bitmap squaredBitmap = Bitmap.createBitmap(source, 0, 0, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(220, 220, source.getConfig());
        float radius = (200)/2f;
        Canvas canvas = new Canvas(bitmap);

        if(mBorderColor != 0)
        {
            Paint borderPaint = new Paint();
            borderPaint.setColor(mBorderColor);
            borderPaint.setAntiAlias(true);
            canvas.drawCircle(radius + 10, radius + 10, radius + 10, borderPaint);
        }

        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(mBackgroundColor);
        backgroundPaint.setAntiAlias(true);

        canvas.drawCircle(radius + 10, radius + 10, radius, backgroundPaint );

        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        canvas.drawCircle(radius + 10, radius + 10, radius, paint);
        squaredBitmap.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "circle";
    }
}