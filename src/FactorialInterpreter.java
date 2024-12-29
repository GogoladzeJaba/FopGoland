import java.util.*;

public class FactorialInterpreter {

    private final Map<String, Integer> variables = new HashMap<>(); // Variable storage
    private final Scanner scanner = new Scanner(System.in); // To get user input

    public void eval(String code) {
        String[] lines = code.split(";"); // Split the input by statement terminator
        int currentLineIndex = 0;

        while (currentLineIndex < lines.length) {
            String line = lines[currentLineIndex].trim();
            if (line.isEmpty()) {
                currentLineIndex++;
                continue;
            }

            // Handle variable assignment (e.g., N := 5)
            if (line.contains(":=")) {
                handleAssignment(line);
            }
            // Handle FOR loops (e.g., FOR I FROM 1 TO N)
            else if (line.startsWith("FOR")) {
                currentLineIndex = handleForLoop(lines, currentLineIndex);
            }
            // Handle print statements (e.g., PRINT(FACTORIAL))
            else if (line.startsWith("PRINT")) {
                handlePrint(line);
            }

            currentLineIndex++;
        }
    }

    private void handleAssignment(String line) {
        String[] parts = line.split(":="); // Split the assignment by ":="

        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid assignment syntax: " + line);
        }

        String varName = parts[0].trim(); // Extract variable name
        String valueExpr = parts[1].trim(); // Extract assigned value

        // If the variable is N and the value is empty, prompt the user for input
        if (varName.equals("N") && valueExpr.isEmpty()) {
            System.out.print("Please enter a value for N: ");
            int value = scanner.nextInt();
            variables.put(varName, value);
        } else {
            // Otherwise, evaluate the expression and assign the result
            int value = evaluateExpression(valueExpr);
            variables.put(varName, value);
        }
    }

    private int handleForLoop(String[] lines, int startLineIndex) {
        String loopHeader = lines[startLineIndex].trim();

        // Parse the loop header: "FOR I FROM 1 TO N"
        String[] parts = loopHeader.split("FROM");
        String loopVar = parts[0].replace("FOR", "").trim();
        String[] rangeParts = parts[1].split("TO");
        int start = evaluateExpression(rangeParts[0].trim());
        int end = evaluateExpression(rangeParts[1].trim());

        // Locate the end of the loop
        int endLoopIndex = startLineIndex + 1;
        while (endLoopIndex < lines.length && !lines[endLoopIndex].trim().equals("END FOR")) {
            endLoopIndex++;
        }

        // Execute the loop body
        for (int i = start; i <= end; i++) {
            variables.put(loopVar, i); // Update loop variable
            for (int j = startLineIndex + 1; j < endLoopIndex; j++) {
                eval(lines[j].trim() + ";"); // Re-evaluate each line in the loop body
            }
        }

        return endLoopIndex; // Skip to the line after "END FOR"
    }

    private void handlePrint(String line) {
        // Extract variable name from PRINT(variable)
        String varName = line.substring(line.indexOf('(') + 1, line.indexOf(')')).trim();

        // Print the value of the variable
        Integer value = variables.get(varName);
        if (value != null) {
            System.out.println(value);
        } else {
            System.out.println("Variable " + varName + " is not defined.");
        }
    }

    private int evaluateExpression(String expr) {
        // Evaluate arithmetic expressions (e.g., "FACTORIAL * I")
        if (expr.matches("\\d+")) { // If it's a number, return it
            return Integer.parseInt(expr);
        } else if (variables.containsKey(expr)) { // If it's a variable, get its value
            return variables.get(expr);
        } else if (expr.contains("*")) { // Handle multiplication
            String[] parts = expr.split("\\*");
            return evaluateExpression(parts[0].trim()) * evaluateExpression(parts[1].trim());
        } else if (expr.contains("+")) { // Handle addition
            String[] parts = expr.split("\\+");
            return evaluateExpression(parts[0].trim()) + evaluateExpression(parts[1].trim());
        } else if (expr.contains("-")) { // Handle subtraction
            String[] parts = expr.split("-");
            return evaluateExpression(parts[0].trim()) - evaluateExpression(parts[1].trim());
        } else if (expr.contains("/")) { // Handle division
            String[] parts = expr.split("/");
            return evaluateExpression(parts[0].trim()) / evaluateExpression(parts[1].trim());
        }
        throw new IllegalArgumentException("Invalid value expression: " + expr);
    }

    public static void main(String[] args) {
        FactorialInterpreter interpreter = new FactorialInterpreter();

        // Ask for user input for N
        System.out.print("Input value of N: ");
        int n = interpreter.scanner.nextInt();  // Get value of N from user

        // Simple program to calculate the factorial of N
        String program = String.format("""
            N := %d;
            FACTORIAL := 1;
            FOR I FROM 1 TO N;
                FACTORIAL := FACTORIAL * I;
            END FOR;
            PRINT(FACTORIAL);
        """, n);

        interpreter.eval(program); // Run the interpreter on the program
    }
}
