package com.example.mavenjavafx;

import com.database.DataBaseController;
import com.database.DataBaseService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class MenuController {
    double x,y;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    Button button = new Button();

    public void switchToMainMenu(ActionEvent event) throws IOException{
        root = FXMLLoader.load(Main.class.getResource("main-menu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        root.setOnMousePressed(evt ->{
            x = evt.getSceneX();
            y = evt.getSceneY();
        });
        root.setOnMouseDragged(evt ->{
            stage.setX(evt.getScreenX() - x);
            stage.setY(evt.getScreenY() - y);
        });

        stage.setScene(scene);
        stage.show();

    }

    public void switchToAbout(ActionEvent event) throws IOException{
        root = FXMLLoader.load(Main.class.getResource("about.fxml"));


        root.setOnMousePressed(evt ->{
            x = evt.getSceneX();
            y = evt.getSceneY();
        });
        root.setOnMouseDragged(evt ->{
            stage.setX(evt.getScreenX() - x);
            stage.setY(evt.getScreenY() - y);
        });


        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);


        stage.setScene(scene);
        stage.show();
    }

    public void switchToResources(ActionEvent event) throws IOException{
        root = FXMLLoader.load(Main.class.getResource("resources.fxml"));


        root.setOnMousePressed(evt ->{
            x = evt.getSceneX();
            y = evt.getSceneY();
        });
        root.setOnMouseDragged(evt ->{
            stage.setX(evt.getScreenX() - x);
            stage.setY(evt.getScreenY() - y);
        });


        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);


        stage.setScene(scene);
        stage.show();
    }

    public void exitButton(ActionEvent event) throws SQLException {
        stage = (Stage)button.getScene().getWindow();
        stage.close();
        DataBaseService dbs = new DataBaseService();

        dbs.removeRoomsMiscellaneous();
    }
}


