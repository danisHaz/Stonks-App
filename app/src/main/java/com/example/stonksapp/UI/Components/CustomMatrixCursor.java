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
        SymbolQuery resultQuery = client.symbolLookup(String.format(
                Constants.GET_SYMBOL_LOOKUP_TEMPLATE, query, Constants.API_TOKEN));

        String[] symbolResult = resultQuery
                .getArrayOf(SymbolQuery.SYMBOL_TYPE);

        String[] descriptionResult = resultQuery
                .getArrayOf(SymbolQuery.DESCRIPTION_TYPE);

        Object[][] res = new Object[symbolResult.length][3];

        for (int pos = 0; pos < symbolResult.length; pos++) {
            res[pos][0] = pos;
            res[pos][1] = descriptionResult[pos];
            res[pos][2] = symbolResult[pos];
            pos++;
        }

        String[] columnNames = new String[3];
        columnNames[0] = "_id";
        columnNames[1] = "suggest_text_1";
        columnNames[2] = "suggest_intent_data";

        MatrixCursor cursor = new MatrixCursor(columnNames);

        for (int i = 0; i < java.lang.Math.min(symbolResult.length, 3); i++) {
            cursor.addRow(res[i]);
        }

        return cursor;
    }
}
