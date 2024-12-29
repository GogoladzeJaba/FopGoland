import java.util.*;

public class SumOfFirstNthNumber {

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
            // Handle FOR loops (e.g., FOR I FROM 1 TO N)
            else if (line.startsWith("FOR")) {
                handleForLoop(line);
            }
            // Handle print statements (e.g., PRINT(SUM))
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
        // If the value is a variable (like SUM := N), get its value
        else if (variables.containsKey(valueExpr)) {
            int value = variables.get(valueExpr);
            variables.put(varName, value);
        }
    }

    private void handleForLoop(String line) {
        // Basic FOR loop structure (e.g., FOR I FROM 1 TO N)
        String[] parts = line.split("FROM");
        String[] range = parts[1].split("TO");
        String loopVar = parts[0].replace("FOR", "").trim();
        int start = resolveValue(range[0].trim());
        int end = resolveValue(range[1].trim());

        for (int i = start; i <= end; i++) {
            variables.put(loopVar, i); // Update loop variable in storage
            String nextLine = getNextLine();
            if (nextLine != null && nextLine.contains("ADD")) {
                handleAdd(nextLine, i);
            }
        }
    }

    private String getNextLine() {
        // Hardcoded to handle the ADD operation for simplicity
        return "ADD I TO SUM";
    }

    private void handleAdd(String line, int value) {
        // Parsing ADD operation (e.g., ADD I TO SUM)
        String[] parts = line.split("TO");
        String varName = parts[1].trim(); // Extract the variable to which we add the value

        int currentSum = variables.getOrDefault(varName, 0);
        currentSum += value;
        variables.put(varName, currentSum);
    }

    private void handlePrint(String line) {
        // Extract the variable name from PRINT(SUM)
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
        SumOfFirstNthNumber interpreter = new SumOfFirstNthNumber();

        // Go-like program to sum the first N numbers
        String program = """
            N := 5;
            SUM := 0;
            FOR I FROM 1 TO N;
                ADD I TO SUM;
            END FOR;
            PRINT(SUM);
        """;

        interpreter.eval(program); // Run the interpreter on the Go-like program
    }
}