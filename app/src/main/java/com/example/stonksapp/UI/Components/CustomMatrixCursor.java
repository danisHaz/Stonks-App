package com.example.stonksapp.UI.Components;

import com.example.stonksapp.financial.Network.HTTPSRequestClient;
import com.example.stonksapp.financial.Components.SymbolQuery;
import com.example.stonksapp.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.stonksapp.financial.SimpleStockTransporter;

import android.database.MatrixCursor;

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

        String[] columnNames = new String[3];
        columnNames[0] = "_id";
        columnNames[1] = "suggest_text_1";
        columnNames[2] = "suggest_intent_data";

        MatrixCursor cursor = new MatrixCursor(columnNames);

        if (resultQuery == null)
            return cursor;

        String[] symbolResult = resultQuery
                .getArrayOf(SymbolQuery.SYMBOL_TYPE);

        String[] descriptionResult = resultQuery
                .getArrayOf(SymbolQuery.DESCRIPTION_TYPE);

        Gson gson = new GsonBuilder().create();
        String[] result = new String[symbolResult.length];
        for (int pos = 0; pos < symbolResult.length; pos++) {
            SimpleStockTransporter transporter = new SimpleStockTransporter(symbolResult[pos],
                    descriptionResult[pos]);
            result[pos] = gson.toJson(transporter, SimpleStockTransporter.class);
        }

        Object[][] res = new Object[symbolResult.length][3];

        for (int pos = 0; pos < symbolResult.length; pos++) {
            res[pos][0] = pos;
            res[pos][1] = descriptionResult[pos];
            res[pos][2] = result[pos];
            pos++;
        }

        for (int i = 0; i < java.lang.Math.min(symbolResult.length, 3); i++) {
            cursor.addRow(res[i]);
        }

        return cursor;
    }
}
