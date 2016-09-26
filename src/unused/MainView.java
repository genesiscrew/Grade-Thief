package unused;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @Author Adam Wareing
 * This view is the Main Screen of the Application. Its called on Application launch.
 */
public class MainView extends Application {

    Stage primaryStage;

    @Override
    /**
     * Called on application launch
     */
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        Parent root = new FXMLLoader().load(getClass().getResource("/unused/scenes/home_screen.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    /**
     * Launches the application
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
