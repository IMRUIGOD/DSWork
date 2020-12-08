package edu.wschina.baceadapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyHelp extends SQLiteOpenHelper {
    public MyHelp(@Nullable Context context) {
        super(context, "cai_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE dc(id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(8),imgUrl VARCHAR(100),count INTEGER,price INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
