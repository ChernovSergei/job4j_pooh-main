package ru.job4j.mult;

import java.util.Scanner;

public class MultiplicationTest {
    private static int timerPreset = 5;
    private static Scanner scanner = new Scanner(System.in);
    private static MultiplicationGenerator example = new MultiplicationGenerator();
    private static ExerciseTimer exerciseTimer = new ExerciseTimer(timerPreset);

    public static void printMenu() {
        System.out.println("Enter 1 to solve an example");
        System.out.println("Enter 2 to enter timer set point (default is 5 sec)");
        System.out.println("Enter 6 to solve an exit");
    }

    public static boolean exerciseMultiplication() {
        System.out.println(example.generateExample());
        exerciseTimer.setStatus(true);
        while (exerciseTimer.getStatus()) {
            example.getAnswer();
            if (example.checkAnswer() && exerciseTimer.getStatus()) {
                exerciseTimer.setStatus(false);
                return true;
            } else if (exerciseTimer.getStatus()) {
                System.out.println("WRONG!!! You still have a time to answer.");
            }
        }
        return false;
    }

    public static void main(String[] args) throws InterruptedException {
        int replyNumber = 0;
        Thread timerThread = new Thread(exerciseTimer);
        timerThread.start();
        exerciseTimer.switchON();
        while (replyNumber != 6) {
            printMenu();
            replyNumber = scanner.nextInt();
            if (replyNumber == 1) {
                String message;
                String correctAnswer = "CORRECT!!! You've answered on time";
                String didntAnswer = "You didn't answer on time\n";
                message = exerciseMultiplication() ? correctAnswer : didntAnswer;
                System.out.println(message);
            }
            if (replyNumber == 2) {
                System.out.println("Enter timer set point in seconds");
                timerPreset = scanner.nextInt();
                exerciseTimer.preSetTimer(timerPreset);
            }
            if (replyNumber == 6) {
                exerciseTimer.setStatus(false);
            }
        }
        exerciseTimer.switchOFF();
        timerThread.join();
        System.out.println("End of multiplication exercise");
    }
}
