package edu.ags.teams;

public class Team {
    public int Id;
    public String Description;
    public String City;

    public Team(int id, String description, String city)
    {
        //Initialize fields when instantiating the object
        Id = id;
        Description = description;
        City = city;
    }
}
