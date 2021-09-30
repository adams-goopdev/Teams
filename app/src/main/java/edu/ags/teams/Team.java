package edu.ags.teams;

public class Team {
    public int Id;
    public String Description;
    public String City;
    public int ImgId;

    public Team(int id, String description, String city, int imgId)
    {
        //Initialize fields when instantiating the object
        Id = id;
        Description = description;
        City = city;
        ImgId = imgId;
    }

    public String toString()
    {
        return Id + "|" + Description + "|" + City + "|" + ImgId;
    }

}
