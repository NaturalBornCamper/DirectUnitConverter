package com.naturalborncamper.android.directunitconverter.data;

import android.provider.BaseColumns;

/**
 * Created by Marco on 2018-01-18.
 */

public class UnitConverterContract {

    // To convert a unit using multiplier: first divide by its multiplier, then multiplier by the target unit's multiplier
    // To convert a unit using formula: ...?
    public static final class UnitsEntry implements BaseColumns{
        public static final String TABLE_NAME = "units";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CATEGORIES = "categories";
        public static final String COLUMN_MULTIPLIER = "multiplier";    // If multiplier is "1", will be "default" for base conversion (Call it favorite unit or base unit)
        public static final String COLUMN_FORMULA = "formula";
        public static final String COLUMN_ORDER = "ordering";
    }

    // T(°F) = T(°C) × 1.8 + 32
    // T(°C) = (T(°F) - 32) / 1.8
}
