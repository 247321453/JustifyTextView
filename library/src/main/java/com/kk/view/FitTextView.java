package com.kk.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;

import com.kk.justifytextview.R;

public class FitTextView extends CompactTextView {

    private boolean mMeasured = true;
    /**
     * 不需要调整大小
     */
    private boolean mNeedFit = true;
    protected float mOriginalTextSize = 0;
    private float mMinTextSize, mMaxTextSize;
    protected CharSequence mOriginalText;
    /**
     * 正在调整字体大小
     */
    protected volatile boolean mFittingText = false;
    protected FitTextHelper mFitTextHelper;

    public FitTextView(Context context) {
        this(context, null);
    }

    public FitTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FitTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mOriginalTextSize = getTextSize();
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, new int[]{
                    R.attr.ftMaxTextSize,
                    R.attr.ftMinTextSize,
                    });
            mMaxTextSize = a.getDimension(R.attr.ftMaxTextSize, mOriginalTextSize * 2.0f);
            mMinTextSize = a.getDimension(R.attr.ftMinTextSize, mOriginalTextSize / 2.0f);
            a.recycle();
        } else {
            mMinTextSize = mOriginalTextSize / 2.0f;
            mMaxTextSize = mOriginalTextSize * 2.0f;
        }
    }

    private FitTextHelper getFitTextHelper() {
        if (mFitTextHelper == null) {
            mFitTextHelper = new FitTextHelper(this);
        }
        return mFitTextHelper;
    }

    /**
     * @return 最小字体大小
     */
    public float getMinTextSize() {
        return mMinTextSize;
    }

    /**
     * @param minTextSize 最小字体大小
     */
    public void setMinTextSize(float minTextSize) {
        mMinTextSize = minTextSize;
    }

    /**
     * @return 最大字体大小
     */
    public float getMaxTextSize() {
        return mMaxTextSize;
    }

    /**
     * @param maxTextSize 最大字体大小
     */
    public void setMaxTextSize(float maxTextSize) {
        mMaxTextSize = maxTextSize;
    }

    /**
     * 是否需要调整字体
     *
     * @return
     */
    public boolean isNeedFit() {
        return mNeedFit;
    }

    /**
     * @param needFit 是否需要调整字体大小
     */
    public void setNeedFit(boolean needFit) {
        mNeedFit = needFit;
    }

    @Override
    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        mOriginalTextSize = getTextSize();
    }

    public float getOriginalTextSize() {
        return mOriginalTextSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("kk", this + "::onDraw");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.UNSPECIFIED
                && heightMode == MeasureSpec.UNSPECIFIED) {
            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, mOriginalTextSize);
            mMeasured = true;
        } else {
            mMeasured = false;
            fitText(getOriginalText());
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        mOriginalText = text;
        super.setText(text, type);
        fitText(text);
    }

    public CharSequence getOriginalText() {
        return mOriginalText;
    }

    private void setTextSizeNoDraw(int unit, float size) {
        Context c = getContext();
        Resources r;
        if (c == null)
            r = Resources.getSystem();
        else
            r = c.getResources();
        if (size != getPaint().getTextSize()) {
            getPaint().setTextSize(TypedValue.applyDimension(
                    unit, size, r.getDisplayMetrics()));
        }
    }

    /**
     * 调整字体大小
     *
     * @param text 内容
     */
    protected void fitText(CharSequence text) {
        if (!mNeedFit || mSingleLine || TextUtils.isEmpty(text)) {
            return;
        }
        if (mMeasured || mFittingText) {
            mMeasured = false;
            return;
        }
        mFittingText = true;
        TextPaint oldPaint = getPaint();
        float size = getFitTextHelper().fitTextSize(oldPaint, text, mMaxTextSize, mMinTextSize);
        setTextSizeNoDraw(TypedValue.COMPLEX_UNIT_PX, size);
        super.setText(getFitTextHelper().getLineBreaks(text, getPaint()));
        mFittingText = false;
    }
}
