@file:OptIn(ExperimentalAnimationApi::class)

package org.the_chance.honeymart.ui.feature.product_details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import org.the_chance.design_system.R
import org.the_chance.honeymart.ui.composables.ContentVisibility
import org.the_chance.honeymart.ui.composables.EventHandler
import org.the_chance.honeymart.ui.feature.authentication.signup.authentication.navigateToAuthScreen
import org.the_chance.honeymart.ui.feature.home.composables.ItemLabel
import org.the_chance.honeymart.ui.feature.product_details.composeable.ProductAppBar
import org.the_chance.honeymart.ui.feature.product_details.composeable.SmallProductImages
import org.the_chance.honeymart.ui.feature.productreview.navigateToProductReviewsScreen
import org.the_chance.honymart.ui.composables.AverageRating
import org.the_chance.honymart.ui.composables.CardReviews
import org.the_chance.honymart.ui.composables.ConnectionErrorPlaceholder
import org.the_chance.honymart.ui.composables.CustomAlertDialog
import org.the_chance.honymart.ui.composables.HoneyFilledButton
import org.the_chance.honymart.ui.composables.HoneyIconButton
import org.the_chance.honymart.ui.composables.HoneyOutlineText
import org.the_chance.honymart.ui.composables.ImageNetwork
import org.the_chance.honymart.ui.composables.Loading
import org.the_chance.honymart.ui.composables.SnackBarWithDuration
import org.the_chance.honymart.ui.theme.dimens

@Composable
fun ProductDetailsScreen(
    viewModel: ProductDetailsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    EventHandler(
        effects = viewModel.effect,
        handleEffect = { effect, navController ->
            when (effect) {
                is ProductDetailsUiEffect.AddProductToWishListEffectError -> {}
                is ProductDetailsUiEffect.AddToCartError -> {}
                is ProductDetailsUiEffect.AddToCartSuccess -> {
                    viewModel.showSnackBar(effect.message)
                }

                ProductDetailsUiEffect.OnBackClickEffect -> navController.navigateUp()
                is ProductDetailsUiEffect.ProductNotInSameCartMarketExceptionEffect -> {
                    viewModel.showDialog(effect.productId, effect.count)
                }

                ProductDetailsUiEffect.UnAuthorizedUserEffect ->
                    navController.navigateToAuthScreen()

                is ProductDetailsUiEffect.NavigateToReviewsScreen ->
                    navController.navigateToProductReviewsScreen(productId = effect.productId)
            }
        })
    ConnectionErrorPlaceholder(
        state = state.isConnectionError,
        onClickTryAgain = viewModel::getData
    )
    Loading(state = state.isLoading)
    ContentVisibility(state = !state.isLoading && !state.isConnectionError) {
        ProductDetailsContent(state = state, listener = viewModel)
    }
}


@Composable
private fun ProductDetailsContent(
    state: ProductDetailsUiState,
    listener: ProductDetailsInteraction
) {
    ProductDetailsMainContent(state, listener)
    Box(modifier = Modifier.fillMaxSize()) {
        if (state.dialogState.showDialog) {
            CustomAlertDialog(
                message = stringResource(R.string.add_from_different_cart_message),
                onConfirm = {
                    listener.confirmDeleteLastCartAndAddProductToNewCart(
                        state.dialogState.productId, state.dialogState.count
                    )
                    listener.resetDialogState()
                },
                onCancel = { listener.resetDialogState() },
                onDismissRequest = { listener.resetDialogState() },
                modifier = Modifier
                    .align(Alignment.Center)
                    .zIndex(2f)
            )
        }
    }
}

