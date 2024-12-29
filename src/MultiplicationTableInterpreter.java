import java.util.*;

public class MultiplicationTableInterpreter {

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
            // Handle FOR loops (e.g., FOR I FROM 1 TO 10)
            else if (line.startsWith("FOR")) {
                currentLineIndex = handleForLoop(lines, currentLineIndex);
            }
            // Handle PRINT statements (e.g., PRINT(RESULT))
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

        // Parse the loop header: "FOR I FROM 1 TO 10"
        String[] parts = loopHeader.split("FROM");
        String loopVar = parts[0].replace("FOR", "").trim(); // Extract loop variable name
        String[] rangeParts = parts[1].split("TO");
        int start = evaluateExpression(rangeParts[0].trim()); // Evaluate start value
        int end = evaluateExpression(rangeParts[1].trim()); // Evaluate end value

        // Locate the end of the loop
        int endLoopIndex = startLineIndex + 1;
        while (endLoopIndex < lines.length && !lines[endLoopIndex].trim().equals("END FOR")) {
            endLoopIndex++;
        }

        // Execute the loop body
        for (int i = start; i <= end; i++) {
            variables.put(loopVar, i); // Initialize/update the loop variable
            for (int j = startLineIndex + 1; j < endLoopIndex; j++) {
                eval(lines[j].trim() + ";"); // Re-evaluate each line in the loop body
            }
        }

        return endLoopIndex; // Skip to the line after "END FOR"
    }

    private void handlePrint(String line) {
        // Extract variable name or expression from PRINT(...)
        String content = line.substring(line.indexOf('(') + 1, line.indexOf(')')).trim();

        // If the content is an arithmetic expression, evaluate it and print details
        if (content.contains("*")) {
            String[] parts = content.split("\\*");
            int leftValue = evaluateExpression(parts[0].trim());
            int rightValue = evaluateExpression(parts[1].trim());
            int result = leftValue * rightValue;

            // Print the full multiplication expression
            System.out.println(leftValue + " * " + rightValue + " = " + result);
        } else {
            // Handle simple variable printing
            Integer value = variables.get(content);
            if (value != null) {
                System.out.println(value);
            } else {
                System.out.println("Variable " + content + " is not defined.");
            }
        }
    }

    private int evaluateExpression(String expr) {
        // Handle basic expressions (e.g., constants, variables, and arithmetic)
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
        } else if (expr.contains("%")) { // Handle modulus
            String[] parts = expr.split("%");
            return evaluateExpression(parts[0].trim()) % evaluateExpression(parts[1].trim());
        }
        throw new IllegalArgumentException("Invalid value expression: " + expr);
    }

    public static void main(String[] args) {
        MultiplicationTableInterpreter interpreter = new MultiplicationTableInterpreter();

        // Ask for user input for N
        System.out.print("Input value of N: ");
        int n = interpreter.scanner.nextInt();  // Get value of N from user

        // Updated Go-like program string
        String program = String.format("""
            N := %d;
            FOR I FROM 1 TO 10;
                RESULT := N * I;
                PRINT(N * I);
            END FOR;
        """, n);

        interpreter.eval(program); // Run the interpreter on the program
    }
}
