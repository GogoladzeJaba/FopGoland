import java.util.*;

public class FibonacciInterpreter {

    Scanner scanner = new Scanner(System.in);

    private final Map<String, Integer> variables = new HashMap<>(); // Variable storage

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
            // Handle print statements (e.g., PRINT(FIBONACCI))
            else if (line.startsWith("PRINT")) {
                handlePrint(line);
            }

            currentLineIndex++;
        }
    }

    private void handleAssignment(String line) {
        String[] parts = line.split(":=");
        String varName = parts[0].trim(); // Extract variable name
        String valueExpr = parts[1].trim(); // Extract assigned value

        // Evaluate the expression and assign the result
        int value = evaluateExpression(valueExpr);
        variables.put(varName, value);
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
        // Handle basic expressions (e.g., A + B, constants, and variables)
        if (expr.matches("\\d+")) { // If it's a number, return it
            return Integer.parseInt(expr);
        } else if (variables.containsKey(expr)) { // If it's a variable, get its value
            return variables.get(expr);
        } else if (expr.contains("+")) { // Handle addition
            String[] parts = expr.split("\\+");
            return evaluateExpression(parts[0].trim()) + evaluateExpression(parts[1].trim());
        } else if (expr.contains("-")) { // Handle subtraction
            String[] parts = expr.split("-");
            return evaluateExpression(parts[0].trim()) - evaluateExpression(parts[1].trim());
        }
        throw new IllegalArgumentException("Invalid value expression: " + expr);
    }

    public static void main(String[] args) {
        FibonacciInterpreter interpreter = new FibonacciInterpreter();

        int n;
        do {
            System.out.print("Input value of N (natural number): ");
            n = interpreter.scanner.nextInt(); // Get value of N from user

            if (n <= 0) {
                System.out.println("Invalid input! Please enter a natural number (1 or greater).");
            }
        } while (n <= 0); // Keep asking until a valid natural number is entered

        n--; // Subtract 1 from the input value of N for zero-based index calculations

        // Simple program to calculate the N-th Fibonacci number with adjusted N
        String program = String.format("""
            N := %d;
            A := 0;
            B := 1;
            FOR I FROM 1 TO N;
                TEMP := A + B;
                A := B;
                B := TEMP;
            END FOR;
            PRINT(A);
        """, n);

        interpreter.eval(program); // Run the interpreter on the Fibonacci program
    }
}
