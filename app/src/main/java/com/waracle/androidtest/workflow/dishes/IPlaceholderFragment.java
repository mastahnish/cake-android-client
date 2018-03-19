package com.waracle.androidtest.workflow.dishes;

import com.waracle.androidtest.data.Dish;

import java.util.List;

/**
 * Created by Jacek on 2018-03-19.
 */

public interface IPlaceholderFragment {

    interface Presenter{
        void loadItems();
    }

    interface View{
        void updateView(List<Dish> output);
    }
}
