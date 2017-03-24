package msc.project.sticktoyourbudget;


public class DataModel {
    public String date;
    public String category;
    public String price;
    public String description;
    public String place;
    public int id;

    public DataModel(String date, String category, String price, String description, String place, int id) {
        this.date = date;
        this.category = category;
        this.price = price;
        this.description = description;
        this.id = id;
        this.place = place;
    }

}
