package com.example.a2;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a disaster report.
 */

public record DisasterReport(String type, String location, String severity, String description) {

    public StringProperty typeProperty() {
        return new SimpleStringProperty(type);
    }

    public StringProperty locationProperty() {
        return new SimpleStringProperty(location);
    }

    public StringProperty severityProperty() {
        return new SimpleStringProperty(severity);
    }

    public StringProperty descriptionProperty() {
        return new SimpleStringProperty(description);
    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getSeverity() {
        return severity;
    }
}
