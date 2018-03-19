package com.waracle.androidtest.data;

/**
 * Created by Jacek on 2018-03-19.
 */

public class Dish {

    private String title;
    private String description;
    private String url;

    public Dish() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static Dish makeDish(String title, String description, String url){
        Dish dish = new Dish();
        dish.title = title;
        dish.description = description;
        dish.url = url;

        return dish;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
