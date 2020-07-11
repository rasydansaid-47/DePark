package com.example.depark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ParkingDB.db";

    // User table name
    private static final String TABLE_USER = "users";
    private static final String TABLE_VALET = "valet";
    private static final String TABLE_STAFF = "staff";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "userid";
    private static final String COLUMN_USER_NAME = "username";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_USER_PHONENUMBER = "phonenumber";

    private static final String COLUMN_VALET_ID = "valetid";
    private static final String COLUMN_VALET_CAR = "car";
    private static final String COLUMN_VALET_CARPLATENO = "carplateno";
    private static final String COLUMN_VALET_TIME = "time";
    private static final String COLUMN_VALET_STAFF = "staffid";
    private static final String COLUMN_VALET_USER = "userid";

    private static final String COLUMN_STAFF_ID = "staffid";
    private static final String COLUMN_STAFF_NAME = "staffname";
    private static final String COLUMN_STAFF_AGE = "age";
    private static final String COLUMN_STAFF_GENDER = "gender";
    private static final String COLUMN_STAFF_PHONENUMBER = "phonenumber";

    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_NAME + " TEXT PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD
            + " TEXT," + COLUMN_USER_PHONENUMBER + " INTEGER " + ")";

    private String CREATE_STAFF_TABLE = "CREATE TABLE " + TABLE_STAFF + "("
            + COLUMN_STAFF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_STAFF_NAME + " TEXT,"
            + COLUMN_STAFF_AGE + " TEXT," + COLUMN_STAFF_GENDER + " TEXT," + COLUMN_STAFF_PHONENUMBER + " INTEGER " + ")";

    private String CREATE_VALET_TABLE = "CREATE TABLE " + TABLE_VALET + "("
            + COLUMN_VALET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_VALET_CAR + " TEXT,"
            + COLUMN_VALET_CARPLATENO + " TEXT," + COLUMN_VALET_TIME + " TEXT," + COLUMN_VALET_USER + " TEXT, " + COLUMN_VALET_STAFF + " INTEGER," +
            "FOREIGN KEY(" + COLUMN_VALET_USER + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_NAME + ")," +
            "FOREIGN KEY(" + COLUMN_VALET_STAFF + ") REFERENCES " + TABLE_STAFF + "(" + COLUMN_STAFF_ID + ")" + ")";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private String DROP_STAFE_TABLE = "DROP TABLE IF EXISTS " + TABLE_STAFF;
    private String DROP_VALET_TABLE = "DROP TABLE IF EXISTS " + TABLE_VALET;

    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_STAFF_TABLE);
        db.execSQL(CREATE_VALET_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_STAFE_TABLE);
        db.execSQL(DROP_VALET_TABLE);

        // Create tables again
        onCreate(db);

    }

    public boolean insert_USER(String username, String password, String email, String phonenumber){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("email", email);
        contentValues.put("phonenumber", phonenumber);
        long ins = db.insert("user", null, contentValues);
        if(ins == -1) return false;
        else return true;
    }

    public Boolean chkusername(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from users where username=?", new String[]{username});
        if(cursor.getCount()>0) return false;
        else return true;
    }

    public Boolean USER_usernamepassword(String username, String pasword){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from users where username=? and password=?", new String[]{username, pasword});
        if(cursor.getCount()>0) return true;
        else return false;
    }
}
