package com.vito.tiprankshometask.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vito.tiprankshometask.remote.StockItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StockCard(
    stock: StockItem,
    index: Int,
    selectedCard: Boolean,
    onLongClick: ()-> Unit,
    onUidClick: (String) -> Unit,
    onCardClick: (Int, String)-> Unit
) {
//    var selectedCard by remember { mutableStateOf(false) }

    val cardColor by animateColorAsState(
        targetValue = when {
            selectedCard -> MaterialTheme.colorScheme.secondaryContainer
            else -> MaterialTheme.colorScheme.surface
        },
        label = "CardColor"
    )
    val isLight = !isSystemInDarkTheme()
    val borderColor = when {
        selectedCard -> MaterialTheme.colorScheme.primary
        isLight -> Color.Blue
        else -> Color.Cyan
    }
    val elevation by animateFloatAsState(
        targetValue = if (selectedCard) 18f else 8f,
        label = "CardElevation"
    )
    val scale by animateFloatAsState(
        targetValue = if (selectedCard) 1.03f else 1f,
        label = "CardScale"
    )


    Card(
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .border(width = 1.6.dp, color = borderColor, shape = RoundedCornerShape(18.dp))
            .combinedClickable(
                onClick = { onCardClick(index, stock.label) },
                onLongClick = onLongClick
            )
            .padding(2.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(
            Modifier
                .padding(14.dp)
                .fillMaxHeight()
        ) {
            Text(
                text = stock.label,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stock.uid,
                modifier = Modifier.clickable { onUidClick(stock.uid) },
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = stock.value,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = stock.category,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}