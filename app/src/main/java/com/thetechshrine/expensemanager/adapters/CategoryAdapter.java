package com.thetechshrine.expensemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thetechshrine.expensemanager.R;
import com.thetechshrine.expensemanager.models.Category;
import com.thetechshrine.expensemanager.utils.Event;
import com.thetechshrine.expensemanager.utils.EventBus;
import com.thetechshrine.expensemanager.utils.ResourcesUtils;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView name;
        LinearLayout actionsContainer;
        Button actionDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.category_item_icon);
            name = itemView.findViewById(R.id.category_item_name);
            actionsContainer = itemView.findViewById(R.id.category_item_actions_container);
            actionDelete = itemView.findViewById(R.id.category_item_action_delete);
        }
    }

    private Context context;
    private List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View categoryView = inflater.inflate(R.layout.category_item, parent, false);
        return new ViewHolder(categoryView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        Category category = categories.get(position);

        holder.icon.setImageResource(ResourcesUtils.from(context).getDrawableResourceId(category.getIconName()));
        holder.name.setText(category.getName());

        holder.actionsContainer.setVisibility(category.isEditable() ? View.VISIBLE : View.GONE);
        holder.actionDelete.setOnClickListener(v -> EventBus.publish(EventBus.SUBJECT_CATEGORIES_FRAGMENT, new Event(Event.DELETE_CATEGORY, category.getId())));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void updateCategories(List<Category> list) {
        categories.clear();
        categories.addAll(list);
        notifyDataSetChanged();
    }

}
