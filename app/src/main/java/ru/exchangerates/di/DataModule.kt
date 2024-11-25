package ru.exchangerates.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import ru.exchangerates.data.local.DataStoreManagerImpl
import ru.exchangerates.data.local.DataStoreManagerImpl.Companion.DATA_STORE_FILE_NAME
import ru.exchangerates.data.remote.api.CurrencyConversationApi
import ru.exchangerates.data.remote.api.CurrencyConversationApi.Companion.BASE_URL_EXCHANGE_RATE
import ru.exchangerates.data.remote.api.HistoryPeriodApi
import ru.exchangerates.data.remote.api.HistoryPeriodApi.Companion.BASE_URL_CB_RF_SCRIPT
import ru.exchangerates.data.remote.api.LatestRatesApi
import ru.exchangerates.data.remote.api.LatestRatesApi.Companion.BASE_URL_CB_RF_DAILY
import ru.exchangerates.data.repository.ExchangeRepositoryImpl
import ru.exchangerates.domain.helper.DataStoreManager
import ru.exchangerates.domain.repository.CurrencyConversationRepository
import ru.exchangerates.domain.repository.HistoryPeriodRepository
import ru.exchangerates.domain.repository.LatestRatesRepository
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_FILE_NAME)

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    private const val TIME_WAIT_RESPONSE_API = 5000


    @Provides
    @Singleton
    fun provideContext(
        @ApplicationContext context: Context
    ): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideDataStore(
        context: Context
    ): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .connectTimeout(TIME_WAIT_RESPONSE_API.toLong(), TimeUnit.MILLISECONDS)
            .readTimeout(TIME_WAIT_RESPONSE_API.toLong(), TimeUnit.MILLISECONDS)
            .writeTimeout(TIME_WAIT_RESPONSE_API.toLong(), TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    }

    @Provides
    @Singleton
    fun provideLatestRatesApi(
        moshi: Moshi,
        okHttpClient: OkHttpClient,
    ): LatestRatesApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_CB_RF_DAILY)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
            .create(LatestRatesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCurrencyConversationApi(
        moshi: Moshi,
        okHttpClient: OkHttpClient,
    ): CurrencyConversationApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_EXCHANGE_RATE)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
            .create(CurrencyConversationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideHistoryPeriodApi(
        okHttpClient: OkHttpClient,
    ): HistoryPeriodApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_CB_RF_SCRIPT)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(HistoryPeriodApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLatestRatesRepository(
        historyPeriodApi: HistoryPeriodApi,
        currencyConversationApi: CurrencyConversationApi,
        latestRatesApi: LatestRatesApi,
        dataStoreManager: DataStoreManager,
        moshi: Moshi,
    ): LatestRatesRepository {
        return ExchangeRepositoryImpl(
            historyPeriodApi = historyPeriodApi,
            currencyConversationApi = currencyConversationApi,
            latestRatesApi = latestRatesApi,
            dataStoreManager = dataStoreManager,
            moshi = moshi,
        )
    }

    @Provides
    @Singleton
    fun provideCurrencyConversationRepository(
        historyPeriodApi: HistoryPeriodApi,
        currencyConversationApi: CurrencyConversationApi,
        latestRatesApi: LatestRatesApi,
        dataStoreManager: DataStoreManager,
        moshi: Moshi,
    ): CurrencyConversationRepository {
        return ExchangeRepositoryImpl(
            historyPeriodApi = historyPeriodApi,
            currencyConversationApi = currencyConversationApi,
            latestRatesApi = latestRatesApi,
            dataStoreManager = dataStoreManager,
            moshi = moshi,
        )
    }

    @Provides
    @Singleton
    fun provideHistoryPeriodRepository(
        historyPeriodApi: HistoryPeriodApi,
        currencyConversationApi: CurrencyConversationApi,
        latestRatesApi: LatestRatesApi,
        dataStoreManager: DataStoreManager,
        moshi: Moshi,
    ): HistoryPeriodRepository {
        return ExchangeRepositoryImpl(
            historyPeriodApi = historyPeriodApi,
            currencyConversationApi = currencyConversationApi,
            latestRatesApi = latestRatesApi,
            dataStoreManager = dataStoreManager,
            moshi = moshi,
        )
    }

    @Provides
    @Singleton
    fun provideDataStoreManager(
        moshi: Moshi,
        dataStore: DataStore<Preferences>,
    ): DataStoreManager {
        return DataStoreManagerImpl(moshi = moshi, dataStore = dataStore)
    }
}