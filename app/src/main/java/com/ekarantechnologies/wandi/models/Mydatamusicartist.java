package com.ekarantechnologies.wandi.models;

import java.util.Objects;

public class Mydatamusicartist {
    public int id;
    public String artist;
    public String song;
    public String streams;
    public int time;
    public String size;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mydatamusicartist that = (Mydatamusicartist) o;
        return id == that.id ;
    }
}
