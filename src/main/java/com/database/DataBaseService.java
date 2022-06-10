package com.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timeTable.Discipline;
import com.timeTable.Event;
import com.timeTable.TimeTable;
import com.timeTable.algorithm.Edge;
import com.timeTable.classes.*;
import org.jgrapht.Graph;
import org.jgrapht.traverse.DepthFirstIterator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class DataBaseService {
    private final DataBaseController dataBaseController = new DataBaseController();
    ObjectMapper mapper = new ObjectMapper();


    public void addTimeTableInitial(List<TimeTable> listOfTimeTables) throws JsonProcessingException {
        try {
            StringBuilder sql = new StringBuilder("Insert Into TimeTableInitial (TimeTable, day, start, TimeTableInitial.[end], discipline, type, teacher, room, capacity) " +
                    "Values (?, ?, ?, ?, ?, ?, ?, ?, ?);");
            PreparedStatement statement = dataBaseController.getDataBaseConnection().getConnection().prepareStatement(String.valueOf(sql), Statement.RETURN_GENERATED_KEYS);

            int count = 0;

            for (TimeTable t : listOfTimeTables) {
                for (Event e : t.listOfEvents) {
                    count++;
                    statement.setString(1, t.getNameOfTimeTable());
                    statement.setString(2, e.getDayOfWeek());
                    statement.setString(3, e.getStartTime());
                    statement.setString(4, e.getEndTime());
                    statement.setString(5, e.getDiscipline().getName());
                    statement.setString(6, e.getType());
                    statement.setString(7, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(e.getDiscipline().getTeacher()));
                    statement.setString(8, "");
                    statement.setInt(9, 0);

                    statement.addBatch();

                    if (count % 50 == 0) {
                        DataBaseController.executeSQL(statement);
                        statement.clearParameters();
                    }
                }
                DataBaseController.executeSQL(statement);
                statement.clearParameters();

            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTimeTable(Graph<Event, Edge> eventsGraph) throws JsonProcessingException {
        try {
            String sql = "Insert Into TimeTable (TimeTable, day, start, TimeTable.[end], discipline, type, teacher, room, capacity) " +
                    "Values (?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement statement = dataBaseController.getDataBaseConnection().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            int count = 0;

            Iterator<Event> iter2 = new DepthFirstIterator<>(eventsGraph);
            while (iter2.hasNext()) {
                Event vertex = iter2.next();
                if (vertex.getRoom() != null) {
                    count++;
                    statement.setString(1, vertex.getTimeTableName());
                    statement.setString(2, vertex.getDayOfWeek());
                    statement.setString(3, vertex.getStartTime());
                    statement.setString(4, vertex.getEndTime());
                    statement.setString(5, vertex.getDiscipline().getName());
                    statement.setString(6, vertex.getType());
                    statement.setString(7, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(vertex.getDiscipline().getTeacher()));
                    statement.setString(8, vertex.getRoom().getName());
                    statement.setInt(9, vertex.getRoom().getCapacity());

                    statement.addBatch();

                    if (count % 200 == 0) {
                        DataBaseController.executeSQL(statement);
                        statement.clearParameters();
                    }
                }
            }
            DataBaseController.executeSQL(statement);
            statement.clearParameters();

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeRoomsMiscellaneous() throws SQLException {
        String sql = "Delete from Rooms";
        String sql2 = "DBCC CHECKIDENT (Rooms, RESEED, 0)";

        PreparedStatement statement = dataBaseController.getDataBaseConnection().getConnection().prepareStatement(sql);

        statement.addBatch();
        DataBaseController.executeSQL(statement);
        statement.clearParameters();

        statement = dataBaseController.getDataBaseConnection().getConnection().prepareStatement(sql2);
        statement.addBatch();
        DataBaseController.executeSQL(statement);
        statement.clearParameters();
    }

    public void addRoomsMiscellaneous(List<Room> allRooms) throws JsonProcessingException {
        try {
            String sql = "Insert Into Rooms (room, capacity, chalk, sponge, computer, videoprojector, type) " +
                    "Values (?, ?, ?, ?, ?, ?,?);";
            PreparedStatement statement = dataBaseController.getDataBaseConnection().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            int count = 0;
          for(Room r : allRooms) {
              count++;
              statement.setString(1, r.getName());
              statement.setInt(2, r.getCapacity());
              statement.setInt(3, r.getNumberOfChalk());
              statement.setInt(4, r.getNumberOfSponges());
              statement.setInt(5, r.getNumberOfComputers());
              statement.setInt(6, r.getNumberOfVideoProjectors());
              statement.setString(7, r.getType().toString());
              statement.addBatch();

              if (count % 200 == 0) {
                  DataBaseController.executeSQL(statement);
                  statement.clearParameters();
              }
              DataBaseController.executeSQL(statement);
              statement.clearParameters();
          }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


        public List<TimeTable> selectTimeTableInitial() {

        try {
            String sql = "Select TimeTable, day, start, TimeTableInitial.[end], discipline, type, teacher, room, capacity from TimeTableInitial;";
            List<TimeTable> listOfTimeTables = new ArrayList<>();
            ResultSet resultSet;
            resultSet = DataBaseController.selectSQL(sql);
            String currentTimeTableName = "";
            while (resultSet.next()) {
                TimeTable timeTable = new TimeTable();
                Event event = new Event();
                currentTimeTableName = resultSet.getString(1);
                listOfTimeTables.add(timeTable);
                timeTable.setNameOfTimeTable(currentTimeTableName);
                timeTable.listOfEvents.add(event);

                event.setDayOfWeek(resultSet.getString(2));
                event.setStartTime(resultSet.getString(3));
                event.setEndTime(resultSet.getString(4));
                Discipline discipline = new Discipline(resultSet.getString(5));
                event.setDiscipline(discipline);
                event.setType(resultSet.getString(6));
                List<String> teachers = new ArrayList<>();
                teachers = mapper.readValue(resultSet.getString(7), teachers.getClass());
                discipline.setTeacher(teachers);
                break;
            }

            int indexOfTimeTable = 0;
            TimeTable timeTable;
            while (resultSet.next()) {
                if (!resultSet.getString(1).equals(currentTimeTableName)) {
                    currentTimeTableName = resultSet.getString(1);
                    timeTable = new TimeTable();
                    listOfTimeTables.add(timeTable);
                    timeTable.setNameOfTimeTable(currentTimeTableName);
                    indexOfTimeTable++;
                } else {
                    timeTable = listOfTimeTables.get(indexOfTimeTable);
                }
                Event event = new Event();
                timeTable.listOfEvents.add(event);

                event.setDayOfWeek(resultSet.getString(2));
                event.setStartTime(resultSet.getString(3));
                event.setEndTime(resultSet.getString(4));
                Discipline discipline = new Discipline(resultSet.getString(5));
                event.setDiscipline(discipline);
                event.setType(resultSet.getString(6));
                List<String> teachers = new ArrayList<>();
                teachers = mapper.readValue(resultSet.getString(7), teachers.getClass());
                discipline.setTeacher(teachers);

            }
            return listOfTimeTables;
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    public List<Room> selectRoomInitial() throws SQLException {
        String sql = "SELECT [room],[capacity],[chalk],[sponge],[computer],[videoprojector],[type] FROM [dbo].[RoomsInitial]";

        List<Room> rooms = new ArrayList<>();
        ResultSet resultSet;
        resultSet = DataBaseController.selectSQL(sql);

        while (resultSet.next()) {
            Room room = null;
            String type = resultSet.getString(7);
            if (type.contains("LECTURE")) {
                room = new Lecture(0, 0, 0, 0);
            } else if (type.contains("SEMINARY")) {
                room = new Seminary(0, 0, 0, 0);
            } else if (type.contains("LABORATORY")) {
                room = new Laboratory(0, 0, 0, 0);
            }

            assert room != null;

            room.setName(resultSet.getString(1));
            room.setCapacity(resultSet.getInt(2));
            room.setNumberOfChalk(resultSet.getInt(3));
            room.setNumberOfSponges(resultSet.getInt(4));
            room.setNumberOfComputers(resultSet.getInt(5));
            room.setNumberOfVideoProjectors(resultSet.getInt(6));

            rooms.add(room);


        }

        return rooms;

    }


    public void addRoomsInitial(Set<Room> listOfRooms) {
        StringBuilder sql;

        for (Room r : listOfRooms) {
            sql = new StringBuilder("Insert Into RoomsInitial (room, capacity, type) Values (");
            String roomName = "'" + r.getName() + "', ";
            sql.append(roomName);
            String capacity = "'" + r.getCapacity() + "', ";
            sql.append(capacity);
            String type = "'" + r.getType() + "');";
            sql.append(type);


            // getDataBaseController().executeSQL();
            System.out.println(sql);
        }

    }

    public void addDisciplines(List<TimeTable> listOfTimeTables) {
        try {
            PreparedStatement statement = dataBaseController
                    .getDataBaseConnection()
                    .getConnection()
                    .prepareStatement(String.valueOf("Insert Into Disciplines (discipline, teacher) Values (?, ?);"), Statement.RETURN_GENERATED_KEYS);
            Set<Discipline> disciplineSet = new HashSet<>();

            for (TimeTable t : listOfTimeTables)
                for (Event e : t.listOfEvents) {
                    disciplineSet.add(e.getDiscipline());
                }

            for (Discipline d : disciplineSet) {
                statement.setString(1, d.getName());
                statement.setString(2, mapper.writeValueAsString(d.getTeacher()));

                statement.addBatch();
                getDataBaseController().executeSQL(statement);

            }
            statement.close();
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
        }


    }

    public static void selectMiscellaneous() throws SQLException {
        String sql = "SELECT * FROM Miscellaneous";
        ResultSet resultSet = DataBaseController.selectSQL(sql);

        Miscellaneous miscellaneous = Miscellaneous.getInstance();

        while (resultSet.next()) {
            miscellaneous.setTotalNumberOfChalk(resultSet.getInt(2));
            miscellaneous.setTotalNumberOfSponges(resultSet.getInt(3));
            miscellaneous.setTotalNumberOfVideoProjectors(resultSet.getInt(4));
            miscellaneous.setTotalNumberOfComputers(resultSet.getInt(5));
        }
    }


    public DataBaseController getDataBaseController() {
        return dataBaseController;
    }
}
