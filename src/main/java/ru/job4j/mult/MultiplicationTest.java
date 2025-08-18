package ru.job4j.mult;

import java.util.Scanner;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MultiplicationTest {

    public static void printMenu() {
        System.out.println("Enter 1 to solve an example");
        System.out.println("Enter 2 to enter timer set point (default is 5 sec)");
        System.out.println("Enter 6 to solve an exit");
    }

    public static void main(String[] args) {
        ScheduledExecutorService timerService = new ScheduledThreadPoolExecutor(1);
        MultiplicationGenerator example = new MultiplicationGenerator();
        Alarm alarm = new Alarm();
        Scanner scanner = new Scanner(System.in);
        int timerPreset = 5;
        int replyNumber = 0;
        String correctAnswer = "CORRECT!!! You've answered on time";
        String didntAnswer = "You didn't answer on time\n";
        String answerRequest = "Enter your answer\n";

        while (replyNumber != 6) {
            printMenu();
            replyNumber = scanner.nextInt();
            if (replyNumber == 1) {
                String conclusion = "";
                int answer;

                alarm.switchAlarm(true);
                System.out.println(example.generateExample());
                System.out.println(answerRequest);
                timerService.schedule(alarm, timerPreset, TimeUnit.SECONDS);
                answer = scanner.nextInt();
                example.setAnswer(answer);
                conclusion = (example.checkAnswer() && alarm.getStatus()) ? correctAnswer : didntAnswer;
                System.out.println(conclusion);
                alarm.switchAlarm(false);
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