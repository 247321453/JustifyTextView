TextView Plus
========================
JustifyTextView
----------------
    两端对齐：
	中文：每个字距变大
	英文：标点和空格变宽
	
CompactTextView继承JustifyTextView
----------------------
	缩放单行的字间距
	CompactTextView.setNeedScaleText(是否缩放字间距)
	
FitTextView继承CompactTextView
----------------------
	自适应内容（调整大小填满view的大小），两端对齐
	FitTextView.setMaxTextSize(最大字号);
    FitTextView.setMinTextSize(最小字号);
    FitTextView.setJustify(两端对齐);