package com.kk.view;

import android.content.Context;
import android.util.AttributeSet;

public class CompactTextView extends BaseTextView {
    protected boolean mNeedScaleText = false;

    public CompactTextView(Context context) {
        this(context, null);
    }

    public CompactTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompactTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isNeedScaleText() {
        return mNeedScaleText;
    }

    public void setNeedScaleText(boolean needScaleText) {
        mNeedScaleText = needScaleText;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode != MeasureSpec.UNSPECIFIED) {
            mNeedScaleText = true;
            setText(getText());
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (text != null && mNeedScaleText && mSingleLine) {
            // 单行，并且设置宽度
            if (getTextWidth() > 0) {
                float s = 1.0f;
                setTextScaleX(s);
                while (s > 0.25f) {
                    float width = getPaint().measureText(text, 0, text.length());
                    if (width >= getTextWidth()) {
                        s -= 0.005f;
                        setTextScaleX(s);
                    } else {
                        break;
                    }
                }
            }
        }
        super.setText(text, type);
    }
}
