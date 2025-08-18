package ru.job4j.mult;

import java.util.Random;
import java.util.Scanner;

public class MultiplicationGenerator {
    private int answer;
    private int multiplication;

    public String generateExample() {
        Random rand = new Random();
        int multiplicand = rand.nextInt(10) + 1;
        int multiplier = rand.nextInt(10) + 1;
        multiplication = multiplicand * multiplier;
        return multiplicand + " * " + multiplier + " = ";
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public boolean checkAnswer() {
        return answer == multiplication;
    }
}
