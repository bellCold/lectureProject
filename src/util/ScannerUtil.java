package util;

import java.util.Scanner;

public class ScannerUtil {

    private static final Scanner scanner = new Scanner(System.in);
    public static void printMessage(String message) {
        System.out.println("--------------");
        System.out.println(message);
        System.out.println("--------------");
        System.out.print("> ");
    }
    public static String nextLine(Scanner scanner, String message) {
        printMessage(message);
        String temp = scanner.nextLine();
        //String 변수/상수 혹은 값의경우
        //isEmpty() 메소르를 실행시키면
        //아무런 값도 없으면 true, 아무글자가 하나라도 있으면 false가 티런된다.
        if(temp.isEmpty()) {
            temp = scanner.nextLine();
        }
        return temp;
    }

    public static String nextLine() {
        String temp = scanner.nextLine();
        if(temp.isEmpty()) {
            temp = scanner.nextLine();
        }
        return temp;
    }

    // int 를 nextInt()
    public static int nextInt(Scanner scanner, String message) {
        printMessage(message);
        int temp = scanner.nextInt();
        return temp;
    }

    public static int nextInt() {
        return scanner.nextInt();
    }

    public static int nextInt(Scanner scanner, String message, int min, int max) {
        int temp = nextInt(scanner, message);
        while (temp < min || temp > max) {
            System.out.println("잘못입력하셨습니다.");
            temp = nextInt(scanner, message);
        }
        return temp;
    }
}
