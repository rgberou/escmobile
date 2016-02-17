package com.android.esc.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rg on 2/16/2016.
 */
public class MyDbHandler extends SQLiteOpenHelper {
    private static final int db_version=1;
    private static final String database_name="esc_db";
    private String table_route="save_routes";
    private String column_pujid="puj_id";
    private String column_pujdesc="puj_desc";
    public MyDbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, database_name, factory, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="Create Table "+table_route+"("+column_pujid+"Text Primary key "+column_pujdesc+" Text "+");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists "+table_route);
        onCreate(db);

    }

    public void addJeep(SaveRoute jeep){
        ContentValues contentValues=new ContentValues();
        contentValues.put(column_pujid,jeep.getPUJ_id());
        contentValues.put(column_pujdesc,jeep.getDesc());
        SQLiteDatabase db=getWritableDatabase();
        db.insert(table_route,null,contentValues);
        db.close();

    }
}
