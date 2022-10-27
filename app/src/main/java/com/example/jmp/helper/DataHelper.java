package com.example.jmp.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DataHelper extends SQLiteOpenHelper {

    public static final String DBNAME="mahasiswa.db";
    public static final String TABLENAME="biodata";
    public static final int VER=1;

    public DataHelper(@Nullable Context context) {
        super(context, DBNAME, null, VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="create table "+TABLENAME+"(id integer primary key, name text, telepon text, jk text, alamat text, avatar blob)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query="drop table if exists "+TABLENAME+"";
        db.execSQL(query);
        onCreate(db);
    }
}
