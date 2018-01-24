package com.naturalborncamper.android.directunitconverter.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.naturalborncamper.android.directunitconverter.data.UnitConverterContract.UnitsEntry;

/**
 * Created by Marco on 2018-01-18.
 */

public class UnitConverterDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "unit_converter.db";
    private static final int DATABASE_VERSION = 1;

    public UnitConverterDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + UnitsEntry.TABLE_NAME + "("
                + UnitsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UnitsEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + UnitsEntry.COLUMN_CATEGORIES + " TEXT NOT NULL, "
                + UnitsEntry.COLUMN_MULTIPLIER + " REAL DEFAULT NULL, "
                + UnitsEntry.COLUMN_FORMULA + " TEXT DEFAULT NULL, "
                + UnitsEntry.COLUMN_ORDER + " INT NOT NULL DEFAULT 0"
                + ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UnitsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
