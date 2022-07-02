package controller;

import connector.DBConnector;
import model.StudentLectureDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentLectureController {
    private Connection conn;

    public StudentLectureController(DBConnector connector) {
        conn = connector.makeConnector();
    }


    public void register(StudentLectureDTO s) {
        String query = "INSERT INTO studentlecture(subjectname, userid, professorname, subjecttime, subjectid, studentname ) VALUES (?, ?, ?, ?, ?,?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, s.getSubjectName());
            pstmt.setInt(2, s.getUserId());
            pstmt.setString(3, s.getProfessorName());
            pstmt.setString(4, s.getSubjectTime());
            pstmt.setInt(5, s.getSubjectId());
            pstmt.setString(6, s.getStudentName());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<StudentLectureDTO> selectAll() {
        String query = "SELECT * FROM studentlecture";
        ArrayList<StudentLectureDTO> list = new ArrayList<>();
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                StudentLectureDTO s = new StudentLectureDTO();
                s.setSubjectId(rs.getInt("subjectid"));
                s.setId(rs.getInt("id"));
                s.setUserId(rs.getInt("userid"));
                s.setProfessorName(rs.getString("professorname"));
                s.setSubjectTime(rs.getString("subjecttime"));
                s.setSubjectName(rs.getString("subjectname"));
                s.setStudentName(rs.getString("studentname"));
                list.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public StudentLectureDTO checkLecture(int id) {
        String query = "SELECT * FROM studentlecture WHERE userid = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1,id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                StudentLectureDTO s = new StudentLectureDTO();
                s.setUserId(rs.getInt("userid"));
                return s;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public StudentLectureDTO selectLecture(int subjectId) {
        String query = "SElECT * FROM studentlecture WHERE subjectid = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1,subjectId);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                StudentLectureDTO s = new StudentLectureDTO();
                s.setUserId(rs.getInt("subjectid"));
                return s;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete(int id, int userId) {
        String query = "DELETE FROM studentlecture WHERE subjectid = ? AND userid = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            pstmt.setInt(2, userId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkRegisterLecture(int id, int subjectId) {
        String query = "SELECT * FROM studentlecture WHERE userid = ? AND subjectid = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1,id);
            pstmt.setInt(2,subjectId);
            ResultSet rs =pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public ArrayList<StudentLectureDTO> selectLectureStudent(int subjectId) {
        String query = "SELECT * FROM studentlecture WHERE subjectid = ?";
        ArrayList<StudentLectureDTO> list = new ArrayList<>();
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1,subjectId);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                StudentLectureDTO s = new StudentLectureDTO();
                s.setUserId(rs.getInt("userid"));
                s.setStudentName(rs.getString("studentname"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
