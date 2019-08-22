package michael.attend;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.*;



/*
        HOW TO IMPLEMENT THIS CLASS

        1)

        createUserDB object;

        //create void function to insert user into database with all params

        public void addUser(String name, userName, password, email){
            boolean insertName = object.addName(name);
            boolean insertUsername = object.addUsername(userName);
            boolean insertPassword = object.addPassword(password);
            boolean insertEmail = object.addEmail(email);


            Keep a toast to see whats good if it worked via if else statements

        }


        // i was wondering if we could just create columns in our table through the onCreate and
        // then just create  a regular old function to add data to given column

        public void addShit(String column, String shit){
            boolean insertName = object.addData(column, shit);

            TOAST DAT BIHHHH
        }
 */



//SQL Database shell for putting in name/username/pass/email

public class createUserDB extends SQLiteOpenHelper {

    private static final String TAG = "Registration Tag";

    //Database version
    private static final int DATABASE_VERSION = 1;

    //Database name
    private static final String DATABASE_NAME = "Attend";

    //Table name
    private static final String USER_CREDENTIALS = "User Credentials";

    //Table Columns Names
    private static final String NAME = "Name";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
    private static final String EMAIL = "Email";

    public createUserDB(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override

    public void onCreate(SQLiteDatabase db){
        //create database with our entries
        String CREATE_USERNAME_DATABASE = "CREATE TABLE " + USER_CREDENTIALS + "(" +
                NAME + "TEXT, " +
                USERNAME + "TEXT, " +
                PASSWORD + "TEXT, " +
                EMAIL + "TEXT" + ")";
        db.execSQL(CREATE_USERNAME_DATABASE);

    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + USER_CREDENTIALS);
        onCreate(db);
    }


    //i wonder if this'll work, experimental
    //(adding data to any given table) column = NAME, USERNAME, PASSWORD, EMAIL

    public boolean addData(String column, String data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(column, data);

        Log.d(TAG, "addData: Adding " + data + " to " + column);

        long result = db.insert(USER_CREDENTIALS, null, contentValues);

        if(result == -1){
            return false;
        } else {
            return true;
        }
    }


    //add Name to table
    public boolean addName(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);

        Log.d(TAG, "addName: Adding " + name + " to " + USER_CREDENTIALS);

        long result = db.insert(USER_CREDENTIALS, null, contentValues);

        //check to see if the item was inserted
        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    //add Username to table
    public boolean addUsername(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERNAME, username);

        Log.d(TAG, "addUsername: Adding " + username + " to " + USER_CREDENTIALS);

        long result = db.insert(USER_CREDENTIALS, null, contentValues);

        //check to see if the item was inserted
        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    //add password to table
    public boolean addPassword(String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PASSWORD, password);

        Log.d(TAG, "addPassword: Adding " + password + " to " + USER_CREDENTIALS);

        long result = db.insert(USER_CREDENTIALS, null, contentValues);

        //check to see if the item was inserted
        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    //add email to table
    public boolean addEmail(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EMAIL, email);

        Log.d(TAG, "addEmail: Adding " + email + " to " + USER_CREDENTIALS);

        long result = db.insert(USER_CREDENTIALS, null, contentValues);

        //check to see if the item was inserted
        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

}
