package com.example.temp.DAO;

import com.example.temp.Models.Admin;

public class AdminDAO {
    public Admin getAdminById(int id) {
        // giả lập Admin có id = 999999
        if (id == 999999) {
            return new Admin(999999, "Administrator", "123456", "admin@example.com");
        }
        return null;
    }

}
