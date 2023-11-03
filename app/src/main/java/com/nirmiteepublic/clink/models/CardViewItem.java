package com.nirmiteepublic.clink.models;

public class CardViewItem {
    private String text;
    private boolean isSelected;
    private int uniqueId;

    public CardViewItem(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public CardViewItem(String text) {
        this.text = text;
        isSelected = false;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getText() {
        return text;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
