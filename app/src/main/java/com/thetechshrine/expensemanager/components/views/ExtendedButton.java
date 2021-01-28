package com.thetechshrine.expensemanager.components.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.thetechshrine.expensemanager.R;
import com.thetechshrine.expensemanager.utils.TypefaceUtils;

public class ExtendedButton extends AppCompatButton {
    public ExtendedButton(@NonNull Context context) {
        super(context);
    }

    public ExtendedButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ExtendedButton, 0, 0);
        try {
            String fontWeight = typedArray.getString(R.styleable.ExtendedButton_font_weight);
            boolean transform = typedArray.getBoolean(R.styleable.ExtendedButton_transform, false);
            if (fontWeight != null) super.setTypeface(TypefaceUtils.from(context).getTypeface(fontWeight));
            if (!transform) this.setTransformationMethod(null);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    public void setTypeface(@Nullable Typeface tf) {}
}
