package com.vito.tiprankshometask.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface StockApiService {
    @GET("api/Autocomplete/GetAutocomplete")
    suspend fun getStocks(@Query("name") name: String): List<StockItem>
}
