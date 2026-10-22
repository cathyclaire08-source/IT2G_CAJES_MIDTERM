package com.scc.model;

public record TrainingSessionEntity(int id,
                                    String clientName,
                                    String trainerName,
                                    double sessionFee,
                                    String status) {
}