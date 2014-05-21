package com.takeyuweb.sqlitecursorsample.app;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import gudon.sample.personprovider.Persons;

/*
 * SimpleCursorAdapterにUI更新も任せてしまうサンプル ※3.0以降非推奨

 * 変更の検知はコンテンツプロバイダで
 * c.setNotificationUri(getContext().getContentResolver(), uri); および
 * getContext().getContentResolver().notifyChange(uri, null);
 * を呼び出すことでUriベースで管理している（？）
 *
 * */

public class AutoRequeryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_requery);

        // (Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
        final Cursor c = getContentResolver().
                query(Persons.CONTENT_URI, null, null, null, null);
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.list_item,
                c,
                new String[] {Persons.NAME, Persons.AGE},
                new int[] {R.id.textViewName, R.id.textViewAge}
        );
        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(simpleCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int age = c.getInt(c.getColumnIndex(Persons.AGE));
                Uri uri = ContentUris.withAppendedId(Persons.CONTENT_URI, id);
                ContentValues values = new ContentValues();
                values.put(Persons.AGE, age+1);
                getContentResolver().update(uri, values, null, null);
            }
        });
    }

}
