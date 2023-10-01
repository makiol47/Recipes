package com.Recipes.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class Recipe {

    public static void main(String[] args) {
        SpringApplication.run(Recipe.class, args);
        UserService userService = new UserService();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Hi, login or register to continue");

        boolean isLoggedIn = false;
        User loggedInUser = null;

        do {
            System.out.println("1. Register\n" +
                    "2. Login");
            if (isLoggedIn) {
                System.out.println("3. Add a recipe\n" +
                        "4. Show recipes\n" +
                        "5. Quit");
            } else {
                System.out.println("5. Exit");
            }
            System.out.println("Choice:");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    User user = new User();
                    System.out.println("Enter your username:");
                    String username = scanner.nextLine();
                    System.out.println("Enter your password:");
                    String password = scanner.nextLine();
                    user.setName(username);
                    user.setPassword(password);
                    userService.userRegister(user);
                    if (!"User successfully registered".equals(userService.getStatus())) {
                        System.out.println(userService.getStatus());
                    }
                    break;
                case 2:
                    User loginUser = new User();
                    System.out.println("Enter your username:");
                    String loginUsername = scanner.nextLine();
                    System.out.println("Enter your password:");
                    String loginPassword = scanner.nextLine();
                    loginUser.setName(loginUsername);
                    loginUser.setPassword(loginPassword);
                    if (userService.userLogin(loginUser)) {
                        isLoggedIn = true;
                        loggedInUser = loginUser;
                        System.out.println("Login successful");
                    } else {
                        System.out.println("Invalid username or password, please try again");
                    }
                    break;
                case 3:
                    if (isLoggedIn) {
                        User recipe = new User();
                        System.out.println("Enter the title of the recipe:");
                        String title = scanner.nextLine();
                        System.out.println("Enter the ingredients:");
                        String ingredients = scanner.nextLine();
                        System.out.println("Enter the instructions:");
                        String instructions = scanner.nextLine();
                        System.out.println("Enter the total time (in minutes):");
                        int totalTime = scanner.nextInt();
                        scanner.nextLine();

                        recipe.setTitle(title);
                        recipe.setIngredients(ingredients);
                        recipe.setInstructions(instructions);
                        recipe.setTotalTime(totalTime);
                        userService.createRecipe(recipe);

                        System.out.println(userService.getStatus());
                    } else {
                        System.out.println("Invalid choice. Please select a valid option.");
                    }
                    break;
                case 4:
                    if (isLoggedIn) {
                        List<User> recipes = userService.getAllRecipes();
                        if (!recipes.isEmpty()) {
                            System.out.println("Recipes:");
                            for (User recipe : recipes) {
                                System.out.println("Title: " + recipe.getTitle());
                                System.out.println("Ingredients: " + recipe.getIngredients());
                                System.out.println("Instructions: " + recipe.getInstructions());
                                System.out.println("Total Time: " + recipe.getTotalTime() + " minutes");
                                System.out.println("--------------------------");
                            }
                        } else {
                            System.out.println("No recipes available.");
                        }
                    } else {
                        System.out.println("Invalid choice. Please select a valid option.");
                    }
                    break;
                case 5:
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
        } while (true);
    }
}

