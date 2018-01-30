package com.naturalborncamper.android.directunitconverter;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.naturalborncamper.android.directunitconverter.data.CachedCursor;
import com.naturalborncamper.android.directunitconverter.data.UnitConverterDbHelper;
import com.naturalborncamper.android.directunitconverter.data.UnitsModel;

public class EditUnitsActivity extends AppCompatActivity {

    private UnitsModel mUnitsModel;
    private RecyclerView mUnitsListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_units);

        SQLiteDatabase db = new UnitConverterDbHelper(this).getWritableDatabase();
        mUnitsModel = new UnitsModel(db);
        CachedCursor c = mUnitsModel.getAllUnits();

        mUnitsListRecyclerView = findViewById(R.id.rv_edit_units_list);
        CachedCursorAdapter c2 = new CachedCursorAdapter(this, c);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mUnitsListRecyclerView.setLayoutManager(layoutManager);
        mUnitsListRecyclerView.setAdapter(c2);



    }
}
