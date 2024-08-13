package com.nirmiteepublic.clink.models;

public class FilterCriteria {
    String selectedDistrict;
    String SelectedDate;
    String selectedTehsil;
    String selectedVillage;
    String selectBooth;

    public String getSelectedDistrict() {
        return selectedDistrict;
    }

    public void setSelectedDistrict(String selectedDistrict) {
        this.selectedDistrict = selectedDistrict;
    }

    public String getSelectedDate() {
        return SelectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        SelectedDate = selectedDate;
    }

    public String getSelectedTehsil() {
        return selectedTehsil;
    }

    public void setSelectedTehsil(String selectedTehsil) {
        this.selectedTehsil = selectedTehsil;
    }

    public String getSelectedVillage() {
        return selectedVillage;
    }

    public void setSelectedVillage(String selectedVillage) {
        this.selectedVillage = selectedVillage;
    }

    public String getSelectBooth() {
        return selectBooth;
    }

    public void setSelectBooth(String selectBooth) {
        this.selectBooth = selectBooth;
    }
}
