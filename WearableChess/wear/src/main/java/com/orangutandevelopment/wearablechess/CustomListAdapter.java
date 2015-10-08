package com.orangutandevelopment.wearablechess;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mitch on 28/09/2015.
 */
public class CustomListAdapter extends WearableListView.Adapter {
    private String[] mHeaders;
    private String[] mSubheaders;
    private int[] mResources;
    private final LayoutInflater mInflater;

    public CustomListAdapter(Context context, String[] headers, String[] subheaders, int[] resources) {
        mInflater = LayoutInflater.from(context);
        mHeaders = headers;
        mSubheaders = subheaders;
        mResources = resources;
    }

    public static class ItemViewHolder extends WearableListView.ViewHolder {
        private TextView textView;
        private TextView subtextView;
        private ImageView imageView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.header);
            subtextView = (TextView) itemView.findViewById(R.id.subheader);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(mInflater.inflate(R.layout.list_item, null));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
        ItemViewHolder itemHolder = (ItemViewHolder) holder;
        itemHolder.textView.setText(mHeaders[position]);
        itemHolder.subtextView.setText(mSubheaders[position]);
        itemHolder.imageView.setImageResource(mResources[position]);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mHeaders.length;
    }
}
