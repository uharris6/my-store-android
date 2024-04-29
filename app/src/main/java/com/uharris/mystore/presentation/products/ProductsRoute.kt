package com.uharris.mystore.presentation.products

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.uharris.mystore.navigation.NavRoute

object ProductsRoute: NavRoute<ProductsViewModel> {
    override val route: String
        get() = "products"

    @Composable
    override fun viewModel(): ProductsViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: ProductsViewModel) {
    }
}