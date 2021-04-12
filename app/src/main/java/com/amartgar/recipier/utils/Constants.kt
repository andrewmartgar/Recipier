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

    fun recipeTypes(): ArrayList<String>{
        val list = ArrayList<String>()
        list.add("Breakfast")
        list.add("Lunch")
        list.add("Dinner")
        list.add("Dessert")
        list.add("Cocktail")
        list.add("Snack")
        list.add("Bread")
        list.add("Sandwich")
        list.add("Side dish")
        list.add("Other")

        return list
    }

    fun recipeCategories(): ArrayList<String>{
        val list = ArrayList<String>()
        list.add("Originally created")
        list.add("Arabian")
        list.add("Asian")
        list.add("Chinese")
        list.add("Exotic")
        list.add("French")
        list.add("Greek")
        list.add("Indian")
        list.add("Italian")
        list.add("Japanese")
        list.add("Mexican")
        list.add("Polish")
        list.add("South American")
        list.add("Spanish")
        list.add("Thai")
        list.add("Turkish")
        list.add("Other")

        return list
    }

    fun recipeCookingTime(): ArrayList<String>{
        val list = ArrayList<String>()
        list.add("10-15 min")
        list.add("20-25 min")
        list.add("30-35 min")
        list.add("40-45 min")
        list.add("50-55 min")
        list.add("60 min")
        list.add("60 to 90 min")
        list.add("90 to 120 min")
        list.add("120 to 180 min")
        list.add("more than 180 min ")

        return list
    }
}