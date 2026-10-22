package com.scc.ui;

import com.scc.dao.TrainingSessionDao;
import com.scc.model.TrainingSessionEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class TrainingSessionMenu {

    private final TrainingSessionDao dao;
    private final Scanner scanner = new Scanner(System.in);

    public TrainingSessionMenu(TrainingSessionDao dao) {
        this.dao = dao;
    }

    public void run() {
        int choice = 0;
        while (choice != 3) {
            showMenu();
            System.out.print("Enter choice: ");
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("\nERROR: Please enter a valid number (1-3).\n");
                choice = 0;
                continue;
            }

            switch (choice) {
                case 1 -> addRecord();
                case 2 -> viewRecords();
                case 3 -> System.out.println("\nExiting system... Thank you!");
                default -> System.out.println("\nERROR: Invalid choice. Please select 1, 2, or 3.\n");
            }
        }
        scanner.close();
    }

    private void showMenu() {
        System.out.println("==================================================");
        System.out.println("       FITNESS TRAINER SESSION SCHEDULER");
        System.out.println("==================================================");
        System.out.println("[1] Book Training Session");
        System.out.println("[2] View Booked Sessions");
        System.out.println("[3] Exit System");
        System.out.println("==================================================");
    }

    private void addRecord() {
        System.out.println("\n--- BOOK TRAINING SESSION ---");

        String client = readNonEmpty("Enter Client Name: ");
        String trainer = readNonEmpty("Enter Trainer Name: ");

        double fee;
        while (true) {
            System.out.print("Enter Session Fee: ");
            try {
                fee = Double.parseDouble(scanner.nextLine().trim());
                if (fee < 0) {
                    System.out.println("ERROR: Fee cannot be negative.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Please enter a valid number.");
            }
        }

        String status;
        while (true) {
            System.out.print("Enter Status (SCHEDULED / COMPLETED): ");
            status = scanner.nextLine().trim().toUpperCase();
            if (status.equals("SCHEDULED") || status.equals("COMPLETED")) {
                break;
            }
            System.out.println("ERROR: Status must be SCHEDULED or COMPLETED.");
        }

        try {
            // id = 0 is a placeholder; the database assigns the real ID
            boolean saved = dao.insert(new TrainingSessionEntity(0, client, trainer, fee, status));
            System.out.println(saved
                    ? "\nSUCCESS: Personal training session booked!\n"
                    : "\nERROR: Could not save the session.\n");
        } catch (SQLException e) {
            System.out.println("\nDATABASE ERROR: " + e.getMessage() + "\n");
        }
    }

    private void viewRecords() {
        System.out.println();
        System.out.println("=======================================================================================");
        System.out.println("                              TRAINER SESSION SCHEDULES");
        System.out.println("=======================================================================================");

        try {
            List<TrainingSessionEntity> records = dao.findAll();
            if (records.isEmpty()) {
                System.out.println("No booked sessions yet.\n");
                return;
            }

            String line = "+----+----------------------+----------------------+--------------+----------------+";
            System.out.println(line);
            System.out.printf("| %-2s | %-20s | %-20s | %-12s | %-14s |%n",
                    "ID", "CLIENT NAME", "TRAINER NAME", "SESSION FEE", "STATUS");
            System.out.println(line);

            for (TrainingSessionEntity s : records) {
                System.out.printf("| %-2d | %-20s | %-20s | %-12.2f | %-14s |%n",
                        s.id(), s.clientName(), s.trainerName(), s.sessionFee(), s.status());
            }

            System.out.println(line);
            System.out.println("Total Records Displayed: " + records.size() + "\n");
        } catch (SQLException e) {
            System.out.println("DATABASE ERROR: " + e.getMessage() + "\n");
        }
    }

    private String readNonEmpty(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("ERROR: This field cannot be empty.");
            }
        } while (input.isEmpty());
        return input;
    }
}