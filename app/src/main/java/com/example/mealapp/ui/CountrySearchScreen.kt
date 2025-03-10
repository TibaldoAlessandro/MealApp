package com.example.mealapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mealapp.R
import com.example.mealapp.api.Country
import com.example.mealapp.api.MealApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun CountrySearchScreen(navController: NavHostController) {
    var countries by remember { mutableStateOf<List<Country>>(emptyList()) }

    LaunchedEffect(Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(MealApi::class.java)
        val response = withContext(Dispatchers.IO) { api.getCountries() }
        countries = response.meals
    }

    // Mappa per associare il nome dello stato al file di risorsa
    val flagMap = mapOf(
        "American" to R.drawable.american,
        "British" to R.drawable.british,
        "Canadian" to R.drawable.canadian,
        "Chinese" to R.drawable.chinese,
        "Croatian" to R.drawable.croatian,
        "Dutch" to R.drawable.dutch,
        "Egyptian" to R.drawable.egyptian,
        "Filipino" to R.drawable.filipino,
        "French" to R.drawable.french,
        "Greek" to R.drawable.greek,
        "Indian" to R.drawable.indian,
        "Irish" to R.drawable.irish,
        "Italian" to R.drawable.italian,
        "Jamaican" to R.drawable.jamaican,
        "Japanese" to R.drawable.japanese,
        "Kenyan" to R.drawable.kenyan,
        "Malaysian" to R.drawable.malaysian,
        "Mexican" to R.drawable.mexican,
        "Moroccan" to R.drawable.moroccan,
        "Polish" to R.drawable.polish,
        "Portuguese" to R.drawable.portuguese,
        "Russian" to R.drawable.russian,
        "Spanish" to R.drawable.spanish,
        "Thai" to R.drawable.thai,
        "Tunisian" to R.drawable.tunisian,
        "Turkish" to R.drawable.turkish,
        "Ukrainian" to R.drawable.ukrainian,
        "Uruguayan" to R.drawable.uruguayan,
        "Vietnamese" to R.drawable.vietnamese
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(countries) { country ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        // Naviga alla CountryMealsScreen con il nome della nazione
                        navController.navigate("CountryMeals/${country.strArea}")
                    }
            ) {
                // Bandiera della nazione
                val flagResId = flagMap[country.strArea] ?: R.drawable.flag
                Image(
                    painter = painterResource(id = flagResId),
                    contentDescription = country.strArea,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                // Nome della nazione
                Text(
                    text = country.strArea,
                    modifier = Modifier.alignByBaseline()
                )
            }
        }
    }
}