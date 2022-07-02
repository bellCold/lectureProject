package controller;

import connector.DBConnector;
import model.SubjectDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SubjectController {
    private Connection conn;

    public SubjectController(DBConnector connector) {
        conn = connector.makeConnector();
    }

    public SubjectDTO selectOne(int id) {
        String query = "SELECT * FROM  subject WHERE id = ?";
        SubjectDTO s = null;
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                s = new SubjectDTO();
                s.setId(rs.getInt("id"));
                s.setSubjectName(rs.getString("subjectname"));
                s.setProfessorName(rs.getString("professorname"));
                s.setProfessorId(rs.getInt("professorid"));
                s.setSubjectTime(rs.getString("subjecttime"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return s;
    }

    public ArrayList<SubjectDTO> selectAll() {
        String query = "SELECT * FROM subject";
        ArrayList<SubjectDTO> list = new ArrayList<>();
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                SubjectDTO s = new SubjectDTO();
                s.setId(rs.getInt("id"));
                s.setSubjectName(rs.getString("subjectname"));
                s.setProfessorName(rs.getString("professorname"));
                s.setProfessorId(rs.getInt("professorid"));
                s.setSubjectTime(rs.getString("subjecttime"));

                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void delete(int id) {
        String query = "DELETE FROM subject WHERE id = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void update(SubjectDTO s) {
        String query = "UPDATE subject SET subjectname = ? , subjecttime = ? WHERE id = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, s.getSubjectName());
            pstmt.setString(2, s.getSubjectTime());
            pstmt.setInt(3, s.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void register(SubjectDTO s) {
        String query = "INSERT INTO subject(subjectname, professorname, professorid, subjecttime) VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, s.getSubjectName());
            pstmt.setString(2, s.getProfessorName());
            pstmt.setInt(3, s.getProfessorId());
            pstmt.setString(4, s.getSubjectTime());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkMyLecture(int id) {
        String query = "SELECT * FROM subject WHERE professorid = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }


/*
    public ArrayList<SubjectDTO> selectStuLecture(int userid) {
        String query = "SELECT * FROM  subject WHERE userid = ?";
        ArrayList<SubjectDTO> list = new ArrayList<>();
        SubjectDTO s = null;
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userid);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                s = new SubjectDTO();
                s.setId(rs.getInt("id"));
                s.setSubjectName(rs.getString("subjectname"));
                s.setProfessorName(rs.getString("professorname"));
                s.setProfessorId(rs.getInt("professorid"));
                s.setSubjectTime(rs.getString("subjecttime"));
                s.setUserId(rs.getInt("userid"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
*/

}
