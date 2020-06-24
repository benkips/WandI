package com.ekarantechnologies.wandi.models;

import java.util.Objects;

public class Mydataartist {
    public int id;
    public String artist;
    public String song;
    public String total;
    public String photo;
    public int time;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mydataartist that = (Mydataartist) o;
        return id == that.id;
    }


}
