package com.vito.tiprankshometask.remote

class StockRepository {
    private val api = RetrofitInstance.api

    suspend fun fetchStocks(query: String): Result<List<StockItem>> = try {
        val response = api.getStocks(query)
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }
}