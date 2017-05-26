package com.kk.justifytext;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kk.view.CompactTextView;
import com.kk.view.FitTextView;
import com.kk.view.JustifyTextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScrollView scrollView=new ScrollView(this);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(linearLayout);
        setContentView(scrollView);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
        TextView textView = new TextView(this);
        textView.setBackgroundColor(Color.RED);
        final JustifyTextView textView02 = new FitTextView(this);
        textView02.setBackgroundColor(Color.GRAY);
        TextView textView03 = new TextView(this);
        textView03.setBackgroundColor(Color.BLUE);
        JustifyTextView textView04 = new FitTextView(this);
        linearLayout.addView(textView, layoutParams);
        linearLayout.addView(textView02, layoutParams);
        linearLayout.addView(textView03, layoutParams);
        linearLayout.addView(textView04, layoutParams);
        FitTextView textView05 = new FitTextView(this);
        FitTextView textView06 = new FitTextView(this);
        FitTextView textView07 = new FitTextView(this);
        FitTextView textView08 = new FitTextView(this);
        textView05.setBackgroundColor(Color.GRAY);
        textView07.setBackgroundColor(Color.BLUE);
        textView06.setBackgroundColor(Color.RED);
        textView08.setBackgroundColor(Color.GRAY);
        linearLayout.addView(textView05, layoutParams);
        linearLayout.addView(textView07, layoutParams);
        linearLayout.addView(textView06, layoutParams);
        linearLayout.addView(textView08, layoutParams);
        String str2 = "Cannot be Normal Summoned/Set. Must first be Special Summoned (from your hand) by banishing 1 LIGHT and 1 DARK monster from your Graveyard. Once per turn, you can activate 1 of these effects.\n" +
                "● Target 1 monster on the field; banish that target face-up. This card cannot attack the turn you activate this effect.\n" +
                "● If this attacking card destroys an opponent's monster by battle, after damage calculation: It can make a second attack in a row.";
        String str1 = "这张卡不能通常召唤。把自己墓地的光属性和暗属性怪兽各1只从游戏中除外的场合可以特殊召唤。1回合1次，可以从以下效果选择1个发动。\n●选择场上1只怪兽从游戏中除外。这个效果发动的回合，这张卡不能攻击。\n●这张卡的攻击破坏对方怪兽的场合，只有1次可以继续进。行攻击。";
        textView.setText(str1);
        textView.setTextSize(14);
        textView02.setTextSize(14);
        textView02.setKeepWord(false);
        textView02.setJustify(true);
        if (textView02 instanceof FitTextView) {
            ((FitTextView) textView02).setNeedFit(false);
        }
        textView02.setText(str1);

        textView03.setText(str2);
        textView03.setTextSize(12);
        textView04.setTextSize(12);
        if (textView04 instanceof FitTextView) {
            ((FitTextView) textView04).setNeedFit(false);
        }
        textView04.setKeepWord(true);
        textView04.setJustify(true);
        textView04.setText(str2);

        ////
        textView05.setMinTextSize(24);
        textView05.setMaxTextSize(48);
        textView05.setKeepWord(false);
        textView05.setJustify(true);
        textView05.setNeedFit(true);
        textView05.setText(str1);

        textView06.setTextSize(14);
        textView06.setKeepWord(true);
        textView06.setJustify(true);
        textView06.setNeedFit(true);
        textView06.setText(str2);

        ////
        textView07.setMinTextSize(24);
        textView07.setMaxTextSize(48);
        textView07.setKeepWord(false);
        textView07.setJustify(false);
        textView07.setNeedFit(true);
        textView07.setText(str1);

        textView08.setTextSize(14);
        textView08.setKeepWord(true);
        textView08.setJustify(false);
        textView08.setNeedFit(true);
        textView08.setText(str2);
    }
}
