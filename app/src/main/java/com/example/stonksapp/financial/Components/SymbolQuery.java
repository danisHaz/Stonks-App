package com.example.stonksapp.financial.Components;

import com.google.gson.annotations.SerializedName;

import android.util.Log;

public class SymbolQuery {
    public static final int DESCRIPTION_TYPE = 0;
    public static final int SYMBOL_TYPE = 1;
    @SerializedName(value="count")
    public int count;

    @SerializedName(value="result")
    public SingleResult[] resultArray;

    public static class SingleResult {
        @SerializedName(value="description")
        public String description;

        @SerializedName(value="displaySymbol")
        public String displaySymbol;

        @SerializedName(value="symbol")
        public String symbol;

        @SerializedName(value="type")
        public String type;

    }

    /**
     *
     * @param typeOf: DESCRIPTION_TYPE for array of descriptions,
     *              SYMBOL_TYPE for array of symbols
     *
     * @return String[]
     */
    public String[] getArrayOf(int typeOf) {
        String[] res = new String[resultArray.length];

        int pos = 0;
        for (SingleResult result: resultArray) {
            if (typeOf == DESCRIPTION_TYPE)
                res[pos] = result.description;
            else if (typeOf == SYMBOL_TYPE) {
                res[pos] = result.symbol;
            } else {
                Log.e("Err", "typeOf parameter incorrect");
            }
            pos++;
        }

        return res;
    }

}
