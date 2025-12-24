package com.example.products.di

import com.example.core.domain.dataSource.UserPreferences
import com.example.products.data.dataSource.CountryAProductsDataSourceImpl
import com.example.products.data.dataSource.CountryBProductsDataSourceImpl
import com.example.products.data.dataSource.ProductsDataSourceFactoryImpl
import com.example.products.data.dataSource.PurchaseRemoteDataSourceImpl
import com.example.products.data.remote.api.FakeStoreApi
import com.example.products.data.remote.api.PlatziFakeStoreApi
import com.example.products.data.repository.ProductsRepositoryImpl
import com.example.products.data.repository.PurchaseRepositoryImpl
import com.example.products.domain.dataSource.ProductsDataSourceFactory
import com.example.products.domain.dataSource.PurchaseRemoteDataSource
import com.example.products.domain.repository.ProductsRepository
import com.example.products.domain.repository.PurchaseRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FakeStoreRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PlatziRetrofit

@Module
@InstallIn(SingletonComponent::class)
object ProductsModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

    @Provides
    @Singleton
    @FakeStoreRetrofit
    fun provideFakeStoreRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Provides
    @Singleton
    @PlatziRetrofit
    fun providePlatziRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.escuelajs.co/api/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideFakeStoreApi(
        @FakeStoreRetrofit retrofit: Retrofit
    ): FakeStoreApi =
        retrofit.create(FakeStoreApi::class.java)

    @Provides
    @Singleton
    fun providePlatziFakeStoreApi(
        @PlatziRetrofit retrofit: Retrofit
    ): PlatziFakeStoreApi =
        retrofit.create(PlatziFakeStoreApi::class.java)

    @Provides
    @Singleton
    fun providesCountryADataSource(api: FakeStoreApi): CountryAProductsDataSourceImpl =
        CountryAProductsDataSourceImpl(api)

    @Provides
    @Singleton
    fun providesCountryBDataSource(api: PlatziFakeStoreApi): CountryBProductsDataSourceImpl =
        CountryBProductsDataSourceImpl(api)

    @Provides
    @Singleton
    fun providesProductsDataSourceFactory(countryADataSource: CountryAProductsDataSourceImpl, countryBDataSource: CountryBProductsDataSourceImpl): ProductsDataSourceFactory =
        ProductsDataSourceFactoryImpl(countryADataSource, countryBDataSource)


    @Provides
    @Singleton
    fun providesRepository(factory: ProductsDataSourceFactory, userPreferences: UserPreferences): ProductsRepository =
        ProductsRepositoryImpl(factory, userPreferences)

    @Provides
    @Singleton
    fun providesFirestore(): FirebaseFirestore =
        FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun providesPurchaseRemoteDataSource(firestore: FirebaseFirestore): PurchaseRemoteDataSource =
        PurchaseRemoteDataSourceImpl(firestore)

    @Provides
    @Singleton
    fun providesPurchaseRepository(purchaseRemoteDataSource: PurchaseRemoteDataSource): PurchaseRepository =
        PurchaseRepositoryImpl(purchaseRemoteDataSource)

}