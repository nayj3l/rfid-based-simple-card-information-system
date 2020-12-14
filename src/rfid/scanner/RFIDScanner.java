package rfid.scanner;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Optional;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class RFIDScanner extends Application {

    private static Double locX;
    private static Double locY;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("RFIDScanner.fxml"));
        Scene scene = new Scene(root);
        locX = stage.getX();
        locY = stage.getY();
        stage.setResizable(false);
        stage.xProperty().addListener((obs, oldVal, newVal) -> { locX = newVal.doubleValue(); });
        stage.yProperty().addListener((obs, oldVal, newVal) -> { locY = newVal.doubleValue(); });
        stage.setScene(scene);
        stage.setTitle("RFID Based Banking System");
        stage.show();
    }
    
    public static boolean go = false;
    public static void show(String title, String content, Alert.AlertType type){     
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.setX(locX + 270);
        alert.setY(locY + 200);
        Optional<ButtonType> result = alert.showAndWait();
        go = result.get() == ButtonType.OK;
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
