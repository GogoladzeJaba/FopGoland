import java.util.*;

public class FactorialInterpreter {

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
            // Handle FOR loops (e.g., FOR I FROM 1 TO N)
            else if (line.startsWith("FOR")) {
                handleForLoop(line);
            }
            // Handle print statements (e.g., PRINT(FACTORIAL))
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
        // If the value is a variable (like SET FACTORIAL TO N), get its value
        else if (variables.containsKey(valueExpr)) {
            int value = variables.get(valueExpr);
            variables.put(varName, value);
        }
    }

    private void handleForLoop(String line) {
        // Basic FOR loop structure (e.g., FOR I FROM 1 TO N)
        String[] parts = line.split("FROM");
        String[] range = parts[1].split("TO");
        int start = resolveValue(range[0].trim());
        int end = resolveValue(range[1].trim());

        // Extract the body of the loop
        for (int i = start; i <= end; i++) {
            // Multiply I to FACTORIAL
            int currentFactorial = variables.getOrDefault("FACTORIAL", 1);
            currentFactorial *= i;
            variables.put("FACTORIAL", currentFactorial);
        }
    }

    private void handlePrint(String line) {
        // Extract the variable name from PRINT(FACTORIAL)
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
        FactorialInterpreter interpreter = new FactorialInterpreter();

        // GoLanf-like program to calculate the factorial of N
        String program = """
            SET N TO 6;
            SET FACTORIAL TO 1;
            FOR I FROM 1 TO N;
                MULTIPLY I TO FACTORIAL;
            END FOR;
            PRINT(FACTORIAL);
        """;

        interpreter.eval(program); // Run the interpreter on the GoLanf-like program
    }
}
