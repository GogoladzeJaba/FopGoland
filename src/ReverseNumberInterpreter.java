import java.util.*;

public class ReverseNumberInterpreter {

    private final Map<String, Integer> variables = new HashMap<>(); // Variable storage
    private final Scanner scanner = new Scanner(System.in);  // To get user input

    public void eval(String code) {
        String[] lines = code.split(";"); // Split by statement terminator
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Handle variable assignment (e.g., N := 123)
            if (line.contains(":=")) {
                handleAssignment(line);
            }
            // Handle reverse operation (e.g., REVERSE(N) INTO RESULT)
            else if (line.startsWith("REVERSE")) {
                handleReverse(line);
            }
            // Handle print statements (e.g., PRINT(RESULT))
            else if (line.startsWith("PRINT")) {
                handlePrint(line);
            }
        }
    }

    private void handleAssignment(String line) {
        String[] parts = line.split(":=");
        String varName = parts[0].trim(); // Extract the variable name
        String valueExpr = parts[1].trim(); // The value assigned to the variable

        // If the value is a number (like N := 123), parse it directly
        if (valueExpr.matches("\\d+")) {
            int value = Integer.parseInt(valueExpr);
            variables.put(varName, value);
        }
        // If the value is a variable (like RESULT := N), get its value
        else if (variables.containsKey(valueExpr)) {
            int value = variables.get(valueExpr);
            variables.put(varName, value);
        } else {
            throw new IllegalArgumentException("Invalid value expression: " + valueExpr);
        }
    }

    private void handleReverse(String line) {
        // Parse REVERSE operation (e.g., REVERSE(N) INTO RESULT)
        String[] parts = line.split("INTO");
        String reverseExpr = parts[0].replace("REVERSE", "").trim();
        String resultVar = parts[1].trim();

        String varName = reverseExpr.substring(1, reverseExpr.length() - 1); // Extract variable inside REVERSE()
        int value = resolveValue(varName);
        int reversed = 0;

        // Reverse the number
        while (value != 0) {
            int digit = value % 10;
            reversed = reversed * 10 + digit;
            value /= 10;
        }

        variables.put(resultVar, reversed); // Store the reversed number in the result variable
    }

    private void handlePrint(String line) {
        // Extract the variable name from PRINT(RESULT)
        String varName = line.substring(line.indexOf('(') + 1, line.indexOf(')')).trim();
        // Print the value of the variable
        Integer value = variables.get(varName);
        if (value != null) {
            System.out.println(value);
        } else {
            System.out.println("Variable " + varName + " is not defined.");
        }
    }

    private int resolveValue(String expr) {
        // If the expression is a number, parse it
        if (expr.matches("\\d+")) {
            return Integer.parseInt(expr);
        }
        // Otherwise, it must be a variable
        else if (variables.containsKey(expr)) {
            return variables.get(expr);
        }
        throw new IllegalArgumentException("Invalid value expression: " + expr);
    }

    public static void main(String[] args) {
        ReverseNumberInterpreter interpreter = new ReverseNumberInterpreter();

        // Ask for user input for N
        System.out.print("Input value of N: ");
        int n = interpreter.scanner.nextInt();  // Get value of N from user

        // Simple program to reverse the number with the input value of N
        String program = String.format("""
            N := %d;
            REVERSE(N) INTO RESULT;
            PRINT(RESULT);
        """, n);

        interpreter.eval(program); // Run the interpreter on the program
    }
}
