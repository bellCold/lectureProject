package viewer;

import connector.DBConnector;
import controller.GradeController;
import controller.StudentLectureController;
import controller.SubjectController;
import model.GradeDTO;
import model.StudentLectureDTO;
import model.SubjectDTO;
import model.UserDTO;
import util.ScannerUtil;

import java.util.ArrayList;
import java.util.Scanner;

public class SubjectViewer {
    private DBConnector connector;
    private Scanner scanner;
    private UserDTO logIn;

    public SubjectViewer(Scanner scanner, DBConnector connector, UserDTO logIn) {
        this.scanner = scanner;
        this.connector = connector;
        this.logIn = logIn;
    }

    /*     Professor       */
    public void showProMenu() {
        int userChoice = ScannerUtil.nextInt(scanner, "1.나의강의 리스트보기 2.강의 등록하기 3.학생 성적주기 4.뒤로가기");
        if (userChoice == 1) {
            myLectureList();
        } else if (userChoice == 2) {
            registerLecture();
        } else if (userChoice == 3) {
            /*************************/
        } else if (userChoice == 4) {
            UserViewer userViewer = new UserViewer(scanner, connector);
            userViewer.showProMenu();
        }

    }

    private void registerLecture() {
        SubjectController controller = new SubjectController(connector);
        SubjectDTO s = new SubjectDTO();
        s.setProfessorId(logIn.getId());
        s.setProfessorName(logIn.getUserName());
        s.setSubjectName(ScannerUtil.nextLine(scanner, "강의 이름을 입력해주세요."));
        s.setSubjectTime(ScannerUtil.nextLine(scanner, "강의 시간을 입력해주세요."));

        controller.register(s);

        showProMenu();
    }

    private void myLectureList() {
        SubjectController controller = new SubjectController(connector);
        ArrayList<SubjectDTO> list = controller.selectAll();
        if (controller.checkMyLecture(logIn.getId())) {
            System.out.println("등록된 나의 강의가 없습니다.");
            showProMenu();
        } else {
            System.out.println("---------강의 리스트----------");
            for (SubjectDTO s : list) {
                if (s.getProfessorId() == logIn.getId()) {
                    System.out.printf("%d. 강의 이름: %s 강의 시간: %s\n", s.getId(), s.getSubjectName(), s.getSubjectTime());
                }
            }
            int userChoice = ScannerUtil.nextInt(scanner, "1.강의 상세보기 2.강의 정정하기 /0 - > 뒤로가기");
            if (userChoice == 1) {
                showDetailLecture();
            } else if (userChoice == 2) {
                changeMyLecture();
            } else {
                showProMenu();
            }

        }

    }

    private void showDetailLecture() {
        SubjectController controller = new SubjectController(connector);
        int userChoice = ScannerUtil.nextInt(scanner, "상세보기할 강의번호를 입력해주세요. /0 -> 뒤로가기");
        while (userChoice != 0 && controller.selectOne(userChoice) == null) {
            String yesNo = ScannerUtil.nextLine(scanner, "잘못입력하셨습니다. 다시입력하시겠습니까 Y/N");
            if (yesNo.equalsIgnoreCase("Y")) {
                userChoice = ScannerUtil.nextInt(scanner, "상세보기할 강의번호를 입력해주세요. /0 -> 뒤로가기");
            }
            showProMenu();
        }
        if (userChoice != 0) {
            printStudentList(userChoice);
        }
    }

    private void printStudentList(int subjectId) {
        StudentLectureController studentLectureController = new StudentLectureController(connector);
        ArrayList<StudentLectureDTO> list = studentLectureController.selectLectureStudent(subjectId);
        if (list.isEmpty()) {
            System.out.println("현재 수강자가 없습니다.");
            showDetailLecture();
        } else {
            System.out.println("----------수강자 리스트------------");
            for (StudentLectureDTO s : list) {
                System.out.printf("학생 번호 *%d* 학생 이름: %s\n", s.getUserId(), s.getStudentName());
            }
            int userChoice = ScannerUtil.nextInt(scanner, "1.수강 학생 점수 보기 / 0 - > 뒤로가기");
            if (userChoice == 1) {
                GradeViewer gradeViewer = new GradeViewer(scanner, connector, logIn);
                gradeViewer.printOneLectureGrade(subjectId);
            }
            myLectureList();
        }

    }

