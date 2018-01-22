package com.naturalborncamper.android.directunitconverter;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.naturalborncamper.android.directunitconverter.data.CachedCursor;
import com.naturalborncamper.android.directunitconverter.data.DefaultConversions;
import com.naturalborncamper.android.directunitconverter.data.UnitConverterDbHelper;
import com.naturalborncamper.android.directunitconverter.data.UnitsModel;

import static com.naturalborncamper.android.directunitconverter.data.UnitConverterContract.UnitsEntry;

public class MainActivity extends AppCompatActivity {
    UnitsModel mUnitsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase db = new UnitConverterDbHelper(this).getWritableDatabase();
        mUnitsModel = new UnitsModel(db);

        if (mUnitsModel.isEmpty()) {
            Log.d(C.TAG_DEBUG, "Units table empty, generating default units");
            DefaultConversions.generateDefaultConversions(db);
        }

        CachedCursor allUnits = mUnitsModel.getAllUnits();
        if (allUnits != null) {
            allUnits.moveToFirst();
            do {
                try {
                    Log.d(C.TAG_DEBUG, allUnits.getString(UnitsEntry.COLUMN_TITLE));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } while (allUnits.moveToNext());

        } else Log.d(C.TAG_DB, "cached cursor null");

    }
}


// TODO Side menu display
// TODO Fragment layout main activity
// TODO SQLite db with basic conversions
// TODO dynamically create conversion fields on category we are in
// TODO Swipe to switch conversion category without opening side menu
// TODO tablet side menu layout always visible