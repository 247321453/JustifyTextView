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

import com.kk.justifytextview.R;

import java.lang.reflect.Method;

/***
 * 两端对齐
 */
public class JustifyTextView extends TextView {
    protected boolean mSingleLine = false;
    protected boolean mIncludeFontPadding = true;
    protected float mLineSpacingMult = 1;
    protected float mLineSpacingAdd = 0;
    protected int mMaxLines = Integer.MAX_VALUE;
    protected boolean mJustify = false;

    /***
     * 不拆分单词
     */
    protected boolean mKeepWord = true;
    private Method assumeLayout;

    public JustifyTextView(Context context) {
        this(context, null);
    }

    public JustifyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JustifyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        try {
            assumeLayout = TextView.class.getDeclaredMethod("assumeLayout");
            assumeLayout.setAccessible(true);
        } catch (Exception e) {
        }
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, new int[]{
                    android.R.styleable.TextView_includeFontPadding,
                    android.R.styleable.TextView_lineSpacingMultiplier,
                    android.R.styleable.TextView_lineSpacingExtra,
                    android.R.styleable.TextView_maxLines,
                    android.R.styleable.TextView_singleLine,
                    R.styleable.JustifyTextView_justify
            });
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                mIncludeFontPadding = a.getBoolean(
                        android.R.styleable.TextView_includeFontPadding, mIncludeFontPadding);
                mLineSpacingMult = a.getFloat(
                        android.R.styleable.TextView_lineSpacingMultiplier, mLineSpacingMult);
                mLineSpacingAdd = a.getDimensionPixelSize(
                        android.R.styleable.TextView_lineSpacingExtra, (int) mLineSpacingAdd);
                mMaxLines = a.getInteger(android.R.styleable.TextView_maxLines, mMaxLines);
            }
            mSingleLine = a.getBoolean(android.R.styleable.TextView_singleLine, mSingleLine);
            mJustify = a.getBoolean(R.styleable.JustifyTextView_justify, mJustify);
            a.recycle();
        }
    }

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

    public int getTextWidth() {
        return getMeasuredWidth() - getCompoundPaddingLeft()
                - getCompoundPaddingRight();
    }

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

    public boolean isSingleLine() {
        return mSingleLine;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mJustify || mSingleLine) {
            super.onDraw(canvas);
            return;
        }
        forceDraw(canvas);
    }

    protected void forceDraw(Canvas canvas) {
        TextPaint paint = getPaint();
        float mViewWidth = getTextWidth();
        final float wordW = paint.measureText("aa");
        if (isItalicText()) {
            float letterW = paint.measureText("a");
            mViewWidth -= letterW;
        }
        CharSequence text = getText();
        Layout layout = getLayout();
        if (layout == null && assumeLayout != null) {
            try {
                assumeLayout.invoke(this);
                layout = getLayout();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (layout == null) {
            layout = FitTextHelper.getStaticLayout(this, getText(), getPaint());
        }

        int count = layout.getLineCount();
//        int mTop = layout.getTopPadding();
//        boolean firstIsPoint = false;
        for (int i = 0; i < count; i++) {
            int lineStart = layout.getLineStart(i);
//            if (firstIsPoint) {
//                lineStart++;
//            }
            int lineEnd = layout.getLineEnd(i);
            int lbottom = layout.getLineBottom(i);
            float x = layout.getLineLeft(i);
            int mLineY = lbottom - layout.getLineDescent(i);
            CharSequence line = text.subSequence(lineStart, lineEnd);
            if (line.length() == 0) {
                continue;
            }
            if (TextUtils.equals(line.subSequence(line.length() - 1, line.length()), " ")) {
                line = line.subSequence(0, line.length() - 1);
            }
            if (TextUtils.equals(line.subSequence(0, 1), " ")) {
                line = line.subSequence(1, line.length() - 1);
            }
            float lineWidth = getPaint().measureText(text, lineStart, lineEnd);
            boolean needScale = i < (count - 1) && (needScale(text.subSequence(lineEnd - 1, lineEnd)));
            if ((mKeepWord && needScale) || (!mKeepWord && mViewWidth > lineWidth && lineWidth >= (mViewWidth - wordW*1.5f))) {
                //标点数
                final int lineLen = line.length();
                float d;
//                char nextLineFirst = ' ';
//                if (mKeepWord && (lineEnd + 1) < text.length()) {
//                    nextLineFirst = text.charAt(lineEnd + 1);
//                }
                if (!mKeepWord) {
                    d = (mViewWidth - lineWidth) / (float) (lineLen - 1);
                    if (lineLen > 0 && isEmpty(line.charAt(lineLen - 1))) {
                        float f = (d / 2.0f) / (float) lineLen;
                        d += f;
                    }
                } else {
                    int clen = countEmpty(line);
                    d = (mViewWidth - lineWidth) / clen;
                }
//                firstIsPoint = mKeepWord && isPoint(nextLineFirst);
                float suffix = 0;//!firstIsPoint ? 0 :
//                        -(paint.measureText("" + nextLineFirst) / (float) (lineLen - 1));
                for (int j = 0; j < lineLen; j++) {
                    float cw = getPaint().measureText(line, j, j + 1);
                    canvas.drawText(line, j, j + 1, x, mLineY, paint);
                    x += (cw + suffix);
                    if (!mKeepWord) {
                        x += d;
                    } else {
                        //当前是标点
                        if (isEmpty(line.charAt(j))) {
                            x += d;
                        }
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
        if (mKeepWord) {
            //英文
            return ' ' == c || '\t' == c;
        }
        //非英文
        return isPoint(c);
    }

    protected boolean isPoint(char c) {
        return ' ' == c || '\t' == c || '，' == c ||
                '？' == c || '！' == c || '；' == c ||
                '：' == c || '、' == c || ',' == c || '.' == c;
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
