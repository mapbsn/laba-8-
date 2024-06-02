package DataBase;

import Managers.PasswordManager;
import Objects.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

public class DataBaseManager extends Configs {
   Connection dbConnection;

   public Connection getDbConnection() throws ClassNotFoundException, SQLException {
      String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
      Class.forName("com.mysql.cj.jdbc.Driver");
      dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
      return dbConnection;
   }

   public boolean checkUser(String user_name) {
      boolean exists = false;

      try {
         String sql = "SELECT COUNT(*) AS count FROM users WHERE user_name = ?";
         PreparedStatement preparedStatement = getDbConnection().prepareStatement(sql);
         preparedStatement.setString(1, user_name);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            int count = resultSet.getInt("count");
            if (count > 0) {
               exists = true;
            }
         }

         resultSet.close();
         preparedStatement.close();
      } catch (SQLException var7) {
         System.out.println("Error with sql");
      } catch (ClassNotFoundException e) {
         throw new RuntimeException(e);
      }

      return exists;
   }

   public void registerUser(String user_name, String pswd) {
      PreparedStatement preparedStatement = null;

      try {
         String sql = "INSERT INTO users (user_name, password) VALUES (?, ?)";
         preparedStatement = getDbConnection().prepareStatement(sql);
         preparedStatement.setString(1, user_name);
         preparedStatement.setString(2, pswd);
         preparedStatement.executeUpdate();
         System.out.println("User added successfully.");
      } catch (SQLException var13) {
         System.out.println("Error adding user registration");
      } catch (ClassNotFoundException e) {
         throw new RuntimeException(e);
      } finally {
         try {
            if (preparedStatement != null) {
               preparedStatement.close();
            }
         } catch (SQLException var12) {
            System.out.println("Error with closing statement");
         }

      }

   }

   public boolean checkPassword(String user_name, String pswd) {
      String sql = "SELECT password FROM Users WHERE user_name = ?";

      try {
         PreparedStatement prepareStatement = getDbConnection().prepareStatement(sql);
         prepareStatement.setString(1, user_name);
         ResultSet resultSet = prepareStatement.executeQuery();
         if (resultSet.next()) {
            String hashedPassword = resultSet.getString("password");
            String hashedInputPassword = PasswordManager.hashPassword(pswd);
            prepareStatement.close();
            resultSet.close();
            return hashedInputPassword.equals(hashedPassword);
         } else {
            prepareStatement.close();
            resultSet.close();
            return false;
         }
      } catch (SQLException var8) {
         throw new RuntimeException(var8);
      } catch (ClassNotFoundException e) {
         throw new RuntimeException(e);
      }
   }

   public ArrayList<String> getUsers() {
      String sql = "SELECT user_name FROM Users;";
      ArrayList users = new ArrayList();

      try {
         PreparedStatement prepareStatement = getDbConnection().prepareStatement(sql);
         ResultSet resultSet = prepareStatement.executeQuery();

         while (resultSet.next()) {
            String user = resultSet.getString("user_name");
            users.add(user);
         }

         if (!users.isEmpty()) {
            prepareStatement.close();
            resultSet.close();
            return users;
         } else {
            users.add("There are no users yet...");
            return users;
         }
      } catch (SQLException var6) {
         throw new RuntimeException(var6);
      } catch (ClassNotFoundException e) {
         throw new RuntimeException(e);
      }
   }

   public TreeSet<StudyGroup> readFromDataBase() {
      TreeSet studyGroups = new TreeSet();

      try {
         String sql = "SELECT id, name, study_x, study_y, countStudents, transfer, forms, semester, person, weight,Passport, Local_x,Local_y,Local_name,user_name FROM StudyGroup";
         PreparedStatement preparedStatement = getDbConnection().prepareStatement(sql);
         ResultSet rs = preparedStatement.executeQuery();

         while (rs.next()) {
            StudyGroup studyGroup = new StudyGroup();
            Coordinates coordinates = new Coordinates();
            Person person = new Person();
            Location location = new Location();
            studyGroup.setId((long) rs.getInt("id"));
            IdManager.AddId((long) rs.getInt("id"));
            studyGroup.setName(rs.getString("name"));
            coordinates.setX(rs.getDouble("study_x"));
            coordinates.setY(rs.getInt("study_y"));
            studyGroup.setCoordinates(coordinates);
            studyGroup.setStudentsCount(rs.getLong("countStudents"));
            studyGroup.setTransferredStudents(rs.getInt("transfer"));
            studyGroup.setFormOfEducation(FormOfEducation.valueOf(rs.getString("forms")));
            studyGroup.setSemesterEnum(Semester.valueOf(rs.getString("semester")));
            person.setName(rs.getString("person"));
            person.setWeight(rs.getFloat("weight"));
            person.setPassportID(rs.getString("Passport"));
            location.setX(rs.getFloat("Local_x"));
            location.setY(rs.getLong("Local_y"));
            location.setName(rs.getString("Local_name"));
            person.setLocation(location);
            studyGroup.setGroupAdmin(person);
            studyGroup.setUser_name(rs.getString("user_name"));
            studyGroups.add(studyGroup);
         }

         rs.close();
         preparedStatement.close();
      } catch (SQLException var8) {
         var8.printStackTrace();
      } catch (ClassNotFoundException e) {
         throw new RuntimeException(e);
      }

      return studyGroups;
   }

   public void saveToDataBase(TreeSet<StudyGroup> studyGroups, String user_name) {
      StringBuilder sql = new StringBuilder("DELETE FROM StudyGroup WHERE user_name = ?;");
      Iterator var5 = studyGroups.iterator();

      while (var5.hasNext()) {
         StudyGroup studyGroup = (StudyGroup) var5.next();
         if (studyGroup.getUser_name().equals(user_name) && !studyGroup.getUser_name().isEmpty()) {
            String value = getValue(studyGroup);
            sql.append(value);
         }
      }

      try {
         PreparedStatement prepareStatement = getDbConnection().prepareStatement(sql.toString());
         prepareStatement.setString(1, user_name);
         prepareStatement.executeQuery();
         prepareStatement.close();
      } catch (SQLException var8) {
         System.out.println("save was successfully");
      } catch (ClassNotFoundException e) {
         throw new RuntimeException(e);
      }

   }

   private static String getValue(StudyGroup studyGroup) {
      String value = "INSERT INTO StudyGroup(id, name, study_x, study_y, countStudents, transfer, forms, semester, person, weight, Passport, Local_x, Local_y, Local_name, user_name) VALUES";
      value = value + "(" + studyGroup.getId() + ",'" + studyGroup.getName() + "'," + studyGroup.getCoordinates().getX() + "," + studyGroup.getCoordinates().getY() + ",'" + studyGroup.getStudentsCount() + "," + studyGroup.getTransferredStudents() + ",'" + studyGroup.getFormOfEducation() + "','" + studyGroup.getSemesterEnum() + "','" + studyGroup.getGroupAdmin().getName() + "'," + studyGroup.getGroupAdmin().getWeight() + ",'" + studyGroup.getGroupAdmin().getPassportID() + "'," + studyGroup.getGroupAdmin().getLocation().getX() + "," + studyGroup.getGroupAdmin().getLocation().getY() + ",'" + studyGroup.getGroupAdmin().getLocation().getName() + "','" + studyGroup.getUser_name() + "');";
      return value;
   }


}