package gudon.sample.personprovider;

/*
* コンテンツプロバイダ - 愚鈍人 さまより
* http://ichitcltk.hustle.ne.jp/gudon/modules/pico_rd/index.php?content_id=75
* */

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class PersonProvider extends ContentProvider {
    private PersonOpenHelper personOpenHelper;
    private static final UriMatcher uriMatcher;

    private static final int PERSONS = 1;
    private static final int PERSON_ID = 2;

    private static HashMap<String, String> personProjectionMap;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Persons.AUTHORITY, "persons", PERSONS);
        uriMatcher.addURI(Persons.AUTHORITY, "persons/#", PERSON_ID);

        personProjectionMap = new HashMap<String, String>();
        personProjectionMap.put(Persons._ID, Persons._ID);
        personProjectionMap.put(Persons.NAME, Persons.NAME);
        personProjectionMap.put(Persons.AGE, Persons.AGE);
    }

    @Override
    public boolean onCreate() {
        personOpenHelper = new PersonOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(PersonOpenHelper.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case PERSONS:
                qb.setProjectionMap(personProjectionMap);
                break;
            case PERSON_ID:
                qb.setProjectionMap(personProjectionMap);
                qb.appendWhere(Persons._ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = personOpenHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null,
                null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != PERSONS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (values.containsKey(Persons.NAME) == false) {
            values.put(Persons.NAME, "詠み人知らず");
        }

        SQLiteDatabase db = personOpenHelper.getWritableDatabase();
        long rowId = db.insert(PersonOpenHelper.TABLE_NAME, null, values);
        if (rowId > 0) {
            Uri returnUri = ContentUris.withAppendedId(Persons.CONTENT_URI,
                    rowId);
            getContext().getContentResolver().notifyChange(returnUri, null);
            return returnUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = personOpenHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)) {
            case PERSONS:
                count = db.update(PersonOpenHelper.TABLE_NAME, values, selection,
                        selectionArgs);
                break;

            case PERSON_ID:
                String id = uri.getPathSegments().get(1);
                count = db.update(PersonOpenHelper.TABLE_NAME, values, Persons._ID
                        + "="
                        + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                        + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = personOpenHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)) {
            case PERSONS:
                count = db.delete(PersonOpenHelper.TABLE_NAME, selection,
                        selectionArgs);
                break;

            case PERSON_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(PersonOpenHelper.TABLE_NAME, Persons._ID
                        + "="
                        + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                        + ")" : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case PERSONS:
                return Persons.CONTENT_TYPE;
            case PERSON_ID:
                return Persons.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }
}
