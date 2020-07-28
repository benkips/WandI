package com.ekarantechnologies.wandi.models;

import java.util.Objects;

public class Mydatamusicfolder {
    public int id;
    public String folder;
    public String song;
    public String streams;
    public int time;
    public String size;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mydatamusicfolder that = (Mydatamusicfolder) o;
        return id == that.id ;
    }

}
