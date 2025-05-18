package com.vito.tiprankshometask.remote

data class StockItem(
    val label: String,
    val ticker: String?,
    val value: String,
    val category: String,
    val uid: String
)
