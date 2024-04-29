package com.uharris.mystore.presentation.detail

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.uharris.mystore.navigation.NavRoute

object ProductDetailRoute: NavRoute<ProductDetailViewModel> {
    override val route: String
        get() = PRODUCT_DETAIL_ROUTE

    @Composable
    override fun viewModel(): ProductDetailViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: ProductDetailViewModel) {
        ProductDetailScreenStateful(viewModel = viewModel)
    }
}

fun navigateProductDetail(id: Long) =
    ProductDetailRoute.route.replace(
        "{$PRODUCT_DETAIL_ID_PARAM}",
        id.toString()
    )

private const val PRODUCT_DETAIL_ROUTE = "productDetail/{id}"
const val PRODUCT_DETAIL_ID_PARAM = "id"