package com.kk.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/***
 * 两端对齐
 * 标点句尾
 */
public class FitTextHelper {
    protected static final float LIMIT = 0.001f;// 误差
    private static final boolean LastNoSpace = false;
    protected ITextView textView;

    //region space list
    public final static List<Character> sSpcaeList = new ArrayList<>();

    static {
        sSpcaeList.add(',');
        sSpcaeList.add('.');
        sSpcaeList.add(';');
        sSpcaeList.add('\'');
        sSpcaeList.add('\"');
        sSpcaeList.add(':');
        sSpcaeList.add('?');
        sSpcaeList.add('~');
        sSpcaeList.add('!');
        sSpcaeList.add('‘');
        sSpcaeList.add('’');
        sSpcaeList.add('”');
        sSpcaeList.add('“');
        sSpcaeList.add('；');
        sSpcaeList.add('：');
        sSpcaeList.add('，');
        sSpcaeList.add('。');
        sSpcaeList.add('？');
        sSpcaeList.add('！');
        sSpcaeList.add('(');
        sSpcaeList.add(')');
        sSpcaeList.add('[');
        sSpcaeList.add(']');
        sSpcaeList.add('@');
        sSpcaeList.add('/');
        sSpcaeList.add('#');
        sSpcaeList.add('$');
        sSpcaeList.add('%');
        sSpcaeList.add('^');
        sSpcaeList.add('&');
        sSpcaeList.add('*');
        sSpcaeList.add('{');
        sSpcaeList.add('}');
        sSpcaeList.add('<');
        sSpcaeList.add('>');
        sSpcaeList.add('/');
        sSpcaeList.add('\\');
        sSpcaeList.add('+');
        sSpcaeList.add('-');
        sSpcaeList.add('·');
        sSpcaeList.add('●');
        sSpcaeList.add('【');
        sSpcaeList.add('】');
        sSpcaeList.add('《');
        sSpcaeList.add('》');
        sSpcaeList.add('『');
        sSpcaeList.add('』');
        sSpcaeList.add('／');
    }
    //endregion

    protected volatile boolean mFittingText = false;

    public FitTextHelper(ITextView textView) {
        this.textView = textView;
    }

    public static boolean isSpace(char charSequence){
        return sSpcaeList.contains(charSequence);
    }
    /***
     * @param textView textview
     * @return 是否是单行
     */
    public static boolean isSingleLine(TextView textView) {
        if (textView == null) return false;
        if (textView instanceof BaseTextView) {
            return ((BaseTextView) textView).isSingleLine();
        }
        if (textView == null) {
            return false;
        }
        int type = textView.getInputType();
        return (type & EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE) == EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE;
    }

//    public float getLineHieght() {
//        Paint.FontMetrics fm = textView.getPaint().getFontMetrics();
//        float baseline = fm.descent - fm.ascent;
//        float multi = textView.getLineSpacingMultiplierCompat();
//        float space = textView.getLineSpacingExtraCompat();
//        //字距
//        return (baseline + fm.leading)
//                * multi + space;
//    }

    /**
     * @return 文本框的当前最大行数
     */
    protected int getMaxLineCount() {
        float vspace = textView.getTextLineHeight();
        float height = textView.getTextHeight();
        return (int) (height / vspace);
    }

    //
//    protected boolean isSingle(TextView textView) {
//        int inputType = textView.getInputType();
//        return (inputType & EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE) == EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE;
//    }

    /**
     * 文本框的宽度
     *
     * @param textView 文本框
     * @return 宽度
     */
    public static int getTextWidth(TextView textView) {
        return textView.getMeasuredWidth() - textView.getCompoundPaddingLeft()
                - textView.getCompoundPaddingRight();
    }

    /***
     * @param text  文本
     * @param paint 画笔
     * @return 文本布局
     */
    public StaticLayout getStaticLayout(CharSequence text, TextPaint paint) {
        return getStaticLayout(textView.getTextView(), text, paint);
    }

