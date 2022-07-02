package viewer;

import connector.DBConnector;
import controller.StudentLectureController;
import controller.SubjectController;
import controller.UserController;
import model.StudentLectureDTO;
import model.SubjectDTO;
import model.UserDTO;
import util.ScannerUtil;

import java.util.ArrayList;
import java.util.Scanner;

public class UserViewer {
    private Scanner scanner;
    private DBConnector connector;
    private UserDTO logIn;
    private final int PROFESSOR = 1;
    private final int EMPLOYEE = 2;
    private final int STUDENT = 3;

    public UserViewer(Scanner scanner, DBConnector connector) {
        this.scanner = scanner;
        this.connector = connector;
    }

    public void showIndex() {
        while (true) {
            int userChoice = ScannerUtil.nextInt(scanner, "1.로그인 2.회원가입 3.종료");
            if (userChoice == 1) {
                logIn();
                showUserMenu();
            } else if (userChoice == 2) {
                register();
            } else if (userChoice == 3) {
                System.out.println("사용해주셔서 감사합니다");
                break;
            }
        }
    }

    private void logIn() {
        String userId = ScannerUtil.nextLine(scanner, "아이디를 입력해주세요");
        String userPassword = ScannerUtil.nextLine(scanner, "비밀번호를 입력해주세요.");
        UserController controller = new UserController(connector);
        logIn = controller.logIn(userId, userPassword);

        while (logIn == null) {
            String yesNo = ScannerUtil.nextLine(scanner, "잘못된 정보입니다. 다시 입력하시겠습니까? Y/N");
            if (yesNo.equalsIgnoreCase("N")) {
                break;
            }
            userId = ScannerUtil.nextLine(scanner, "아이디를 입력해주세요");
            userPassword = ScannerUtil.nextLine(scanner, "비밀번호를 입력해주세요.");
            logIn = controller.logIn(userId, userPassword);
        }

        if (logIn != null) {
            System.out.println("로그인 되었습니다.");
        }

    }

    private void register() {
        String userId = ScannerUtil.nextLine(scanner, "아이디를 입력해주세요");
        String userPassword = ScannerUtil.nextLine(scanner, "비밀번호를 입력해주세요.");
        String userName = ScannerUtil.nextLine(scanner, "성함을 입력해주세요.");
        int userGrade = ScannerUtil.nextInt(scanner, "정보를 입력해주세요. 1 => 교수 ,2 => 직원, 3 => 학생", 1, 3);

        UserDTO u = new UserDTO();
        u.setUserId(userId);
        u.setUserPassword(userPassword);
        u.setUserName(userName);
        u.setUserGrade(userGrade);

        UserController controller = new UserController(connector);
        while (!controller.register(u)) {
            String yesNo = ScannerUtil.nextLine(scanner, "잘못된 정보입니다. 다시 입력하시겠습니까? Y/N");
            if (yesNo.equalsIgnoreCase("N")) {
                break;
            }
            u.setUserId(ScannerUtil.nextLine(scanner, "새로운 아이디를 입력해주세요"));
        }
    }

    private void showUserMenu() {
        if (logIn != null && logIn.getUserGrade() == PROFESSOR) {
            showProMenu();
        } else if (logIn != null && logIn.getUserGrade() == EMPLOYEE) {
            showEmpMenu();
        } else if (logIn != null && logIn.getUserGrade() == STUDENT) {
            showStuMenu();
        }
    }

    public void showProMenu() {
        while (logIn != null) {
            int userChoice = ScannerUtil.nextInt(scanner, "1.회원 정보 보기 2.강의 및 학생 성적 3.로그아웃");
            if (userChoice == 1) {
                printOne();
            } else if (userChoice == 2) {
                SubjectViewer subjectViewer = new SubjectViewer(scanner, connector, logIn);
                subjectViewer.showProMenu();
            } else if (userChoice == 3) {
                logOut();
            }
        }

    }

    public void showEmpMenu() {
        int userChoice = ScannerUtil.nextInt(scanner, "1.내 정보 보기 2.등록된 전체 회원 보기 3.강의리스트보기 4.로그아웃");
        if (userChoice == 1) {
            printOne();
        } else if (userChoice == 2) {
            showTotalUserList();
        } else if (userChoice == 3) {
            SubjectViewer subjectViewer = new SubjectViewer(scanner, connector, logIn);
            subjectViewer.printLectureList();
        } else if (userChoice == 4) {
            logOut();
        }

    }

    public void showStuMenu() {
        while (logIn != null) {
            int userChoice = ScannerUtil.nextInt(scanner, "1.내 정보보기 2.수강 관련 목록 보기 3.로그아웃");
            if (userChoice == 1) {
                printOne();
            } else if (userChoice == 2) {
                SubjectViewer subjectViewer = new SubjectViewer(scanner, connector, logIn);
                subjectViewer.showStuMenu();
            } else if (userChoice == 3) {
                logOut();
            }
        }
    }

    private void printOne() {
        System.out.println(logIn.getUserGrade());
        System.out.printf("아이디: %s 이름: %s 직책: %s\n", logIn.getUserId(), logIn.getUserName(), logInUserGrade());
        int userChoice = ScannerUtil.nextInt(scanner, "1.정보 수정하기 2.탈퇴하기 3.뒤로가기");
        if (userChoice == 1) {
            update();
        } else if (userChoice == 2) {
            delete();
        } else if (userChoice == 3) {
            showUserMenu();
        }
    }

