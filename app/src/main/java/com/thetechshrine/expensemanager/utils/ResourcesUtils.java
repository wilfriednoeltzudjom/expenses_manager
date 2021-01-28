package com.thetechshrine.expensemanager.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

public class ResourcesUtils {

    private Context context;

    public ResourcesUtils(Context context) {
        this.context = context;
    }

    public static ResourcesUtils from(Context context) {
        return new ResourcesUtils(context);
    }

    private int getResourceId(String name, String defType) {
        Resources resources = context.getResources();
        return resources.getIdentifier(name, defType, context.getPackageName());
    }

    public int getDrawableResourceId(String name) {
        return getResourceId(name, ResourceDefType.DRAWABLE);
    }

    private class ResourceDefType {
        static final String DRAWABLE = "drawable";
    }
}
