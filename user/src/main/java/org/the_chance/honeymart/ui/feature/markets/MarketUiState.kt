package org.the_chance.honeymart.ui.feature.markets

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.the_chance.honeymart.domain.model.MarketEntity
import org.the_chance.honeymart.domain.util.ErrorHandler


data class MarketsUiState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val error: ErrorHandler? = null,
    val markets: Flow<PagingData<MarketUiState>>  = flow{},
)

data class MarketUiState(
    val marketId: Long = 0L,
    val marketName: String = "",
    val marketImage: String = "",
    val isClicked : Boolean = false
)


fun MarketEntity.toMarketUiState(): MarketUiState {
    return MarketUiState(
        marketId = marketId,
        marketName = marketName,
        marketImage = imageUrl
    )
}