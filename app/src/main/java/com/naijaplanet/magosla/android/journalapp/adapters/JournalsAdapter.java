package com.naijaplanet.magosla.android.journalapp.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.naijaplanet.magosla.android.journalapp.R;
import com.naijaplanet.magosla.android.journalapp.models.JournalsItem;
import com.naijaplanet.magosla.android.journalapp.utilities.Values;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class JournalsAdapter extends RecyclerView.Adapter<JournalsAdapter.JournalViewHolder> {

    private final Context context;
    private final ItemListeners mItemListeners;
    private List<JournalsItem> mJournals;

    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(Values.DATE_FORMAT, Locale.getDefault());

    public interface ItemListeners {
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


        return new JournalViewHolder(view);
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

    class JournalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnCreateContextMenuListener
    {
        private final TextView mTitleView;
        private final TextView mTimestampView;
        private final TextView mJournalTypeView;
        private final ImageView mTypeImageView;

        private JournalViewHolder(View itemView) {
            super(itemView);
            mTitleView = itemView.findViewById(R.id.text_title);
            mTimestampView = itemView.findViewById(R.id.text_datetime);
            mTypeImageView = itemView.findViewById(R.id.image_journal_type);
            mJournalTypeView = itemView.findViewById(R.id.text_journal_type);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
           // context.registerForContextMenu(itemView);
        }

        private void bind(JournalsItem journalsItem) {
            // Store the key to identify item
            itemView.setTag(journalsItem.getKey());
            itemView.setTag(R.id.journal_item_layout, 24);

            mTitleView.setText(journalsItem.getTitle());

            mJournalTypeView.setText(journalsItem.getType());

            // display creation time only if the journal is not edited
            Long timestamp = journalsItem.getEditTimestamp() > 0
                    ? journalsItem.getEditTimestamp() : journalsItem.getTimestamp();

            mTimestampView.setText(
                    dateFormatter.format(timestamp)
            );
            mTypeImageView.setContentDescription(
                    String.format("%s is a %s", journalsItem.getTitle(), journalsItem.getType().toLowerCase())
            );

        }

        @Override
        public void onClick(View v) {
            mItemListeners.onItemClickListener(v);
        }

        @Override
        public boolean onLongClick(View v) {
            return mItemListeners.onItemLongClickListener(v);
        }

        // TODO: - Implement a Context menu instead of directly editing after a long click
        @SuppressWarnings("unused")
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //mItemListeners.onCreateContextMenu(menu, v, menuInfo);
        }

    }
}
