package za.co.dapps.recycleviewswipedismiss.ui.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import za.co.dapps.recycleviewswipedismiss.R;
import za.co.dapps.recycleviewswipedismiss.SimpleItemTouchHelperCallback;
import za.co.dapps.recycleviewswipedismiss.ui.adapter.ExampleRecycleViewAdapter;
import za.co.dapps.recycleviewswipedismiss.ui.adapter.ItemTouchHelperAdapter;
import za.co.dapps.recycleviewswipedismiss.ui.domain.ExampleItem;

public class MainActivity extends AppCompatActivity {

    final int LIST_COUNT = 30;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private ExampleRecycleViewAdapter exampleRecycleViewAdapter;
    private CompositeSubscription subs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        subs = new CompositeSubscription();

        exampleRecycleViewAdapter = new ExampleRecycleViewAdapter(this);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(exampleRecycleViewAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        List<ExampleItem> items = new ArrayList<>(LIST_COUNT);
        for (int i = 0; i < LIST_COUNT; i++) {
            items.add(new ExampleItem(getString(R.string.item_title, i)));
        }

        exampleRecycleViewAdapter.apply(items);
        exampleRecycleViewAdapter.notifyDataSetChanged();


        final ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(new ItemTouchHelperAdapter() {
            @Override
            public boolean onItemMove(int fromPosition, int toPosition) {
                return false;
            }

            @Override
            public void onItemDismiss(final int position) {
                // Show Undo button
                final ExampleItem item = exampleRecycleViewAdapter.getItem(position);

                // Add for pending remove
                addTimerRemove(item);

                // Mark and update the item
                item.showUndo = true;
                exampleRecycleViewAdapter.notifyItemChanged(position);

            }
        });
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void addTimerRemove(@NonNull final ExampleItem item) {
        // Wait X Seconds then remove from the List
        if (!item.showUndo) {
            Subscription sub = Observable.just(item)
                    .delay(5, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<ExampleItem>() {
                        @Override

                        public void call(ExampleItem item) {
                            final int pos = exampleRecycleViewAdapter.remove(item);
                            exampleRecycleViewAdapter.notifyItemRemoved(pos);
                        }
                    });
            subs.add(sub);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        subs.clear();
    }
}
