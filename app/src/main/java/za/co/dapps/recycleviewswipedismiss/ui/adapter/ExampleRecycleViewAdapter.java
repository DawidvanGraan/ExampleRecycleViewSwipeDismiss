package za.co.dapps.recycleviewswipedismiss.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.dapps.recycleviewswipedismiss.R;
import za.co.dapps.recycleviewswipedismiss.ui.domain.ExampleItem;

public final class ExampleRecycleViewAdapter
        extends RecyclerView.Adapter<ExampleRecycleViewAdapter.NotificationViewHolder> {

    private Context context;
    private final List<ExampleItem> items = new ArrayList<>();


    public ExampleRecycleViewAdapter(@NonNull final Context context) {
        this.context = context;
    }

    public ExampleItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_example_list_item, parent, false);

        return new NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        final ExampleItem item = getItem(position);
        holder.tvTitle.setText(item.title);
        holder.tvUndo.setVisibility(item.showUndo ? View.VISIBLE : View.GONE);
    }

    public void remove(final int position) {
        this.items.remove(position);
    }

    public int remove(final ExampleItem item) {
        final int pos = this.items.indexOf(item);
        this.items.remove(pos);
        return pos;
    }

    public void apply(@NonNull final List<ExampleItem> list) {
        this.items.clear();
        this.items.addAll(list);
    }

    /**
     * Holds ConvertView views
     */
    public static class NotificationViewHolder extends RecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder {

        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvUndo)
        TextView tvUndo;

        public NotificationViewHolder(@NonNull final View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {
            //itemView.setBackgroundColor(0);
        }
    }
}
