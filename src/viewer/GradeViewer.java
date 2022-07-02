package viewer;

import connector.DBConnector;
import controller.GradeController;
import controller.SubjectController;
import model.GradeDTO;
import model.SubjectDTO;
import model.UserDTO;
import util.ScannerUtil;

import java.util.ArrayList;
import java.util.Scanner;

public class GradeViewer {
    private Scanner scanner;
    private DBConnector connector;
    private UserDTO logIn;

    public GradeViewer(Scanner scanner, DBConnector connector, UserDTO logIn) {
        this.scanner = scanner;
        this.connector = connector;
        this.logIn = logIn;
    }

    public void printOneLectureGrade(int subjectId) {
        GradeController gradeController = new GradeController(connector);
        ArrayList<GradeDTO> list = gradeController.selectSubject(subjectId);

        for (GradeDTO g : list) {
            String score;
            if (g.getScore() == 0) {
                score = "점수가 없습니다.";
                System.out.printf("학생번호 %d. 학생 이름: %s 학생 점수: %s\n", g.getUserId(), g.getUserName(), score);
            } else {
                System.out.printf("학생번호 %d. 학생 이름: %s 학생 점수: %d\n", g.getUserId(), g.getUserName(), g.getScore());
            }
        }

        int userChoice = ScannerUtil.nextInt(scanner, "점수 등록할 학생을 선택해주세요 / 뒤로가기 0");
        while (userChoice != 0 && gradeController.selectOne(userChoice, subjectId)) {
            String yesNo = ScannerUtil.nextLine(scanner, "잘못입력하셨습니다. 다시입력하시겠습니까 Y/N");
            if (yesNo.equalsIgnoreCase("Y")) {
                userChoice = ScannerUtil.nextInt(scanner, "점수 등록할 학생을 선택해주세요 / 뒤로가기 0");
            }
        }
        if (userChoice != 0) {
            registerScore(userChoice, subjectId);
        }

    }

    private void registerScore(int userId, int subjectId) {
        GradeController gradeController = new GradeController(connector);
        int score = ScannerUtil.nextInt(scanner, "점수를 등록해주세요.", 0, 100);
        if (gradeController.checkDup(userId, subjectId)) {
            System.out.println("이미 점수등록아 되어있습니다.");
        } else {
            gradeController.registerScore(userId, subjectId, score);
            System.out.println("점수등록이 완료되었습니다.");
        }

    }

/*asdfjkbasdufasdfohasdioufhasoidfhaoisdfhaosidfhausidof*/
    public void showGrade() {
        SubjectController controller = new SubjectController(connector);
        ArrayList<SubjectDTO> list = controller.selectAll();
        if (list.isEmpty()) {
            System.out.println("수강중인 강의가 없습니다.");
        } else {
            for (SubjectDTO s : list) {
                System.out.printf("%d. 강의 교수: %s 강의 이름: %s 강의 시간: %s\n", s.getId(), s.getProfessorName(), s.getSubjectName(), s.getSubjectTime());
            }
            int userChoice = ScannerUtil.nextInt(scanner, "성적 보실 강의 번호를 입력해주세요. /뒤로가기 0");
            while (userChoice != 0 && ) {
                String yesNo = ScannerUtil.nextLine(scanner, "잘못입력하셨습니다. 다시입력하시겠습니까 Y/N");
                if (yesNo.equalsIgnoreCase("Y")) {
                    userChoice = ScannerUtil.nextInt(scanner, "성적 보실 강의 번호를 입력해주세요. /뒤로가기 0");
                }
            }
            if (userChoice != 0) {
                (userChoice);
            }
        }


    }
}
