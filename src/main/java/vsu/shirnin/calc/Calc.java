package vsu.shirnin.calc;

import java.util.*;

/**
 * Parses a string of an arithmetic expression using reverse Polish notation and evaluates it
 */
public class Calc {
    /**
     * Operators used string
     */
    private static String operators = "+-*/^";

    /**
     * String with characters, used to parse a string
      */
    private static String delimiters = "() " + operators;

    /**
     * Flag - correctness of the entered expression
     */
    private static boolean isCorrectExpression = true;

    /**
     * The main method, parses the string and using the queue
     * @param expression string with arithmetic expression
     * @return solution expression
     */
    public static Double calculate(String expression) {
        List<String> postfix = parseRPN(expression);

        if (!isCorrectExpression) {
            throw new IllegalArgumentException("Expression is invalid");
        }

        Deque<Double> stack = new ArrayDeque<Double>();
        for (String x : postfix) {
            if (x.equals("sqrt")) stack.push(Math.sqrt(stack.pop()));
            else if (x.equals("+")) stack.push(stack.pop() + stack.pop());
            else if (x.equals("-")) {
                Double b = stack.pop(), a = stack.pop();
                stack.push(a - b);
            }
            else if (x.equals("*"))
                stack.push(stack.pop() * stack.pop());
            else if (x.equals("/")) {
                Double b = stack.pop(), a = stack.pop();
                stack.push(a / b);
            }
            else if (x.equals("^")) {
                Double b = stack.pop(), a = stack.pop();
                stack.push(Math.pow(a, b));
            }
            else stack.push(Double.valueOf(x));
        }
        return stack.pop();
    }

    /**
     * Verification of belonging to the correct signs
     * @param token string containing characters
     * @return false if no valid delimiters
     */
    private static boolean isDelimiter(String token) {
        if (token.length() != 1) return false;
        for (int i = 0; i < delimiters.length(); i++) {
            if (token.charAt(0) == delimiters.charAt(i)) return true;
        }
        return false;
    }

    /**
     * Operator sign check
     * @param token string containing characters
     * @return true if the string contains the operator
     */
    private static boolean isOperator(String token) {
        for (int i = 0; i < operators.length(); i++) {
            if (token.charAt(0) == operators.charAt(i)) return true;
        }
        return false;
    }

    /**
     * @param token string containing characters
     * @return true if the string is a function
     */
    private static boolean isFunction(String token) {
        if (token.equals("sqrt")) return true;
        return false;
    }

    /**
     * @param token string containing characterS
     * @return priority of the operation
     */
    private static int priority(String token) {
        if (token.equals("(")) return 1;
        if (token.equals("+") || token.equals("-")) return 2;
        if (token.equals("*") || token.equals("/")) return 3;
        if (token.equals("^")) return 4;
        return 5;
    }

    /**
     * Parses an arithmetic expression into reverse Polish notation
     * @param expression string containing an arithmetic expression
     * @return string with a reverse polish expression
     */
    private static List<String> parseRPN(String expression) {
        List<String> postfix = new ArrayList<String>();
        Deque<String> stack = new ArrayDeque<String>();
        StringTokenizer tokenizer = new StringTokenizer(expression, delimiters, true);
        String currentToken = "";
        while (tokenizer.hasMoreTokens()) {
            currentToken = tokenizer.nextToken();
            if (!tokenizer.hasMoreTokens() && isOperator(currentToken)) {
                isCorrectExpression = false;
                return postfix;
            }
            if (currentToken.equals(" "))
                continue;
            if (isFunction(currentToken))
                stack.push(currentToken);
            else if (isDelimiter(currentToken)) {
                if (currentToken.equals("("))
                    stack.push(currentToken);
                else if (currentToken.equals(")")) {
                    while (!stack.peek().equals("(")) {
                        postfix.add(stack.pop());
                        if (stack.isEmpty()) {
                            isCorrectExpression = false;
                            return postfix;
                        }
                    }
                    stack.pop();
                    if (!stack.isEmpty() && isFunction(stack.peek())) {
                        postfix.add(stack.pop());
                    }
                }
                else {
                    while (!stack.isEmpty() && (priority(currentToken) <= priority(stack.peek()))) {
                        postfix.add(stack.pop());
                    }
                    stack.push(currentToken);
                }

            }
            else {
                postfix.add(currentToken);
            }
        }

        while (!stack.isEmpty()) {
            if (isOperator(stack.peek()))
                postfix.add(stack.pop());
            else {
                isCorrectExpression = false;
                return postfix;
            }
        }
        return postfix;
    }
}
