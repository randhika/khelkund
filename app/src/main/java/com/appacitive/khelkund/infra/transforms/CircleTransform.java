package com.appacitive.khelkund.infra.transforms;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.view.View;

import com.squareup.picasso.Transformation;

/**
 * Created by sathley on 4/4/2015.
 */
public class CircleTransform implements Transformation {

    private int mBackgroundColor;

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

        Bitmap bitmap = Bitmap.createBitmap(200, 200, source.getConfig());
        float radius = (200)/2f;
        Canvas canvas = new Canvas(bitmap);

        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(mBackgroundColor);
        backgroundPaint.setAntiAlias(false);

        canvas.drawCircle(radius, radius, radius, backgroundPaint );

        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(false);

        canvas.drawCircle(radius, radius, radius, paint);
        squaredBitmap.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "circle";
    }
}