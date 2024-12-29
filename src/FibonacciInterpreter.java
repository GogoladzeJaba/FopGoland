import java.util.*;

public class FibonacciInterpreter {

    private final Map<String, Integer> variables = new HashMap<>(); // Variable storage

    public void eval(String code) {
        String[] lines = code.split(";"); // Split by statement terminator
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Handle variable assignment (e.g., SET N TO 5)
            if (line.startsWith("SET")) {
                handleAssignment(line);
            }
            // Handle Fibonacci calculation (e.g., FIBONACCI N INTO RESULT)
            else if (line.startsWith("FIBONACCI")) {
                handleFibonacci(line);
            }
            // Handle print statements (e.g., PRINT(RESULT))
            else if (line.startsWith("PRINT")) {
                handlePrint(line);
            }
        }
    }

    private void handleAssignment(String line) {
        String[] parts = line.split("TO");
        String varName = parts[0].replace("SET", "").trim(); // Extract the variable name
        String valueExpr = parts[1].trim(); // The value assigned to the variable

        // If the value is a number (like SET N TO 5), parse it directly
        if (valueExpr.matches("\\d+")) {
            int value = Integer.parseInt(valueExpr);
            variables.put(varName, value);
        }
        // If the value is a variable (like SET RESULT TO N), get its value
        else if (variables.containsKey(valueExpr)) {
            int value = variables.get(valueExpr);
            variables.put(varName, value);
        }
    }

    private void handleFibonacci(String line) {
        // Parse FIBONACCI operation (e.g., FIBONACCI N INTO RESULT)
        String[] parts = line.split("INTO");
        String varName = parts[0].replace("FIBONACCI", "").trim();
        String resultVar = parts[1].trim();

        int value = resolveValue(varName);

        if (value <= 0) {
            throw new IllegalArgumentException("Fibonacci sequence is not defined for values less than 1.");
        }

        int fibValue = calculateFibonacci(value);

        variables.put(resultVar, fibValue); // Store the Fibonacci result
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

    private int calculateFibonacci(int n) {
        if (n == 1) return 0; // 1st Fibonacci number
        if (n == 2) return 1; // 2nd Fibonacci number

        int a = 0, b = 1;
        for (int i = 3; i <= n; i++) {
            int temp = a + b;
            a = b;
            b = temp;
        }
        return b;
    }

    public static void main(String[] args) {
        FibonacciInterpreter interpreter = new FibonacciInterpreter();

        // GoLanf-like program to calculate the Nth Fibonacci number
        String program = """
            SET N TO 3;
            FIBONACCI N INTO RESULT;
            PRINT(RESULT);
        """;

        interpreter.eval(program); // Run the interpreter on the GoLanf-like program
    }
}
