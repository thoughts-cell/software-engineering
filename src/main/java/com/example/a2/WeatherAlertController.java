package com.example.a2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Random;

import static com.example.a2.DisasterAssessmentController.Navigation.switchTo;

public class WeatherAlertController {
    @FXML
    Button home;
    @FXML
    private TextField locationField;
    @FXML
    private TextArea weatherInfoArea;
    @FXML
    private ListView<String> alertListView;
    private final Random random = new Random();

    @FXML
    private void handleGetWeather() {
        String location = locationField.getText();
        if (location.isEmpty()) {
            showAlert();
            return;
        }

        String weatherInfo = getWeatherInfo(location);
        weatherInfoArea.setText(weatherInfo);
    }

    @FXML
    public void handleRefreshAlerts() {
        ObservableList<String> alerts = getActiveAlerts();
        alertListView.setItems(alerts);
    }

    public String getWeatherInfo(String location) {

        int temperature = random.nextInt(40) + 3;
        String[] conditions = {"Sunny", "Cloudy", "Rainy", "Windy", "Stormy"};
        String condition = conditions[random.nextInt(conditions.length)];

        return "Weather for " + location + ":\nTemperature: " + temperature + "°C\nCondition: " + condition;
    }

    /**
     *
     * @return a list of active alerts
     */
    public ObservableList<String> getActiveAlerts() {

        ObservableList<String> alerts = FXCollections.observableArrayList();
        String[] possibleAlerts = {
                "Severe Thunderstorm Warning",
                "Flash Flood Watch",
                "Tornado Warning",
                "Hurricane Advisory",
                "Extreme Heat Warning"
        };

        int numAlerts = random.nextInt(3) + 1;
        for (int i = 0; i < numAlerts; i++) {
            alerts.add(possibleAlerts[random.nextInt(possibleAlerts.length)]);

        }

        return alerts;
    }

    public void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Please enter a location.");
        alert.showAndWait();
    }

    public void handleHomeButton(ActionEvent actionEvent) {

        Stage stage = (Stage) home.getScene().getWindow();
        switchTo("DisasterReport.fxml", stage);
    }
}
