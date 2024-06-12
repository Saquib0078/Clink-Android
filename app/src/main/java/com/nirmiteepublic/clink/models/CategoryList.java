package com.nirmiteepublic.clink.models;

import java.util.List;

public class CategoryList {
    private List<String> categories;

    public List<String> getCategories() {
        return categories;
    }
    @Override
    public String toString() {
        return "CategoryList{" +
                "categories=" + categories +
                '}';
    }
}
