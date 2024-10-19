package testPackage;

import com.example.a2.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;

public class DisasterReporControllerTest {

    private DisasterReportController controller;
    private static final String REPORT_FILE = "disaster_reports.txt";
    private WeatherAlertController wetherAlertController;
    private DisasterAssessmentController assessmentController;
    private ResponseCoordinationController cordinationController;
    @BeforeEach
    void setUp() {
        controller = new DisasterReportController();
        assessmentController = new DisasterAssessmentController();
        wetherAlertController = new WeatherAlertController();
       cordinationController = new ResponseCoordinationController();
    }

    @Test
    //test to submit a disaster report
    void testSubmitDisasterReport() {
        controller.saveReport("Hurricane", "New Castle", "Severe", "Severe flooding reported with wind speeds exceeding 120 mph.");
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



        assertEquals("Hurricane", reports.getLast().getType());
        assertEquals("New Castle", reports.getLast().getLocation());
        assertEquals("Severe", reports.getLast().getSeverity());
        assertEquals("Severe flooding reported with wind speeds exceeding 120 mph.", reports.getLast().getDescription());

    }





@Test
    void testAssessDisaster() {
        DisasterReport disaster = new DisasterReport("Hurricane", "New Castle", "Severe", "Severe flooding reported with wind speeds exceeding 120 mph.");
        int score = assessmentController.assessDisaster(disaster);
        assertEquals(80, score);
        DisasterReport disaster2 = new DisasterReport("Earthquake", "Brisbane", "Moderate", "Moderate shaking reported with minor damage to buildings.");
        int score2 = assessmentController.assessDisaster(disaster2);
        assertEquals(50, score2);
        DisasterReport disaster3 = new DisasterReport("Tornado", "Sydney", "Low", "Low wind speeds reported with minimal damage to buildings.");
        int score3 = assessmentController.assessDisaster(disaster3);
        assertEquals(50, score3);
    }

    @Test
    void testDeterminePriority() {
        assertEquals("High", assessmentController.determinePriority(90));
        assertEquals("Moderate", assessmentController.determinePriority(60));
        assertEquals("Low", assessmentController.determinePriority(30));

    }

//    public static void main(String[] args) {
//        DisasterReporControllerTest test = new DisasterReporControllerTest();
//        test.setUp();
//        test.testSubmitDisasterReport();
//        test.testAssessDisaster();
//        test.testDeterminePriority();
//    }

  @Test
  void  testGetWeatherInfo(){
        String weatherInfo = wetherAlertController.getWeatherInfo("New Castle");
        assertNotNull(weatherInfo);
        assertTrue(weatherInfo.contains("Weather for New Castle:"));
        assertTrue(weatherInfo.contains("Temperature:"));
        assertTrue(weatherInfo.contains("Condition:"));
        String weatherInfo2 = wetherAlertController.getWeatherInfo("Brisbane");
        assertNotNull(weatherInfo2);
        assertTrue(weatherInfo2.contains("Weather for Brisbane:"));
        assertTrue(weatherInfo2.contains("Temperature:"));
        assertTrue(weatherInfo2.contains("Condition:"));
    }

    @Test
    void testGetActiveAlerts(){
        ObservableList<String> alerts = wetherAlertController.getActiveAlerts();
        assertNotNull(alerts);
        assertFalse(alerts.isEmpty());
        assertTrue(alerts.size() <= 3);
  }


}
