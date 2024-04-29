package com.uharris.mystore.presentation.cart

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.uharris.mystore.navigation.NavRoute

object CartRoute: NavRoute<CartViewModel> {
    override val route: String
        get() = CART_ROUTE

    @Composable
    override fun viewModel(): CartViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: CartViewModel) {
        CartScreenStateful(viewModel = viewModel)
    }
}

private const val CART_ROUTE = "cart"

