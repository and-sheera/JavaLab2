package vsu.shirnin.calc;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String expression = in.nextLine();
        System.out.println(Calc.calculate(expression));
    }
}