@Composable
fun ProductDetailsMainContent(state: ProductDetailsUiState, listener: ProductDetailsInteraction) {
    val reviews = state.reviews
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        bottomBar = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onTertiary)
                        .align(Alignment.BottomCenter)
                ) {
                    HoneyFilledButton(
                        label = stringResource(id = R.string.add_to_cart),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                bottom = MaterialTheme.dimens.space56,
                                top = MaterialTheme.dimens.space16,
                                start = MaterialTheme.dimens.space16,
                                end = MaterialTheme.dimens.space16,
                            )
                            .align(Alignment.BottomCenter),
                        onClick = {
                            state.product.productId.let {
                                listener.addProductToCart(it, state.quantity)
                            }
                        },
                        icon = R.drawable.icon_cart,
                        isLoading = state.isAddToCartLoading
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 120.dp)
                ) {
                    AnimatedVisibility(
                        visible = state.snackBar.isShow,
                        enter = fadeIn(animationSpec = tween(durationMillis = 2000)) + slideInVertically(),
                        exit = fadeOut(animationSpec = tween(durationMillis = 500)) + slideOutHorizontally()
                    ) {
                        SnackBarWithDuration(
                            message = state.snackBar.massage,
                            onDismiss = listener::resetSnackBarState,
                            undoAction = {},
                            text = ""
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                ) {
                    if (state.dialogState.showDialog) {
                        CustomAlertDialog(
                            message = stringResource
                                (R.string.add_from_different_cart_message),
                            onConfirm = {
                                listener.confirmDeleteLastCartAndAddProductToNewCart(
                                    state.dialogState.productId, state.dialogState.count
                                )
                                listener.resetDialogState()
                            },
                            onCancel = { listener.resetDialogState() },
                            onDismissRequest = { listener.resetDialogState() }
                        )
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(Modifier.fillMaxSize()) {
            item {
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val (imageProduct, smallImageProduct, info) = createRefs()
                    Box(modifier = Modifier
                        .fillMaxHeight(0.5F)
                        .constrainAs(imageProduct) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }) {

                        ImageNetwork(
                            imageUrl = state.image, modifier = Modifier.fillMaxSize()
                        )

                        ProductAppBar(
                            modifier = Modifier.padding(horizontal = MaterialTheme.dimens.space16),
                            state = state,
                            onBackClick = listener::onClickBack,
                            onFavoriteClick = { listener.onClickFavorite(state.product.productId) },
                        )
                    }
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.secondary)
                        .fillMaxHeight(0.5F)
                        .padding(
                            top = MaterialTheme.dimens.space24,
                            start = MaterialTheme.dimens.space16,
                            end = MaterialTheme.dimens.space16,
                        )
                        .constrainAs(info) {
                            top.linkTo(imageProduct.bottom)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = state.product.productName,
                                style = MaterialTheme.typography.displayMedium.copy(
                                    color = MaterialTheme.colorScheme.onSecondary
                                ),
                            )
                            Row {
                                HoneyIconButton(
                                    iconPainter = painterResource(id = R.drawable.icon_remove_from_cart),
                                    background = Color.Transparent,
                                    isLoading = state.isAddToCartLoading,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.primary,
                                            CircleShape
                                        ),
                                    onClick = listener::decreaseProductCount,
                                )

                                Text(
                                    text = state.quantity.toString(),
                                    style = MaterialTheme.typography.displayMedium.copy(
                                        color = MaterialTheme.colorScheme.onBackground
                                    ),
                                    modifier = Modifier
                                        .padding(horizontal = MaterialTheme.dimens.space12)
                                )
                                HoneyIconButton(
                                    iconPainter = painterResource(id = R.drawable.icon_add_to_cart),
                                    background = MaterialTheme.colorScheme.primary,
                                    isLoading = state.isAddToCartLoading,
                                    onClick = listener::increaseProductCount,
                                )
                            }
                        }
                        HoneyOutlineText(
                            modifier = Modifier.padding(vertical = MaterialTheme.dimens.space8),
                            text = state.totalPriceInCurrency,
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = MaterialTheme.dimens.space16),
                            text = state.product.productDescription,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            ),
                            maxLines = 1,
                        )
                    }
                    SmallProductImages(
                        state = state.smallImages,
                        modifier = Modifier.constrainAs(smallImageProduct) {
                            top.linkTo(imageProduct.bottom)
                            bottom.linkTo(info.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                        onClickImage = { index ->
                            listener.onClickSmallImage(state.smallImages[index])
                        }
                    )
                }
            }
            if (reviews.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.user_reviews),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                items(reviews) { review ->
                    AverageRating(
                        averageRating = state.reviewStatisticUiState.averageRating.toString(),
                        rating = state.reviewStatisticUiState.averageRating.toFloat(),
                        reviewCount = "${state.reviewStatisticUiState.reviewsCount} Ratings"
                    )
                    CardReviews(
                        userName = review.fullName ?: " ",
                        reviews = review.content,
                        data = review.reviewDate.toString(),
                        rating = review.rating.toFloat()
                    )
                }
                item {
                    ItemLabel(
                        label = "See All",
                        iconPainter = painterResource(id = R.drawable.ic_seall),
                        onClick = {
                            state.product.productId.let {
                                listener.onClickSeeAllReviews(
                                    it,
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewScreen() {
    ProductDetailsScreen()
}