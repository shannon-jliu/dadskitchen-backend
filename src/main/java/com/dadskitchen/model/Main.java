package com.dadskitchen.model;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        System.out.println("Dads kitchen");



        /*

        necessary databases:
            database for:
                recipe object, rating object

            database for inventory

            database for:
                users, ??

        Chef side:

                Recipe:
                category : bfast, l, din
                has the recipe as string
                - ingredients
                - key notes
                has an image of the dish
                has a rating, stars and commenting
                last made
                frequency made

                Inventory:
                has a list of ingredients, the availability, last used

                User/eater database:
                name, highest rated food, frequency, foods eaten w recommender

                Make the dish:
                ensures you have enough ingredients from the inventory

                User side:

                Rate the dish; stars and commenting
                Recommend the dish to other users

        */

        Chef chef = new Chef("Dad");

        chef.getInventory().addIngredient(new Ingredient("eggs", 6, "units"));
        chef.getInventory().addIngredient(new Ingredient("milk", 2, "cups"));
        chef.getInventory().addIngredient(new Ingredient("flour", 3, "cups"));

        System.out.println("inventory: " + chef.getInventory().items);

        ArrayList<Ingredient> pancakeIngredients = new ArrayList<>();
        pancakeIngredients.add(new Ingredient("eggs", 2, "units"));
        pancakeIngredients.add(new Ingredient("milk", 1, "cups"));
        pancakeIngredients.add(new Ingredient("flour", 2, "cups"));

        System.out.println("inventory: " + chef.getInventory().items);

        chef.createRecipe("Pancakes", "breakfast", pancakeIngredients, "Mix and cook in pan");

        Recipe pancakes = chef.getCreatedRecipes().get(0);

        if (chef.makeDish(pancakes)) {
            System.out.println("Made: " + pancakes.getName());
        } else {
            System.out.println("Failed to make: " + pancakes.getName());
        }

        chef.makeDish(pancakes);



        User user = new User("Alex");
        user.makeRating(pancakes, 5, "So good!");

        System.out.println("User favorite: " + user.getFavorite().getName());
        System.out.println("Recipe rating: " + pancakes.getAverageStars());

        System.out.println("inventory: " + chef.getInventory().items);



    }
}