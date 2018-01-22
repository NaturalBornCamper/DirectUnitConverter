package com.naturalborncamper.android.directunitconverter.data;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.naturalborncamper.android.directunitconverter.C;

import java.util.HashMap;

/**
 * Created by Marco on 2018-01-20.
 */

public class CachedCursor extends CursorWrapper {

    HashMap<String, Integer> cachedColumnIndexes;
    ArrayMap<String, Integer> mCachedColumnIndexes;

    public CachedCursor(Cursor cursor) {
        super(cursor);
        mCachedColumnIndexes = new ArrayMap<>();
    }


    private void cachedColumnName(String columnName) {
        if (!mCachedColumnIndexes.containsKey(columnName))
            mCachedColumnIndexes.put(columnName, getColumnIndex(columnName));
    }

    public float getFloat(String columnName) {
        cachedColumnName(columnName);

        return super.getFloat(mCachedColumnIndexes.get(columnName));
    }


    public int getInt(String columnName) {
        cachedColumnName(columnName);

        return super.getInt(mCachedColumnIndexes.get(columnName));
    }

    public String getString(String columnName) {
//        Log.d(C.TAG_DEBUG, columnName);
        cachedColumnName(columnName);

        return super.getString(mCachedColumnIndexes.get(columnName));
    }
}
