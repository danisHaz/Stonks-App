package com.example.stonksapp.UI.Components;

import com.example.stonksapp.financial.Network.HTTPSRequestClient;
import com.example.stonksapp.financial.Components.SymbolQuery;
import com.example.stonksapp.Constants;

import android.database.MatrixCursor;
import android.util.Log;

import java.lang.Object;

public class CustomMatrixCursor {

    public CustomMatrixCursor() {}

    public static CustomMatrixCursor createInstance() {
        return new CustomMatrixCursor();
    }

    public MatrixCursor getCursorFromQuery(String query) {
        HTTPSRequestClient.GET client = new HTTPSRequestClient.GET();
        String[] result = client.symbolLookup(String.format(
                Constants.GET_SYMBOL_LOOKUP_TEMPLATE, query, Constants.API_TOKEN))
                .getArrayOf(SymbolQuery.SYMBOL_TYPE);

        Object[][] res = new Object[result.length][3];

        int pos = 0;
        for (String cur: result) {
            res[pos][0] = pos;
            res[pos][1] = cur;
            res[pos][2] = cur;
            pos++;
        }

        String[] columnNames = new String[3];
        columnNames[0] = "_id";
        columnNames[1] = "suggest_text_1";
        columnNames[2] = "suggest_intent_data";

        MatrixCursor cursor = new MatrixCursor(columnNames);

        for (int i = 0; i < java.lang.Math.min(pos, 3); i++) {
            cursor.addRow(res[i]);
        }

        return cursor;
    }
}
