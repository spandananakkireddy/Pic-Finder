package com.example.manup.group32_inclass11;

import java.util.ArrayList;

/**
 * Created by manup on 4/9/2018.
 */

public class Parser {

    ArrayList<images> images ;

    public ArrayList<Parser.images> getImages() {
        return images;
    }

    public void setImages(ArrayList<Parser.images> images) {
        this.images = images;
    }


    public static class images
    {
        ArrayList<display_sizes> display_sizes;

        public ArrayList<display_sizes> getSizesList() {
            return display_sizes;
        }

        public void setSizesList(ArrayList<display_sizes> sizesList) {
            this.display_sizes = sizesList;
        }

        @Override
        public String toString() {
            return "Images{" +
                    "sizesList=" + display_sizes +
                    '}';
        }
    }
    public static class display_sizes
    {
        String uri;

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        @Override
        public String toString() {
            return "Sizes{" +
                    "uri='" + uri + '\'' +
                    '}';
        }
    }
}
