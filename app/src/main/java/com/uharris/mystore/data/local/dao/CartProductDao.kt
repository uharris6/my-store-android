package com.uharris.mystore.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.uharris.mystore.data.local.entity.CartProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface CartProductDao {
    @Query("SELECT * FROM CartProduct")
    fun getAllCartProducts(): Flow<List<CartProduct>>

    @Query("SELECT * FROM CartProduct WHERE id = :id")
    fun getCartProduct(id: Long): Flow<CartProduct?>

    @Insert
    suspend fun insertCartProduct(cartProduct: CartProduct)

    @Update
    suspend fun updateCartProduct(cartProduct: CartProduct)

    @Delete
    suspend fun deleteCartProduct(cartProduct: CartProduct)
}