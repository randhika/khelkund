package com.appacitive.khelkund.infra.transforms;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.squareup.picasso.Transformation;

/**
 * Created by sathley on 4/9/2015.
 */
public class CircleTransform2 implements Transformation {
    private int mBackgroundColor;

    public CircleTransform2(int backgroundColor)
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
        backgroundPaint.setDither(true);
        backgroundPaint.setAntiAlias(true);

        canvas.drawCircle(radius, radius, radius, backgroundPaint);


        Paint paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);

        canvas.drawBitmap(squaredBitmap, radius - (squaredBitmap.getWidth() / 2f), radius - (squaredBitmap.getHeight() / 2f), paint);

        squaredBitmap.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "circle";
    }
}
