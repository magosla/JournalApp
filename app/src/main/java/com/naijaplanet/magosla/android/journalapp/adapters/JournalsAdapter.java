package com.naijaplanet.magosla.android.journalapp.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.naijaplanet.magosla.android.journalapp.R;
import com.naijaplanet.magosla.android.journalapp.models.Journal;
import com.naijaplanet.magosla.android.journalapp.models.JournalsItem;
import com.naijaplanet.magosla.android.journalapp.utilities.Values;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;

public class JournalsAdapter extends RecyclerView.Adapter<JournalViewHolder> {

    private final Context context;
    private final ItemListeners mItemListeners;
    private List<JournalsItem> mJournals;
/**
    private SortedList<JournalsItem> sortedJournalsItem =
            new SortedList<JournalsItem>(JournalsItem.class, new SortedListAdapterCallback<JournalsItem>(JournalsAdapter) {
        @Override
        public int compare(JournalsItem o1, JournalsItem o2) {
            return 0;
        }

        @Override
        public boolean areContentsTheSame(JournalsItem oldItem, JournalsItem newItem) {
            return false;
        }

        @Override
        public boolean areItemsTheSame(JournalsItem item1, JournalsItem item2) {
            return false;
        }
    });
*/
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(Values.DATE_FORMAT, Locale.getDefault());

    public static interface ItemListeners {
        void onItemClickListener(View v);

        boolean onItemLongClickListener(View v);
        @SuppressWarnings("unused")
        void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo);
    }

    public JournalsAdapter(Context context, ItemListeners clickListener) {
        this.context = context;
        this.mItemListeners = clickListener;
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the layout view
        // The viewType is begin used as the resource ID because getItemViewType is overriden to return the view layout id
        View view = LayoutInflater.from(context)
                .inflate(viewType, parent, false);


        return new JournalViewHolder(view, mItemListeners);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {

        holder.bind(mJournals.get(position));
    }


    @Override
    public int getItemViewType(int position) {
        // just return the layout id
        if (position % 2 == 0) {
            return R.layout.journal_item_even;
        } else {
            return R.layout.journal_item;
        }
    }

    @Override
    public int getItemCount() {
        if (mJournals == null)
            return 0;
        else
            return mJournals.size();
    }

    public void setJournals(List<JournalsItem> mJournals) {
        this.mJournals = mJournals;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        // this could be made to return the ItemId as key
        return super.getItemId(position);
    }

}
