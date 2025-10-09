package com.duoc.rotompedia

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

//Url de la base POKEAPI
private const val BASE_URL = "https://pokeapi.co/api/v2/"


//Interfaz retrofit
interface PokeApiService {
    @GET("pokemon/{id}")
    suspend fun getPokemon(
        @Path("id") pokemonId: Int
    ): PokemonData
}

//Objeto singleton que crea y mantiene una unica instancia
object PokemonApi{
    //1 inicializa el moshi
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    //2 inicializa la instancia usando la url base y el moshi como translate
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    //3 crea la instancia del servgicio que usara la app
    val service: PokeApiService by lazy {
        retrofit.create(PokeApiService::class.java)
    }
}