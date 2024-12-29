import java.util.*;

public class MultiplicationTableInterpreter {

    private final Map<String, Integer> variables = new HashMap<>(); // Variable storage

    public void eval(String code) {
        String[] lines = code.split(";"); // Split by statement terminator
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Handle variable assignment (e.g., N := 5)
            if (line.contains(":=")) {
                handleAssignment(line);
            }
            // Handle multiplication table operation (e.g., MULTIPLICATIONTABLE N)
            else if (line.startsWith("MULTIPLICATIONTABLE")) {
                handleMultiplicationTable(line);
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

        // If the value is a number (like N := 5), parse it directly
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

    private void handleMultiplicationTable(String line) {
        // Parse MULTIPLICATIONTABLE operation (e.g., MULTIPLICATIONTABLE N)
        String varName = line.replace("MULTIPLICATIONTABLE", "").trim();
        int value = resolveValue(varName);

        System.out.println("Multiplication Table for " + value + ":");
        for (int i = 1; i <= 10; i++) {
            System.out.println(value + " x " + i + " = " + (value * i));
        }
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
        MultiplicationTableInterpreter interpreter = new MultiplicationTableInterpreter();

        // Go-like program to generate a multiplication table
        String program = """
            N :=3;
            MULTIPLICATIONTABLE N;
        """;

        interpreter.eval(program); // Run the interpreter on the Go-like program
    }
}
