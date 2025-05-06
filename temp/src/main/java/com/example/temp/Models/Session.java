// com.example.temp.Models.Session.java
package com.example.temp.Models;

public class Session {
    public static boolean isAdmin = false;
    public static int userId = -1;
    public static String userName = "";
    public static String userRole = ""; // "admin" hoặc "employee"

    public static void clear() {
        isAdmin = false;
        userId = -1;
        userName = "";
        userRole = "";
    }
}
