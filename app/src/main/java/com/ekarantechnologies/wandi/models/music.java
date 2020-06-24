package com.ekarantechnologies.wandi.models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class music {
    public String status;
    public Integer total;

    public List<Mydatamusic> mydata;



    public music(String status, Integer total, List<Mydatamusic> mydata) {
        this.status = status;
        this.total = total;
        this.mydata = mydata;
    }

}
