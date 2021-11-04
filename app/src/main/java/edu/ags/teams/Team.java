package edu.ags.teams;

import android.graphics.Bitmap;

public class Team {
    public void setId(int id) {
        Id = id;
    }

    private int Id;
    private String Name;
    private String City;

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    private Bitmap photo;

    public int getImgId() {
        return ImgId;
    }

    public void setImgId(int imgId) {
        ImgId = imgId;
    }

    private int ImgId;
    private float Rating;
    public String CellNumber;

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    private Boolean isFavorite;

    public Team() {
        Id = -1;
        ImgId = R.drawable.noimage;
        isFavorite = false;
    }

    public int getId()
    {
        return Id;
    }

    public String getName()
    {
        return Name;
    }
    public void setName(String name)
    {
        Name = name;
    }

    public String getCity()
    {
        return City;
    }
    public void setCity(String city)
    {
        City = city;
    }

    public String getCellNumber()
    {
        return CellNumber;
    }
    public void setCellNumber(String cellNumber)
    {
        CellNumber = cellNumber;
    }

    public float getRating()
    {
        return Rating;
    }
    public void setRating(float rating)
    {
        Rating = rating;
    }

    public Team(int id, String name, String city, int imgId, float rating, String cellNumber, Boolean isfavorite) {
        // Initialize fields when instantiating the object
        Id = id;
        Name = name;
        City = city;
        ImgId = imgId;
        Rating = rating;
        CellNumber = cellNumber;
        isFavorite = isfavorite;
    }

    public String toString() {
        return Id + "|" + Name + "|" + City + "|" + ImgId + "|" + Rating + "|" + CellNumber + "|" + isFavorite;
    }
}
