package com.naturalborncamper.android.directunitconverter;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.naturalborncamper.android.directunitconverter.data.CachedCursor;
import com.naturalborncamper.android.directunitconverter.data.DefaultConversions;
import com.naturalborncamper.android.directunitconverter.data.UnitConverterDbHelper;
import com.naturalborncamper.android.directunitconverter.data.UnitsModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.naturalborncamper.android.directunitconverter.data.UnitConverterContract.UnitsEntry;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnFocusChangeListener {
    UnitsModel mUnitsModel;
    public static final String EXTRA_CURRENT_CATEGORY = "extra_category";
    private int mFocusedUnit;
    private ArrayList<Integer> mCurrentUnitIds = new ArrayList<>();
    private CachedCursor mAllUnitsCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        SQLiteDatabase db = new UnitConverterDbHelper(this).getWritableDatabase();
        mUnitsModel = new UnitsModel(db);

        if (mUnitsModel.isEmpty()) {
            Log.d(C.TAG_DEBUG, "Units table empty, generating default units");
            DefaultConversions.generateDefaultConversions(db);
        }

        buildConversionScreen(savedInstanceState);
    }

    public void buildConversionScreen(@Nullable Bundle savedInstanceState) {
        mCurrentUnitIds.clear();

        String currentInputCategory = (savedInstanceState != null) ? savedInstanceState.getString(EXTRA_CURRENT_CATEGORY, "") : "";
        mAllUnitsCursor = mUnitsModel.getAllUnits();
        if (mAllUnitsCursor != null) {
            mAllUnitsCursor.moveToFirst();
            do {
                try {
                    List<String> categories = Arrays.asList(mAllUnitsCursor.getString(UnitsEntry.COLUMN_CATEGORIES).split(","));
                    if ("".equals(currentInputCategory)) currentInputCategory = categories.get(0);

                    Log.d(C.TAG_DEBUG, "current id: " + mAllUnitsCursor.getInt(UnitsEntry._ID));
                    // Add this unit in the current conversion screen
                    if (categories.contains(currentInputCategory)) {
                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.input_window);
                        TextInputLayout textInputLayout = new TextInputLayout(this);

//                        EditText input = new EditText(this, null);
                        UnitEditText input = new UnitEditText(this, mAllUnitsCursor);
                        input.setId(mAllUnitsCursor.getPosition());
                        mCurrentUnitIds.add(mAllUnitsCursor.getPosition());
                        input.setHint(mAllUnitsCursor.getString(UnitsEntry.COLUMN_TITLE));
//                    input.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                        textInputLayout.addView(input);
                        linearLayout.addView(textInputLayout);
                    }
                } catch (Exception e) {
                    Log.e(C.TAG_DB, "Error retrieving units data: " + e.getMessage());
                    e.printStackTrace();
                }

            } while (mAllUnitsCursor.moveToNext());

        } else Log.d(C.TAG_DB, "cached cursor null");
    }

//    public void unitInputChanged() {
////        Log.d(C.TAG_DEBUG, "DSFSdddddddddddddddDFSDF");
//    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        int focusedUnitId = findViewById(mFocusedUnit).getId();
        mAllUnitsCursor.moveToPosition(focusedUnitId);
        Float focusedUnitMultiplier = mAllUnitsCursor.getFloat(UnitsEntry.COLUMN_MULTIPLIER);
        Float focusedUnitValue = (editable.length() > 0)? Float.valueOf(editable.toString()): 0;
        Float focusedUnitUnmultipliedValue = focusedUnitValue/focusedUnitMultiplier;

        for (int id : mCurrentUnitIds) {
            if (id != focusedUnitId) {
                mAllUnitsCursor.moveToPosition(id);
                Float currentUnitMultiplier = mAllUnitsCursor.getFloat(UnitsEntry.COLUMN_MULTIPLIER);

                UnitEditText currentEditText = findViewById(id);
                currentEditText.setText(String.valueOf(focusedUnitUnmultipliedValue*currentUnitMultiplier));
            }
        }
    }

    @Override
    public void onFocusChange(View view, boolean isFocused) {
//        UnitEditText bob = (UnitEditText) view;
//        bob.getPositionInCursor();
        mFocusedUnit = view.getId();
        if (isFocused) Log.d(C.TAG_DEBUG, "Focused id: " + view.getId());
    }
}


// TODO Add drawer menu
// TODO Add categories in drawer menu

// TODO In second activity, list all conversions
// TODO In second activity, edit conversion
// TODO In second activity, delete conversion
// TODO In second activity, add conversion
// TODO Validation either multiplier of formula, cannot both be empty
// TODO Validation multiplier cannot be 0 (divide by zero)
// TODO Validation name only characters and numbers
// TODO Validation categories only characters, numbers and comma (trim after change, if possible)

// TODO App lifecycle (onPause, resume, destroy, etc) for cursor

// TODO Save/load current category in Bundle
// TODO Save/load current unit in Bundle
// TODO Save/load current unit value in Bundle

// TODO Add default conversions so the app is not empty by default

// TODO Fragment layout main activity for tablet view, with categories always visible instead of drawer menu

// TODO Swipe to switch conversion category without opening side menu