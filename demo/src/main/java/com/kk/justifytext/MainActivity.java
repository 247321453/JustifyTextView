package com.kk.justifytext;

import android.app.Activity;
import android.graphics.Color;
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
        final JustifyTextView textView1 = new FitTextView(this);
        textView1.setBackgroundColor(Color.GRAY);
        TextView textView03 = new TextView(this);
        textView03.setBackgroundColor(Color.BLUE);
        JustifyTextView textView04 = new FitTextView(this);
        linearLayout.addView(textView, layoutParams);
        linearLayout.addView(textView1, layoutParams);
        linearLayout.addView(textView03, layoutParams);
        linearLayout.addView(textView04, layoutParams);
        String str2 = "Cannot be Normal Summoned/Set. Must first be Special Summoned (from your hand) by banishing 1 LIGHT and 1 DARK monster from your Graveyard. Once per turn, you can activate 1 of these effects.\n" +
                "● Target 1 monster on the field; banish that target face-up. This card cannot attack the turn you activate this effect.\n" +
                "● If this attacking card destroys an opponent's monster by battle, after damage calculation: It can make a second attack in a row.";
        String str1 = "这张卡不能通常召唤。把自己墓地的光属性和暗属性怪兽各1只从游戏中除外的场合可以特殊召唤。1回合1次，可以从以下效果选择1个发动。\n●选择场上1只怪兽从游戏中除外。这个效果发动的回合，这张卡不能攻击。\n●这张卡的攻击破坏对方怪兽的场合，只有1次可以继续进。行攻击。";
        textView.setText(str1);
        textView.setTextSize(14);
        textView1.setTextSize(14);
        textView1.setKeepWord(false);
        textView1.setJustify(true);
        if (textView1 instanceof FitTextView) {
            ((FitTextView) textView1).setFittingText(false);
        }
        textView1.setText(str1);

        textView03.setText(str2);
        textView03.setTextSize(12);
        textView04.setTextSize(12);
        if (textView04 instanceof FitTextView) {
            ((FitTextView) textView04).setFittingText(false);
        }
        textView04.setKeepWord(true);
        textView04.setJustify(true);
        textView04.setText(str2);
    }
}
