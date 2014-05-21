package com.takeyuweb.sqlitecursorsample.app;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import gudon.sample.personprovider.Persons;

/*
 * CursorLoaderを用いてUIスレッドとは非同期でコンテンツプロバイダからデータを読み込み、反映するサンプル
 *
 * 参考
 * http://www.mori-soft.com/2008-08-15-01-36-37/smartphone/109-android-sqlite-cursorloader-23-content-provider
 * http://ichitcltk.hustle.ne.jp/gudon/modules/pico_rd/index.php?content_id=75
 * http://yuki312.blogspot.jp/2012/03/androidcursoradapter.html
 */

public class AsyncSwapCursorActivity extends Activity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_swap_cursor);

        simpleCursorAdapter =  new SimpleCursorAdapter(
                this,
                R.layout.list_item,
                null, // 後々onLoadFinishedで設定するのでここではカーソルを指定しない
                new String[] {Persons.NAME, Persons.AGE},
                new int[] {R.id.textViewName, R.id.textViewAge},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );
        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(simpleCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = ContentUris.withAppendedId(Persons.CONTENT_URI, id);
                Cursor c = getContentResolver().query(uri, new String[] {Persons.AGE}, null, null, null);
                if (c.moveToFirst()) {
                    int age = c.getInt(c.getColumnIndex(Persons.AGE));
                    ContentValues values = new ContentValues();
                    values.put(Persons.AGE, age + 1);
                    getContentResolver().update(uri, values, null, null);
                }
            }
        });

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, Persons.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        simpleCursorAdapter.swapCursor(c);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        simpleCursorAdapter.swapCursor(null);
    }
}
