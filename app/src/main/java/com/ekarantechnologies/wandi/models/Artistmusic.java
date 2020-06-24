package com.ekarantechnologies.wandi.models;

import java.util.List;

public class Artistmusic {
    public String status;
    public Integer total;

    public List<Mydatamusicartist> mydata;



    public Artistmusic(String status, Integer total, List<Mydatamusicartist> mydata) {
        this.status = status;
        this.total = total;
        this.mydata = mydata;
    }

}
