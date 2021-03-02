package com.example.stonksapp.financial

import com.finnhub.api.apis.DefaultApi
import com.finnhub.api.infrastructure.ApiClient
import android.util.Log;

// TODO: rewrite this stuff

class FinancialMainRetriever(private val token: String = "c0v7fl748v6pr2p77re0") {
    private val apiClient = DefaultApi()
    private var isApiKeySet: Boolean = false

    private fun applyApiKey() {
        ApiClient.apiKey["token"] = token
    }

    fun getListOfMainSymbols(country: String, maxSymbols: Int): MutableList<String?> {
        if (!isApiKeySet)
            applyApiKey()
        var list: List<com.finnhub.api.models.Stock> =
                retrieveCommonSymbols(country)

        Log.d("tag", list.size.toString())

        var result: MutableList<String?> = mutableListOf()
        for (i: Int in 0 until maxSymbols) {
            result.add(list.getOrNull(i)?.symbol)
        }

        Log.d("tag", "msg")

        return result
    }

    private fun retrieveCommonSymbols(country: String) = apiClient.stockSymbols(country)
}

