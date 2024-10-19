package com.example.a2;

public class Task {
    private final String description;
    private final String assignee;

    public Task(String description, String assignee) {
        this.description = description;
        this.assignee = assignee;
    }

    public String getDescription() {
        return description;
    }

    public String getAssignee() {
        return assignee;
    }
}