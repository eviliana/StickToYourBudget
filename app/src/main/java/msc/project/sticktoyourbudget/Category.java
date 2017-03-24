package msc.project.sticktoyourbudget;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class Category {

    private int id;
    private String name;
    private String desc;
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHandler;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return desc;
    }

    public void setDescription(String desc) {
        this.desc = desc;
    }

    //Ο κατασκευαστής περνάει το context από το main activity για να δουλέψουμε με την βάση
    public Category(Context context){

        dbHandler = new DatabaseHandler(context);
    }

    public Category(int id, String name, String desc) {

        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public static boolean addCategory(String name, String desc, Context context) {
        Category cat = new Category(context);
        cat.setDescription(desc);
        cat.setName(name);
        return cat.save();
    }

    public ArrayList<String> getCategoryName() {
        db = dbHandler.getReadableDatabase();
        ArrayList<String> categories = new ArrayList<String>();
        Cursor c = db.query("CATEGORIES",new String[] {"id","CAT_NAME","CAT_DESCRIPTION"}, null,null,null,null,null);
        if (c != null) c.moveToFirst();
        //Log.d("Rows", String.valueOf(c.getCount()));
        c.moveToFirst();
        while (!c.isAfterLast()) {
            categories.add(c.getString(1));
            c.moveToNext();
        }
        c.close();
        return categories;
    }

    public Category(int id, SQLiteDatabase db ){ //throws Exception{
        //db = dbHandler.getReadableDatabase();
        Cursor c = db.query("CATEGORIES",new String[] {"id","CAT_NAME","CAT_DESCRIPTION"}, "id=?",new String[] {Integer.toString(id)},null,null,null);

        if (c.moveToFirst()) {
            this.setId(c.getInt(0));
            this.setName(c.getString(1));
            this.setDescription(c.getString(2));

            c.close();

        } else {
            c.close();
           // throw new Exception();
        }
    }

    public boolean save() {
        db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CAT_NAME", this.name);
        values.put("CAT_DESCRIPTION", this.desc);
        long returnedId = db.insert("CATEGORIES",null,values);
        //Εαν κατι παει στραβα η insert επιστρεφει -1 αλλιως επιστρεφει το id της εγγραφης που προστεθηκε
        if (returnedId == -1 ) {
            return  false;
        } else {
            this.id = (int)returnedId;
            return true;
        }
    }
    public static Category getCategoryFromName(String name, Context context) {
        Category category = new Category(context);
        category.setName(name);
        category.load();
        return category;
    }
    private void load() {
        db = dbHandler.getReadableDatabase();
        Cursor c = db.query("CATEGORIES",new String[] {"id","CAT_NAME","CAT_DESCRIPTION"}, "CAT_NAME=?",new String[] {this.getName()},null,null,null);

        if (c.moveToFirst()) {
            this.setId(c.getInt(0));
            this.setName(c.getString(1));
            this.setDescription(c.getString(2));

            c.close();

        } else {
            c.close();
        }
    }

}