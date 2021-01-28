package com.thetechshrine.expensemanager.components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.thetechshrine.expensemanager.R;
import com.thetechshrine.expensemanager.utils.TypefaceUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarView extends LinearLayout {

    private Context context;

    private TextView date;
    private Button prev, next;
    private GridView gridView;

    private ItemDayAdapter adapter;
    private Calendar calendar = Calendar.getInstance();
    private List<Date> dates;
    private OnDateSelectedListener listener;

    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM, yyyy", Locale.getDefault());

    public CalendarView(Context context) {
        super(context);
        this.context = context;
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        bindViews();
        styleViews();
        setupViews();
    }

    private void bindViews() {
        View rootView = LayoutInflater.from(context).inflate(R.layout.calendar, this, true);
        gridView = rootView.findViewById(R.id.view_calendar_grid_view);
        date = rootView.findViewById(R.id.view_calendar_date);

        prev = rootView.findViewById(R.id.view_calendar_prev);
        next = rootView.findViewById(R.id.view_calendar_next);
    }

    private void styleViews() {
        Typeface bold = TypefaceUtils.from(context).getSemiBoldTypeface();

        date.setTypeface(TypefaceUtils.from(context).getMediumTypeface());
        next.setTransformationMethod(null);
        next.setTypeface(bold);
        prev.setTransformationMethod(null);
        prev.setTypeface(bold);
    }

    private void setupViews() {
        updateAdapter();
        initNext();
        initPrev();
        initGridView();
    }

    private void initPrev() {
        prev.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, -1);
            updateAdapter();
        });
    }

    private void initNext() {
        next.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, 1);
            updateAdapter();
        });
    }

    private void updateAdapter() {
        dates = new ArrayList<>();
        Calendar calendar = (Calendar)this.calendar.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        calendar.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        int i = 0;
        while (i < 42) {
            dates.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            i++;
        }
        String dateText = formatter.format(this.calendar.getTime());
        date.setText(dateText);
        adapter = new ItemDayAdapter(context.getApplicationContext(), dates, this.calendar);
        gridView.setAdapter(adapter);
    }

    private void initGridView() {
        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            if (dates != null && dates.size() > 0) {
                Date date = dates.get(i);
                if (listener != null) listener.onDateSelected(date);
            }
        });
    }

    private class ItemDayAdapter extends BaseAdapter {

        private Context context;
        private List<Date> days;
        private LayoutInflater inflater;
        private Calendar currentCalendar;

        public ItemDayAdapter(Context context, List<Date> days, Calendar currentCalendar) {

            this.context = context;
            this.days = days;
            this.inflater = LayoutInflater.from(context);
            this.currentCalendar = currentCalendar;
        }

        @Override
        public int getCount() {
            return days.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.calendar_item_day, null);
            ViewHolder holder = new ViewHolder(view);

            Date date = days.get(i);
            Calendar cal = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int currentMonth = currentCalendar.get(Calendar.MONTH) + 1;
            int displayMonth = calendar.get(Calendar.MONTH) + 1;
            int currentYear = currentCalendar.get(Calendar.YEAR);
            int displayYear = calendar.get(Calendar.YEAR);
            int displayDay = calendar.get(Calendar.DAY_OF_MONTH);
            if (currentMonth != displayMonth || currentYear != displayYear) {
                view.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                holder.indicator.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
                holder.value.setTextColor(context.getResources().getColor(R.color.colorGray));
            }
            if (cal.get(Calendar.MONTH) + 1 == displayMonth && cal.get(Calendar.YEAR) == displayYear && cal.get(Calendar.DAY_OF_MONTH) == displayDay) {
                view.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
                holder.value.setTextColor(context.getResources().getColor(R.color.colorWhite));
//                holder.value.setTypeface(TypefaceFactory.from(context, TypefaceFactory.BOLD));
            }
            holder.value.setTypeface(TypefaceUtils.from(context).getRegularTypeface());
            holder.value.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));

            return view;
        }

        public class ViewHolder {

            public View indicator;
            public TextView value;

            public ViewHolder(View rootView) {
                indicator = rootView.findViewById(R.id.item_day_indicator);
                value = rootView.findViewById(R.id.item_day_value);
            }
        }
    }

    public interface OnDateSelectedListener {
        void onDateSelected(Date date);
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        this.listener = listener;
    }
}
