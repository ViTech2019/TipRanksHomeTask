package com.vito.tiprankshometask.ui

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.vito.tiprankshometask.remote.StockItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StockGrid(
    stocks: List<StockItem>,
    onUidClick: (String)-> Unit,
    onCardClick: (Int, String)-> Unit,
    selectedStockIndex: Int,
    onLongClick: (Int)-> Unit,
    modifier: Modifier = Modifier
) {
    val columns = calculateColumns()
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(
            start = 8.dp, end = 8.dp, top = 8.dp, bottom = 24.dp
        )
    ) {
        itemsIndexed(stocks) { indx, item ->
            StockCard(
                stock = item,
                index = indx,
                selectedCard = selectedStockIndex == indx,
                onLongClick = { onLongClick(indx) },
                onUidClick = onUidClick,
                onCardClick = onCardClick
            )
        }
    }
}


@Composable
fun calculateColumns(): Int {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    return when {
        screenWidth >= 900 && isLandscape -> 5
        screenWidth >= 600 && isLandscape -> 5
        screenWidth >= 600 -> 3
        screenWidth >= 480 && isLandscape -> 5
        else -> 3
    }
}