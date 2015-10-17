package com.spyapp.android.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public class SpyProvider extends ContentProvider {

    public static final String AUTHORITY = "com.spyapp.android.provider";
    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    private static final UriMatcher mUriMatcher;
    private SQLiteDatabase mDatabase;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, SpyContracts.Sms.TABLE_NAME, SpyContracts.Sms.TYPE_DIR);
        mUriMatcher.addURI(AUTHORITY, SpyContracts.Sms.TABLE_NAME+"/#", SpyContracts.Sms.TYPE_ITEM);
    }

    @Override
    public boolean onCreate() {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        mDatabase = dbHelper.getWritableDatabase();
        return mDatabase != null;
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(SpyContracts.Sms.TABLE_NAME);
        switch (mUriMatcher.match(uri)) {
            case SpyContracts.Sms.TYPE_DIR:
                break;
            case SpyContracts.Sms.TYPE_ITEM:
                queryBuilder.appendWhere(SpyContracts.Sms.Columns._ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new UnsupportedOperationException("Unsupported URI: " + uri);
        }

        String orderBy = TextUtils.isEmpty(sortOrder) ?
                (SpyContracts.Sms.Columns.DATE + " DESC") : sortOrder;
        Cursor c = queryBuilder.query(
                mDatabase, projection, selection, selectionArgs, null, null, orderBy);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case SpyContracts.Sms.TYPE_DIR:
                return SpyContracts.Sms.CONTENT_TYPE;
            case SpyContracts.Sms.TYPE_ITEM:
                return SpyContracts.Sms.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unsupported URI: " + uri);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri result = null;
        switch (mUriMatcher.match(uri)) {
            case SpyContracts.Sms.TYPE_DIR:
                long rowId = mDatabase.insert(SpyContracts.Sms.TABLE_NAME, null, values);
                if (rowId > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    result = ContentUris.withAppendedId(SpyContracts.Sms.CONTENT_URI, rowId);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unsupported URI: " + uri);
        }
        return result;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int affectedRows;
        switch (mUriMatcher.match(uri)) {
            case SpyContracts.Sms.TYPE_DIR:
                affectedRows = mDatabase.delete(SpyContracts.Sms.TABLE_NAME, selection, selectionArgs);
                break;
            case SpyContracts.Sms.TYPE_ITEM:
                String segment = uri.getLastPathSegment();
                String whereClause = SpyContracts.Sms.Columns._ID + "=" + segment
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
                affectedRows = mDatabase.delete(SpyContracts.Sms.TABLE_NAME, whereClause, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported URI: " + uri);
        }
        if (affectedRows > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return affectedRows;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int affectedRows;
        switch (mUriMatcher.match(uri)) {
            case SpyContracts.Sms.TYPE_DIR:
                affectedRows = mDatabase.update(SpyContracts.Sms.TABLE_NAME, values, selection, selectionArgs);
                break;
            case SpyContracts.Sms.TYPE_ITEM:
                String segment = uri.getLastPathSegment();
                String whereClause = SpyContracts.Sms.Columns._ID + "=" + segment
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
                affectedRows = mDatabase.update(SpyContracts.Sms.TABLE_NAME, values, whereClause, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported URI: " + uri);
        }
        if (affectedRows > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return affectedRows;
    }

}