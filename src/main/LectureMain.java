package main;

import connector.MySqlConnector;
import viewer.UserViewer;

import java.util.Scanner;

public class LectureMain {
    public static void main(String[] args) {
        UserViewer userViewer = new UserViewer(new Scanner(System.in), new MySqlConnector());
        userViewer.showIndex();
    }
}
