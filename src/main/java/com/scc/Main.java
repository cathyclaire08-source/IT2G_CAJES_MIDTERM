package com.scc;

import com.scc.dao.TrainingSessionDao;
import com.scc.ui.TrainingSessionMenu;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            TrainingSessionDao dao = new TrainingSessionDao();
            new TrainingSessionMenu(dao).run();
        } catch (SQLException e) {
            System.out.println("Failed to start the system: " + e.getMessage());
        }
    }
}