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

    public static final int SMS = 1;
    public static final int SMS_ID = 2;
    public static final int GPS = 3;
    public static final int GPS_ID = 4;

    private static final UriMatcher mUriMatcher;
    private SQLiteDatabase mDatabase;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, SpyContracts.Sms.TABLE_NAME, SMS);
        mUriMatcher.addURI(AUTHORITY, SpyContracts.Sms.TABLE_NAME+"/#", SMS_ID);
        mUriMatcher.addURI(AUTHORITY, SpyContracts.Gps.TABLE_NAME, GPS);
        mUriMatcher.addURI(AUTHORITY, SpyContracts.Gps.TABLE_NAME+"/#", GPS_ID);
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
        switch (mUriMatcher.match(uri)) {
            case SMS:
                queryBuilder.setTables(SpyContracts.Sms.TABLE_NAME);
                sortOrder = TextUtils.isEmpty(sortOrder) ? SpyContracts.Sms.DEFAULT_SORT_ORDER : sortOrder;
                break;
            case SMS_ID:
                queryBuilder.setTables(SpyContracts.Sms.TABLE_NAME);
                queryBuilder.appendWhere(SpyContracts.Sms.Columns._ID + "=" + uri.getLastPathSegment());
                break;
            case GPS:
                queryBuilder.setTables(SpyContracts.Gps.TABLE_NAME);
                sortOrder = TextUtils.isEmpty(sortOrder) ? SpyContracts.Gps.DEFAULT_SORT_ORDER : sortOrder;
                break;
            case GPS_ID:
                queryBuilder.setTables(SpyContracts.Gps.TABLE_NAME);
                queryBuilder.appendWhere(SpyContracts.Gps.Columns._ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new UnsupportedOperationException("Unsupported URI: " + uri);
        }

        Cursor c = queryBuilder.query(
                mDatabase, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case SMS:
                return SpyContracts.Sms.CONTENT_TYPE;
            case SMS_ID:
                return SpyContracts.Sms.CONTENT_ITEM_TYPE;
            case GPS:
                return SpyContracts.Gps.CONTENT_TYPE;
            case GPS_ID:
                return SpyContracts.Gps.CONTENT_ITEM_TYPE;
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
            case SMS:
                long rowId = mDatabase.insert(SpyContracts.Sms.TABLE_NAME, null, values);
                if (rowId > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    result = ContentUris.withAppendedId(SpyContracts.Sms.CONTENT_URI, rowId);
                }
                break;
            case GPS:
                rowId = mDatabase.insert(SpyContracts.Gps.TABLE_NAME, null, values);
                if (rowId > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    result = ContentUris.withAppendedId(SpyContracts.Gps.CONTENT_URI, rowId);
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
            case SMS:
                affectedRows = mDatabase.delete(SpyContracts.Sms.TABLE_NAME, selection, selectionArgs);
                break;
            case SMS_ID:
                String segment = uri.getLastPathSegment();
                String whereClause = SpyContracts.Sms.Columns._ID + "=" + segment
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
                affectedRows = mDatabase.delete(SpyContracts.Sms.TABLE_NAME, whereClause, selectionArgs);
                break;
            case GPS:
                affectedRows = mDatabase.delete(SpyContracts.Gps.TABLE_NAME, selection, selectionArgs);
                break;
            case GPS_ID:
                segment = uri.getLastPathSegment();
                whereClause = SpyContracts.Gps.Columns._ID + "=" + segment
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
                affectedRows = mDatabase.delete(SpyContracts.Gps.TABLE_NAME, whereClause, selectionArgs);
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
            case SMS:
                affectedRows = mDatabase.update(SpyContracts.Sms.TABLE_NAME, values, selection, selectionArgs);
                break;
            case SMS_ID:
                String segment = uri.getLastPathSegment();
                String whereClause = SpyContracts.Sms.Columns._ID + "=" + segment
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
                affectedRows = mDatabase.update(SpyContracts.Sms.TABLE_NAME, values, whereClause, selectionArgs);
                break;
            case GPS:
                affectedRows = mDatabase.update(SpyContracts.Gps.TABLE_NAME, values, selection, selectionArgs);
                break;
            case GPS_ID:
                segment = uri.getLastPathSegment();
                whereClause = SpyContracts.Gps.Columns._ID + "=" + segment
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
                affectedRows = mDatabase.update(SpyContracts.Gps.TABLE_NAME, values, whereClause, selectionArgs);
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