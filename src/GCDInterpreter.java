import java.util.*;

public class GCDInterpreter {

    private final Map<String, Integer> variables = new HashMap<>(); // Variable storage

    public void eval(String code) {
        String[] lines = code.split(";"); // Split by statement terminator
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Handle variable assignment (e.g., A := 5)
            if (line.contains(":=")) {
                handleAssignment(line);
            }
            // Handle GCD calculation (e.g., GCD(A, B) INTO RESULT)
            else if (line.startsWith("GCD")) {
                handleGCD(line);
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

        // If the value is a number (like A := 5), parse it directly
        if (valueExpr.matches("\\d+")) {
            int value = Integer.parseInt(valueExpr);
            variables.put(varName, value);
        }
        // If the value is a variable (like RESULT := A), get its value
        else if (variables.containsKey(valueExpr)) {
            int value = variables.get(valueExpr);
            variables.put(varName, value);
        } else {
            throw new IllegalArgumentException("Invalid value expression: " + valueExpr);
        }
    }

    private void handleGCD(String line) {
        // Parse GCD operation (e.g., GCD(A, B) INTO RESULT)
        String[] parts = line.split("INTO");
        String gcdExpr = parts[0].replace("GCD", "").trim();
        String resultVar = parts[1].trim();

        String[] operands = gcdExpr.substring(1, gcdExpr.length() - 1).split(",");
        String var1 = operands[0].trim();
        String var2 = operands[1].trim();

        int a = resolveValue(var1);
        int b = resolveValue(var2);

        // Calculate GCD using the Euclidean algorithm
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }

        variables.put(resultVar, a); // Store the result in the result variable
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
        GCDInterpreter interpreter = new GCDInterpreter();

        // Go-like program to calculate the GCD of A and B
        String program = """
            A := 48;
            B := 18;
            GCD(A, B) INTO RESULT;
            PRINT(RESULT);
        """;

        interpreter.eval(program); // Run the interpreter on the Go-like program
    }
}
