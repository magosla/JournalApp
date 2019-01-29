package com.naijaplanet.magosla.android.journalapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.naijaplanet.magosla.android.journalapp.R;
import com.naijaplanet.magosla.android.journalapp.models.JournalsItem;
import com.naijaplanet.magosla.android.journalapp.utilities.Values;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class JournalViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener, View.OnCreateContextMenuListener {

    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(Values.DATE_FORMAT, Locale.getDefault());
    private final TextView mTitleView;
    private final TextView mTimestampView;
    private final TextView mJournalTypeView;
    private final ImageView mTypeImageView;
    private final JournalsAdapter.ItemListeners mItemListeners;

    protected JournalViewHolder(View itemView, JournalsAdapter.ItemListeners listeners) {
        super(itemView);

        mItemListeners = listeners;

        mTitleView = itemView.findViewById(R.id.text_title);
        mTimestampView = itemView.findViewById(R.id.text_datetime);
        mTypeImageView = itemView.findViewById(R.id.image_journal_type);
        mJournalTypeView = itemView.findViewById(R.id.text_journal_type);
       // itemView.setLongClickable(true);
        itemView.setOnClickListener(this);
        //itemView.setOnLongClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
        // context.registerForContextMenu(itemView);
    }


    protected void bind(JournalsItem journalsItem) {
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


        menu.setHeaderTitle(mTitleView.getText().toString());
        menu.add(Menu.NONE, R.id.ctx_menu_edit, Menu.NONE, R.string.action_edit);

       // menuInfo.getMenuInflater().inflate(R.menu.activity_journals_item_context, menu);

        menu.add(Menu.NONE, R.id.ctx_menu_delete, Menu.NONE, R.string.action_delete);
    }

}