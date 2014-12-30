package com.winginno.charitynow;

import android.text.style.ReplacementSpan;

import java.lang.CharSequence;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class RoundedBackgroundSpan extends ReplacementSpan
{

    private int backgroundColor;
    private int foregroundColor;

    public RoundedBackgroundSpan(int bgColor, int fgColor) {
        backgroundColor = bgColor;
        foregroundColor = fgColor;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint)
    {
        RectF rect = new RectF(x, top, x + (int) measureText(paint, text, start, end), bottom);
        paint.setColor(backgroundColor);
        canvas.drawRoundRect(rect, 5, 5, paint);
        paint.setColor(foregroundColor);
        canvas.drawText(text, start, end, x, y, paint);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm)
    {
        return (int) Math.round(measureText(paint, text, start, end));
    }

    private double measureText(Paint paint, CharSequence text, int start, int end)
    {
        return paint.measureText(text, start, end);
    }
}
