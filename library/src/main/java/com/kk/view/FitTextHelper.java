package com.kk.view;

import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;

/***
 * 两端对齐
 * 标点句尾
 */
class FitTextHelper {
    private static final float LIMIT = 0.001f;// 误差
    private JustifyTextView textView;

    FitTextHelper(JustifyTextView textView) {
        this.textView = textView;
    }

    /**
     * 判断内容是否在框内
     *
     * @param text  文本
     * @param paint 画笔
     * @return 没有超过框
     */
    boolean isFit(CharSequence text, TextPaint paint) {
        // 自动换行
        boolean mSingleLine = textView.isSingleLine();
        int maxLines = textView.getMaxLinesCompat();
        float multi = textView.getLineSpacingMultiplierCompat();
        float space = textView.getLineSpacingExtraCompat();

        if (multi > 1.0) {
            space += ((float) paint.getFontMetricsInt(null)) * (multi - 1.0f);
        }
        int height = textView.getTextHeight();
        if (!mSingleLine) {
            height += Math.round(space);
        }

        int lines = mSingleLine ? 1 : Math.max(1, maxLines);
        StaticLayout layout = textView.createLayout(text, paint);
        return layout.getLineCount() <= lines && layout.getHeight() <= height;
    }

    /**
     * 调整字体大小
     *
     * @param oldPaint 旧画笔
     * @param text     内容
     * @param max      最大字体
     * @param min      最小字体
     * @return 适合字体大小
     */
    float fitTextSize(TextPaint oldPaint, CharSequence text, float max, float min) {
        if (TextUtils.isEmpty(text)) {
            if (oldPaint != null) {
                return oldPaint.getTextSize();
            }
            if (textView != null) {
                return textView.getTextSize();
            }
        }
        float low = min;
        float high = max;
        TextPaint paint = new TextPaint(oldPaint);
        //二分法，取最适合的字体大小
        while (Math.abs(high - low) > LIMIT) {
            paint.setTextSize((low + high) / 2.0f);
            if (isFit(getLineBreaks(text, paint), paint)) {
                low = paint.getTextSize();
            } else {
                high = paint.getTextSize();
            }
        }
        return low;
    }

    /**
     * 拆入空格，解决中英文的换行问题
     *
     * @param text  内容
     * @param paint 画笔
     * @return 调整后的内容
     */
    CharSequence getLineBreaks(
            CharSequence text, TextPaint paint) {
        int width = textView.getTextWidth();
        boolean keepWord = textView.isKeepWord();
        if (width <= 0 || keepWord)
            return text;
        int length = text.length();
        int start = 0, end = 1;
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        while (end <= length) {
            CharSequence c = text.subSequence(end - 1, end);
            if (TextUtils.equals(c, "\n")) {// 已经换行
                ssb.append(text, start, end);
                start = end;
            } else {
                float lw = paint.measureText(text, start, end);
                if (lw > width) {// 超出宽度，退回一个位置
                    ssb.append(text, start, end - 1);
                    start = end - 1;
                    if (end < length) {
                        CharSequence c2 = text.subSequence(end - 1, end);
                        if (!TextUtils.equals(c2, "\n"))
                            ssb.append(' ');
                    }
                } else if (lw == width) {
                    ssb.append(text, start, end);
                    start = end;
                    if (end < length) {
                        CharSequence c2 = text.subSequence(end, end + 1);
                        if (!TextUtils.equals(c2, "\n"))
                            ssb.append(' ');
                    }
                } else if (end == length) {
                    // 已经是最后一个字符
                    ssb.append(text, start, end);
                    start = end;
                }
            }
            end++;
        }
        return ssb;
    }
}
