package model;

public class GradeDTO {
    private int id;
    private int userId;
    private int subjectId;
    private int score;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean equals(Object o) {
        if (o instanceof GradeDTO) {
            GradeDTO g = (GradeDTO) o;
            return id == g.id;
        }
        return false;
    }
}
