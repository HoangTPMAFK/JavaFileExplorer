import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) throws IOException {
        // System.out.println("Working Directory = " + System.getProperty("user.dir"));
        // System.out.println(System.getProperty("os.name"));
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fileExplorerScene.fxml"));
        stage.setTitle("File Explorer");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
