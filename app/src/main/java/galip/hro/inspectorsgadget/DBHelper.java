package galip.hro.inspectorsgadget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gebruiker on 13-10-2014.
 */
public class DBHelper extends SQLiteOpenHelper{


    public static final String DATABASE_NAME = "InspectorsGadget.db";
    public static final String UNCODES_TABLE_NAME = "UnCodes";
    public static final String UNCODES_COLUMN_ID = "Id";
    public static final String UNCODES_COLUMN_UnCode = "UnCode";
    public static final String UNCODES_COLUMN_DescriptionShippingName = "Description";

    private HashMap hp;


    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table UnCodes " +
                        "(Id integer primary key, UnCode text, DescriptionShippingName text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS UnCodes");
        onCreate(db);
    }
    public boolean insertContact  (String unCode, String description)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("UnCode", unCode);
        contentValues.put("Description", description);

        db.insert("UnCodes", null, contentValues);
        return true;
    }


    public Cursor getData(String code){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from UnCodes where UnCode="+code, null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, UNCODES_TABLE_NAME);
        return numRows;
    }

    public boolean updateUnCode (Integer unDd, String unCode, String description)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("UnCodes", unCode);
        contentValues.put("Description", description);

        db.update("UnCodes", contentValues, "id = ? ", new String[] { Integer.toString(unDd) } );
        return true;
    }
    public Integer deleteUnCode (Integer unId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("UnCodes",
                "id = ? ",
                new String[] { Integer.toString(unId) });
    }


    public ArrayList getAllUnCodes()
    {
        ArrayList array_list = new ArrayList();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from UnCodes", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(UNCODES_COLUMN_UnCode)));
            res.moveToNext();
        }
        return array_list;
    }

    public void create()

    {
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase("InspectorsGadget",null);

        database.execSQL("CREATE TABLE IF NOT EXISTS Users(Username VARCHAR(100),Password VARCHAR(20));");
        database.execSQL("INSERT INTO Users VALUES('admin','admin');");
        database.execSQL("CREATE TABLE IF NOT EXISTS UnCodes(Id INT,UnCode VARCHAR(10),Description VARCHAR(255));");
        database.execSQL("INSERT INTO UnCodes VALUES('1','1428','sodium');");

    }

}



