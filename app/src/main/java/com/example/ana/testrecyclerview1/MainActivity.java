package com.example.ana.testrecyclerview1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "Sample";
    private List<FeedItem> feedItemList;
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        updateUI();
    }

    private void updateUI() {
        feedItemList = new ArrayList<>();
        for(int i=0; i<100; i++) {
            FeedItem feedItem = new FeedItem();
            feedItem.setThumbnail("t1" + i);
            feedItem.setTitle("title" + i);
            feedItemList.add(feedItem);
        }
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this, feedItemList);
        recyclerView.setAdapter(myRecyclerViewAdapter);
    }
}
