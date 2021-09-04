package com.example.notesintern;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public static final String DB_NAME="Notes.db";
    public static final int DB_VN=1;
    public static final String TBNAME="my_table";
    public static final String cid="id";
    public static final String ctitle="title";
    public static final String cdesc="descp";
    public static final String cauthor="author";
    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VN);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query= "CREATE TABLE "+TBNAME+
                " ("+cid+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                ctitle+" TEXT, "+
                cdesc+" TEXT, "+
                cauthor+" TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TBNAME);
    }
    public  void addNote(String t,String d,String a){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(ctitle,t);
        cv.put(cdesc,d);
        cv.put(cauthor,a);
        long result=db.insert(TBNAME,null,cv);
        if(result==-1)
            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show();
    }
    Cursor readAllData(String a){
        String query="SELECT * FROM "+TBNAME+" WHERE (author = "+"\""+a+"\""+")";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=null;
        if(db!=null){
            cursor=db.rawQuery(query,null);
        }
        return cursor;
    }
    void updateData(String row_id,String t,String d,String a){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(ctitle,t);
        cv.put(cdesc,d);
        cv.put(cauthor,a);
        Log.d("hsit2","**");
        long result=db.update(TBNAME,cv,"id=?",new String[]{row_id});
        if (result==-1){
            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show();
        }
    }
    void deleteOneRow(String row_id){
        SQLiteDatabase db=this.getWritableDatabase();
        long result=db.delete(TBNAME,"id=?",new String[]{row_id});
        if (result==-1){
            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show();
        }
    }
}
