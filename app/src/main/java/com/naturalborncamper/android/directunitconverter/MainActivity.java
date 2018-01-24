package com.naturalborncamper.android.directunitconverter;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.naturalborncamper.android.directunitconverter.data.CachedCursor;
import com.naturalborncamper.android.directunitconverter.data.DefaultConversions;
import com.naturalborncamper.android.directunitconverter.data.UnitConverterDbHelper;
import com.naturalborncamper.android.directunitconverter.data.UnitsModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import static com.naturalborncamper.android.directunitconverter.data.UnitConverterContract.UnitsEntry;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnFocusChangeListener {
    UnitsModel mUnitsModel;
    public static final String EXTRA_CURRENT_CATEGORY = "extra_category";
    private int mFocusedUnit;
    private ArrayList<Integer> mCurrentUnitIds = new ArrayList<>();
    private CachedCursor mAllUnitsCursor;


    private String[] mCategories;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

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

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.categories_drawer);

        buildConversionScreen(savedInstanceState);
    }

    public void buildConversionScreen(@Nullable Bundle savedInstanceState) {
        mCurrentUnitIds.clear();
        TreeSet<String> categories = new TreeSet<>();

        String currentInputCategory = (savedInstanceState != null) ? savedInstanceState.getString(EXTRA_CURRENT_CATEGORY, "") : "";
        mAllUnitsCursor = mUnitsModel.getAllUnits();
        mAllUnitsCursor.moveToFirst();
        String category = mAllUnitsCursor.getString(UnitsEntry.COLUMN_CATEGORIES);
        if ("".equals(currentInputCategory)) currentInputCategory = category;

        // TODO When DB empty, make sure nothing crashes
        do {
            try {
                categories.add(mAllUnitsCursor.getString(UnitsEntry.COLUMN_CATEGORIES));
                category = mAllUnitsCursor.getString(UnitsEntry.COLUMN_CATEGORIES); // Repeated on first iteration
                categories.add(category);

                // Add this unit in the current conversion screen
                if (category.equals(currentInputCategory)) {
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.input_window);
                    TextInputLayout textInputLayout = new TextInputLayout(this);

                    UnitEditText input = new UnitEditText(this, mAllUnitsCursor);
                    input.setId(mAllUnitsCursor.getPosition());
                    mCurrentUnitIds.add(mAllUnitsCursor.getPosition());
                    input.setHint(mAllUnitsCursor.getString(UnitsEntry.COLUMN_TITLE));
                    textInputLayout.addView(input);
                    linearLayout.addView(textInputLayout);
                }
            } catch (Exception e) {
                Log.e(C.TAG_DB, "Error retrieving units data: " + e.getMessage());
                e.printStackTrace();
            }

        } while (mAllUnitsCursor.moveToNext());

        categories.add("Weight");
        categories.add("Volume");

        // Not sure if it's better to write a TreeSetAdapter or just convert it to an Array and use ArrayAdapter for the categories menu
        mCategories = categories.toArray(new String[categories.size()]);

        mDrawerList.setAdapter(new ArrayAdapter(this, R.layout.category_menu_item, mCategories));
        mDrawerList.setOnItemClickListener(new CategoryMenuItemClickListener());
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
        Float focusedUnitValue = (editable.length() > 0) ? Float.valueOf(editable.toString()) : 0;
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
        mFocusedUnit = view.getId();
        if (isFocused) Log.d(C.TAG_DEBUG, "Focused id: " + view.getId());
    }

    private class CategoryMenuItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            String bob = (String) adapterView.getAdapter().getItem(position);
            Log.d(C.TAG_DEBUG, bob);
        }
    }
}


// TODO Move all Todo in Trello?

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

// TODO Make an app icon

// TODO Fragment layout main activity for tablet view, with categories always visible instead of drawer menu

// TODO Swipe to switch conversion category without opening side menu