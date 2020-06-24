package com.ekarantechnologies.wandi.models;

import java.util.List;

public class Foldermusic {
    public String status;
    public Integer total;
    public List<Mydatamusicfolder> mydata;

    public Foldermusic(String status, Integer total, List<Mydatamusicfolder> mydata) {
        this.status = status;
        this.total = total;
        this.mydata = mydata;
    }
}
