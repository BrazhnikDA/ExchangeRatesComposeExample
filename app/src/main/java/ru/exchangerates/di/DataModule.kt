package ru.exchangerates.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import ru.exchangerates.data.remote.ExchangeRatesApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    fun provideExchangeRatesApi(): ExchangeRatesApi {
        return Retrofit.Builder()
            .baseUrl(ExchangeRatesApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }
}