    private void changeMyLecture() {
        SubjectController controller = new SubjectController(connector);
        int userChoice = ScannerUtil.nextInt(scanner, "정정 강의번호를 입력해주세요. /0 -> 뒤로가기");
        while (userChoice != 0 && controller.selectOne(userChoice) == null) {
            String yesNo = ScannerUtil.nextLine(scanner, "잘못입력하셨습니다. 다시입력하시겠습니까 Y/N");
            if (yesNo.equalsIgnoreCase("Y")) {
                userChoice = ScannerUtil.nextInt(scanner, "정정 강의번호를 입력해주세요. /0 -> 뒤로가기");
            }
            showProMenu();
        }
        if (userChoice != 0) {
            modify(userChoice);
        }
    }

    private void modify(int id) {
        int userChoice = ScannerUtil.nextInt(scanner, "1.강의 수정 2.강의 삭제 3.뒤로가기");
        if (userChoice == 1) {
            update(id);
        } else if (userChoice == 2) {
            delete(id);
        }
    }

    private void delete(int id) {
        SubjectController controller = new SubjectController(connector);
        String yesNo = ScannerUtil.nextLine(scanner, "정말로 삭제하시겟습니까? Y/N");
        if (yesNo.equalsIgnoreCase("Y")) {
            controller.delete(id);
        }
        myLectureList();
    }

    private void update(int id) {
        SubjectController controller = new SubjectController(connector);
        SubjectDTO s = controller.selectOne(id);
        s.setSubjectName(ScannerUtil.nextLine(scanner, "새로운 강의 이름을 입력주세요."));
        s.setSubjectTime(ScannerUtil.nextLine(scanner, "새로운 강의 시간을 입력할 주세요."));
        String yesNo = ScannerUtil.nextLine(scanner, "정말로 수정하시겟습니까? Y/N");
        if (yesNo.equalsIgnoreCase("Y")) {
            controller.update(s);
        }
        myLectureList();
    }


    /*     student    */
    public void showStuMenu() {
        int userChoice = ScannerUtil.nextInt(scanner, "1.수강 신청 2.내 수강 리스트 보기 3.내 성적 보기 4.뒤로가기", 1, 4);
        if (userChoice == 1) {
            printSubjectAll();
        } else if (userChoice == 2) {
            showStuSubjectList();
        } else if (userChoice == 3) {
            GradeViewer gradeViewer = new GradeViewer(scanner, connector, logIn);
            gradeViewer.showGrade();
        }

    }

    private void showStuSubjectList() {
        StudentLectureController studentLectureController = new StudentLectureController(connector);

        if (studentLectureController.checkLecture(logIn.getId()) == null) {
            System.out.println("현재 수강중인 강의가 없습니다");
        } else {
            showLectureList();
        }
        showStuMenu();
    }

    public void showLectureList() {
        StudentLectureController studentLectureController = new StudentLectureController(connector);
        ArrayList<StudentLectureDTO> list = studentLectureController.selectAll();
        System.out.println("---------강의 리스트----------");
        for (StudentLectureDTO s : list) {
            if (s.getUserId() == logIn.getId()) {
                System.out.printf("%d. 강의 이름: %s 교수 이름 %s 강의 시간: %s\n", s.getSubjectId(), s.getSubjectName(), s.getProfessorName(), s.getSubjectTime());
            }
        }
        int userChoice = ScannerUtil.nextInt(scanner, "수강 취소할 강의번호를 눌러주세요  / 0 - >뒤로가기");
        while (userChoice != 0 && studentLectureController.selectLecture(userChoice) == null) {
            String yesNo = ScannerUtil.nextLine(scanner, "잘못입력하셨습니다 다시 입력하시겠습니까? y/n");
            if (yesNo.equalsIgnoreCase("n")) {
                break;
            }
            userChoice = ScannerUtil.nextInt(scanner, "수강 취소할 강의번호를 눌러주세요  / 0 - >뒤로가기");
        }
        if (userChoice != 0) {
            deleteLecture(userChoice);
        }

    }

