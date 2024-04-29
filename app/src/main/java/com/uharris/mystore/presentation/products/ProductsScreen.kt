package com.uharris.mystore.presentation.products

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
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
import kotlinx.coroutines.launch

@Composable
fun ProductsScreenStateful(
    viewModel: ProductsViewModel,
    drawerState: DrawerState,
    category: String
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(category) {
        viewModel.onLoad(category)
    }

    ProductsScreen(
        drawerState = drawerState,
        state = state,
        onEvent = viewModel::sendEvent
    )
}

@Composable
fun ProductsScreen(
    drawerState: DrawerState,
    state: ProductsState,
    onEvent: (ProductsEvent) -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R.string.app_name),
                icon = Icons.Filled.Menu,
                contentDescription = "Menu",
                onClick = { coroutineScope.launch { drawerState.open() } },
                actions = {
                    Box {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black
                            ),
                            onClick = { onEvent(ProductsEvent.OnCartClick) }
                        ) {
                            Text(
                                text = stringResource(id = R.string.cart),
                                style = Typography.labelMedium
                            )
                        }
                        if (state.amountInCart > 0) {
                            Badge(
                                modifier = Modifier
                                    .border(1.dp, color = Color.White, shape = CircleShape)
                                    .align(Alignment.TopEnd)
                                    .clip(CircleShape)
                            ) {
                                Text(text = state.amountInCart.toString())
                            }
                        }
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                if (state.products.isNotEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        item(
                            key = state.products.first().id,
                            span = { GridItemSpan(maxLineSpan) }
                        ) {
                            val product = state.products.first()
                            FeaturedProduct(
                                context = context,
                                product = product,
                                onClick = {
                                    onEvent(ProductsEvent.OnProductClick(product.id))
                                },
                                onAddProductClick = {
                                    onEvent(ProductsEvent.OnAddProductClick(product))
                                },
                                onDecreaseClick = {
                                    onEvent(
                                        ProductsEvent.OnDecreaseAmountInCart(
                                            product.copy(amountInCart = product.amountInCart - 1)
                                        )
                                    )
                                },
                                onIncreaseClick = {
                                    onEvent(
                                        ProductsEvent.OnIncreaseAmountInCart(
                                            product.copy(amountInCart = product.amountInCart + 1)
                                        )
                                    )
                                }
                            )
                        }
                        items(
                            state.products.subList(1, state.products.size),
                            key = { it.id }
                        ) { product ->
                            ProductGridItem(
                                context = context,
                                product = product,
                                onClick = {
                                    onEvent(ProductsEvent.OnProductClick(product.id))
                                },
                                onAddProductClick = {
                                    onEvent(ProductsEvent.OnAddProductClick(product))
                                },
                                onDecreaseClick = {
                                    onEvent(
                                        ProductsEvent.OnDecreaseAmountInCart(
                                            product.copy(amountInCart = product.amountInCart - 1)
                                        )
                                    )
                                },
                                onIncreaseClick = {
                                    onEvent(
                                        ProductsEvent.OnIncreaseAmountInCart(
                                            product.copy(amountInCart = product.amountInCart + 1)
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeaturedProduct(
    context: Context,
    product: Product,
    onClick: () -> Unit,
    onAddProductClick: () -> Unit,
    onIncreaseClick: () -> Unit,
    onDecreaseClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clickable {
                onClick()
            }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(product.image)
                        .dispatcher(Dispatchers.IO)
                        .memoryCacheKey(product.image)
                        .diskCacheKey(product.image)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.featured),
                        style = Typography.titleMedium
                    )
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = product.title,
                        style = Typography.labelLarge
                    )
                    Text(
                        text = product.price.toCurrency(),
                        style = Typography.labelMedium
                    )
                }
            }
            AddProductButton(
                amountInCart = product.amountInCart,
                isInCart = product.isInCart,
                onClick = onAddProductClick,
                onIncreaseClick = onIncreaseClick,
                onDecreaseClick = onDecreaseClick,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun ProductGridItem(
    context: Context,
    product: Product,
    onClick: () -> Unit,
    onAddProductClick: () -> Unit,
    onIncreaseClick: () -> Unit,
    onDecreaseClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .height(250.dp)
            .clickable {
                onClick()
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(product.image)
                    .dispatcher(Dispatchers.IO)
                    .memoryCacheKey(product.image)
                    .diskCacheKey(product.image)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = product.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
            )
            Text(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                text = product.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = Typography.labelLarge
            )
            Text(
                text = product.price.toCurrency(),
                style = Typography.labelMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            AddProductButton(
                amountInCart = product.amountInCart,
                isInCart = product.isInCart,
                onClick = onAddProductClick,
                onIncreaseClick = onIncreaseClick,
                onDecreaseClick = onDecreaseClick,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Preview
@Composable
fun ProductsScreenPreview() {
    MyStoreTheme {
        ProductsScreen(
            drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
            state = ProductsState.initial().copy(
                isLoading = false,
                products = listOf(
                    Product(
                        id = 1,
                        title = "Title 1",
                        description = "Description 1",
                        image = "",
                        rate = 3.5,
                        price = 1990.99,
                        isInCart = true,
                        amountInCart = 1
                    ),
                    Product(
                        id = 2,
                        title = "Title 2",
                        description = "Description 2",
                        image = "",
                        rate = 3.9,
                        price = 299.99,
                        isInCart = true,
                        amountInCart = 1
                    ),
                    Product(
                        id = 3,
                        title = "Title 3",
                        description = "Description 1",
                        image = "",
                        rate = 3.5,
                        price = 1990.99
                    )
                ),
            ),
            onEvent = {}
        )
    }
}