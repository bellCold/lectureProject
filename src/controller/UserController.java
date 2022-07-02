package controller;

import connector.DBConnector;
import model.UserDTO;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserController {
    private Connection conn;

    public UserController(DBConnector connector) {
        conn = connector.makeConnector();
    }

    private final int PROFESSOR = 1;
    private final int EMPLOYEE = 2;
    private final int STUDENT = 3;

    /*로그인*/
    public UserDTO logIn(String userid, String userpassword) {
        String query = "SELECT * FROM user WHERE userid = ? AND password = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, userid);
            pstmt.setString(2, convertToSha(userpassword));

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                UserDTO u = new UserDTO();
                u.setId(rs.getInt("id"));
                u.setUserId(rs.getString("userid"));
                u.setUserPassword(rs.getString("password"));
                u.setUserName(rs.getString("username"));
                u.setUserGrade(rs.getInt("usergrade"));
                return u;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*sha2 암호화*/
    public String convertToSha(String password) {
        String converted = null;
        StringBuilder builder = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));

            builder = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                builder.append(String.format("%02x", 255 & hash[i]));
            }
            converted = builder.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return converted;
    }

    /*회원등록*/
    public boolean register(UserDTO u) {
        String query = "INSERT INTO user(userid, password, username, usergrade) VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, u.getUserId());
            pstmt.setString(2, convertToSha(u.getUserPassword()));
            pstmt.setString(3, u.getUserName());
            pstmt.setInt(4, u.getUserGrade());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public void update(UserDTO u) {
        String query = "UPDATE user SET password = ?, username = ? WHERE id = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, convertToSha(u.getUserPassword()));
            pstmt.setString(2, u.getUserName());
            pstmt.setInt(3, u.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String query = "DELETE FROM user WHERE id = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<UserDTO> selectAll() {
        String query = "SELECT * FROM user";
        ArrayList<UserDTO> list = new ArrayList<>();
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                UserDTO u = new UserDTO();
                u.setId(rs.getInt("id"));
                u.setUserId(rs.getString("userid"));
                u.setUserGrade(rs.getInt("usergrade"));
                u.setUserName(rs.getString("username"));
                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<UserDTO> selectUser(int userGrade) {
        String query = "SELECT * FROM user WHERE usergrade = ?";
        ArrayList<UserDTO> list = new ArrayList<>();

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userGrade);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                UserDTO u = new UserDTO();
                u.setId(rs.getInt("id"));
                u.setUserName(rs.getString("username"));
                u.setUserGrade(rs.getInt("usergrade"));
                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public UserDTO selectOne(int id) {
        String query = "SELECT * FROM user WHERE id = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                UserDTO u = new UserDTO();
                u.setId(rs.getInt("id"));
                u.setUserName(rs.getString("username"));
                u.setUserGrade(rs.getInt("usergrade"));
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
