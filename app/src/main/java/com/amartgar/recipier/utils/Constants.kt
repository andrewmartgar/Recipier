package com.amartgar.recipier.utils

object Constants {

    const val RECIPE_TYPE: String = "RecipeType"
    const val RECIPE_CATEGORY: String = "RecipeCategory"
    const val RECIPE_COOKING_TIME: String = "RecipeCookingTime"
    const val RECIPE_IMAGE_SOURCE_LOCAL: String = "Local"
    const val RECIPE_IMAGE_SOURCE_ONLINE: String = "Online"
    const val RECIPE_EXTRA_DETAILS = "RecipeDetails"
    const val ALL_ITEMS: String = "All"
    const val FILTER_SELECTION: String = "FilterSelection"

    //Info for the API interface RandomRecipeAPI
    const val API_BASE_URL: String = "https://api.spoonacular.com/"
    const val API_ENDPOINT: String = "recipes/random"
    const val API_KEY: String = "apiKey"
    const val API_KEY_VALUE: String = "77c6e786ee4e4f17aeba64e581d461ec"
    const val API_LIMIT_LICENSE: String = "limitLicense"
    const val API_LIMIT_LICENSE_VALUE: Boolean = true
    const val API_TAGS: String = "tags"
    const val API_TAGS_VALUE: String = "vegetarian"
    const val API_NUMBER: String = "number"
    const val API_NUMBER_VALUE: Int = 1

    //Notifications
    const val NOTIFICATION_ID = "Recipier_notification_id"
    const val NOTIFICATION_NAME = "Recipier"
    const val NOTIFICATION_CHANNEL = "Recipier_channel_01"

    fun recipeTypes(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("breakfast")
        list.add("main course")
        list.add("side dish")
        list.add("salad")
        list.add("appetizer")
        list.add("dessert")
        list.add("snack")
        list.add("drink")
        list.add("bread")
        list.add("soup")
        list.add("beverage")
        list.add("sauce")
        list.add("marinade")
        list.add("fingerfood")
        list.add("other")

        return list
    }

    fun recipeCategories(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("Originally created")
        list.add("African")
        list.add("American")
        list.add("British")
        list.add("Cajun")
        list.add("Caribbean")
        list.add("Chinese")
        list.add("Eastern European")
        list.add("European")
        list.add("French")
        list.add("German")
        list.add("Polish")
        list.add("Greek")
        list.add("Indian")
        list.add("Irish")
        list.add("Italian")
        list.add("Japanese")
        list.add("Jewish")
        list.add("Korean")
        list.add("Latin America")
        list.add("Mediterranean")
        list.add("Mexican")
        list.add("Middle Eastern")
        list.add("Nordic")
        list.add("Southern")
        list.add("Spanish")
        list.add("Thai")
        list.add("Vietnamese")
        list.add("Other")

        return list
    }

    fun recipeCookingTime(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("10-15 min")
        list.add("15-20 min")
        list.add("20-25 min")
        list.add("25-30 min")
        list.add("30-40 min")
        list.add("40-50 min")
        list.add("50-60 min")
        list.add("1 hour")
        list.add("1 to 1.5 hours")
        list.add("1.5 to 2 hours")
        list.add("2 to 3 hours")
        list.add("more than 3 hours")

        return list
    }
}