package com.thetechshrine.expensemanager.utils.modals;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Window;

import androidx.appcompat.widget.LinearLayoutCompat;

public abstract class Modal {
    protected Context context;
    protected Dialog dialog;
    protected OnModalChangeListener listener;

    Modal(Context context) {
        initModal(context);
    }

    Modal(Context context, OnModalChangeListener listener) {
        this.listener = listener;
        initModal(context);
    }

    private void initModal(Context context) {
        this.context = context;
        this.dialog = new Dialog(context);

        setupViews();
    }

    private void setupViews() {
        this.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (this.dialog.getWindow() != null) {
            this.dialog.getWindow().setLayout(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    public void show() {
        this.dialog.show();
    }

    public void hide() {
        this.dialog.dismiss();
    }

    public interface OnModalChangeListener {}
}
