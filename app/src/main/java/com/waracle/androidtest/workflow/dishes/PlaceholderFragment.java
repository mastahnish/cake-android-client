package com.waracle.androidtest.workflow.dishes;


import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.waracle.androidtest.R;
import com.waracle.androidtest.data.Dish;
import com.waracle.androidtest.databinding.FragmentMainBinding;

import java.util.List;

/**
 * Fragment is responsible for loading in some JSON and
 * then displaying a list of cakes with images.
 * Fix any crashes
 * Improve any performance issues
 * Use good coding practices to make code more secure
 */
public class PlaceholderFragment extends Fragment implements IPlaceholderFragment.View {

    private static final String TAG = PlaceholderFragment.class.getSimpleName();

    private FragmentMainBinding binding;
    private MyAdapter mAdapter;
    private PlaceholderFragmentPresenterImpl presenter;

    public PlaceholderFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter = new PlaceholderFragmentPresenterImpl(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        initialize();

        presenter.loadItems();

        return binding.getRoot();
    }

    private void initialize() {

        Activity ac = getActivity();
        if (ac != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(ac);
            binding.recyclerView.setLayoutManager(layoutManager);
            mAdapter = new MyAdapter(ac);
            binding.recyclerView.setAdapter(mAdapter);
        }
    }


    @Override
    public void updateView(List<Dish> output) {
        mAdapter.setItems(output);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            presenter.loadItems();;
        }

        return super.onOptionsItemSelected(item);
    }
}

