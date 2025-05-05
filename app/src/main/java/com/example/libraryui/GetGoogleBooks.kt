package com.example.libraryui

import retrofit2.http.GET
import retrofit2.http.Query

interface GetGoogleBooks {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 20,
        @Query("fields") fields: String = "items(id,volumeInfo/title,volumeInfo/authors,volumeInfo/pageCount)"
    ): GoogleBooksResponse
}