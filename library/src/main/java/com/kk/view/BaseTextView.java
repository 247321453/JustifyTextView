package com.kk.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

class BaseTextView extends TextView {
    protected boolean mSingleLine = false;
    protected float mOriginalTextSize = 0;
    protected boolean mIncludeFontPadding = true;
    protected float mLineSpacingMult = 1;
    protected float mLineSpacingAdd = 0;
    protected int mMaxLines = Integer.MAX_VALUE;
    private static final int INCLUDE_FONT_PADDING = 0;
    private static final int LINE_SPACING_MULTIPLIER = 1;
    private static final int LINE_SPACING_EXTRA = 2;
    private static final int MAX_LINES = 3;
    private static final int SINGLE_LINE = 4;

    @SuppressWarnings("deprecation")
    private static final int[] ANDROID_ATTRS = new int[]{
            android.R.attr.includeFontPadding,
            android.R.attr.lineSpacingMultiplier,
            android.R.attr.lineSpacingExtra,
            android.R.attr.maxLines,
            android.R.attr.singleLine
    };

    public BaseTextView(Context context) {
        this(context, null);
    }

    public BaseTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mOriginalTextSize = getTextSize();
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, ANDROID_ATTRS);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                mIncludeFontPadding = a.getBoolean(INCLUDE_FONT_PADDING,
                        mIncludeFontPadding);
                mLineSpacingMult = a.getFloat(LINE_SPACING_MULTIPLIER,
                        mLineSpacingMult);
                mLineSpacingAdd = a.getDimensionPixelSize(LINE_SPACING_EXTRA,
                        (int) mLineSpacingAdd);
                mMaxLines = a.getInteger(MAX_LINES, mMaxLines);
            }
            mSingleLine = a.getBoolean(SINGLE_LINE, mSingleLine);
            a.recycle();
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean getIncludeFontPaddingCompat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return getIncludeFontPadding();
        } else {
            return mIncludeFontPadding;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public float getLineSpacingMultiplierCompat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return getLineSpacingMultiplier();
        } else {
            return mLineSpacingMult;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public float getLineSpacingExtraCompat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return getLineSpacingExtra();
        } else {
            return mLineSpacingAdd;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public int getMaxLinesCompat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return getMaxLines();
        } else {
            return mMaxLines;
        }
    }

    @Override
    public void setLineSpacing(float add, float mult) {
        super.setLineSpacing(add, mult);
        mLineSpacingAdd = add;
        mLineSpacingMult = mult;
    }

    @Override
    public void setIncludeFontPadding(boolean includepad) {
        super.setIncludeFontPadding(includepad);
        mIncludeFontPadding = includepad;
    }

    @Override
    public void setMaxLines(int maxlines) {
        super.setMaxLines(maxlines);
        mMaxLines = maxlines;
    }

    @Override
    public void setSingleLine(boolean singleLine) {
        super.setSingleLine(singleLine);
        mSingleLine = singleLine;
    }

    @Override
    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        mOriginalTextSize = getTextSize();
    }

    public int getTextWidth() {
        return getMeasuredWidth() - getCompoundPaddingLeft()
                - getCompoundPaddingRight();
    }

    public int getTextHeight() {
        return getMeasuredHeight() - getCompoundPaddingTop()
                - getCompoundPaddingBottom();
    }

    public void setBoldText(boolean b) {
        getPaint().setFakeBoldText(b);
    }

    public void setItalicText(boolean b) {
        getPaint().setTextSkewX(b ? -0.25f : 0f);
    }

    public boolean isSingleLine() {
        return mSingleLine;
    }

    public float getOriginalTextSize() {
        return mOriginalTextSize;
    }
}
