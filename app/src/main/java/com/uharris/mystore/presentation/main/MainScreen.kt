package com.uharris.mystore.presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.uharris.mystore.R
import com.uharris.mystore.presentation.cart.CartRoute
import com.uharris.mystore.presentation.cart.CartRoute.composable
import com.uharris.mystore.presentation.detail.ProductDetailRoute
import com.uharris.mystore.presentation.detail.ProductDetailRoute.composable
import com.uharris.mystore.presentation.products.ProductsRoute
import com.uharris.mystore.presentation.products.ProductsRoute.composable
import com.uharris.mystore.presentation.products.ProductsScreenStateful
import com.uharris.mystore.ui.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    state: MainState,
    onEvent: (MainEvent) -> Unit,
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                CategoriesList(
                    drawerState = drawerState,
                    categories = state.categories,
                    categorySelected = state.categorySelected.orEmpty(),
                    onEvent = onEvent
                )
            }
        },
    ) {
        NavHost(navController = navController, startDestination = ProductsRoute.route) {
            ProductsRoute.composable(this, navController) {
                ProductsScreenStateful(it, drawerState, state.categorySelected.orEmpty())
            }
            ProductDetailRoute.composable(this, navController)
            CartRoute.composable(this, navController)
        }
    }
}

@Composable
fun CategoriesList(
    drawerState: DrawerState,
    categories: List<String>,
    categorySelected: String,
    onEvent: (MainEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.categories),
            style = Typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn {
            item {
                CategoryItem(
                    drawerState = drawerState,
                    category = stringResource(id = R.string.all),
                    selected = categorySelected.lowercase() == stringResource(id = R.string.all).lowercase(),
                    onEvent = onEvent,
                )
            }
            items(categories, key = { it }) {
                CategoryItem(
                    drawerState = drawerState,
                    category = it,
                    selected = categorySelected.lowercase() == it.lowercase(),
                    onEvent = onEvent,
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    drawerState: DrawerState,
    category: String,
    selected: Boolean,
    onEvent: (MainEvent) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    NavigationDrawerItem(
        label = {
            Text(
                text = category,
                style = Typography.bodyMedium
            )
        },
        selected = selected,
        onClick = {
            if (selected) {
                coroutineScope.launch {
                    drawerState.close()
                }
                return@NavigationDrawerItem
            }
            onEvent(MainEvent.OnCategoryClick(category))
            coroutineScope.launch {
                drawerState.close()
            }
        }
    )
}