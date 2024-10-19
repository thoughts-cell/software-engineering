package com.example.a2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


/*
 * Controller for the Disaster Report form.
 */
public class DisasterReportController {
    private static final String REPORT_FILE = "disaster_reports.txt";


    @FXML
    private Button weather;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private TextField locationInput;
    @FXML
    private ComboBox<String> severityComboBox;
    @FXML
    private TextArea descriptionInput;
    @FXML
    private Button coord;
    @FXML
    private Button assess;

    @FXML

    private void toAssesssment() {
        Stage stage = (Stage) assess.getScene().getWindow();
        Navigation.switchTo("DisasterAssessment.fxml", stage);
    }

    @FXML
    public void handleSubmitButtonAction() {
        String type = typeComboBox.getValue();
        String location = locationInput.getText();
        String severity = severityComboBox.getValue();
        String description = descriptionInput.getText();
        if (type == null || location.isEmpty() || severity == null || description.isEmpty()) {
            showAlert("Error,all fields are required.");
            return;
        }
        saveReport(type, location, severity, description);
        clearFields();
    }


    /*
     * Saves the report to the file.
     * @param type the type of disaster
     * @param location the location of the disaster
     * @param severity the severity of the disaster
     * @param description a description of the disaster

     */

    public void saveReport(String type, String location, String severity, String description) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(REPORT_FILE, true))) {
            writer.write(type + "," + location + "," + severity + "," + description + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to save the report.");
        }


    }

    private void clearFields() {
        typeComboBox.setValue(null);
        locationInput.clear();
        severityComboBox.setValue(null);
        descriptionInput.clear();
    }

    private void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void toCoordination() {
        Stage stage = (Stage) coord.getScene().getWindow();
        Navigation.switchTo("ResponseCoordination.fxml", stage);
    }

    public void toWeather(ActionEvent actionEvent) {
        Stage stage = (Stage) weather.getScene().getWindow();
        Navigation.switchTo("WeatherAlert.fxml", stage);
    }

    /**
     * Navigates to the specified FXML file.
     */

    static class Navigation {

        public static void switchTo(String name, Stage stage) {
            DisasterAssessmentController.Navigation.redirectTo(name, stage);
        }
    }
}
