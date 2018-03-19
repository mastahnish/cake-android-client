package com.waracle.androidtest.workflow.dishes;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.waracle.androidtest.R;
import com.waracle.androidtest.http.HttpUtils;
import com.waracle.androidtest.http.LoadItemsTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Fragment is responsible for loading in some JSON and
 * then displaying a list of cakes with images.
 * Fix any crashes
 * Improve any performance issues
 * Use good coding practices to make code more secure
 */
public class PlaceholderFragment extends ListFragment {

    private static final String TAG = PlaceholderFragment.class.getSimpleName();

    private ListView mListView;
    private MyAdapter mAdapter;

    public PlaceholderFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (ListView) rootView.findViewById(android.R.id.list);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Create and set the list adapter.
        mAdapter = new MyAdapter(getActivity());
        mListView.setAdapter(mAdapter);

        // Load data from net.
        try {
            loadData();
        } catch (IOException | JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }


    private void loadData() throws IOException, JSONException {
        URL url = new URL(HttpUtils.JSON_URL);
        new LoadItemsTask(new LoadItemsTask.AsyncResponseListener() {
            @Override
            public void processFinish(JSONArray output) {
                mAdapter.setItems(output);
            }
        }).execute(url);
    }
}

