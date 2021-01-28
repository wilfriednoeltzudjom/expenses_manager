package com.thetechshrine.expensemanager.utils.modals;

import android.content.Context;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.thetechshrine.expensemanager.R;

public class ConfirmationDeleteModal extends Modal {

    private ImageButton actionClose;
    private TextView message;
    private Button actionNo;
    private Button actionYes;

    public ConfirmationDeleteModal(Context context, OnModalChangeListener listener) {
        super(context, listener);

        bindViews();
        setupViews();
    }

    private void bindViews() {
        dialog.setContentView(R.layout.modal_confirmation_delete);

        actionClose = dialog.findViewById(R.id.modal_action_close);
        message = dialog.findViewById(R.id.modal_message);
        actionNo = dialog.findViewById(R.id.modal_action_no);
        actionYes = dialog.findViewById(R.id.modal_action_yes);
    }

    private void setupViews() { setupActions(); }

    private void setupActions() {
        actionClose.setOnClickListener(v -> hide());

        actionNo.setOnClickListener(v -> hide());

        actionYes.setOnClickListener(v -> {
            hide();
            if (listener != null) ((OnConfirmationDeleteModalChangeListener) listener).onDelete();
        });
    }

    public void setMessage(String messageValue) {
        message.setText(messageValue);
    }

    public interface OnConfirmationDeleteModalChangeListener extends OnModalChangeListener {
        void onDelete();
    }
}
