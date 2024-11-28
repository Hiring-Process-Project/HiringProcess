package com.example.hiringProcess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class DataBaseConnection {
        private static final String URL = "jdbc:postgresql://localhost:5432/candidate";
        private static final String USER = "postgres";
        private static final String PASSWORD = "Sia6992883310";

        public static Connection connect() {
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connected to the database!");
            } catch (SQLException e) {
                System.out.println("Connection failed!");
                e.printStackTrace();
            }
            return conn;
        }

        public static void main(String[] args) {
            connect();
        }
    }


