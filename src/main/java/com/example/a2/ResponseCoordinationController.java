package com.example.a2;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.example.a2.DisasterAssessmentController.Navigation.switchTo;

public class ResponseCoordinationController {

    private static final String COMMUNICATION_LOG_FILE = "communication_log.txt";
    private static final String TASKS_FILE = "tasks.txt";
    @FXML
    private TableColumn taskDescriptionColumn;
    @FXML
    private TableColumn taskAssigneeColumn;
    @FXML
    private TextField taskDescriptionField;
    @FXML
    private ComboBox<String> taskAssigneeComboBox;
    @FXML
    private ComboBox<String> activityComboBox;
    @FXML
    private ComboBox<String> departmentComboBox;
    @FXML
    private TextArea messageTextArea;
    @FXML
    private ListView<String> communicationLogListView;
    @FXML
    private TableView<Task> taskTableView;
    @FXML
    private Button home;
    private Map<String, ObservableList<String>> departmentActivities;


    @FXML
    public void initialize() {
        initializeDepartments();
        initializeActivities();
        initializeTaskAssignees();
        loadCommunicationLog();
        loadTasks();

        departmentComboBox.setOnAction(e -> updateActivityComboBox());
    }

    private void initializeDepartments() {
        departmentComboBox.getItems().addAll(
                "Fire Department", "Police",
                "Medical Services", "Public Works", "Utility Companies"

        );
    }

    private void initializeActivities() {
        departmentActivities = new HashMap<>();

        departmentActivities.put("Fire Department", FXCollections.observableArrayList(
                "Search and Rescue", "Fire Suppression", "Hazardous Materials Management"
        ));
        departmentActivities.put("Police", FXCollections.observableArrayList(
                "Evacuation Support", "Security Enforcement", "Traffic Control"
        ));
        departmentActivities.put("Medical Services", FXCollections.observableArrayList(
                "Emergency Medical Care", "Public Health", "Hospital Coordination"
        ));
        departmentActivities.put("Public Works", FXCollections.observableArrayList(
                "Demolition of buildings", "Infrastructure Repair", "Building Assessment"
        ));
        departmentActivities.put("Utility Companies", FXCollections.observableArrayList(
                "Power Restoration", "Water Supply Management", "Communication Systems Repair"
        ));

    }

    private void initializeTaskAssignees() {
        taskAssigneeComboBox.getItems().addAll(
                "John Doe", "Jane Smith", "Alice Johnson", "Bob Brown", "Charlie White"
        );
    }

    /*
     * Reads the tasks from the file and loads them into the task table view.
     */
    public void loadTasks() {
        ObservableList<Task> tasks = FXCollections.observableArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader(TASKS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    tasks.add(new Task(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to load the tasks.");
        }
        assignTask(tasks);
    }

    /**
     * Assigns the tasks to the task table view.
     *
     * @param tasks the tasks to assign
     */
    private void assignTask(ObservableList<Task> tasks) {
        if (!tasks.isEmpty()) {

            taskDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            taskAssigneeColumn.setCellValueFactory(new PropertyValueFactory<>("assignee"));
            taskTableView.setItems(tasks);
        }
    }

    private void updateActivityComboBox() {
  //      activityComboBox.getItems().clear();
        String selectedDepartment = departmentComboBox.getValue();
        if (selectedDepartment != null) {

            activityComboBox.setItems(departmentActivities.get(selectedDepartment));
        }

    }

    @FXML
    private void handleSendMessage() {
        String department = departmentComboBox.getValue();
        String message = messageTextArea.getText();

        if (department == null || message.isEmpty()) {
            showAlert("Please select a department and enter a message.");
            return;
        }
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String logEntry = timestamp + " - To " + department + ": " + message;

        saveCommunicationLog(logEntry);

        communicationLogListView.getItems().addFirst(logEntry);
        clearFields();
    }

    @FXML
    public void handleAddTask() {
        String description = taskDescriptionField.getText();
        String assignee = taskAssigneeComboBox.getValue();

        if (description.isEmpty() || assignee == null) {
            showAlert("Please enter a task description and select an assignee.");
            return;
        }

        Task task = new Task(description, assignee);
        taskTableView.getItems().add(task);
        saveTask(task);
        clearTaskFields();
    }

    public void saveTask(Task task) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASKS_FILE, true))) {
            writer.write(task.getDescription() + "," + task.getAssignee() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to save the task.");
        }
    }

    public void clearTaskFields() {
        taskDescriptionField.clear();
        taskAssigneeComboBox.setValue(null);
        departmentComboBox.setValue(null);
    }

    /**
     * reads the communication log file and loads the log entries into the communication log list view.
     * The log entries are displayed in reverse order so that the most recent entry is at the top of the list view.
     */
    public void loadCommunicationLog() {
        ObservableList<String> logEntries = FXCollections.observableArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader(COMMUNICATION_LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logEntries.addFirst(line);
            }
        } catch (IOException e) {
            showAlert("Failed to load the communication log.");
        }
        communicationLogListView.setItems(logEntries);
    }


    public void saveCommunicationLog(String logEntry) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(COMMUNICATION_LOG_FILE, true))) {
            writer.write(logEntry + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to save the communication log.");
        }
    }

    private void clearFields() {
        departmentComboBox.setValue(null);
        messageTextArea.clear();
    }

    private void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void handleHomeButton(ActionEvent actionEvent) {

        Stage stage = (Stage) home.getScene().getWindow();
        switchTo("DisasterReport.fxml", stage);
    }


}
