package com.thetechshrine.expensemanager.utils;

import android.content.Context;
import android.graphics.Typeface;

public class TypefaceUtils {
    private Context context;

    public TypefaceUtils(Context context) {
        this.context = context;
    }

    public static TypefaceUtils from(Context context) {
        return new TypefaceUtils(context);
    }

    private Typeface getTypefaceFromPath(String path) {
        return Typeface.createFromAsset(context.getAssets(), path);
    }

    private String getPath(String fontWeight) {
        return "fonts/poppins/Poppins-" + fontWeight + ".ttf";
    }

    public Typeface getMediumTypeface() {
        return getTypefaceFromPath(getPath(FontWeight.MEDIUM));
    }

    public Typeface getBoldTypeface() {
        return getTypefaceFromPath(getPath(FontWeight.BOLD));
    }

    public Typeface getSemiBoldTypeface() {
        return getTypefaceFromPath(getPath(FontWeight.SEMI_BOLD));
    }

    public Typeface getRegularTypeface() {
        return getTypefaceFromPath(getPath(FontWeight.REGULAR));
    }

    public Typeface getTypeface(String fontWeight) {
        return Typeface.createFromAsset(context.getAssets(), getPath(fontWeight));
    }

    private class FontWeight {
        static final String MEDIUM = "Medium";
        static final String BOLD = "Bold";
        static final String SEMI_BOLD = "SemiBold";
        static final String REGULAR = "Regular";
    }
}