    private void showTotalUserList() {
        UserController controller = new UserController(connector);
        ArrayList<UserDTO> list = controller.selectAll();
        for (UserDTO u : list) {
            String grade = "";
            if (u.getUserGrade() == 1) {
                grade = "교수";
            } else if (u.getUserGrade() == 2) {
                grade = "직원";
            } else if (u.getUserGrade() == 3) {
                grade = "학생";
            }
            System.out.printf("%d. 아이디: %s 이름: %s 직책: %s\n", u.getId(), u.getUserId(), u.getUserName(), grade);
        }
        int userChoice = ScannerUtil.nextInt(scanner, "1.교수진 보기 2.학생진 보기 3.뒤로가기");
        if (userChoice == 1) {
            selectUser(PROFESSOR);
        } else if (userChoice == 2) {
            selectUser(STUDENT);
        } else if (userChoice == 3) {
            showEmpMenu();
        }


    }

    private void selectUser(int userGrade) {
        UserController controller = new UserController(connector);
        ArrayList<UserDTO> list = controller.selectUser(userGrade);
        for (UserDTO u : list) {
            String grade = "";
            if (u.getUserGrade() == 1) {
                grade = "교수";
            } else if (u.getUserGrade() == 2) {
                grade = "직원";
            } else if (u.getUserGrade() == 3) {
                grade = "학생";
            }
            System.out.printf("%d. 이름: %s 직책: %s\n", u.getId(), u.getUserName(), grade);
        }

        if (userGrade == PROFESSOR) {
            printProfessorOneInfo();
        } else if (userGrade == STUDENT) {
            printStudentOneInfo();
        }
    }

    private void printProfessorOneInfo() {
        int userChoice = ScannerUtil.nextInt(scanner, "상세보기할 교수번호  /0 -> 뒤로가기");
        UserController userController = new UserController(connector);
        UserDTO u = userController.selectOne(userChoice);
        System.out.printf("%d. 이름: %s \n", u.getId(), u.getUserName());
        SubjectController controller = new SubjectController(connector);

        if (controller.checkMyLecture(userChoice)) {
            System.out.println("해당 교수는 현재 등록된 강의가 없습니다.");
        } else {
            ArrayList<SubjectDTO> list = controller.selectAll();
            System.out.println("---------강의 리스트----------");
            for (SubjectDTO s : list) {
                if (s.getProfessorId() == userChoice) {
                    System.out.printf("%d. 강의 이름:  %s 강의 시간: %s\n", s.getId(), s.getSubjectName(), s.getSubjectTime());
                }
            }
        }
        showEmpMenu();
    }

    private void printStudentOneInfo() {
        UserController controller = new UserController(connector);
        int userChoice = ScannerUtil.nextInt(scanner, "상세보기할 학생번호  /0 -> 뒤로가기");
        UserDTO u = controller.selectOne(userChoice);
        System.out.printf("%d. 이름: %s \n", u.getId(), u.getUserName());
        StudentLectureController studentLectureController = new StudentLectureController(connector);
        if (studentLectureController.checkLecture(userChoice) == null) {
            System.out.println("해당 학생은 현재 수강중인 강의가 없습니다");
        } else {
            ArrayList<StudentLectureDTO> list = studentLectureController.selectAll();
            System.out.println("---------강의 리스트----------");
            for (StudentLectureDTO s : list) {
                if (s.getUserId() == userChoice) {
                    System.out.printf("%d. 강의 이름: %s 교수 이름 %s 강의 시간: %s\n", s.getSubjectId(), s.getSubjectName(), s.getProfessorName(), s.getSubjectTime());
                }
            }
        }
        showEmpMenu();
    }

    private void delete() {
        UserController controller = new UserController(connector);
        if (controller.convertToSha(doubleCheckPassword()).equals(logIn.getUserPassword())) {
            String yesNo = ScannerUtil.nextLine(scanner, "정말로 탈퇴하시겠습니까? Y/N");
            if (yesNo.equalsIgnoreCase("N")) {
                printOne();
            } else {
                controller.delete(logIn.getId());
                System.out.println("아이디가 삭제되었습니다.");
                logIn = null;
            }
        }
    }

    private void update() {
        UserController controller = new UserController(connector);
        String newUserName = ScannerUtil.nextLine(scanner, "새로운 이름을 입력해주세요.");
        String newPassword = ScannerUtil.nextLine(scanner, "새로운 비밀번호를 입력해주세요.");
        if (controller.convertToSha(doubleCheckPassword()).equals(logIn.getUserPassword())) {
            logIn.setUserPassword(newPassword);
            logIn.setUserName(newUserName);
            controller.update(logIn);
            System.out.println("회원 정보가 수정 완료 되었습니다. 다시 로그인해주세요.");
            logIn = null;
        }

    }

    private String doubleCheckPassword() {
        UserController controller = new UserController(connector);
        String oldPassword = ScannerUtil.nextLine(scanner, "기존 비밀번호를 입력해주세요.");
        while (!controller.convertToSha(oldPassword).equals(logIn.getUserPassword())) {
            String yesNo = ScannerUtil.nextLine(scanner, "비밀번호가 일치하지 않습니다 다시입력하시겠습니까 Y/N");
            if (yesNo.equalsIgnoreCase("N")) {
                break;
            }
            oldPassword = ScannerUtil.nextLine(scanner, "기존 비밀번호를 입력해주세요.");
        }
        return oldPassword;
    }

    private String logInUserGrade() {
        if (logIn.getUserGrade() == 1) {
            return "교수";
        } else if (logIn.getUserGrade() == 2) {
            return "직원";
        } else if (logIn.getUserGrade() == 3) {
            return "학생";
        }
        return null;
    }

    private void logOut() {
        System.out.println("로그아웃 되었습니다");
        logIn = null;
    }
}
