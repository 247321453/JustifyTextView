package com.kk.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.kk.justifytextview.R;

public class CompactTextView extends BaseTextView {
    protected boolean mNeedScaleText = false;
    private final static float MIN_SCALEX = 0.25f;
    protected final float mMinScaleX;
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
                    R.attr.needScaleText, R.attr.minScaleX
            });
            mNeedScaleText = a.getBoolean(0, mNeedScaleText);
            mMinScaleX = a.getFloat(1, MIN_SCALEX);
            a.recycle();
        } else {
            mMinScaleX = MIN_SCALEX;
        }
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
        if (mNeedScaleText && mSingleLine) {
            setText(getText());
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (text != null && mNeedScaleText && mSingleLine) {
            // 单行，并且设置宽度
            float textWidth = getTextWidth();
            if (textWidth > 0) {
                setTextScaleX(1.0f);
//                float low = MIN_SCALEX;
//                float high = 1.0f;
//                while (Math.abs(low - high) > 0.001f) {
//                    setTextScaleX((low + high) / 2.0f);
//                    float width = getPaint().measureText(text, 0, text.length());
//                    if (width == textWidth) {
//                        break;
//                    } else if (width > textWidth) {
//                        high = getTextScaleX();
//                    } else {
//                        low = getTextScaleX();
//                    }
//                }
                float width = getPaint().measureText(text, 0, text.length());
                if (width > textWidth) {
//                    float w2 = width = getPaint().measureText(text, 0, text.length() - 1);
//                    float d = Math.max(textWidth / width, textWidth / w2);
//                    d = Math.max(textWidth / width, (d + textWidth / width)/2.0f);
                    float s = Math.max(mMinScaleX, textWidth / width);
                    setTextScaleX(s);
                }
            }
        }
        super.setText(text, type);
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        if (SUPER_DRAW || getTextScaleX() == 1.0f) {
//            super.onDraw(canvas);
//            return;
//        }
//        if (mNeedScaleText && mSingleLine) {
//            Layout layout = getLayout();
//            if (layout == null) {
//                layout = FitTextHelper.getStaticLayout(this, getText(), getPaint());
//            }
//            CharSequence text = getText();
//            float textWidth = getTextWidth();
//            float width = getPaint().measureText(text, 0, text.length());
//            float d = (textWidth - width) / text.length();
//            float x = layout.getLineLeft(0);
//            float y = getLineHeight();
//            for (int j = 0; j < text.length(); j++) {
//                float cw = getPaint().measureText(text, j, j + 1);
//                canvas.drawText(text, j, j + 1, x, y, getPaint());
//                x += cw + d;
//            }
//        } else {
//            super.onDraw(canvas);
//        }
//    }
}
