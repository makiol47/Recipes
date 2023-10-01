package com.Recipes.demo;

import org.springframework.stereotype.Service;
import java.sql.*;
import java.util.*;

@Service
public class UserService {

    User user = new User();


    private static final String INSERT_QUERY = "INSERT INTO Users(username, password) VALUES (?, ?)";
    private static final String SELECT_QUERY = "SELECT * FROM Users WHERE username = ? AND password = ?";
    private static final String INSERT_RECIPE_QUERY = "INSERT INTO recipes (tittle, ingredients, instructions, total_time, created_at) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_RECIPE_QUERY = "UPDATE recipes SET title = ?, ingredients = ?, instructions = ?, total_time = ? WHERE id = ?";
    private static final String DELETE_RECIPE_QUERY = "DELETE FROM recipes WHERE id = ?";
    private static final String SELECT_RECIPE_QUERY = "SELECT * FROM recipes WHERE id = ?";
    private String status;

    public void createRecipe(User user) {
        try (Connection con = DBConnec.createDBConnection();
             PreparedStatement pstm = con.prepareStatement(INSERT_RECIPE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, user.getTitle());
            pstm.setString(2, user.getIngredients());
            pstm.setString(3, user.getInstructions());
            pstm.setInt(4, user.getTotalTime());
            pstm.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            int cnt = pstm.executeUpdate();
            if (cnt != 0) {
                status = "Recipe created successfully";
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateRecipe(User user) {
        try (Connection con = DBConnec.createDBConnection();
             PreparedStatement pstm = con.prepareStatement(UPDATE_RECIPE_QUERY)) {
            pstm.setString(1, user.getTitle());
            pstm.setString(2, user.getIngredients());
            pstm.setString(3, user.getInstructions());
            pstm.setInt(4, user.getTotalTime());
            pstm.setInt(5, user.getId());
            int cnt = pstm.executeUpdate();
            if (cnt != 0) {
                status = "Recipe updated successfully";
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteRecipe(int recipeId) {
        try (Connection con = DBConnec.createDBConnection();
             PreparedStatement pstm = con.prepareStatement(DELETE_RECIPE_QUERY)) {
            pstm.setInt(1, recipeId);
            int cnt = pstm.executeUpdate();
            if (cnt != 0) {
                status = "Recipe deleted successfully";
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public User getRecipe(int recipeId) {
        try (Connection con = DBConnec.createDBConnection();
             PreparedStatement pstm = con.prepareStatement(SELECT_RECIPE_QUERY)) {
            pstm.setInt(1, recipeId);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                User recipe = new User();
                recipe.setId(rs.getInt("id"));
                recipe.setTitle(rs.getString("title"));
                recipe.setIngredients(rs.getString("ingredients"));
                recipe.setInstructions(rs.getString("instructions"));
                recipe.setTotalTime(rs.getInt("total_time"));
                recipe.setCreatedAt(rs.getTimestamp("created_at"));
                return recipe;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<User> getAllRecipes() {
        List<User> recipes = new ArrayList<>();
        try (Connection con = DBConnec.createDBConnection();
             PreparedStatement pstm = con.prepareStatement("SELECT * FROM recipes");
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                User recipe = new User();
                recipe.setId(rs.getInt("id"));
                recipe.setTitle(rs.getString("title"));
                recipe.setIngredients(rs.getString("ingredients"));
                recipe.setInstructions(rs.getString("instructions"));
                recipe.setTotalTime(rs.getInt("total_time"));
                recipe.setCreatedAt(rs.getTimestamp("created_at"));
                recipes.add(recipe);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return recipes;
    }


    public void userRegister(User user) {
        if (isUsernameTaken(user.getName())) {
            status = "Username is already taken. Please choose a different username.";
        } else {
            try (Connection con = DBConnec.createDBConnection();
                 PreparedStatement pstm = con.prepareStatement(INSERT_QUERY)) {
                pstm.setString(1, user.getName());
                pstm.setString(2, user.getPassword());
                int cnt = pstm.executeUpdate();
                if (cnt != 0)
                    status = "User successfully registered";
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean userLogin(User user) {
        try (Connection con = DBConnec.createDBConnection();
             PreparedStatement pstm = con.prepareStatement(SELECT_QUERY)) {
            pstm.setString(1, user.getName());
            pstm.setString(2, user.getPassword());
            ResultSet rs = pstm.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean isUsernameTaken(String username) {
        try (Connection con = DBConnec.createDBConnection();
             PreparedStatement pstm = con.prepareStatement("SELECT * FROM Users WHERE username = ?")) {
            pstm.setString(1, username);
            ResultSet rs = pstm.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }


    public String getStatus() {
        return status;
    }
}