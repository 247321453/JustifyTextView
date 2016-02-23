package com.kk.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;

public class FitCompactTextView extends CompactTextView {
    /***
     * 不拆分单词
     */
    protected boolean mKeepWord = true;
    /**
     * 不需要调整大小
     */
    protected boolean mMeasured = false;

    protected float mMinTextSize, mMaxTextSize;
    protected volatile boolean mFittingText = false;
    protected FitTextHelper mFitTextHelper;

    public FitCompactTextView(Context context) {
        this(context, null);
    }

    public FitCompactTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FitCompactTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, new int[]{
                    com.kk.justifytextview.R.attr.ftMaxTextSize,
                    com.kk.justifytextview.R.attr.ftMinTextSize,
            });
            mMinTextSize = a.getDimension(0, mOriginalTextSize / 2.0f);
            mMaxTextSize = a.getDimension(1, mOriginalTextSize * 2.0f);
            a.recycle();
        } else {
            mMinTextSize = mOriginalTextSize;
            mMaxTextSize = mOriginalTextSize;
        }
    }

    protected FitTextHelper getFitTextHelper() {
        if (mFitTextHelper == null) {
            mFitTextHelper = new FitTextHelper(this);
        }
        return mFitTextHelper;
    }

    public boolean isKeepWord() {
        return mKeepWord;
    }

    public void setKeepWord(boolean keepWord) {
        mKeepWord = keepWord;
    }

    public float getMinTextSize() {
        return mMinTextSize;
    }

    public void setMinTextSize(float minTextSize) {
        mMinTextSize = minTextSize;
    }

    public float getMaxTextSize() {
        return mMaxTextSize;
    }

    public void setMaxTextSize(float maxTextSize) {
        mMaxTextSize = maxTextSize;
    }


    public boolean isMeasured() {
        return mMeasured;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.UNSPECIFIED
                && heightMode == MeasureSpec.UNSPECIFIED) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, mOriginalTextSize);
            mMeasured = false;
        } else {
            mMeasured = true;
            fitText(getText());
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        fitText(getText());
    }

    protected void fitText(CharSequence text) {
        if (!mMeasured || mFittingText || mSingleLine)
            return;
        mFittingText = true;
        TextPaint oldPaint = getPaint();
        float size = getFitTextHelper().fitTextSize(oldPaint, text, mMaxTextSize, mMinTextSize);
        setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        setText(getFitTextHelper().getLineBreaks(text, getPaint()));
        mFittingText = false;
    }
}
