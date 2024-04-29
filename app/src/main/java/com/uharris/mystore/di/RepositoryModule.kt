package com.uharris.mystore.di

import com.uharris.mystore.data.local.dao.CartProductDao
import com.uharris.mystore.data.remote.api.StoreApi
import com.uharris.mystore.data.repository.StoreRepositoryImpl
import com.uharris.mystore.domain.repository.StoreRepository
import com.uharris.mystore.navigation.MyRouteNavigator
import com.uharris.mystore.navigation.RouteNavigator
import com.uharris.mystore.utils.Executor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun providesStoreRepository(
        api: StoreApi,
        dao: CartProductDao,
        executor: Executor
    ): StoreRepository = StoreRepositoryImpl(api, dao, executor)


}

@Module
@InstallIn(ViewModelComponent::class)
object RouteModule {
    @Provides
    @ViewModelScoped
    fun bindRouteNavigator(): RouteNavigator = MyRouteNavigator()
}

@Module
@InstallIn(SingletonComponent::class)
object CoroutinesDispatchersModule {
    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainImmediateDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainImmediateDispatcher