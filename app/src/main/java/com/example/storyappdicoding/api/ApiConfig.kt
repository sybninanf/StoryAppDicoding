package com.example.storyappdicoding.api

import android.content.Context
import androidx.room.Room
import com.example.storyappdicoding.BuildConfig
import com.example.storyappdicoding.api.db.StoryDb
import com.example.storyappdicoding.api.models.UserPreference
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
object ApiConfig {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit
    }

    @Provides
    @Singleton
    fun providesApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): UserPreference =
        UserPreference(context)

    @Provides
    @Singleton
    fun provideStoryDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context, StoryDb::class.java, "db_story"
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideStoryDao(db: StoryDb) = db.storyDao()

    @Provides
    @Singleton
    fun provideRemoteKeysDao(db: StoryDb) = db.remoteKeysDao()
}