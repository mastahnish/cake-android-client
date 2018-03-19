package com.waracle.androidtest.workflow.dishes;

import android.util.Log;

import com.waracle.androidtest.data.Dish;
import com.waracle.androidtest.http.JSONFields;
import com.waracle.androidtest.http.HttpUtils;
import com.waracle.androidtest.http.LoadItemsTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacek on 2018-03-19.
 */

public class PlaceholderFragmentPresenterImpl implements IPlaceholderFragment.Presenter {

    private static final String TAG = PlaceholderFragmentPresenterImpl.class.getSimpleName();

    private IPlaceholderFragment.View mView;

    public PlaceholderFragmentPresenterImpl(IPlaceholderFragment.View view) {
        this.mView = view;
    }

    @Override
    public void loadItems() {
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
                mView.updateView(parseJsonArrayToList(output));
            }
        }).execute(url);
    }

    private List<Dish> parseJsonArrayToList(JSONArray output){
        JSONObject tempJson = null;
        List<Dish> dishes = new ArrayList<>();
        for (int i = 0; i < output.length(); i++) {
            try {
               tempJson = (JSONObject) output.get(i);
               dishes.add(Dish.makeDish(tempJson.getString(JSONFields.TITLE), tempJson.getString(JSONFields.DESCRIPTION), tempJson.getString(JSONFields.URL)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return dishes;
    }
}
