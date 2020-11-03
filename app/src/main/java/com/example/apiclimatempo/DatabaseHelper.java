package com.example.apiclimatempo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBClimaTempo.db";
    public static final String PREVISAO_TABLE_NAME = "Previsao";
    public static final String PREVISAO_ID = "id ";
    public static final String PREVISAO_DATA = "Data ";
    public static final String PREVISAO_CIDADE = "Cidade ";
    public static final String PREVISAO_ICONE = "Icone ";
    public static final String PREVISAO_CLIMA = "Clima ";
    public static final String PREVISAO_TEMPERATURA = "Temperatura ";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME , null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table Previsao " +
                        "(id integer primary key,Data text, Icone text,Clima text, Temperatura text,Cidade text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS Previsao");
        onCreate(db);
    }

    public boolean insertDia (Previsao p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PREVISAO_DATA, p.get_Data()) ;
        contentValues.put(PREVISAO_ICONE, p.get_Icone().toString());
        contentValues.put(PREVISAO_CLIMA, p.get_Clima());
        contentValues.put(PREVISAO_TEMPERATURA, p.get_Temperatura());
        contentValues.put(PREVISAO_CIDADE, p.get_Cidade());
        db.insert(PREVISAO_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getTemperatura(Integer _id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Previsao where id="+_id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, PREVISAO_TABLE_NAME);
        return numRows;
    }

    public boolean updatePrevisao (Previsao p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PREVISAO_DATA, p.get_Data());
        contentValues.put(PREVISAO_ICONE, p.get_Icone());
        contentValues.put(PREVISAO_CLIMA, p.get_Clima());
        contentValues.put(PREVISAO_TEMPERATURA, p.get_Temperatura());
        contentValues.put(PREVISAO_CIDADE, p.get_Cidade());
        db.update(PREVISAO_TABLE_NAME, contentValues, "id = ? ", new String[] { p.get_id().toString() } );
        return true;
    }

    public Integer deletePrevisao (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PREVISAO_TABLE_NAME,
                "id = ? ",
                new String[] { id.toString() });
    }

    public Integer deleteAll () {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PREVISAO_TABLE_NAME,
                null,
                null);
    }

    public ArrayList<String> getAllPrevisao() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Previsao", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(PREVISAO_TABLE_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
    public ArrayList<Previsao> getPrevisaoProx() {
        ArrayList<Previsao> lista = new ArrayList<>() ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * FROM Previsao ORDER BY id DESC LIMIT 5", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            Previsao p = new Previsao();
            p.set_id(Integer.parseInt(res.getString(res.getColumnIndex(PREVISAO_ID))));
            p.set_Data(res.getString(res.getColumnIndex(PREVISAO_DATA)));
            p.set_Icone(Integer.parseInt(res.getString(res.getColumnIndex(PREVISAO_ICONE))));
            p.set_Clima(res.getString(res.getColumnIndex(PREVISAO_CLIMA)));
            p.set_Temperatura(res.getString(res.getColumnIndex(PREVISAO_TEMPERATURA)));
            p.set_Cidade(res.getString(res.getColumnIndex(PREVISAO_CIDADE)));

            lista.add(p);
            res.moveToNext();
        }
            Log.d("Quantos: ",lista.size()+"");

        return lista;
    }
}
