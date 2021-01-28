package com.thetechshrine.expensemanager.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thetechshrine.expensemanager.R;
import com.thetechshrine.expensemanager.utils.TypefaceUtils;

import java.util.ArrayList;
import java.util.List;

public class BottomMenu extends LinearLayout {
    private Context globalContext;

    private RecyclerView recyclerView;

    private BottomMenuAdapter adapter;
    private List<BottomMenuItem> bottomMenuItems;

    public BottomMenu(Context context) {
        super(context);
    }

    public BottomMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        globalContext = context;


        bindViews();
        setupViews();
    }

    private void bindViews() {
        LayoutInflater.from(globalContext).inflate(R.layout.bottom_menu, this, true);

        recyclerView = findViewById(R.id.bottom_menu_recycler_view);
    }

    private void setupViews() {
        bottomMenuItems = new ArrayList<>();

        String[] titles = getResources().getStringArray(R.array.menu_titles);
        int[] enabledIcons = new int[]{R.drawable.ic_menu_dashboard_enabled, R.drawable.ic_menu_categories_enabled,
                R.drawable.ic_menu_stats_enabled, R.drawable.ic_menu_profile_enabled};
        int[] disabledIcons = new int[]{R.drawable.ic_menu_dashboard_disabled, R.drawable.ic_menu_categories_disabled,
                R.drawable.ic_menu_stats_disabled, R.drawable.ic_menu_profile_disabled};

        for (int i = 0; i < titles.length; i++) {
            bottomMenuItems.add(new BottomMenuItem(titles[i], enabledIcons[i], disabledIcons[i], true));
        }

        adapter = new BottomMenuAdapter(globalContext, bottomMenuItems);
        GridLayoutManager layoutManager = new GridLayoutManager(globalContext, 10);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 1 || position == 2 ? 3 : 2;
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setCurrentMenu(0);
    }

    private class BottomMenuItem {
        private String title;
        private int enabledIcon;
        private int disabledIcon;
        private boolean selected;
        private boolean valid;

        public BottomMenuItem(String title, int enabledIcon, int disabledIcon, boolean valid) {
            this.valid = valid;
            if (valid) {
                this.title = title;
                this.enabledIcon = enabledIcon;
                this.disabledIcon = disabledIcon;
            }
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getEnabledIcon() {
            return enabledIcon;
        }

        public void setEnabledIcon(int enabledIcon) {
            this.enabledIcon = enabledIcon;
        }

        public int getDisabledIcon() {
            return disabledIcon;
        }

        public void setDisabledIcon(int disabledIcon) {
            this.disabledIcon = disabledIcon;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }
    }

    private class BottomMenuAdapter extends RecyclerView.Adapter<BottomMenuAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {
            FrameLayout container;
            ImageView icon;
            TextView title;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                container = itemView.findViewById(R.id.bottom_menu_item_container);
                icon = itemView.findViewById(R.id.bottom_menu_item_icon);
                title = itemView.findViewById(R.id.bottom_menu_item_title);
            }
        }

        private Context context;
        private List<BottomMenuItem> bottomMenuItems;
        private OnBottomMenuChangeListener listener;

        public BottomMenuAdapter(Context context, List<BottomMenuItem> bottomMenuItems) {
            this.context = context;
            this.bottomMenuItems = bottomMenuItems;
        }

        @NonNull
        @Override
        public BottomMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View bottomMenuItemView = inflater.inflate(R.layout.bottom_menu_item, parent, false);
            return new ViewHolder(bottomMenuItemView);
        }

        @Override
        public void onBindViewHolder(@NonNull BottomMenuAdapter.ViewHolder holder, int position) {
            BottomMenuItem bottomMenuItem = bottomMenuItems.get(position);

            if (bottomMenuItem.isValid()) {
                if (bottomMenuItem.isSelected()) {
                    holder.icon.setImageResource(bottomMenuItem.getEnabledIcon());
                    holder.title.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    holder.icon.setImageResource(bottomMenuItem.getDisabledIcon());
                    holder.title.setTextColor(getResources().getColor(R.color.colorDarkGray));
                }
            }

            holder.title.setTypeface(TypefaceUtils.from(context).getMediumTypeface());
            holder.title.setText(bottomMenuItem.getTitle());
            holder.container.setOnClickListener(v -> {
                if (listener != null) listener.onBottomMenuChange(position);
            });
        }

        @Override
        public int getItemCount() {
            return bottomMenuItems.size();
        }

        public void setCurrentMenu(int position) {
            List<BottomMenuItem> snapshot = bottomMenuItems.subList(0, bottomMenuItems.size());
            for (int i=0; i<snapshot.size(); i++) {
                BottomMenuItem item = snapshot.get(i);
                if (i == position) item.setSelected(true);
                else item.setSelected(false);
            }

            bottomMenuItems = snapshot;
            notifyDataSetChanged();
        }

        public void setOnBottomMenuChangeListener(OnBottomMenuChangeListener listener) {
            this.listener = listener;
        }
    }

    public interface OnBottomMenuChangeListener {
        void onBottomMenuChange(int position);
    }

    public void setOnBottomMenuChangeListener(OnBottomMenuChangeListener listener) {
        adapter.setOnBottomMenuChangeListener(listener);
    }

    public void setCurrentMenu(int position) {
        adapter.setCurrentMenu(position);
    }
}
