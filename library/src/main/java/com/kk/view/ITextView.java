package com.kk.view;

import android.widget.TextView;

public interface ITextView {
    /***
     *
     * @return 行高度
     */
    float getTextLineHeight();

    /***
     * @return 文本框高度
     */
    int getTextHeight();


    /***
     * @return view
     */
    TextView getTextView();

    /***
     * @return 是否当行
     */
    boolean isSingleLine();


    /***
     * @return 最大行数
     */
    int getMaxLinesCompat();


    /***
     * @return 多倍行距
     */
    float getLineSpacingMultiplierCompat();


    /***
     * @return 行间距
     */
    float getLineSpacingExtraCompat();


    /***
     * @return 当前字体大小
     */
    float getTextSize();

    /***
     * @return 文本框宽度
     */
    int getTextWidth();


    /***
     * @return 不拆分单词
     */
    boolean isKeepWord();

    /** 设置的初始字体 */
    float getOriginalTextSize();

    /**
     *
     * @return 原始文本
     */
    CharSequence getOriginalText();
}
