import java.util.Scanner;

public class SimpleInterpreter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to the Simple Interpreter! Choose an algorithm:");
            System.out.println("1. Sum of First N Numbers");
            System.out.println("2. Factorial of N");
            System.out.println("3. GCD of Two Numbers");
            System.out.println("4. Reverse a Number");
            System.out.println("5. Prime Checker");
            System.out.println("6. Palindrome Checker");
            System.out.println("7. Largest Digit in a Number");
            System.out.println("8. Sum of Digits");
            System.out.println("9. Multiplication Table");
            System.out.println("10. Nth Fibonacci Number");
            System.out.println("0. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> SumOfFirstNthNumber.main(args);
                case 2 -> FactorialInterpreter.main(args);
                case 3 -> GCDInterpreter.main(args);
                case 4 -> ReverseNumberInterpreter.main(args);
                case 5 -> PrimeNumberInterpreter.main(args);
                case 6 -> PalindromeNumberInterpreter.main(args);
                case 7 -> LargestDigitInterpreter.main(args);
                case 8 -> SumOfDigitsInterpreter.main(args);
                case 9 -> MultiplicationTableInterpreter.main(args);
                case 10 -> FibonacciInterpreter.main(args);
                case 0 -> {
                    System.out.println("Exiting the interpreter. Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }

            System.out.println() ;
        }
    }
}
