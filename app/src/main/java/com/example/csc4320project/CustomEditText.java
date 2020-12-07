package com.example.csc4320project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomEditText extends androidx.appcompat.widget.AppCompatEditText {

    private Rect rect;
    private Paint paint;

    public CustomEditText(Context context) {
        super(context);
        customInit();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        customInit();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        customInit();
    }

    private void customInit() {
        rect = new Rect();
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GRAY);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.MONOSPACE);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int baseline;
        int lineCount = getLineCount();
        int lineNumber = 1;

        for (int a = 0; a < lineCount; a++) {
            baseline = getLineBounds(a, null);
            if (a == 0) {
                canvas.drawText("" + lineNumber, rect.left, baseline, paint);
                lineNumber++;
            } else if (getText().charAt(getLayout().getLineStart(a) - 1) == '\n') {
                canvas.drawText("" + lineNumber, rect.left, baseline, paint);
                lineNumber++;
            }
        }

        int calcPadding = (int) Math.floor(Math.log10(lineNumber - 1)) * 18 + 24;
        setPadding(calcPadding, getPaddingTop(), getPaddingRight(), getPaddingBottom());

        super.onDraw(canvas);
    }

}
