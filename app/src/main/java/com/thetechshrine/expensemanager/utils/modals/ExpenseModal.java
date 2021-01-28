package com.thetechshrine.expensemanager.utils.modals;

import android.content.Context;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.thetechshrine.expensemanager.R;
import com.thetechshrine.expensemanager.utils.DateUtils;
import com.thetechshrine.expensemanager.utils.EntitiesUtils;

import java.util.Date;

public class ExpenseModal extends Modal {

    private ImageButton actionClose;
    private TextView date;
    private TextView amount;

    public ExpenseModal(Context context) {
        super(context);

        bindViews();
        setupViews();
    }

    private void bindViews() {
        dialog.setContentView(R.layout.modal_expense);

        actionClose = dialog.findViewById(R.id.modal_action_close);
        date = dialog.findViewById(R.id.modal_expense_date);
        amount = dialog.findViewById(R.id.modal_expense_amount);
    }

    private void setupViews() {
        setupActions();
    }

    private void setupActions() {
        actionClose.setOnClickListener(v -> hide());
    }

    public void setValues(Date dateValue, double amountValue) {
        date.setText(DateUtils.formatDayMonthYear(dateValue));
        amount.setText(EntitiesUtils.from(context).formatAmount(amountValue, true, false));
    }
}
