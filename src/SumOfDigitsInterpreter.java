import java.util.*;

public class SumOfDigitsInterpreter {

    private final Map<String, Integer> variables = new HashMap<>(); // Variable storage

    public void eval(String code) {
        String[] lines = code.split(";"); // Split by statement terminator
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Handle variable assignment (e.g., N := 121)
            if (line.contains(":=")) {
                handleAssignment(line);
            }
            // Handle sum of digits operation (e.g., SUMOFDIGITS(N) INTO RESULT)
            else if (line.startsWith("SUMOFDIGITS")) {
                handleSumOfDigits(line);
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

        // If the value is a number (like N := 121), parse it directly
        if (valueExpr.matches("\\d+")) {
            int value = Integer.parseInt(valueExpr);
            variables.put(varName, value);
        }
        // If the value is a variable (like RESULT := N), get its value
        else if (variables.containsKey(valueExpr)) {
            int value = variables.get(valueExpr);
            variables.put(varName, value);
        }
    }

    private void handleSumOfDigits(String line) {
        // Parse SUMOFDIGITS operation (e.g., SUMOFDIGITS(N) INTO RESULT)
        String[] parts = line.split("INTO");
        String functionCall = parts[0].trim();
        String resultVar = parts[1].trim();

        String varName = functionCall.substring(functionCall.indexOf('(') + 1, functionCall.indexOf(')')).trim();

        int value = resolveValue(varName);
        int sumOfDigits = findSumOfDigits(value);

        variables.put(resultVar, sumOfDigits); // Store the sum of digits
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

    private int findSumOfDigits(int n) {
        int sum = 0;

        while (n != 0) {
            sum += n % 10;
            n /= 10;
        }

        return sum;
    }

    public static void main(String[] args) {
        SumOfDigitsInterpreter interpreter = new SumOfDigitsInterpreter();

        // GoLand-like program to find the sum of digits in a number
        String program = """
            N := 12351;
            SUMOFDIGITS(N) INTO RESULT;
            PRINT(RESULT);
        """ ;

        interpreter.eval(program); // Run the interpreter on the GoLand-like program
    }
}