    private void deleteLecture(int id) {
        StudentLectureController studentLectureController = new StudentLectureController(connector);
        String yesNo = ScannerUtil.nextLine(scanner, "정말로 삭제하시겟습니까? Y/N");
        if (yesNo.equalsIgnoreCase("Y")) {
            studentLectureController.delete(id, logIn.getId());
        }
    }

    public void printSubjectAll() {
        SubjectController controller = new SubjectController(connector);
        ArrayList<SubjectDTO> list = controller.selectAll();
        if (list.isEmpty()) {
            System.out.println("현재 신청가능한 강의가 없습니다.");
        } else {
            System.out.println("---------강의 리스트----------");
            for (SubjectDTO s : list) {
                System.out.printf("%d. 강의 이름: %s 강의 시간: %s\n", s.getId(), s.getSubjectName(), s.getSubjectTime());
            }
            int userChoice = ScannerUtil.nextInt(scanner, "신청할 강의번호를 입력해주세요. /뒤로가기 0");
            while (userChoice != 0 && controller.selectOne(userChoice) == null) {
                String yesNo = ScannerUtil.nextLine(scanner, "잘못입력하셨습니다. 다시입력하시겠습니까 Y/N");
                if (yesNo.equalsIgnoreCase("Y")) {
                    userChoice = ScannerUtil.nextInt(scanner, "신청할 강의번호를 입력해주세요. /뒤로가기 0");
                }
            }
            if (userChoice != 0) {
                studentRegisterLecture(userChoice);
            }
        }
        showStuMenu();
    }

    private void studentRegisterLecture(int userChoice) {
        SubjectController controller = new SubjectController(connector);
        SubjectDTO s = controller.selectOne(userChoice);

        StudentLectureDTO studentLectureDTO = new StudentLectureDTO();

        studentLectureDTO.setUserId(logIn.getId());
        studentLectureDTO.setStudentName(logIn.getUserName());
        studentLectureDTO.setSubjectTime(s.getSubjectTime());
        studentLectureDTO.setProfessorName(s.getProfessorName());
        studentLectureDTO.setSubjectName(s.getSubjectName());
        studentLectureDTO.setSubjectId(userChoice);

        GradeController gradeController = new GradeController(connector);

        GradeDTO g = new GradeDTO();
        g.setUserId(logIn.getId());
        g.setUserName(logIn.getUserName());
        g.setSubjectId(s.getId());

        StudentLectureController studentLectureController = new StudentLectureController(connector);

        if (studentLectureController.checkRegisterLecture(logIn.getId(), userChoice)) {
            System.out.println("선택하실수 없는 강의입니다");
        } else {
            studentLectureController.register(studentLectureDTO);
            gradeController.register(g);
            System.out.println("수강신청이 완료되었습니다.");
        }

    }

    public void printLectureList() {
        SubjectController controller = new SubjectController(connector);
        ArrayList<SubjectDTO> list = controller.selectAll();
        if (list.isEmpty()) {
            System.out.println("강의 리스트가 없습니다.");
        } else {
            System.out.println("---------강의 리스트----------");
            for (SubjectDTO s : list) {
                System.out.printf("%d. 강의 교수: %s 강의 이름: %s 강의 시간: %s\n", s.getId(), s.getProfessorName(), s.getSubjectName(), s.getSubjectTime());
            }
        }
        UserViewer userViewer = new UserViewer(scanner, connector);
        userViewer.showEmpMenu();
    }
}