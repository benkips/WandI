package com.ekarantechnologies.wandi.models;

import java.util.Objects;

public class Mydatamusic {
    public int id;
    public String song;
    public String streams;
    public int time;
    public String size;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mydatamusic that = (Mydatamusic) o;
        return id == that.id;
    }


}
