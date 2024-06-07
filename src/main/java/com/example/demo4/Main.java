package com.example.demo4;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage primaryStage;
    private Scene scene1;
    private Scene scene2;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("queue.fxml"));
        fxmlLoader1.setControllerFactory(controllerClass -> new Controller());
        Parent root1 = fxmlLoader1.load();
        scene1 = new Scene(root1, 320, 240);

        FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("queue_2.fxml"));
        fxmlLoader2.setControllerFactory(controllerClass -> new Controller());
        Parent root2 = fxmlLoader2.load();
        scene2 = new Scene(root2, 600, 400);

        primaryStage.setScene(scene1);
        primaryStage.setTitle("Queue Management");
        primaryStage.show();


        Button switchButton = (Button) root1.lookup("#startButton");
        switchButton.setOnAction(event -> switchToScene2());
    }


    private void switchToScene2() {
        primaryStage.setScene(scene2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
