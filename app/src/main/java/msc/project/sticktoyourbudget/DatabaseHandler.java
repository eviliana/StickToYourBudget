package msc.project.sticktoyourbudget;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

    private Context context;
    //Το όνομα της βάσης δεδομένων
    private static final String DATABASE_NAME = "budget.db";
    //Ο τρέχων αριθμός έκδοσης της βάσης δεδομένων
    private static final int DATABASE_VERSION = 4;
    private SQLiteDatabase db;
    private SQLiteDatabase.CursorFactory cursor;
    public String LOGTAG;

    //κατασκευαστής που καλεί τον κατασκευαστή της υπερκλάσης
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override  //Μια εναλλακτική υλοποίηση του upgrade, πιο λιτή από αυτή του παραδείγματος
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS CATEGORIES");
        db.execSQL("DROP TABLE IF EXISTS EXPENSES");
        onCreate(db);
        Log.i(LOGTAG,"Database upgrade successful");
    }
    /*Δημιουργία βάσης με 2 πίνακες για κατηγορίες και έξοδα και αρχικοποίηση κατηγοριών
      Τα πεδία που επιλέχθηκαν αναλύονται στην εργασία*/
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE CATEGORIES ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "CAT_NAME TEXT NOT NULL UNIQUE, "
                + "CAT_DESCRIPTION TEXT);");
        db.execSQL("CREATE TABLE EXPENSES ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "EXP_PRICE REAL NOT NULL, "
                + "EXP_CATEGORY INTEGER NOT NULL, "
                + "EXP_WHEN INTEGER NOT NULL, "
                + "EXP_LAT REAL NOT NULL, "
                + "EXP_LON REAL NOT NULL, "
                + "EXP_PLACE TEXT, "
                + "EXP_DESCRIPTION TEXT);");
        Log.i(LOGTAG,"Database creation successful");
        initializeCategories(db, context.getString(R.string.cat1),
                context.getString(R.string.cat1desc));
        initializeCategories(db, context.getString(R.string.cat2),
                context.getString(R.string.cat2desc));
        initializeCategories(db, context.getString(R.string.cat3),
                context.getString(R.string.cat3desc));
        initializeCategories(db, context.getString(R.string.cat4),
                context.getString(R.string.cat4desc));
        initializeCategories(db, context.getString(R.string.cat5),
                context.getString(R.string.cat5desc));
        initializeCategories(db, context.getString(R.string.cat6),
                context.getString(R.string.cat6desc));

    }

    //Η μέθοδος αρχικοποίησης του πίνακα Categories για να προστεθούν 6 κατηγορίες εξόδων
    private void initializeCategories(SQLiteDatabase db, String catName, String catDesc) {
        ContentValues catValues = new ContentValues();
        catValues.put("CAT_NAME", catName);
        catValues.put("CAT_DESCRIPTION", catDesc);
        db.insert("CATEGORIES", null, catValues);

        Log.i(LOGTAG,"Category creation successful");
    }


   /* private void initializeExpenses(SQLiteDatabase db, double expPrice, int expCat, int expWhen, double expLat, double expLon, String expPlace, String expDesc) {
        ContentValues catValues = new ContentValues();
        catValues.put("EXP_PRICE", expPrice);
        catValues.put("EXP_CATEGORY", expCat);
        catValues.put("EXP_WHEN", expWhen);
        catValues.put("EXP_LAT", expLat);
        catValues.put("EXP_LON", expLon);
        catValues.put("EXP_PLACE", expPlace);
        catValues.put("EXP_DESCRIPTION", expDesc);
        db.insert("EXPENSES", null, catValues);
    }*/
}

