package com.uharris.mystore.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.uharris.mystore.R
import com.uharris.mystore.domain.model.Product
import com.uharris.mystore.ui.components.AddProductButton
import com.uharris.mystore.ui.components.TopBar
import com.uharris.mystore.ui.theme.MyStoreTheme
import com.uharris.mystore.ui.theme.Typography
import com.uharris.mystore.utils.toCurrency
import kotlinx.coroutines.Dispatchers

@Composable
fun ProductDetailScreenStateful(
    viewModel: ProductDetailViewModel
) {
    val state by viewModel.state.collectAsState()

    ProductDetailScreen(state = state, onEvent = viewModel::sendEvent)
}

@Composable
fun ProductDetailScreen(
    state: ProductDetailState,
    onEvent: (ProductDetailEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R.string.detail),
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Atrás",
                onClick = { onEvent(ProductDetailEvent.OnBackClick) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(state.product?.image)
                        .dispatcher(Dispatchers.IO)
                        .memoryCacheKey(state.product?.image)
                        .diskCacheKey(state.product?.image)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = state.product?.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = state.product?.title.orEmpty(),
                        style = Typography.titleLarge
                    )
                    Text(
                        text = state.product?.price?.toCurrency().orEmpty(),
                        style = Typography.labelMedium
                    )
                }
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = state.product?.description.orEmpty(),
                    style = Typography.bodyMedium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = state.product?.rate.toString())
                    AddProductButton(
                        amountInCart = state.product?.amountInCart ?: 0,
                        isInCart = state.product?.isInCart ?: false,
                        onClick = {
                            onEvent(ProductDetailEvent.OnAddProductClick)
                        },
                        onDecreaseClick = {
                            state.product?.let {
                                onEvent(
                                    ProductDetailEvent.OnDecreaseAmountInCart(
                                        it.copy(
                                            amountInCart = it.amountInCart - 1
                                        )
                                    )
                                )
                            }
                        },
                        onIncreaseClick = {
                            state.product?.let {
                                onEvent(
                                    ProductDetailEvent.OnIncreaseAmountInCart(
                                        it.copy(
                                            amountInCart = it.amountInCart + 1
                                        )
                                    )
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ProductDetailScreenPreview() {
    MyStoreTheme {
        ProductDetailScreen(
            state = ProductDetailState.initial().copy(
                isLoading = false,
                product = Product(
                    id = 1,
                    title = "Title 1",
                    description = "Description 1",
                    image = "",
                    rate = 3.5,
                    price = 1990.99,
                    amountInCart = 1,
                    isInCart = true
                )
            ),
            onEvent = {}
        )
    }
}