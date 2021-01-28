package com.thetechshrine.expensemanager.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.thetechshrine.expensemanager.R;
import com.thetechshrine.expensemanager.utils.Event;
import com.thetechshrine.expensemanager.utils.EventBus;

public class CreateCategoryFragment extends BottomSheetDialogFragment {

    private EditText name;
    private Button save;

    public static CreateCategoryFragment newInstance() {
        return new CreateCategoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.create_category_fragment, container, false);
        bindViews(rootView);
        setupViews();

        return rootView;
    }

    private void bindViews(View rootView) {
        name = rootView.findViewById(R.id.create_category_name);
        save = rootView.findViewById(R.id.create_category_save);
    }

    private void setupViews() {
        setupSave();
    }

    private void setupSave() {
        save.setOnClickListener(v -> {
            String nameValue = name.getText() != null ? name.getText().toString().trim() : "";
            if (nameValue.isEmpty()) return;

            dismiss();
            EventBus.publish(EventBus.SUBJECT_CATEGORIES_FRAGMENT, new Event(Event.CREATE_CATEGORY, nameValue));
        });
    }
}
