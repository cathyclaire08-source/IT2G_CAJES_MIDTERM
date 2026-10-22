package com.scc.dao;

import com.scc.model.TrainingSessionEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrainingSessionDao {

    private static final String INSERT_SQL =
            "INSERT INTO training_sessions (client_name, trainer_name, session_fee, status) VALUES (?, ?, ?, ?)";

    private static final String SELECT_ALL_SQL =
            "SELECT id, client_name, trainer_name, session_fee, status FROM training_sessions ORDER BY id";

    public TrainingSessionDao() throws SQLException {
        DatabaseConnection.initializeDatabase();
    }

    public boolean insert(TrainingSessionEntity entity) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            ps.setString(1, entity.clientName());
            ps.setString(2, entity.trainerName());
            ps.setDouble(3, entity.sessionFee());
            ps.setString(4, entity.status());
            return ps.executeUpdate() > 0;
        }
    }

    public List<TrainingSessionEntity> findAll() throws SQLException {
        List<TrainingSessionEntity> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new TrainingSessionEntity(
                        rs.getInt("id"),
                        rs.getString("client_name"),
                        rs.getString("trainer_name"),
                        rs.getDouble("session_fee"),
                        rs.getString("status")));
            }
        }
        return list;
    }
}