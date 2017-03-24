package msc.project.sticktoyourbudget;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class Expense {

    private int id;
    private double price;
    private Category category;
    private Date when;
    private double lat;
    private double lon;
    private String desc;
    private String place;
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHandler;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    //Ο κατασκευαστής περνάει το context από το main activity για να δουλέψουμε με την βάση
    public Expense(Context context){
        dbHandler = new DatabaseHandler(context);
    }


    public Expense(int id, double price, Category category, Date when, double lat, double lon, String place, String desc) {
        this.id = id;
        this.price = price;
        this.category = category;
        this.when = when;
        this.lat = lat;
        this.lon = lon;
        this.desc = desc;
        this.place = place;
    }

    public ArrayList<Expense> getAll() {
        db = dbHandler.getReadableDatabase();
        ArrayList<Expense> expenses = new ArrayList<>();
        Cursor c = db.query("EXPENSES",new String[] {"id","EXP_PRICE","EXP_CATEGORY","EXP_WHEN","EXP_LAT",
                "EXP_LON","EXP_PLACE","EXP_DESCRIPTION"},null,null,null,null,null);
        if (c != null) c.moveToFirst();
        while(!c.isAfterLast()) {
            try {
                Expense expense = new Expense(
                        c.getInt(0),
                        c.getDouble(1),
                        new Category(c.getInt(2), db),
                        new Date(c.getLong(3)),
                        c.getDouble(4),
                        c.getDouble(5),
                        c.getString(6),
                        c.getString(7)
                );
                expenses.add(expense);
            } catch (Exception ex) {
                //den uparxei i katigoria
            }
            c.moveToNext();
        }
        c.close();
        return expenses;

    }
    public static boolean addExpense(double price, String category, String description, Date date, double latitude, double longitude, String location, Context context)
    {
        Expense exp = new Expense(context);
        exp.setPrice(price);
        exp.setCategory(Category.getCategoryFromName(category, context));
        exp.setDesc(description);
        exp.setWhen(date);
        exp.setLat(latitude);
        exp.setLon(longitude);
        exp.setPlace(location);
        return exp.save();
    }

    public boolean save() {
        db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put("id", null);
        values.put("EXP_PRICE", this.price);
        values.put("EXP_CATEGORY", this.category.getId());
        values.put("EXP_WHEN", this.when.getTime());
        values.put("EXP_LAT", this.lat);
        values.put("EXP_LON", this.lon);
        values.put("EXP_DESCRIPTION", this.desc);
        values.put("EXP_PLACE", this.place);
        long returnedId = db.insert("EXPENSES",null, values);
        if (returnedId == -1 ) {
            return  false;
        } else {
            this.id = (int)returnedId;
            return true;
        }
    }
    //Δεν υλοποιήθηκε στην εφαρμογή
    public boolean deleteExpense(int id){
        db = dbHandler.getWritableDatabase();
        //db.execSQL("DELETE FROM EXPENSES WHERE id='"+id+"'");
        long returnedId =  db.delete("EXPENSES","id = ?",new String[] {Integer.toString(id)});
        Log.d("TAG", String.valueOf(returnedId));
        if (returnedId == 0 ) {
            return  false;
        } else {
            return true;
        }


    }
    public static ArrayList<DataModel> getData (Context context) {
        ArrayList<DataModel> data = new ArrayList<>();
        Expense exp = new Expense(context);
        ArrayList<Expense> expenses = exp.getAll();
        for (Expense ex : expenses) {
            data.add(new DataModel(ex.getWhen().toLocaleString(), ex.getCategory().getName(), String.valueOf(ex.getPrice()), ex.getDesc(), ex.getPlace(), ex.getId()));
        }
        return data;
    }
    public static ArrayList<DataModel> getAnalysisData(Context context, long dStart, long dEnd) {
        ArrayList<DataModel> data = new ArrayList<>();
        Expense exp = new Expense(context);
        ArrayList<Expense> expenses = exp.getForPeriod(dStart, dEnd);
        for (Expense ex : expenses) {
            data.add(new DataModel(ex.getWhen().toLocaleString(), ex.getCategory().getName(), String.valueOf(ex.getPrice()), ex.getDesc(), ex.getPlace(), ex.getId()));
        }
        return data;
    }
    public ArrayList<Expense> getForPeriod(long start, long end) {
        db = dbHandler.getReadableDatabase();
        ArrayList<Expense> expenses = new ArrayList<>();
        Cursor c = db.query("EXPENSES",null, "EXP_WHEN >= ? AND EXP_WHEN < ?", new String[]{String.valueOf(start), String.valueOf(end)}, null, null, "EXP_PRICE ASC", null);

        if (c != null) c.moveToFirst();
        while(!c.isAfterLast()) {
            try {
                Expense expense = new Expense(
                        c.getInt(0),
                        c.getDouble(1),
                        new Category(c.getInt(2), db),
                        new Date(c.getLong(3)),
                        c.getDouble(4),
                        c.getDouble(5),
                        c.getString(6),
                        c.getString(7)
                );
                expenses.add(expense);
            } catch (Exception ex) {
                //den uparxei i katigoria
            }
            c.moveToNext();
        }
        c.close();
        return expenses;

    }
}

