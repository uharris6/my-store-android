package com.uharris.mystore.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.uharris.mystore.data.local.dao.CartProductDao
import com.uharris.mystore.data.local.entity.CartProduct

@Database(entities = [CartProduct::class], version = 2)
abstract class MyStoreDatabase: RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao
}