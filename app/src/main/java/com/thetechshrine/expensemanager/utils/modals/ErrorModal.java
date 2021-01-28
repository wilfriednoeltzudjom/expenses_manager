package com.thetechshrine.expensemanager.utils.modals;

import android.content.Context;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.thetechshrine.expensemanager.R;

public class ErrorModal extends Modal {

    private ImageButton actionClose;
    private TextView message;
    private Button actionOk;

    public ErrorModal(Context context) {
        super(context);

        bindViews();
        setupViews();
    }

    private void bindViews() {
        dialog.setContentView(R.layout.modal_error);

        actionClose = dialog.findViewById(R.id.modal_action_close);
        message = dialog.findViewById(R.id.modal_message);
        actionOk = dialog.findViewById(R.id.modal_action_ok);
    }

    private void setupViews() {
        setupActions();
    }

    private void setupActions() {
        actionClose.setOnClickListener(v -> hide());
        actionOk.setOnClickListener(v -> hide());
    }

    public void setMessage(String messageValue) {
        message.setText(messageValue);
    }
}
