package gudon.sample.personprovider;

/*
* コンテンツプロバイダ - 愚鈍人 さまより
* http://ichitcltk.hustle.ne.jp/gudon/modules/pico_rd/index.php?content_id=75
* */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PersonOpenHelper extends SQLiteOpenHelper {
    final static private int DB_VERSION = 1;
    final static String TABLE_NAME = "person_table";

    public PersonOpenHelper(Context context) {
        super(context, null, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // table create
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + Persons._ID
                + " INTEGER PRIMARY KEY," + Persons.NAME + " TEXT,"
                + Persons.AGE + " INTEGER" + ");");

        // table row insert
        db.execSQL("insert into " + TABLE_NAME + "(" + Persons.NAME + ","
                + Persons.AGE + ") values ('本田 圭佑', 24);");
        db.execSQL("insert into " + TABLE_NAME + "(" + Persons.NAME + ","
                + Persons.AGE + ") values ('遠藤 保仁', 30);");
        db.execSQL("insert into " + TABLE_NAME + "(" + Persons.NAME + ","
                + Persons.AGE + ") values ('松井 大輔', 29);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //
    }
}
