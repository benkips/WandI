package com.ekarantechnologies.wandi.models;

import java.util.Objects;

public class Mydatafolders {
    public int id;
    public String folder;
    public String total;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mydatafolders that = (Mydatafolders) o;
        return id == that.id;
    }


}
