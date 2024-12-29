import java.util.*;

public class PalindromeNumberInterpreter {

    private final Map<String, Integer> variables = new HashMap<>(); // Variable storage

    public void eval(String code) {
        String[] lines = code.split(";"); // Split by statement terminator
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Handle variable assignment (e.g., SET N TO 121)
            if (line.startsWith("SET")) {
                handleAssignment(line);
            }
            // Handle palindrome check operation (e.g., ISPALINDROME N INTO RESULT)
            else if (line.startsWith("ISPALINDROME")) {
                handleIsPalindrome(line);
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

        // If the value is a number (like SET N TO 121), parse it directly
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

    private void handleIsPalindrome(String line) {
        // Parse ISPALINDROME operation (e.g., ISPALINDROME N INTO RESULT)
        String[] parts = line.split("INTO");
        String varName = parts[0].replace("ISPALINDROME", "").trim();
        String resultVar = parts[1].trim();

        int value = resolveValue(varName);
        boolean isPalindrome = checkPalindrome(value);

        variables.put(resultVar, isPalindrome ? 1 : 0); // Store 1 if palindrome, 0 otherwise
    }

    private void handlePrint(String line) {
        // Extract the variable name from PRINT(RESULT)
        String varName = line.substring(line.indexOf('(') + 1, line.indexOf(')')).trim();
        // Print the value of the variable
        Integer value = variables.get(varName);
        if (value != null) {
            System.out.println(value == 1 ? "PALINDROME" : "NOT PALINDROME");
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

    private boolean checkPalindrome(int n) {
        int original = n;
        int reversed = 0;

        while (n != 0) {
            int digit = n % 10;
            reversed = reversed * 10 + digit;
            n /= 10;
        }

        return original == reversed;
    }

    public static void main(String[] args) {
        PalindromeNumberInterpreter interpreter = new PalindromeNumberInterpreter();

        // GoLand-like program to check if a number is a palindrome
        String program = """
            SET N TO 121;
            ISPALINDROME N INTO RESULT;
            PRINT(RESULT);
        """;

        interpreter.eval(program); // Run the interpreter on the GoLand-like program
    }
}
