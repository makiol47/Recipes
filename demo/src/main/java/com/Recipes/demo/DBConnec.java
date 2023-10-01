package com.Recipes.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnec {

    static Connection con;

    public static Connection createDBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/recipes";
            String username = "root";
            String password = "Root54321";

            con = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException ex) {
            System.err.println("Nie znaleziono klasy sterownika JDBC.");
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.err.println("Błąd podczas nawiązywania połączenia z bazą danych.");
            ex.printStackTrace();
        }
        return con;
    }

    public static void closeConnection(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            System.err.println("Błąd podczas zamykania połączenia.");
            ex.printStackTrace();
        }
    }
}