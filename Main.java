package com.Connections;

import java.io.IOException;
import java.util.Date;
import java.util.Formatter;
import java.util.Scanner;

import java.sql.*;

/**
 * Getter links on pairs
 * Sqlite link: https: //mvnrepository.com/artifact/org.xerial/sqlite-jdbc
 * In the 'db' folder you can find a test database with which you can do everything you need
 * The program has basic methods for getting and editing data from the database
 * AUTHOR: Dmitry Protasevich(Archiprogrammer22)
 */


public class Main {

    static Scanner scanner = new Scanner(System.in);
    static Date date = new Date();

    static Statement statement;
    static ResultSet resultSet;

    protected static Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:groups.db");
    }

    public static void getLinks() throws SQLException{
        System.out.print("Enter group: ");
        String group = scanner.next();

        Formatter sql = new Formatter();
        sql.format("SELECT * FROM %s WHERE id = ?", group);

        PreparedStatement preparedStatement = connect().prepareStatement(sql.toString());

        int day = date.getDay();
        preparedStatement.setInt(1, day);
        resultSet  = preparedStatement.executeQuery();

        //output info, if all columns with pairs have link.
        //If you don't have a several pairs, input something in the column.
        //For example "no pair" or just space
        
        while (resultSet.next())    
            System.out.println(
                            "1. " + resultSet.getString("first") +"\n"+
                            "2. " + resultSet.getString("second") +"\n"+
                            "3. " + resultSet.getString("third") +"\n"+
                            "4. " + resultSet.getString("fourth")
            );
        System.out.println("\n");
    }

    public static void createTable() throws SQLException{
        Formatter sql = new Formatter();
        statement = connect().createStatement();

        System.out.print("Enter name of new group: ");
        String name = scanner.next();
        sql.format("CREATE TABLE IF NOT EXISTS %s(" +
                "id INTEGER NOT NULL," +
                "day TEXT NOT NULL," +
                "first TEXT," +
                "second TEXT," +
                "third TEXT," +
                "fourth TEXT," +
                "PRIMARY KEY(id AUTOINCREMENT))", name);
        statement.execute(sql.toString());
        System.out.println("\n");
    }

    public static void updateLinks() throws SQLException{
        Formatter sql = new Formatter();
        System.out.print("Enter group: ");
        String group = scanner.next();
        System.out.print("Enter day:(1-5) ");
        int day = scanner.nextInt();

        sql.format("UPDATE %s SET first = ?, second = ?, third = ?, fourth = ? WHERE id = %d",
                group, day);
        PreparedStatement preparedStatement = connect().prepareStatement(sql.toString());

        String[] links = new String[4];
        for(int i = 0; i < links.length; i++){
            System.out.print("Enter " + (i+1) + " link: ");
            links[i] = scanner.next();
            preparedStatement.setString(i+1, links[i]);
        }
        preparedStatement.executeUpdate();
        System.out.println("\n");
    }

    public static void insertDay() throws SQLException{
        Formatter sql = new Formatter();
        System.out.print("Enter group: ");
        String group = scanner.next();
        System.out.print("Enter day: ");
        String day = scanner.next();

        sql.format("INSERT INTO %s(day, first, second, third, fourth) VALUES(?, ?, ?, ?, ?)", group);
        PreparedStatement preparedStatement = connect().prepareStatement(sql.toString());
        preparedStatement.setString(1, day);

        String[] links = new String[4];
        for(int i = 0; i < links.length; i++){
            System.out.print("Enter " + (i+1) + " link: ");
            links[i] = scanner.next();
            preparedStatement.setString(i+2, links[i]);
        }
        preparedStatement.executeUpdate();
        System.out.println("\n");
    }

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            try {
                System.out.println("1 - Get Links\n2 - Update links \n3 - Create new group \n4 - Insert\n9 - Exit");
                int command = scanner.nextInt();
                switch (command) {
                    case 1 -> getLinks();
                    case 2 -> updateLinks();
                    case 3 -> createTable();
                    case 4 -> insertDay();
                    case 9 -> exit = true;
                    default -> throw new IOException();
                }
            } catch (SQLException sqlEr) {
                sqlEr.printStackTrace();
            } catch (IOException ioEr) {
                System.out.println("Enter data is not correct");
            }
        }
    }
}
