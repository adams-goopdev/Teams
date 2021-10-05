package edu.ags.teams;

public class Team {
    public int Id;
    public String Description;
    public String City;
    public int ImgId;
    public float Rating;
    public String CellNumber;

    public Team(int id, String description, String city, int imgId, float rating, String cellNumber)
    {
        //Initialize fields when instantiating the object
        Id = id;
        Description = description;
        City = city;
        ImgId = imgId;
        Rating = rating;
        CellNumber = cellNumber;
    }

    public String toString()
    {
        return Id + "|" + Description + "|" + City + "|" + ImgId + '|' + Rating + "|" + CellNumber;
    }

}
