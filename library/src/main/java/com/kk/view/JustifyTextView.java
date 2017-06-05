package com.kk.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.kk.justifytextview.R;

import java.lang.reflect.Method;

/***
 * 两端对齐
 */
@SuppressLint("AppCompatCustomView")
public class JustifyTextView extends TextView {
    protected boolean mSingleLine = false;
    protected boolean mIncludeFontPadding = false;
    protected float mLineSpacingMult = 1.0f;
    protected float mLineSpacingAdd = 0;
    protected int mMaxLines = Integer.MAX_VALUE;
    /**
     * 两端对齐
     */
    protected boolean mJustify = false;

    /***
     * 不拆分单词，英文模式
     */
    protected boolean mKeepWord = true;

    private Method assumeLayout;
    private Method getLayoutAlignment;

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
            //ignore
        }
        try {
            getLayoutAlignment = TextView.class.getDeclaredMethod("getLayoutAlignment");
            getLayoutAlignment.setAccessible(true);
        } catch (Exception e) {
//ignore
        }
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, new int[]{
                    R.styleable.JustifyTextView_justify
            });
            mSingleLine = isSingleLineCompat();
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

    public boolean isSingleLineCompat() {
        int type = getInputType();
        if ((type & EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE) == EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE) {
            return true;
        }
        return false;
    }

    public boolean isSingleLine() {
        return mSingleLine;
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

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mJustify || mSingleLine) {
            //单行，或者不开启两端对齐
            super.onDraw(canvas);
            return;
        }
        forceDraw(canvas);
    }

    public Layout.Alignment getLayoutAlignmentCompat() {
        if (getLayoutAlignment != null) {
            try {
                return (Layout.Alignment) getLayoutAlignment.invoke(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Layout.Alignment.ALIGN_NORMAL;
        }
        Layout.Alignment alignment;
        switch (getTextAlignment()) {
            case TEXT_ALIGNMENT_GRAVITY:
                switch (getGravity() & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.START:
                        alignment = Layout.Alignment.ALIGN_NORMAL;
                        break;
                    case Gravity.END:
                        alignment = Layout.Alignment.ALIGN_OPPOSITE;
                        break;
                    case Gravity.LEFT:
                        alignment = Layout.Alignment.ALIGN_NORMAL;
                        break;
                    case Gravity.RIGHT:
                        alignment = Layout.Alignment.ALIGN_OPPOSITE;
                        break;
                    case Gravity.CENTER_HORIZONTAL:
                        alignment = Layout.Alignment.ALIGN_CENTER;
                        break;
                    default:
                        alignment = Layout.Alignment.ALIGN_NORMAL;
                        break;
                }
                break;
            case TextView.TEXT_ALIGNMENT_TEXT_START:
                alignment = Layout.Alignment.ALIGN_NORMAL;
                break;
            case TextView.TEXT_ALIGNMENT_TEXT_END:
                alignment = Layout.Alignment.ALIGN_OPPOSITE;
                break;
            case TextView.TEXT_ALIGNMENT_CENTER:
                alignment = Layout.Alignment.ALIGN_CENTER;
                break;
            case TextView.TEXT_ALIGNMENT_VIEW_START:
                alignment = (getLayoutDirection() == TextView.LAYOUT_DIRECTION_RTL) ?
                        Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_NORMAL;
                break;
            case TextView.TEXT_ALIGNMENT_VIEW_END:
                alignment = (getLayoutDirection() == TextView.LAYOUT_DIRECTION_RTL) ?
                        Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
                break;
            case TextView.TEXT_ALIGNMENT_INHERIT:
                // This should never happen as we have already resolved the text alignment
                // but better safe than sorry so we just fall through
            default:
                alignment = Layout.Alignment.ALIGN_NORMAL;
                break;
        }
        return alignment;
    }

    protected StaticLayout createLayout(CharSequence text, TextPaint paint) {
        return new StaticLayout(text, paint, getTextWidth(),
                getLayoutAlignmentCompat(), getLineSpacingMultiplierCompat(),
                getLineSpacingExtraCompat(), getIncludeFontPaddingCompat());
    }

    private CharSequence trim(CharSequence text) {
        final int count = text.length();
        int len = count;
        int st = 0;

        while ((st < len) && (text.charAt(st) <= ' ')) {
            st++;
        }
        while ((st < len) && (text.charAt(len - 1) <= ' ')) {
            len--;
        }
        return ((st > 0) || (len < count)) ? text.subSequence(st, len) : text;
    }

    protected void forceDraw(Canvas canvas) {
        TextPaint paint = getPaint();
        float mViewWidth = getTextWidth();
        if (isItalicText()) {
            float letterW = paint.measureText("a");
            mViewWidth -= letterW;
        }
        CharSequence text = getText();
        //文本绘制容器，里面处理自动换行
        Layout layout = getLayout();
        if (layout == null && assumeLayout != null) {
            //反射获取
            try {
                assumeLayout.invoke(this);
                layout = getLayout();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (layout == null) {
            //手工构造
            layout = createLayout(text, paint);
        }
        int count = layout.getLineCount();
        for (int i = 0; i < count; i++) {
            //每一行
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);
            int lbottom = layout.getLineBottom(i);
            //每行的开始的绘制坐标
            float lineX = layout.getLineLeft(i);
            int lineY = lbottom - layout.getLineDescent(i);
            //每行的字符串
            CharSequence line = text.subSequence(lineStart, lineEnd);
            if (line.length() == 0) {
                continue;
            }
            //最后一个是换行符
            char end = line.charAt(line.length() - 1);
            if (end == '\n') {
                canvas.drawText(line, 0, line.length(), lineX, lineY, paint);
                continue;
            }
            if (i == (count - 1)) {
                //最后一行
                canvas.drawText(line, 0, line.length(), lineX, lineY, paint);
                continue;
            }
            //行首尾去掉空格
            line = trim(line);
            final int lineLen = line.length();
            //当前行字符串的宽度
            float lineWidth = getPaint().measureText(line, 0, line.length());
            if (mViewWidth > lineWidth) {
                //修正多出的空白
                float d;
                if (!mKeepWord) {
                    //非英文模式，把空白的地方的宽度分到全部字符（除首尾2个字符）
                    d = (mViewWidth - lineWidth) / (float) (lineLen - 1);
                } else {
                    //英文模式，把空白的地方的宽度，分到标点字符
                    int clen = countEmpty(line);
                    d = (mViewWidth - lineWidth) / clen;
                }
                for (int j = 0; j < lineLen; j++) {
                    //字宽
                    float cw = getPaint().measureText(line, j, j + 1);
                    canvas.drawText(line, j, j + 1, lineX, lineY, paint);
                    lineX += cw;
                    if (!mKeepWord) {
                        lineX += d;
                    } else {
                        //当前是标点
                        if (isEmpty(line.charAt(j))) {
                            lineX += d;
                        }
                    }
                }
            } else {
                canvas.drawText(line, 0, lineLen, lineX, lineY, paint);
            }
        }
    }

    /**
     * 共有多少个标点/空白字符
     *
     * @param text 内容
     * @return 数量
     */
    private int countEmpty(CharSequence text) {
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
    private boolean isEmpty(char c) {
        if (mKeepWord) {
            //英文
            return ' ' == c || '\t' == c;
        }
        //非英文
        return isPoint(c);
    }

    private boolean isPoint(char c) {
        return ' ' == c || '\t' == c || '，' == c ||
                '？' == c || '！' == c || '；' == c ||
                '：' == c || '、' == c || ',' == c || '.' == c;
    }


    private String debug;

    public void setDebug(String debug) {
        this.debug = debug;
    }

    @Override
    public String toString() {
        if (!TextUtils.isEmpty(debug)) {
            return getClass().getSimpleName() + ":" + debug;
        }
        return super.toString();
    }
}
