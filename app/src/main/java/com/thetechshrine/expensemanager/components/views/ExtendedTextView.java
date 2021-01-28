package com.thetechshrine.expensemanager.components.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.thetechshrine.expensemanager.R;
import com.thetechshrine.expensemanager.utils.TypefaceUtils;

public class ExtendedTextView extends AppCompatTextView {
    public ExtendedTextView(Context context) {
        super(context);
    }

    public ExtendedTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ExtendedTextView, 0, 0);
        try {
            String fontWeight = typedArray.getString(R.styleable.ExtendedTextView_font_weight);
            if (fontWeight != null) super.setTypeface(TypefaceUtils.from(context).getTypeface(fontWeight));
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    public void setTypeface(@Nullable Typeface tf) { }
}
