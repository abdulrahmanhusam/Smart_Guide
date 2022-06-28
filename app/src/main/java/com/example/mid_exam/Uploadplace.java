package com.example.mid_exam;

public class Uploadplace {

    public String placename,location,details,search,imgiconuri,placeimguri,videouri;

    public Uploadplace(){

    }

    public Uploadplace(String placename, String location, String details, String search, String imgiconuri, String placeimguri, String videouri) {
        this.placename = placename;
        this.location = location;
        this.details = details;
        this.search = search;
        this.imgiconuri = imgiconuri;
        this.placeimguri = placeimguri;
        this.videouri = videouri;
    }

    public String getPlacename() {
        return placename;
    }

    public void setPlacename(String placename) {
        this.placename = placename;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getImgiconuri() {
        return imgiconuri;
    }

    public void setImgiconuri(String imgiconuri) {
        this.imgiconuri = imgiconuri;
    }

    public String getPlaceimguri() {
        return placeimguri;
    }

    public void setPlaceimguri(String placeimguri) {
        this.placeimguri = placeimguri;
    }

    public String getVideouri() {
        return videouri;
    }

    public void setVideouri(String videouri) {
        this.videouri = videouri;
    }
}
