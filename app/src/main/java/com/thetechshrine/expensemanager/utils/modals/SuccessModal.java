package com.thetechshrine.expensemanager.utils.modals;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.thetechshrine.expensemanager.R;

public class SuccessModal extends Modal {

    private ImageButton actionClose;
    private TextView message;
    private Button actionOk;

    public SuccessModal(Context context) {
        super(context);

        bindViews();
        setupViews();
    }

    public SuccessModal(Context context, OnModalChangeListener listener) {
        super(context, listener);

        bindViews();
        setupViews();
    }

    private void bindViews() {
        dialog.setContentView(R.layout.modal_success);

        actionClose = dialog.findViewById(R.id.modal_action_close);
        message = dialog.findViewById(R.id.modal_message);
        actionOk = dialog.findViewById(R.id.modal_action_ok);
    }

    private void setupViews() {
        setupActions();
    }

    private void setupActions() {
        actionClose.setOnClickListener(v -> hide());
        actionOk.setOnClickListener(v -> {
            hide();
            if (listener != null) ((OnSuccessModalChangeListener) listener).onOk();
        });
    }

    public void setMessage(String messageValue) {
        message.setText(messageValue);
    }

    public interface OnSuccessModalChangeListener extends OnModalChangeListener {
        void onOk();
    }
}
