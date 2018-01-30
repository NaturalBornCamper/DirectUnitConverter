package com.naturalborncamper.android.directunitconverter.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.naturalborncamper.android.directunitconverter.C;

import static com.naturalborncamper.android.directunitconverter.data.UnitConverterContract.*;

/**
 * Created by Marco on 2018-01-19.
 */

public class UnitsModel {


    private SQLiteDatabase mDb;

    public UnitsModel(@NonNull SQLiteDatabase db) {
        mDb = db;
    }

    public CachedCursor getAllUnits() {
        Cursor c = null;
        try {
            c = mDb.query(
                    UnitsEntry.TABLE_NAME,
//                    new String[]{UnitsEntry.COLUMN_TITLE, UnitsEntry.COLUMN_CATEGORIES},
                    null,
                    null, null, null, null,
                    UnitsEntry.COLUMN_CATEGORIES + "," + UnitsEntry.COLUMN_ORDER + "," + UnitsEntry.COLUMN_TITLE
            );
        } catch (Exception e) {
            Log.e(C.TAG_DB, "Error when getting all units: " + e.getMessage());
            e.printStackTrace();
        }

        return new CachedCursor(c);
    }

    public boolean isEmpty() {
        Cursor countCursor = mDb.rawQuery("SELECT EXISTS(SELECT 1 FROM " + UnitsEntry.TABLE_NAME + ");", null);
        countCursor.moveToFirst();
        boolean isEmpty = countCursor.getInt(0) != 1;
        countCursor.close();

        return isEmpty;
    }
}
