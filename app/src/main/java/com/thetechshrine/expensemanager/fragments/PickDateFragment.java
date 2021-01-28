package com.thetechshrine.expensemanager.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;

import com.thetechshrine.expensemanager.R;
import com.thetechshrine.expensemanager.components.CalendarView;
import com.thetechshrine.expensemanager.utils.Event;
import com.thetechshrine.expensemanager.utils.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class PickDateFragment extends DialogFragment {

    public static final String TAG_SELECTED_DATE = "TAG_SELECTED_DATE";

    private Context context;

    private ImageButton close;
    private CalendarView calendarView;

    public PickDateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pick_date, container, false);

        bindViews(rootView);
        setupViews();

        return rootView;
    }

    private void bindViews(View rootView) {
        calendarView = rootView.findViewById(R.id.pick_date_calendar_view);
        close = rootView.findViewById(R.id.pick_date_close);
    }

    private void setupViews() {
        calendarView.setOnDateSelectedListener(date -> {
            Intent intent = new Intent();
            intent.putExtra(TAG_SELECTED_DATE, date);
            if (getTargetFragment() != null) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), AppCompatActivity.RESULT_OK, intent);
            }
            EventBus.publish(EventBus.SUBJECT_SELECT_DATE, new Event(Event.DATE_SELECTED, date));
            dismiss();
        });
        close.setOnClickListener(v -> dismiss());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}

