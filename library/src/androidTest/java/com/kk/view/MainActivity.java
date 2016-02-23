package com.kk.view;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(linearLayout);

        TextView textView = new TextView(this);
        linearLayout.addView(textView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        FitTextView textView1 = new FitTextView(this);
        FrameLayout frameLayout = new FrameLayout(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(680, 400);
        lp.leftMargin = 10;
        frameLayout.addView(textView1, lp);
        linearLayout.addView(frameLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        textView.setText("Cannot be Normal Summoned/Set. Must first be Special Summoned (from your hand) by banishing 1 LIGHT and 1 DARK monster from your Graveyard. Once per turn, you can activate 1 of these effects.\n" +
                "● Target 1 monster on the field; banish that target face-up. This card cannot attack the turn you activate this effect.\n" +
                "● If this attacking card destroys an opponent's monster by battle, after damage calculation: It can make a second attack in a row.");
        textView1.setBackgroundColor(Color.GRAY);
        textView1.setMaxTextSize(40);
        textView1.setMinTextSize(10);
        textView1.setJustify(true);
        textView1.setText("Cannot be Normal Summoned/Set. Must first be Special Summoned (from your hand) by banishing 1 LIGHT and 1 DARK monster from your Graveyard. Once per turn, you can activate 1 of these effects.\n" +
                "● Target 1 monster on the field; banish that target face-up. This card cannot attack the turn you activate this effect.\n" +
                "● If this attacking card destroys an opponent's monster by battle, after damage calculation: It can make a second attack in a row.");
    }
}
