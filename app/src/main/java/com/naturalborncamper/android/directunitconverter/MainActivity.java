package com.naturalborncamper.android.directunitconverter;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    private String mCurrentInputCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        SQLiteDatabase db = new UnitConverterDbHelper(this).getWritableDatabase();
        mUnitsModel = new UnitsModel(db);

        if (mUnitsModel.isEmpty()) {
            Log.d(C.TAG_DEBUG, "Units table empty, generating default units");
            DefaultConversions.generateDefaultConversions(db);
        }
        mAllUnitsCursor = mUnitsModel.getAllUnits();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.categories_drawer);

        buildConversionScreen(savedInstanceState);

    }

    public void buildConversionScreen(@Nullable Bundle savedInstanceState) {
        mCurrentUnitIds.clear();
        TreeSet<String> categories = new TreeSet<>();
        LinearLayout linearLayout = findViewById(R.id.input_window);
        String category;
        UnitEditText input;
        TextInputLayout textInputLayout;

        // TODO Test with DB empty, make sure nothing crashes below
        mAllUnitsCursor.moveToFirst();
        linearLayout.removeAllViews();

        if ("".equals(mCurrentInputCategory)) {
            if (savedInstanceState != null)
                mCurrentInputCategory = savedInstanceState.getString(EXTRA_CURRENT_CATEGORY, "");
            else
                mCurrentInputCategory = mAllUnitsCursor.getString(UnitsEntry.COLUMN_CATEGORIES);
        }

        do {
            try {
                // Add category in TreeSet for side menu
                categories.add(mAllUnitsCursor.getString(UnitsEntry.COLUMN_CATEGORIES));
                category = mAllUnitsCursor.getString(UnitsEntry.COLUMN_CATEGORIES);
                categories.add(category);

                // Add this unit in the current conversion screen
                if (category.equals(mCurrentInputCategory)) {
                    textInputLayout = new TextInputLayout(this);

                    input = new UnitEditText(this, mAllUnitsCursor);
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

        // Dummy categories for test
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
            // Not really useful at the moment, but might be later
//            mDrawerList.setItemChecked(position, true);

            mDrawerLayout.closeDrawer(mDrawerList);
            mCurrentInputCategory = (String) adapterView.getAdapter().getItem(position);
//            getActionBar().setTitle(mCurrentInputCategory);
            buildConversionScreen(null);
            Log.d(C.TAG_DEBUG, mCurrentInputCategory);
        }
    }
}


// TODO Check if worth it to transform main conversion screen into a RecyclerView
// TODO If using RecyclerView, might be faster since no findViewById in a loop every time there is a change (Check Processor and memory use)