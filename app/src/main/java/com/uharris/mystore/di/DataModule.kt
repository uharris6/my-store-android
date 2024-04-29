package com.uharris.mystore.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.uharris.mystore.data.local.dao.CartProductDao
import com.uharris.mystore.data.local.database.MyStoreDatabase
import com.uharris.mystore.data.remote.api.StoreApi
import com.uharris.mystore.utils.AppExecutor
import com.uharris.mystore.utils.Executor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun providesHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun providesRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun providesRecipesService(retrofit: Retrofit): StoreApi {
        return retrofit.create(StoreApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMyStoreDatabase(
        @ApplicationContext app: Context
    ): MyStoreDatabase = Room.databaseBuilder(
        app,
        MyStoreDatabase::class.java,
        "my_store_database"
    ).build()

    @Provides
    @Singleton
    fun provideCartProductDao(
        db: MyStoreDatabase
    ): CartProductDao = db.cartProductDao()

    @Provides
    @Singleton
    fun providesExecutor(): Executor {
        return AppExecutor()
    }
}