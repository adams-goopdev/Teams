package edu.ags.teams;

public class Team {
    public int Id;
    public String Name;
    public String City;
    public int ImgId;
    public float Rating;
    public String CellNumber;

    public Team()
    {
        Id = -1;
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

    public Team(int id, String name, String city, int imgId, float rating, String cellNumber)
    {
        //Initialize fields when instantiating the object
        Id = id;
        Name = name;
        City = city;
        ImgId = imgId;
        Rating = rating;
        CellNumber = cellNumber;
    }

    public String toString()
    {
        return Id + "|" + Name + "|" + City + "|" + ImgId + '|' + Rating + "|" + CellNumber;
    }

}
