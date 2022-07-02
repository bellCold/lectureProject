package controller;

import connector.DBConnector;
import model.GradeDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GradeController {
    private Connection conn;

    public GradeController(DBConnector connector) {
        conn = connector.makeConnector();
    }


    public ArrayList<GradeDTO> selectSubject(int subjectId) {
        String query = "SELECT * FROM grade WHERE subjectid = ?";
        ArrayList<GradeDTO> list = new ArrayList<>();
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, subjectId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                GradeDTO g = new GradeDTO();
                g.setId(rs.getInt("id"));
                g.setUserId(rs.getInt("userid"));
                g.setUserName(rs.getString("username"));
                g.setSubjectId(rs.getInt("subjectid"));
                g.setScore(rs.getInt("score"));
                list.add(g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void register(GradeDTO g) {
        String query = "INSERT INTO grade(userid, subjectid, score, username) VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, g.getUserId());
            pstmt.setInt(2, g.getSubjectId());
            pstmt.setInt(3, g.getScore());
            pstmt.setString(4, g.getUserName());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean selectOne(int userId, int subjectId) {
        String query = "SELECT * FROM grade WHERE userid =? AND subjectid = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, subjectId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void registerScore(int userId, int subjectId, int score) {
        String query = "UPDATE grade SET score = ? WHERE subjectid = ? AND userid = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, score);
            pstmt.setInt(2, subjectId);
            pstmt.setInt(3, userId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkDup(int userId, int subjectId) {
        String query = "SELECT score FROM grade WHERE userid = ? AND subjectid = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, subjectId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                GradeDTO g = new GradeDTO();
                g.setScore(rs.getInt("score"));
                if (g.getScore() == 0) {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }


}
