import DataBase.Configs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class Main extends Configs {

         public static void main (String[] args) {
            JFrame frame = new JFrame("StudyGroup");
            frame.setSize(600, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JMenuBar mb = new JMenuBar();
            JMenu m1 = new JMenu("Изменение");
            mb.add(m1);
            JMenuItem m11 = new JMenuItem("remove_lower");
            JMenuItem m12 = new JMenuItem("add_if_max");
            JMenuItem m13=new JMenuItem("add");
            JMenuItem m14=new JMenuItem("clear");
            m1.add(m11);
            m1.add(m12);
            m1.add(m13);
            m1.add(m14);
            JMenu m2 = new JMenu("Информация");
            mb.add(m2);
            JMenuItem m21 = new JMenuItem("help");
            JMenuItem m22 = new JMenuItem("info");
            JMenuItem m23 = new JMenuItem("history");
            JMenuItem m24 = new JMenuItem("save");
            JMenuItem m25 = new JMenuItem("group");
            JMenuItem m26 = new JMenuItem("transfer");
            JMenuItem m27 = new JMenuItem("print_unique");
            m2.add(m21);
            m2.add(m22);
            m2.add(m23);
            m2.add(m24);
            m2.add(m25);
            m2.add(m26);
            m2.add(m27);
            JMenu m3 = new JMenu("Пользователь");
            mb.add(m3);
            JMenuItem m31 = new JMenuItem("login");
            JMenuItem m32 = new JMenuItem("register");
            JMenuItem m33 = new JMenuItem("logout");
            m3.add(m31);
            m3.add(m32);
            m3.add(m33);
            JMenu m4=new JMenu("user");
            mb.add(m4);
            JMenu m5 = new JMenu("Завершение работы");
            JMenuItem m51=new JMenuItem("exit");
            mb.add(m5);
            m5.add(m51);
            m51.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  System.exit(0);
               }
            });
            JMenu m6 = new JMenu("Языки");
            mb.add(m6);
            JMenuItem m61 = new JMenuItem("RUS");
            JMenuItem m62 = new JMenuItem("TR");
            JMenuItem m63 = new JMenuItem("LIT");
            JMenuItem m64 = new JMenuItem("ES-CO");
            m6.add(m61);
            m6.add(m62);
            m6.add(m63);
            m6.add(m64);
            DefaultTableModel model = new DefaultTableModel();
            JTable table = new JTable(model);
            model.addColumn("id");
            model.addColumn("name");
            model.addColumn("Study_x");
            model.addColumn("Study_y");
            model.addColumn("countStudents");
            model.addColumn("transfer");
            model.addColumn("forms");
            model.addColumn("semester");
            model.addColumn("person");
            model.addColumn("weight");
            model.addColumn("Passport");
            model.addColumn("Local_x");
            model.addColumn("Local_y");
            model.addColumn("Local_name");
            model.addColumn("user_name");
            try {
               Connection connection = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName, dbUser, dbPass);
               Statement statement = connection.createStatement();
               ResultSet resultSet = statement.executeQuery("SELECT * FROM StudyGroup");
               while (resultSet.next()) {
                  Object[] row = {resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),resultSet.getString(4)
                          , resultSet.getString(5),resultSet.getString(6),resultSet.getString(7),resultSet.getString(8),resultSet.getString(9),
                          resultSet.getString(10),resultSet.getString(11),resultSet.getString(12),resultSet.getString(13),resultSet.getString(14),
                          resultSet.getString(15)};
                  model.addRow(row);
               }
               connection.close();
            } catch (Exception e) {
               e.printStackTrace();
            }
            JScrollPane scrollPane = new JScrollPane(table);
            frame.add(BorderLayout.CENTER,scrollPane);
            frame.add(BorderLayout.NORTH, mb);

            frame.setVisible(true);
   }
}
