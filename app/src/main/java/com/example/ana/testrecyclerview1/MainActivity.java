package com.example.ana.testrecyclerview1;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

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
        myRecyclerViewAdapter.setOnItemClickListner(new MyRecyclerViewAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "click on" + (position+1), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("sure to delete?")
                        .setNegativeButton("cancel", null)
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                myRecyclerViewAdapter.removeData(position);
                            }
                        });
            }
        });
    }
}
