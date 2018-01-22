package com.naturalborncamper.android.directunitconverter.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.naturalborncamper.android.directunitconverter.C;

import static com.naturalborncamper.android.directunitconverter.data.UnitConverterContract.*;

/**
 * Created by Marco on 2018-01-18.
 */

public final class DefaultConversions {

    private DefaultConversions(){}

    public static void generateDefaultConversions(SQLiteDatabase db) {

        ContentValues cv = new ContentValues();
        db.beginTransaction();

        cv.put(UnitsEntry.COLUMN_TITLE, "Meters");
        cv.put(UnitsEntry.COLUMN_CATEGORIES, "Distance");
        cv.put(UnitsEntry.COLUMN_MULTIPLIER, 1);
        addUnitToTransaction(db, cv);


        cv.clear();
        cv.put(UnitsEntry.COLUMN_TITLE, "Miles");
        cv.put(UnitsEntry.COLUMN_CATEGORIES, "Distance");
        cv.put(UnitsEntry.COLUMN_MULTIPLIER, 1/1.6);
        addUnitToTransaction(db, cv);

        try {
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(C.TAG_DB, "Error when executing "+ UnitsEntry.TABLE_NAME + " transaction message: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private static void addUnitToTransaction(SQLiteDatabase db, ContentValues cv) {
        try {
            db.insert(UnitsEntry.TABLE_NAME, null, cv);
        } catch (Exception e) {
            Log.e(C.TAG_DB, "Error inserting data on " + UnitsEntry.TABLE_NAME + ": \"" + cv.get(UnitsEntry.COLUMN_TITLE) + "\", message: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
