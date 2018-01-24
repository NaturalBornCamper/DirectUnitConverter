package com.naturalborncamper.android.directunitconverter;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;

import com.naturalborncamper.android.directunitconverter.data.CachedCursor;
import com.naturalborncamper.android.directunitconverter.data.UnitConverterContract.UnitsEntry;

/**
 * Created by Marco on 2018-01-23.
 */

public class UnitEditText extends android.support.v7.widget.AppCompatEditText {
//    private List<String> categories;
    private float mMultiplier;
    private String mFormula;

    public int getPositionInCursor() {
        return mPositionInCursor;
    }

    private int mPositionInCursor;

    public UnitEditText(Context context, CachedCursor cursor) {
        super(context);
        mMultiplier = cursor.getFloat(UnitsEntry.COLUMN_MULTIPLIER);
        mFormula = cursor.getString(UnitsEntry.COLUMN_FORMULA);
        mPositionInCursor = cursor.getPosition();
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        setOnFocusChangeListener((OnFocusChangeListener) context);
//        this.addTextChangedListener(new UnitInputChangeTextWatcher());
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        TextWatcher context = (TextWatcher) getContext();
        if (focused)
            addTextChangedListener(context);
        else
            removeTextChangedListener(context);
    }

    //    @Override
//    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
//        super.onTextChanged(text, start, lengthBefore, lengthAfter);
////        Log.d(C.TAG_DEBUG, "SDSDF" + text);
//        MainActivity bob = (MainActivity) getContext();
//        bob.unitInputChanged();
//    }

//    private class UnitInputChangeTextWatcher implements TextWatcher {
//
//        @Override
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        }
//
//        @Override
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
////            Log.d(C.TAG_DEBUG, editable.toString());
////            getApplicationContext().unitInputChanged(editable.toString());
//        }
//    }
}
