package com.kk.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.kk.justifytextview.R;

/***
 * 单行变窄
 */
public class CompactTextView extends JustifyTextView {
    protected boolean mNeedScaleText = false;
    private final static float MIN_SCALEX = 0.10f;
    protected final float mMinScaleX;
    private CharSequence mText;
//    protected boolean SUPER_DRAW = false;

    public CompactTextView(Context context) {
        this(context, null);
    }

    public CompactTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompactTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, new int[]{
                    R.styleable.CompactTextView_needScaleText,
                    R.styleable.CompactTextView_minScaleX,
            });
            mNeedScaleText = a.getBoolean(R.styleable.CompactTextView_needScaleText, mNeedScaleText);
            mMinScaleX = a.getFloat(R.styleable.CompactTextView_minScaleX, MIN_SCALEX);
            a.recycle();
        } else {
            mMinScaleX = MIN_SCALEX;
        }
    }

    public boolean isNeedScaleText() {
        return mNeedScaleText;
    }

    /**
     * 水平拉伸，变窄
     */
    public void setNeedScaleText(boolean needScaleText) {
        mNeedScaleText = needScaleText;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mNeedScaleText && mSingleLine) {
            setText(getText());
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        mText = text;
        super.setText(text, type);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mText != null && mNeedScaleText && mSingleLine) {
            // 单行，使字变窄，减少行宽度
            float textWidth = getTextWidth();
            if (textWidth > 0) {
                TextPaint textPaint = getPaint();
                textPaint.setTextScaleX(1.0f);
                float width = textPaint.measureText(mText+"a", 0, mText.length()+1);
                if (width > textWidth) {
                    float s = Math.max(mMinScaleX, textWidth / width);
                    textPaint.setTextScaleX(s);
                }
            }
        }
        super.onDraw(canvas);
    }
}
