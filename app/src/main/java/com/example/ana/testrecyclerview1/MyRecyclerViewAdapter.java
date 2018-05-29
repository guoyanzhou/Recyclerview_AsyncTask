package com.example.ana.testrecyclerview1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    Context context;
    List<FeedItem> feedItemList;
    OnItemClickListner onItemClickListner;

    public interface OnItemClickListner {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListner(OnItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    public MyRecyclerViewAdapter(Context context, List<FeedItem> feedItemList) {
        this.context = context;
        this.feedItemList = feedItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void removeData(int pos) {
        feedItemList.remove(pos);
        notifyItemRemoved(pos);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        FeedItem feedItem = feedItemList.get(i);

//        if (!TextUtils.isEmpty(feedItem.getThumbnail())) {
//            Picasso.with(context).load(feedItem.getThumbnail())
//                    .error(R.drawable.ic_launcher_background)
//                    .placeholder(R.drawable.ic_launcher_background)
//                    .into(viewHolder.imageView);
//        }
        viewHolder.textView.setText(Html.fromHtml(feedItem.getTitle()));
        if (onItemClickListner != null) {
            viewHolder.textView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    onItemClickListner.onItemClick(viewHolder.textView, pos);
                }
            });
            viewHolder.textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int pos = viewHolder.getLayoutPosition();
                    onItemClickListner.onItemLongClick(viewHolder.textView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView textView;

        public ViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.thumbnail);
            this.textView = (TextView) view.findViewById(R.id.title);

        }
    }
}
