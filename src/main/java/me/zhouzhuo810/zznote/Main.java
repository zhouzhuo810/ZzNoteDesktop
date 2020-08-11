package me.zhouzhuo810.zznote;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import me.zhouzhuo810.zznote.event.ExitEvent;
import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("editor.fxml")));
        primaryStage.setTitle("\u5c0f\u5468\u4fbf\u7b7e");
        primaryStage.setScene(new Scene(root, 480, 720));
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                EventBus.getDefault().post(new ExitEvent());
                Platform.exit();
                System.exit(0);
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