    /**
     * @param textView 文本框
     * @param text     文本
     * @param paint    画笔
     * @return 文本布局
     */
    public static StaticLayout getStaticLayout(TextView textView, CharSequence text, TextPaint paint) {
        if (textView instanceof FitTextView) {
            FitTextView fitTextView = (FitTextView) textView;
            return new StaticLayout(text, paint, getTextWidth(textView),
                    getLayoutAlignment(fitTextView), fitTextView.getLineSpacingMultiplierCompat(),
                    fitTextView.getLineSpacingExtraCompat(), fitTextView.getIncludeFontPaddingCompat());
        } else {
            if (Build.VERSION.SDK_INT <= 16) {
                return new StaticLayout(text, paint, getTextWidth(textView),
                        getLayoutAlignment(textView), 0, 0, false);
            }
            return new StaticLayout(text, paint, getTextWidth(textView),
                    getLayoutAlignment(textView), textView.getLineSpacingMultiplier(),
                    textView.getLineSpacingExtra(), textView.getIncludeFontPadding());
        }
    }

    /**
     * 判断内容是否在框内
     *
     * @param text  文本
     * @param paint 画笔
     * @return 没有超过框
     */
    protected boolean isFit(CharSequence text, TextPaint paint) {
        // 自动换行
        boolean mSingleLine = textView.isSingleLine();
        int maxLines = textView.getMaxLinesCompat();
        float multi = textView.getLineSpacingMultiplierCompat();
        float space = textView.getLineSpacingExtraCompat();

        if (multi > 1.0) {
            space += ((float) paint.getFontMetricsInt(null)) * (multi - 1.0f);
        }
        int height = textView.getTextHeight();
        if (LastNoSpace && !mSingleLine) {
            height += Math.round(space);
        }

        int lines = mSingleLine ? 1 : Math.max(1, maxLines);

        StaticLayout layout = getStaticLayout(text, paint);

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
    public float fitTextSize(TextPaint oldPaint, CharSequence text, float max, float min) {
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
        while (Math.abs(high - low) > LIMIT) {
            paint.setTextSize((low + high) / 2.0f);
            if (isFit(getLineBreaks(text, paint), paint)) {
                low = paint.getTextSize();
            } else {
                high = paint.getTextSize();
            }
        }
//        paint.setTextSize(high);
//        while(!isFit(getLineBreaks(text, paint), paint)){
//            high -= (LIMIT/10.0f);
//            paint.setTextSize(high);
//        }
//        while (isFit(getLineBreaks(text, paint), paint)) {
//
//        }

//        float nsize = low;
//        paint.setTextSize(low);
//        int maxlines = getMaxLineCount();//当前最大行数
//        CharSequence ntext = getLineBreaks(text, paint);
//        StaticLayout layout = getStaticLayout(ntext, paint);
//        int lines = layout.getLineCount();//行数
//        int height = textView.getTextHeight();
//        float osize = low;
//        while (maxlines == lines) {
//            //行数相等,放大字体
//            low = osize;
//            osize = osize * 1.001f;
//            if (osize <= max && layout.getHeight() < height) {
//                paint.setTextSize(osize);
//                maxlines = getMaxLineCount();//当前最大行数
//                ntext = getLineBreaks(text, paint);
//                layout = getStaticLayout(ntext, paint);
//                lines = layout.getLineCount();//行数
//            } else {
//                break;
//            }
//        }
        return low;
    }
//
//    protected CharSequence toCharSequence(List<CharSequence> lines, CharSequence text) {
//        if (lines == null || lines.size() == 0) {
//            return text;
//        }
//        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
//        for (CharSequence line : lines) {
//            spannableStringBuilder.append(line);
//            spannableStringBuilder.append("\n");
//        }
//        return spannableStringBuilder;
//    }

    //
//    /***
//     * 标点的正则
//     */
//    protected Pattern getPattern() {
//        return Pattern.compile("[|]'|\"|;|:|,|\\.|\\?|~|!|@|#|$|%|^|&|\\*|\\{|\\}|<|>"
//                + "/|\\\\|\\+|-|·|●| |"
//                + "【|】|‘|’|”|“|【|】|；|：|，|。|？|！|《|》|『|』|／");
//    }
//
//    protected boolean isSpace(CharSequence spcae) {
//        return sSpcaeList.contains(spcae);
//    }
//
//
//    protected CharSequence replaceSpcae(CharSequence line, TextPaint paint, int width) {
////        CharSequence text = line;
////        CharSequence old = text;
////        for (CharSequence sequence : sSpcaeList) {
////            old = text;
////            text = TextUtils.replace(text, new String[]{sequence.toString()}, new CharSequence[]{sequence + " "});
////            float newwidth = paint.measureText(text, 0, text.length());
////            if (newwidth == width) {
////                return text;
////            }
////            if (newwidth > width) {
////                return old;
////            }
////        }
////        return text;
//    }
//
//    protected CharSequence getLineTexts(CharSequence text, TextPaint paint) {
//        if (text == null) return null;
//        int count = text.length();
//        int width = textView.getTextWidth();
//        List<CharSequence> lines = new ArrayList<>();
//        if (width <= 0 || textView.isKeepWord())
//            return text;
//        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
//        int len = 0;
//        for (int i = 0; i < count; i++) {
//            CharSequence cs = text.subSequence(i, i + 1);
//            if (TextUtils.equals(cs, "\n")) {
//                len = 0;
//                lines.add(spannableStringBuilder.subSequence(0, len));
//                spannableStringBuilder.clear();
//            } else {
//                len++;
//                spannableStringBuilder.append(cs);
//                CharSequence ntext = spannableStringBuilder.subSequence(0, len);
//                if (paint.measureText(ntext, 0, ntext.length()) > width) {
//                    //应该换行
//                    len = 1;
//                    lines.add(ntext.subSequence(0, ntext.length() - 1));
//                    spannableStringBuilder.delete(0, ntext.length() - 1);
//                } else if (paint.measureText(ntext, 0, ntext.length()) == width) {
//                    //应该换行
//                    len = 0;
//                    lines.add(ntext);
//                    spannableStringBuilder.clear();
//                } else {
//                    //不用换行
//                }
//
//            }
//        }
//        return toCharSequence(lines, text);
//    }


    /**
     * 拆入换行符，解决中英文的换行问题
     *
     * @param text  内容
     * @param paint 画笔
     * @return 调整后的内容
     */
    public CharSequence getLineBreaks(
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
//            char c = text.charAt(end - 1);// cs最后一个字符
//            boolean needCheck = false;
            if (TextUtils.equals(c, "\n")) {// 已经换行
                ssb.append(text, start, end);
                start = end;
//                needCheck = true;
            } else {
                float lw = paint.measureText(text, start, end);
                if (lw > width) {// 超出宽度，退回一个位置
                    ssb.append(text, start, end - 1);
                    start = end - 1;
                    if (end < length) {
                        CharSequence c2 = text.subSequence(end - 1, end);
                        if (!TextUtils.equals(c2, "\n"))
                            ssb.append('\n');
                    }
//                    needCheck = true;
                } else if (lw == width) {
                    ssb.append(text, start, end);
                    start = end;
                    if (end < length) {
                        CharSequence c2 = text.subSequence(end, end + 1);
                        if (!TextUtils.equals(c2, "\n"))
                            ssb.append('\n');
                    }
//                    needCheck = true;
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

    /***
     * 获取文本框的布局
     *
     * @param textView
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Layout.Alignment getLayoutAlignment(TextView textView) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Layout.Alignment.ALIGN_NORMAL;
        }

        Layout.Alignment alignment;
        switch (textView.getTextAlignment()) {
            case TextView.TEXT_ALIGNMENT_GRAVITY:
                switch (textView.getGravity() & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.START:
                        alignment = Layout.Alignment.ALIGN_NORMAL;
                        break;
                    case Gravity.END:
                        alignment = Layout.Alignment.ALIGN_OPPOSITE;
                        break;
                    case Gravity.LEFT:
                        alignment = (textView.getLayoutDirection() == TextView.LAYOUT_DIRECTION_RTL) ? Layout.Alignment.ALIGN_OPPOSITE
                                : Layout.Alignment.ALIGN_NORMAL;
                        break;
                    case Gravity.RIGHT:
                        alignment = (textView.getLayoutDirection() == TextView.LAYOUT_DIRECTION_RTL) ? Layout.Alignment.ALIGN_NORMAL
                                : Layout.Alignment.ALIGN_OPPOSITE;
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
                alignment = Layout.Alignment.ALIGN_NORMAL;
                break;
            case TextView.TEXT_ALIGNMENT_VIEW_END:
                alignment = Layout.Alignment.ALIGN_OPPOSITE;
                break;
            case TextView.TEXT_ALIGNMENT_INHERIT:
                //
            default:
                alignment = Layout.Alignment.ALIGN_NORMAL;
                break;
        }
        return alignment;
    }

}
