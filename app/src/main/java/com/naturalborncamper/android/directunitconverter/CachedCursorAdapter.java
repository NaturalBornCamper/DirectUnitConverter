package com.naturalborncamper.android.directunitconverter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.naturalborncamper.android.directunitconverter.data.CachedCursor;
import com.naturalborncamper.android.directunitconverter.data.UnitConverterContract.UnitsEntry;

/**
 * Created by Marco on 2018-01-28.
 */

public class CachedCursorAdapter extends RecyclerView.Adapter<CachedCursorAdapter.VH> {

    Context mContext;
    CachedCursor mCursor;

    public CachedCursorAdapter(Context context, CachedCursor cursor) {
        super();
//        mContext = context;
        mCursor = cursor;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_unit_list_item, parent, false);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_unit_list_item_constraint, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        mCursor.moveToPosition(position);
        holder.mUnitNameEditText.setText(mCursor.getString(UnitsEntry.COLUMN_TITLE));
        holder.mUnitCategoryEditText.setText(mCursor.getString(UnitsEntry.COLUMN_CATEGORIES));
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class VH extends RecyclerView.ViewHolder{

        public EditText mUnitNameEditText;
        public EditText mUnitCategoryEditText;

        public VH(View itemView) {
            super(itemView);

            mUnitNameEditText = itemView.findViewById(R.id.et_unit_name);
            mUnitCategoryEditText = itemView.findViewById(R.id.et_unit_category);
        }

    }
}
