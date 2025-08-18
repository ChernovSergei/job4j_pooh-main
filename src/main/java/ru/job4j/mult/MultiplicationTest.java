package ru.job4j.mult;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MultiplicationTest {
    private static int timerPreset = 5;
    private static Scanner scanner = new Scanner(System.in);
    private static MultiplicationGenerator example = new MultiplicationGenerator();
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void printMenu() {
        System.out.println("Enter 1 to solve an example");
        System.out.println("Enter 2 to enter timer set point (default is 5 sec)");
        System.out.println("Enter 6 to solve an exit");
    }

    public static void main(String[] args) {
        int replyNumber = 0;
        ScheduledExecutorService timerService = new ScheduledThreadPoolExecutor(1);
        while (replyNumber != 6) {
            printMenu();
            replyNumber = scanner.nextInt();
            if (replyNumber == 1) {
                Alarm alarm = new Alarm();
                alarm.switchAlarm(true);
                String message = "";
                String correctAnswer = "CORRECT!!! You've answered on time";
                String didntAnswer = "You didn't answer on time\n";
                System.out.println(example.generateExample());
                timerService.schedule(alarm, timerPreset, TimeUnit.SECONDS);
                example.getAnswer();
                message = (example.checkAnswer() && alarm.getStatus()) ? correctAnswer : didntAnswer;
                System.out.println(message);
            }
            if (replyNumber == 2) {
                System.out.println("Enter timer set point in seconds");
                timerPreset = scanner.nextInt();
            }
            if (replyNumber == 6) {
                timerService.shutdown();
            }
        }
        System.out.println("End of multiplication exercise");
    }
}
