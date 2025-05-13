package com.example.lab11e;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class CustomChartView extends View {
    private float[] values;
    private String chartType;
    private Paint paint;
    private Paint textPaint;
    private Paint outlinePaint;
    private int[] colors = {
            Color.parseColor("#FF5722"), // Portocaliu vibrant
            Color.parseColor("#3F51B5"), // Albastru indigo
            Color.parseColor("#4CAF50"), // Verde smarald
            Color.parseColor("#FFC107"), // Galben amber
            Color.parseColor("#E91E63")  // Roz intens
    };
    private float animationProgress = 0f;

    public CustomChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setShadowLayer(10f, 5f, 5f, Color.parseColor("#40000000")); // Umbră

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(30f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setShadowLayer(5f, 2f, 2f, Color.parseColor("#80000000")); // Umbră text

        outlinePaint = new Paint();
        outlinePaint.setAntiAlias(true);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(4f);
        outlinePaint.setColor(Color.WHITE);

        // Inițializează animația
        startAnimation();
    }

    public void setChartData(float[] values, String chartType) {
        this.values = values;
        this.chartType = chartType;
        startAnimation();
        invalidate();
    }

    private void startAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(1500); // Animație mai lungă pentru efect
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            animationProgress = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (values == null || chartType == null) return;

        switch (chartType) {
            case "PieChart":
                drawPieChart(canvas);
                break;
            case "BarChart":
                drawBarChart(canvas);
                break;
            case "ColumnChart":
                drawColumnChart(canvas);
                break;
        }
    }

    private void drawPieChart(Canvas canvas) {
        float total = 0;
        for (float value : values) total += value;
        float startAngle = 0;
        RectF rect = new RectF(100, 100, getWidth() - 100, getHeight() - 100);

        for (int i = 0; i < values.length; i++) {
            paint.setColor(colors[i % colors.length]);
            float sweepAngle = (values[i] / total) * 360 * animationProgress;
            canvas.drawArc(rect, startAngle, sweepAngle, true, paint);
            canvas.drawArc(rect, startAngle, sweepAngle, true, outlinePaint); // Contur alb
            // Etichetă
            if (animationProgress > 0.8f) {
                float angle = startAngle + sweepAngle / 2;
                float radius = (getWidth() - 200) / 2;
                float x = (float) (getWidth() / 2 + Math.cos(Math.toRadians(angle)) * radius * 0.7);
                float y = (float) (getHeight() / 2 + Math.sin(Math.toRadians(angle)) * radius * 0.7);
                // Fundal semi-transparent pentru etichetă
                paint.setColor(Color.parseColor("#80000000"));
                canvas.drawCircle(x, y, 30f, paint);
                canvas.drawText(String.format("%.1f", values[i]), x, y + 10, textPaint);
            }
            startAngle += sweepAngle;
        }
    }

    private void drawBarChart(Canvas canvas) {
        float barHeight = (getHeight() - 50) / values.length; // Înălțime bară (orizontale)
        float maxValue = getMaxValue();

        for (int i = 0; i < values.length; i++) {
            paint.setColor(colors[i % colors.length]);
            float barWidth = (values[i] / maxValue) * (getWidth() - 150) * animationProgress; // Lungime bară
            float top = i * barHeight + 10;
            float bottom = (i + 1) * barHeight - 10;
            // Desenează bară orizontală cu colțuri rotunjite
            RectF barRect = new RectF(50, top, 50 + barWidth, bottom);
            canvas.drawRoundRect(barRect, 10f, 10f, paint);
            canvas.drawRoundRect(barRect, 10f, 10f, outlinePaint); // Contur alb
            // Etichetă
            if (animationProgress > 0.8f) {
                paint.setColor(Color.parseColor("#80000000"));
                canvas.drawRect(50 + barWidth, top, 50 + barWidth + 60, bottom, paint);
                canvas.drawText(String.format("%.1f", values[i]), 50 + barWidth + 30, top + barHeight / 2 + 10, textPaint);
            }
        }
    }

    private void drawColumnChart(Canvas canvas) {
        float barWidth = (getWidth() - 50) / values.length; // Lățime bară (verticale)
        float maxValue = getMaxValue();

        for (int i = 0; i < values.length; i++) {
            paint.setColor(colors[i % colors.length]);
            float barHeight = (values[i] / maxValue) * (getHeight() - 150) * animationProgress;
            float left = i * barWidth + 20;
            float right = (i + 1) * barWidth - 20;
            canvas.drawRect(left, getHeight() - barHeight, right, getHeight() - 50, paint);
            canvas.drawRect(left, getHeight() - barHeight, right, getHeight() - 50, outlinePaint); // Contur alb
            // Etichetă
            if (animationProgress > 0.8f) {
                paint.setColor(Color.parseColor("#80000000"));
                canvas.drawRect(left, getHeight() - barHeight - 40, right, getHeight() - barHeight, paint);
                canvas.drawText(String.format("%.1f", values[i]), left + barWidth / 2, getHeight() - barHeight - 10, textPaint);
            }
        }
    }

    private float getMaxValue() {
        float max = values[0];
        for (float value : values) {
            if (value > max) max = value;
        }
        return max;
    }
}