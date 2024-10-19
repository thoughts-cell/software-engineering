package com.example.a2;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static com.example.a2.DisasterReportController.Navigation.switchTo;

public class DisasterAssessmentController {
    private static final String REPORT_FILE = "disaster_reports.txt";
    @FXML
    private TableView<DisasterReport> disasterTableView;
    @FXML
    private TableColumn<DisasterReport, String> typeColumn;
    @FXML
    private TableColumn<DisasterReport, String> locationColumn;
    @FXML
    private TableColumn<DisasterReport, String> severityColumn;
    @FXML
    private TableColumn<DisasterReport, String> descriptionColumn;
    @FXML
    private Label assessmentResultLabel;
    @FXML
    private Button back;
    @FXML
    private Button coordination;

    @FXML
    public void initialize() {
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        locationColumn.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
        severityColumn.setCellValueFactory(cellData -> cellData.getValue().severityProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        loadDisasterReports();
    }

    /*
     * Loads the disaster reports from the file.
     */
    private void loadDisasterReports() {
        ObservableList<DisasterReport> reports = FXCollections.observableArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader(REPORT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    reports.add(new DisasterReport(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        disasterTableView.setItems(reports);
    }

    @FXML
    private void handleAssessDisaster() {
        DisasterReport selectedDisaster = disasterTableView.getSelectionModel().getSelectedItem();
        if (selectedDisaster == null) {
            showAlert();
            return;
        }

        int score = assessDisaster(selectedDisaster);
        String priority = determinePriority(score);
        assessmentResultLabel.setText("Assessment Result: Score = " + score + ", Priority = " + priority);
    }

    /*
     * Assesses the disaster based on the type and severity.
     * @param disaster the disaster to assess
     * @return the score of the disaster
     */
    public int assessDisaster(DisasterReport disaster) {
        int score = 0;
        switch (disaster.type()) {
            case "Hurricane":
                score += 50;
                break;
            case "Earthquake":
                score += 40;
                break;
            case "Flood":
                score += 30;
                break;
            case "Fire":
                score += 20;
                break;
            case "Tornado":
                score += 45;
                break;
        }
        switch (disaster.severity()) {
            case "Severe":
                score += 30;
                break;
            case "High":
                score += 20;
                break;
            case "Moderate":
                score += 10;
                break;
            case "Low":
                score += 5;
                break;
        }
        return score;
    }

    /**
     * Determines the priority of the disaster based on the score.
     *
     * @param score the score of the disaster
     * @return the priority of the disaster
     */

    public String determinePriority(int score) {
        if (score >= 70) {
            return "High";
        } else if (score >= 60) {
            return "Moderate";
        } else {
            return "Low";
        }
    }


    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Please select a disaster to assess.");
        alert.showAndWait();
    }

    @FXML
    private void handleBackButton(ActionEvent actionEvent) {
        Stage stage = (Stage) back.getScene().getWindow();
        switchTo("ResponseCoordination.fxml", stage);

    }

    @FXML
    private void handleCoordButton(ActionEvent actionEvent) {
        Stage stage = (Stage) coordination.getScene().getWindow();
        switchTo("ResponseCoordination.fxml", stage);
    }

    static class Navigation {

        public static void switchTo(String name, Stage stage) {
            redirectTo(name, stage);
        }

        static void redirectTo(String name, Stage stage) {
            try {
                FXMLLoader loader = new FXMLLoader(DisasterReport.class.getResource(name));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}




