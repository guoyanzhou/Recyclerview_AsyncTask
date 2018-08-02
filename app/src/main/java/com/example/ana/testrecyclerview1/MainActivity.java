package com.example.ana.testrecyclerview1;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "Sample";
    private List<FeedItem> feedItemList;
    private List<JsonItem> jsonItems;
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private ProgressBar progressBar;
    String jsonStr = "{\"status\":0,\"result\":[{\"x\":114.2307489832,\"y\":29.579081808346}]}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        String url = "http://stacktips.com/?json=get_category_posts&slug=news&count=30";
        new DownloadTask().execute(url);
//        updateUI();
    }

    public class DownloadTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer res = 0;
            HttpURLConnection urlConnection;

            try{
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int code = urlConnection.getResponseCode();
                if (code == 200) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder resp = new StringBuilder();
                    String line;
                    while((line = bufferedReader.readLine()) != null) {
                        resp.append(line);
                    }
                    parseResult(resp.toString());
                    res = 1;
                } else {
                    res = 0;
                }
            } catch (Exception e) {

            }
            return res;
        }

        @Override
        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);

            if (result == 1) {
                myRecyclerViewAdapter = new MyRecyclerViewAdapter(MainActivity.this, jsonItems);
                recyclerView.setAdapter(myRecyclerViewAdapter);
                updateUI();
            } else {
                Toast.makeText(MainActivity.this, "failed to get data", Toast.LENGTH_SHORT).show();
            }
        }

        void parseResult(String s) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray jsonArray = jsonObject.optJSONArray("posts");
            feedItemList = new ArrayList<>();

            for( int i=0; i<jsonArray.length(); i++) {
                JSONObject post = jsonArray.optJSONObject(i);
                FeedItem feedItem = new FeedItem();
                feedItem.setTitle(post.optString("title"));
                feedItem.setThumbnail(post.optString("thumbnail"));
                feedItemList.add(feedItem);
            }
        }
    }

    private void updateUI() {
//        updateFeedItem();
        getCordinate();
//        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this, feedItemList);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this, jsonItems);
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

    private void getCordinate() {
        jsonItems = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.optJSONArray("result");
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject results = jsonArray.getJSONObject(i);
                JsonItem jsonItem = new JsonItem();
                if (results.has("x")) {
                    jsonItem.x = results.getDouble("x");
                }
                if (results.has("y")) {
                    jsonItem.y = results.getDouble("y");
                }
                jsonItems.add(jsonItem);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateFeedItem() {
        feedItemList = new ArrayList<>();
        for(int i=0; i<100; i++) {
            FeedItem feedItem = new FeedItem();
            feedItem.setThumbnail("t1" + i);
            feedItem.setTitle("title" + i);
            feedItemList.add(feedItem);
        }
    }
}
