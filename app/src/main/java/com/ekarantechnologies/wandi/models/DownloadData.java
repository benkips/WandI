package com.ekarantechnologies.wandi.models;

import com.tonyodev.fetch2.Download;

public class DownloadData {
        public int id;
        public Download download;
        public long eta = -1;
        public long downloadedBytesPerSecond = 0;

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public String toString() {
            if (download == null) {
                return "";
            }
            return download.toString();
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this || obj instanceof DownloadData && ((DownloadData) obj).id == id;
        }

    }