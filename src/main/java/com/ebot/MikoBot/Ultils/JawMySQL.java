package com.ebot.MikoBot.Ultils;

import com.ebot.MikoBot.Ultils.Entities.UserReference;
import com.ebot.MikoBot.Ultils.Entities.WordPair;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;

/**
 * Add functionality to reboot the bot on heroku without clearing user data
 */
public class JawMySQL {
    public static void addAcronym(String acronym, String formal) {
        try(Connection con = getConnection();
            Statement st = con.createStatement()) {            
            st.execute("insert into acronym(acronymWord, formalWord) values ('" + acronym + "','" + formal + "')");
        } catch (Exception ignored) {
            try (Connection con = getConnection();
                 Statement st = con.createStatement()) {
                st.execute("update acronym set formalWord = '" + formal + "' where acronymWord = '" + acronym + "'");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void removeAcronym(String acronym) {
        try (Connection con = getConnection();
             Statement st = con.createStatement()) {
            st.execute("delete from acronym where acronymWord = '" + acronym + "'");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static ArrayList<WordPair> loadAcronym() {
        ArrayList<WordPair> wordPairs = new ArrayList<>();
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("select * from acronym")) {
            while (rs.next()) {
                wordPairs.add(new WordPair(rs.getString(1), rs.getString(2)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return wordPairs;
    }

    public static void modifyUserReference(String userId, short voiceRef) {
        try (Connection con = getConnection();
            Statement st = con.createStatement()) {
            st.execute("insert into voiceRef(userId, voiceRef) values ('" + userId + "','" + voiceRef + "')");
        } catch (Exception ignored) {
            try (Connection con = getConnection();
            Statement st = con.createStatement()) {
                st.execute("update voiceRef set voiceRef = '" + voiceRef + "' where userId = '" + userId + "'");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static ArrayList<UserReference> loadUserReference() {
        ArrayList<UserReference> userReferences = new ArrayList<>();
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("select * from voiceRef")) {
            while (rs.next()) {
                short voiceRef = 1;
                try {
                    voiceRef = Short.parseShort(rs.getString(2));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                userReferences.add(new UserReference(rs.getString(1), voiceRef));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userReferences;
    }

    public static ArrayList<String> loadTable(String tableName) {
        ArrayList<String> strings = new ArrayList<>();
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("select * from " + tableName)) {
            while (rs.next()) {
                strings.add(rs.getString(1));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return strings;
    }

    public static void addToTable(String tableName, String userId) {
        try (Connection con = getConnection();
            Statement st = con.createStatement()) {
            st.execute("insert into " + tableName + "(userId) values (" + userId + ")");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void removeFromTable(String tableName, String userId) {
        try (Connection con = getConnection();
            Statement st = con.createStatement()) {
            st.execute("delete from " + tableName + " where userId = " + userId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static Connection getConnection() throws URISyntaxException, SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        URI jdbUri = new URI(System.getenv("JAWSDB_URL"));

        String username = jdbUri.getUserInfo().split(":")[0];
        String password = jdbUri.getUserInfo().split(":")[1];
        String port = String.valueOf(jdbUri.getPort());
        String jdbUrl = "jdbc:mysql://" + jdbUri.getHost() + ":" + port + jdbUri.getPath();

        return DriverManager.getConnection(jdbUrl, username, password);
    }
}