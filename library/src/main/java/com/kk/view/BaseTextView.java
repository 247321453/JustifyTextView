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
import android.util.Log;
import android.widget.TextView;

import com.android.internal.util.FastMath;
import com.kk.justifytextview.R;

class BaseTextView extends TextView implements ITextView {
    protected boolean mSingleLine = false;
    protected boolean mIncludeFontPadding = true;
    protected float mLineSpacingMult = 1;
    protected float mLineSpacingAdd = 0;
    protected int mMaxLines = Integer.MAX_VALUE;
    private static final int INCLUDE_FONT_PADDING = 0;
    private static final int LINE_SPACING_MULTIPLIER = 1;
    private static final int LINE_SPACING_EXTRA = 2;
    private static final int MAX_LINES = 3;
    private static final int SINGLE_LINE = 4;
    private static final int LINEEND_NO_SPACE = 5;
    private static final int JUSTIFY = 6;
    protected boolean mLineEndNoSpace = true;
    protected boolean mJustify = false;

    /***
     * 不拆分单词
     */
    protected boolean mKeepWord = true;
    @SuppressWarnings("deprecation")
    private static final int[] ANDROID_ATTRS = new int[]{
            android.R.attr.includeFontPadding,
            android.R.attr.lineSpacingMultiplier,
            android.R.attr.lineSpacingExtra,
            android.R.attr.maxLines,
            android.R.attr.singleLine,
            R.attr.lineEndNoSpace,
            R.attr.justify,
            };

    public BaseTextView(Context context) {
        this(context, null);
    }

    public BaseTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        setBackgroundColor(Color.TRANSPARENT);
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
            mLineEndNoSpace = a.getBoolean(LINEEND_NO_SPACE, mLineEndNoSpace);
            mJustify = a.getBoolean(JUSTIFY, mJustify);
            a.recycle();
        }
    }

    @Override
    public boolean isKeepWord() {
        return mKeepWord;
    }

    public void setKeepWord(boolean keepWord) {
        mKeepWord = keepWord;
    }

    public boolean isJustify() {
        return mJustify;
    }

    public void setJustify(boolean justify) {
        mJustify = justify;
    }

    public boolean isLineEndNoSpace() {
        return mLineEndNoSpace;
    }

    public void setLineEndNoSpace(boolean lineEndNoSpace) {
        mLineEndNoSpace = lineEndNoSpace;
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
    public int getTextWidth() {
        return FitTextHelper.getTextWidth(this);
    }

    @Override
    public int getTextHeight() {
        return getMeasuredHeight() - getCompoundPaddingTop()
                - getCompoundPaddingBottom();
    }

    /**
     * 设置粗体
     *
     * @param bold 粗体
     */
    public void setBoldText(boolean bold) {
        getPaint().setFakeBoldText(bold);
    }

    /**
     * 设置斜体
     *
     * @param italic 斜体
     */
    public void setItalicText(boolean italic) {
        getPaint().setTextSkewX(italic ? -0.25f : 0f);
    }

    public boolean isItalicText() {
        return getPaint().getTextSkewX() != 0f;
    }

    @Override
    public boolean isSingleLine() {
        return mSingleLine;
    }

    @Override
    public float getTextLineHeight() {
        return getLineHeight();
    }

    @Override
    public TextView getTextView() {
        return this;
    }

    public int getLineHeight(TextPaint paint) {
        return FastMath.round(paint.getFontMetricsInt(null) * getLineSpacingMultiplierCompat()
                + getLineSpacingExtraCompat());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mJustify || mSingleLine) {
            super.onDraw(canvas);
            return;
        }
        TextPaint paint = getPaint();
        float mViewWidth = getTextWidth();
        if (isItalicText()) {
            float letterW = getPaint().measureText("a");
            mViewWidth -= letterW;
        }
        CharSequence text = getText();
        Layout layout = getLayout();
        if (layout == null) {
            layout = FitTextHelper.getStaticLayout(this, getText(), getPaint());
        }
        int count = layout.getLineCount();
        int mTop = layout.getTopPadding();
        for (int i = 0; i < count; i++) {
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);
            int top = layout.getLineTop(i);
            float x = layout.getLineLeft(i);
            int mLineY = mTop + top + getLineHeight(paint);
            CharSequence line = text.subSequence(lineStart, lineEnd);
            if (line.length() == 0) {
                continue;
            }
            if (mLineEndNoSpace) {
                if (TextUtils.equals(line.subSequence(line.length() - 1, line.length()), " ")) {
                    line = line.subSequence(0, line.length() - 1);
                }
                if (TextUtils.equals(line.subSequence(0, 1), " ")) {
                    line = line.subSequence(1, line.length() - 1);
                }
            }
            float lineWidth = getPaint().measureText(text, lineStart, lineEnd);
            boolean needScale = i < (count - 1) && (needScale(text.subSequence(lineEnd - 1, lineEnd)));
            if (needScale || mViewWidth > lineWidth) {
//                float sc = mViewWidth / lineWidth;
                //标点数
                int clen = countEmpty(line);
                Log.d("kk", "line=" + i + ":" + line + ",count=" + clen);
                float d = (mViewWidth - lineWidth) / clen;
                final int lineLen = line.length();
                for (int j = 0; j < lineLen; j++) {
                    float cw = getPaint().measureText(line, j, j + 1);
                    canvas.drawText(line, j, j + 1, x, mLineY, paint);
                    x += cw;
                    // 后面是标点
                    if ((j + 1) < (lineLen-2) && isEmpty(line.charAt(j + 1))) {
                        x += d / 2.0f;
                    }
                    //当前是标点
                    if (isEmpty(line.charAt(j))) {
                        x += d / 2.0f;
                    }
                }
            } else {
                canvas.drawText(line, 0, line.length(), x, mLineY, paint);
            }
        }
    }

    /**
     * 共有多少个标点/空白字符
     *
     * @param text 内容
     * @return 数量
     */
    protected int countEmpty(CharSequence text) {
        int len = text.length();
        int count = 0;
        for (int i = 0; i < len; i++) {
            if (isEmpty(text.charAt(i))) {
                count++;
            }
        }
        return count;
    }

    /**
     * 是否是标点/空白字符
     */
    protected boolean isEmpty(char c) {
        return FitTextHelper.isSpace(c);
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

    /**
     * 是否需要两端对齐
     *
     * @param end 结束字符
     */
    protected boolean needScale(CharSequence end) {
        return TextUtils.equals(end, " ");// || !TextUtils.equals(end, "\n");
    }

}
