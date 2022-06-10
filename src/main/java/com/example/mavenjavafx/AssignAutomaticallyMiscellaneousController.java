package com.example.mavenjavafx;

import com.database.DataBaseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.timeTable.Event;
import com.timeTable.algorithm.Edge;
import com.timeTable.algorithm.ResourcesAlgorithm;
import com.timeTable.classes.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jgrapht.Graph;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.database.DataBaseController.dataBaseConnection;

public class AssignAutomaticallyMiscellaneousController implements Initializable {

    double x, y;
    private Stage stage;
    private Scene scene;
    private Parent root;

    public static boolean isTableAssigned = false;

    @FXML
    private TableColumn<RoomTable, Integer> capacity;

    @FXML
    private TableColumn<RoomTable, Integer> chalks;

    @FXML
    private TableColumn<RoomTable, Integer> computers;

    @FXML
    private TableColumn<RoomTable, String> room;

    @FXML
    private TableColumn<RoomTable, Integer> sponges;

    @FXML
    private TableView<RoomTable> tableClasses;

    @FXML
    private TableColumn<RoomTable, String> type;

    @FXML
    private TableColumn<RoomTable, Integer> videoprojectors;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    ObservableList<RoomTable> list = FXCollections.observableArrayList(

    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        room.setCellValueFactory(new PropertyValueFactory<RoomTable, String>("room"));
        type.setCellValueFactory(new PropertyValueFactory<RoomTable, String>("type"));
        capacity.setCellValueFactory(new PropertyValueFactory<RoomTable, Integer>("capacity"));
        chalks.setCellValueFactory(new PropertyValueFactory<RoomTable, Integer>("chalks"));
        sponges.setCellValueFactory(new PropertyValueFactory<RoomTable, Integer>("sponges"));
        videoprojectors.setCellValueFactory(new PropertyValueFactory<RoomTable, Integer>("videoprojectors"));
        computers.setCellValueFactory(new PropertyValueFactory<RoomTable, Integer>("computers"));

        wrapText();

        if (!isTableAssigned) {
            try {
                assignMiscellaneous();
                isTableAssigned = true;
            } catch (FileNotFoundException | JsonProcessingException | SQLException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            loadDate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



    private void loadDate() throws SQLException {
        list.clear();

        connection = dataBaseConnection.getConnection();
        query = "SELECT * FROM Rooms";
        preparedStatement = connection.prepareStatement(query);

        resultSet = preparedStatement.executeQuery();

        while(resultSet.next()){
            list.add(new RoomTable(
                            resultSet.getString("room"),
                            resultSet.getString("type"),
                            resultSet.getInt("capacity"),
                            resultSet.getInt("chalk"),
                            resultSet.getInt("sponge"),
                            resultSet.getInt("videoprojector"),
                            resultSet.getInt("computer")
                    )
            );
        }

        tableClasses.setItems(list);
    }


    void assignMiscellaneous() throws FileNotFoundException, JsonProcessingException, SQLException {

        ResourcesAlgorithm resourcesAlgorithm = new ResourcesAlgorithm();
        List<Room> newList;
        newList = resourcesAlgorithm.startAssignResources();

        DataBaseService dataBaseAlgorithm = new DataBaseService();
        dataBaseAlgorithm.addRoomsMiscellaneous(newList);

    }


    private void wrapText() {

        room.setCellFactory(param -> new TableCell<RoomTable, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    Text text = new Text(item);
                    text.setStyle("-fx-text-alignment:justify;");
                    text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(10));
                    setGraphic(text);
                }
            }
        });

        type.setCellFactory(param -> new TableCell<RoomTable, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    Text text = new Text(item);
                    text.setStyle("-fx-text-alignment:justify;");
                    text.setStyle("-fx-text-fill: white");
                    text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(10));
                    setGraphic(text);
                }
            }
        });

        capacity.setCellFactory(param -> new TableCell<RoomTable, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    Text text = new Text(item.toString());
                    text.setStyle("-fx-text-alignment:justify;");
                    text.setStyle("-fx-text-fill: white");
                    text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(10));
                    setGraphic(text);
                }
            }
        });

        chalks.setCellFactory(param -> new TableCell<RoomTable, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    Text text = new Text(item.toString());
                    text.setStyle("-fx-text-alignment:justify;");
                    text.setStyle("-fx-text-fill: white");
                    text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(10));
                    setGraphic(text);
                }
            }
        });

        sponges.setCellFactory(param -> new TableCell<RoomTable, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    Text text = new Text(item.toString());
                    text.setStyle("-fx-text-alignment:justify;");
                    text.setStyle("-fx-text-fill: white");
                    text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(10));
                    setGraphic(text);
                }
            }
        });

        videoprojectors.setCellFactory(param -> new TableCell<RoomTable, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    Text text = new Text(item.toString());
                    text.setStyle("-fx-text-alignment:justify;");
                    text.setStyle("-fx-text-fill: white");
                    text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(10));
                    setGraphic(text);
                }
            }
        });

        computers.setCellFactory(param -> new TableCell<RoomTable, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    Text text = new Text(item.toString());
                    text.setStyle("-fx-text-alignment:justify;");
                    text.setStyle("-fx-text-fill: white");
                    text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(10));
                    setGraphic(text);
                }
            }
        });
    }



    public void switchToResources(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Main.class.getResource("resources.fxml"));


        root.setOnMousePressed(evt -> {
            x = evt.getSceneX();
            y = evt.getSceneY();
        });
        root.setOnMouseDragged(evt -> {
            stage.setX(evt.getScreenX() - x);
            stage.setY(evt.getScreenY() - y);
        });


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);


        stage.setScene(scene);
        stage.show();
    }

    public void switchToAutomatically(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Main.class.getResource("assign_hub.fxml"));


        root.setOnMousePressed(evt -> {
            x = evt.getSceneX();
            y = evt.getSceneY();
        });
        root.setOnMouseDragged(evt -> {
            stage.setX(evt.getScreenX() - x);
            stage.setY(evt.getScreenY() - y);
        });


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);


        stage.setScene(scene);
        stage.show();
    }

    public void switchToManual(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Main.class.getResource("assign.fxml"));


        root.setOnMousePressed(evt -> {
            x = evt.getSceneX();
            y = evt.getSceneY();
        });
        root.setOnMouseDragged(evt -> {
            stage.setX(evt.getScreenX() - x);
            stage.setY(evt.getScreenY() - y);
        });


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);


        stage.setScene(scene);
        stage.show();
    }

    public void switchToAutomaticallyClasses(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Main.class.getResource("assign_automatically_classes.fxml"));


        root.setOnMousePressed(evt -> {
            x = evt.getSceneX();
            y = evt.getSceneY();
        });
        root.setOnMouseDragged(evt -> {
            stage.setX(evt.getScreenX() - x);
            stage.setY(evt.getScreenY() - y);
        });


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);


        stage.setScene(scene);
        stage.show();
    }

    public void switchToAutomaticallyMiscellaneous(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Main.class.getResource("assign_automatically_miscellaneous.fxml"));


        root.setOnMousePressed(evt -> {
            x = evt.getSceneX();
            y = evt.getSceneY();
        });
        root.setOnMouseDragged(evt -> {
            stage.setX(evt.getScreenX() - x);
            stage.setY(evt.getScreenY() - y);
        });


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);


        stage.setScene(scene);
        stage.show();
    }
}
