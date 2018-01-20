package com.example.zaned.cryptoconverter;

import android.graphics.Typeface;
import android.content.Context;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatTextView;


public class MegrimText extends AppCompatTextView {
    public MegrimText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(
                context.getAssets(),
                "fonts/megrim.ttf"
        ));
    }
}
