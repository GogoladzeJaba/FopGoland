import java.util.*;

public class LargestDigitInterpreter {

    private final Map<String, Integer> variables = new HashMap<>(); // Variable storage
    private final Scanner scanner = new Scanner(System.in); // To get user input

    public void eval(String code) {
        String[] lines = code.split(";"); // Split by statement terminator
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Handle variable assignment (e.g., SET N TO 121)
            if (line.startsWith("SET")) {
                handleAssignment(line);
            }
            // Handle largest digit operation (e.g., RESULT := LARGESTDIGIT(N))
            else if (line.contains(":= LARGESTDIGIT")) {
                handleLargestDigit(line);
            }
            // Handle print statements (e.g., PRINT(RESULT))
            else if (line.startsWith("PRINT")) {
                handlePrint(line);
            }
        }
    }

    private void handleAssignment(String line) {
        String[] parts = line.split(":=");
        String varName = parts[0].replace("SET", "").trim(); // Extract the variable name
        String valueExpr = parts[1].trim(); // The value assigned to the variable

        // If the value is a number (like SET N := 121), parse it directly
        if (valueExpr.matches("\\d+")) {
            int value = Integer.parseInt(valueExpr);
            variables.put(varName, value);
        }
        // If the value is a variable (like SET RESULT := N), get its value
        else if (variables.containsKey(valueExpr)) {
            int value = variables.get(valueExpr);
            variables.put(varName, value);
        }
    }

    private void handleLargestDigit(String line) {
        // Parse LARGESTDIGIT operation (e.g., RESULT := LARGESTDIGIT(N))
        String[] parts = line.split(":=");
        String resultVar = parts[0].trim(); // Extract the result variable
        String functionCall = parts[1].trim(); // Extract the LARGESTDIGIT(N) function

        String varName = functionCall.substring(functionCall.indexOf('(') + 1, functionCall.indexOf(')')).trim();

        int value = resolveValue(varName);
        int largestDigit = findLargestDigit(value);

        variables.put(resultVar, largestDigit); // Store the largest digit
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

    private int findLargestDigit(int n) {
        int largest = 0;

        while (n != 0) {
            int digit = n % 10;
            if (digit > largest) {
                largest = digit;
            }
            n /= 10;
        }

        return largest;
    }

    public static void main(String[] args) {
        LargestDigitInterpreter interpreter = new LargestDigitInterpreter();

        // Ask for user input for N
        System.out.print("Input value of N: ");
        int n = interpreter.scanner.nextInt(); // Get value of N from user

        // Updated program string with aligned syntax
        String program = String.format("""
            SET N := %d;
            RESULT := LARGESTDIGIT(N);
            PRINT(RESULT);
        """, n);

        interpreter.eval(program); // Run the interpreter on the program
    }
}
