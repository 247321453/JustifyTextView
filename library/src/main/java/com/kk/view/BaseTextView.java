package com.kk.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
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
    protected boolean LineNoSpace = true;

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

    public boolean isLineNoSpace() {
        return LineNoSpace;
    }

    public void setLineNoSpace(boolean lineNoSpace) {
        LineNoSpace = lineNoSpace;
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


    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();
        int mViewWidth = getTextWidth();
        CharSequence text = getText();
        int mLineY = 0;
        mLineY += getTextSize();
        Layout layout = getLayout();
        if (layout == null) {
            layout=FitTextHelper.getStaticLayout(this, getText(), getPaint());
        }
        int count = layout.getLineCount();
        for (int i = 0; i < count; i++) {
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);
            CharSequence line = text.subSequence(lineStart, lineEnd);
            if (LineNoSpace) {
                if (TextUtils.equals(line.subSequence(line.length() - 1, line.length()), " ")) {
                    line = line.subSequence(0, line.length() - 1);
                }
            }
            float lineWidth = getPaint().measureText(text, lineStart, lineEnd);
            boolean needScale = i < (count - 1) && (needScale(text.subSequence(lineEnd - 1, lineEnd)));
//            if (i < (count - 1) && needScale(line)) {

            //float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, getPaint());
//                drawScaledText(canvas, mViewWidth, mLineY, lineStart, line, width - getCompoundPaddingLeft() - getCompoundPaddingRight());
//            } else {
//                canvas.drawText(line, 0, line.length(), 0, mLineY, paint);
//            }
            if (needScale) {
//                float sc = mViewWidth / lineWidth;
                float x = getCompoundPaddingLeft();
                int clen = countEmpty(line);
                float d = (mViewWidth - lineWidth) / clen;
                for (int j = 0; j < line.length(); j++) {
                    float cw = getPaint().measureText(line, j, j + 1);
                    canvas.drawText(line, j, j + 1, x, mLineY, getPaint());
                    x += cw;
                    //TODO 是标点
                    if (isEmpty(line, j, j + 1)) {
                        x += d;
                    }
                }
            } else {
                canvas.drawText(line, 0, line.length(), 0, mLineY, paint);
            }
            mLineY += getLineHeight();
        }
    }

    protected int countEmpty(CharSequence text) {
        int len = text.length();
        int count = 0;
        for (int i = 0; i < len; i++) {
            if (isEmpty(text, i, i + 1)) {
                count++;
            }
        }
        return count;
    }

    protected boolean isEmpty(CharSequence c, int start, int end) {
        CharSequence ch = c.subSequence(start, end);
        return FitTextHelper.sSpcaeList.contains(ch);
    }

//    private void drawScaledText(Canvas canvas, int mViewWidth, int mLineY, int lineStart, CharSequence line, float lineWidth) {
//        float x = 0;
//        if (isFirstLineOfParagraph(lineStart, line)) {
//            String blanks = "  ";
//            canvas.drawText(blanks, x, mLineY, getPaint());
//            float bw = StaticLayout.getDesiredWidth(blanks, getPaint());
//            x += bw;
//
//            line = line.subSequence(3, line.length() - 3);
//        }
//
//        float d = (mViewWidth - lineWidth) / line.length() - 1;
//        for (int i = 0; i < line.length(); i++) {
//            String c = String.valueOf(line.charAt(i));
//            float cw = StaticLayout.getDesiredWidth(c, getPaint());
//            canvas.drawText(c, x, mLineY, getPaint());
//            x += cw + d;
//        }
//    }
//
//    private boolean isFirstLineOfParagraph(int lineStart, CharSequence line) {
//        return line.length() > 3 && line.charAt(0) == ' ' && line.charAt(1) == ' ';
//    }

    private boolean needScale(CharSequence end) {
        return TextUtils.equals(end, " ");// || !TextUtils.equals(end, "\n");
    }

}
