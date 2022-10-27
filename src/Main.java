import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    final static String THE_CHARACTER_SEPARATOR_OF_THE_ARITHMETIC_OPERATION = " ";
    final static int THE_FIRST_TEN_ROMAN_NUMERALS = 10;
    final static String[] SET_OF_AVAILABLE_NUMBERS = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    final static String[] MATHEMATICAL_OPERATIONS = {"+", "-", "/", "*"};

    final static int NUMBER_OF_OPERANDS = 2;

    public static void main(String[] args) {
        String expression, result;

        expression = gettingInputData();

        result = calc(expression);
        System.out.println(result);
    }

    static String gettingInputData() {
        var scanner = new Scanner(System.in);

        return scanner.nextLine().toUpperCase();
    }

    static String calc(String input) {
        String[] symbolsOfTheArithmeticOperation, tenRomanNumerals;
        boolean isRomanNumber;
        String result;

        symbolsOfTheArithmeticOperation = splitExpression(input);
        tenRomanNumerals = getRomanNumerals();
        isRomanNumber = checkForRomanNumerals(symbolsOfTheArithmeticOperation);
        result = calculateTheExpression(symbolsOfTheArithmeticOperation, tenRomanNumerals, isRomanNumber);

        return result;
    }

    static String[] splitExpression(String expression) {
        String[] symbolsOfTheArithmeticOperation;

        symbolsOfTheArithmeticOperation = expression.split(THE_CHARACTER_SEPARATOR_OF_THE_ARITHMETIC_OPERATION);

        checkTheNumberOfOperators(symbolsOfTheArithmeticOperation);

        return symbolsOfTheArithmeticOperation;
    }

    static void checkTheNumberOfOperators(String[] symbolsOfTheArithmeticOperation) {
        int operatorCounter = 0;

        for (String s : symbolsOfTheArithmeticOperation) {
            for (String mathematicalOperation : MATHEMATICAL_OPERATIONS)
                if (s.equals(mathematicalOperation)) operatorCounter++;
        }

        if (operatorCounter > 1) {
            try {
                throw new IOException();
            } catch (IOException e) {
                System.out.println("формат математической операции не удовлетворяет заданию - " +
                        "два операнда и один оператор (+, -, /, *)");
                System.exit(-1);
            }
        }
    }

    static String[] getRomanNumerals() {
        var romanNumerals = RomanNumeral.values();
        var tenRomanNumerals = new String[THE_FIRST_TEN_ROMAN_NUMERALS];

        for (int i = 0; i < THE_FIRST_TEN_ROMAN_NUMERALS; i++)
            tenRomanNumerals[i] = String.valueOf(romanNumerals[i]);

        return tenRomanNumerals;
    }

    static boolean checkForRomanNumerals(String[] symbolsOfTheArithmeticOperation) {
        var romanNumerals = RomanNumeral.values();
        var operandCounter = 0;
        try {
            for (RomanNumeral RomanNumeral : romanNumerals) {
                if (symbolsOfTheArithmeticOperation[0].equals(RomanNumeral.toString())) operandCounter++;
                if (symbolsOfTheArithmeticOperation[2].equals(RomanNumeral.toString())) operandCounter++;
            }
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("строка не является математической операцией");
            System.exit(-1);
        }

        return operandCounter == NUMBER_OF_OPERANDS;
    }

    static String calculateTheExpression(String[] symbolsOfTheArithmeticOperation, String[] tenRomanNumerals,
                                         boolean isRomanNumber) {
        String arithmeticOperation;

        int firstSummand = 0, secondSummand = 0, result = 0;
        var romanNumerals = RomanNumeral.values();
        arithmeticOperation = symbolsOfTheArithmeticOperation[1];

        try {
            if (!isRomanNumber) {
                firstSummand = Integer.parseInt(symbolsOfTheArithmeticOperation[0]);
                secondSummand = Integer.parseInt(symbolsOfTheArithmeticOperation[2]);
            } else {
                for (int i = 0; i < tenRomanNumerals.length; i++) {
                    if (symbolsOfTheArithmeticOperation[0].equals(tenRomanNumerals[i])) firstSummand = i + 1;
                    if (symbolsOfTheArithmeticOperation[2].equals(tenRomanNumerals[i])) secondSummand = i + 1;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("используются одновременно разные системы счисления");
            System.exit(-1);
        }

        if (isRomanNumber) checkTheRangeOfRomanNumerals(symbolsOfTheArithmeticOperation, tenRomanNumerals);
        else checkTheRangeOfArabicNumerals(symbolsOfTheArithmeticOperation);

        switch (arithmeticOperation) {
            case "+" -> result = getSum(firstSummand, secondSummand);
            case "-" -> result = getDifference(firstSummand, secondSummand);
            case "/" -> result = getQuotient(firstSummand, secondSummand);
            case "*" -> result = getProduct(firstSummand, secondSummand);
            default -> {
                System.out.println("Вы ввели неверную арифметическую операцию");
                System.exit(-1);
            }
        }

        return checkForNegativeNumbers(isRomanNumber, romanNumerals, result);
    }

    static String checkForNegativeNumbers(boolean isRomanNumber, RomanNumeral[] romanNumerals, int result){
        if (isRomanNumber) {
            try {
                return String.valueOf(romanNumerals[result - 1]);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("в римской системе нет отрицательных чисел");
                System.exit(-1);
            }
        }

        return Integer.toString(result);
    }

    static void checkTheRangeOfRomanNumerals(String[] symbolsOfTheArithmeticOperation, String[] tenRomanNumerals) {
        var isFirstTermInTheRange =
                Arrays.asList(tenRomanNumerals).contains(symbolsOfTheArithmeticOperation[0]);
        var isSecondTermInTheRange =
                Arrays.asList(tenRomanNumerals).contains(symbolsOfTheArithmeticOperation[2]);

        if (!isFirstTermInTheRange || !isSecondTermInTheRange) reportAnIncorrectRange();
    }

    static void checkTheRangeOfArabicNumerals(String[] symbolsOfTheArithmeticOperation) {
        var isFirstTermInTheRange =
                Arrays.asList(SET_OF_AVAILABLE_NUMBERS).contains(symbolsOfTheArithmeticOperation[0]);
        var isSecondTermInTheRange =
                Arrays.asList(SET_OF_AVAILABLE_NUMBERS).contains(symbolsOfTheArithmeticOperation[2]);

        if (!isFirstTermInTheRange || !isSecondTermInTheRange) reportAnIncorrectRange();
    }

    static void reportAnIncorrectRange() {
        System.out.println("Калькулятор принимает на вход целые числа от 1 до 10 включительно");
        System.exit(-1);
    }

    static int getSum(int firstSummand, int secondSummand) {
        return firstSummand + secondSummand;
    }

    static int getDifference(int firstSummand, int secondSummand) {
        return firstSummand - secondSummand;
    }

    static int getQuotient(int firstSummand, int secondSummand) {
        return firstSummand / secondSummand;
    }

    static int getProduct(int firstSummand, int secondSummand) {
        return firstSummand * secondSummand;
    }
}