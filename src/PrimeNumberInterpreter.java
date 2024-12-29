import java.util.*;

public class PrimeNumberInterpreter {

    private final Map<String, Integer> variables = new HashMap<>(); // Variable storage

    public void eval(String code) {
        String[] lines = code.split(";"); // Split by statement terminator
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Handle variable assignment (e.g., N := 7)
            if (line.contains(":=")) {
                handleAssignment(line);
            }
            // Handle prime check operation (e.g., ISPRIME(N) INTO RESULT)
            else if (line.startsWith("ISPRIME")) {
                handleIsPrime(line);
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

        // If the value is a number (like N := 7), parse it directly
        if (valueExpr.matches("\\d+")) {
            int value = Integer.parseInt(valueExpr);
            variables.put(varName, value);
        }
        // If the value is a variable (like RESULT := N), get its value
        else if (variables.containsKey(valueExpr)) {
            int value = variables.get(valueExpr);
            variables.put(varName, value);
        } else {
            throw new IllegalArgumentException("Invalid value expression: " + valueExpr);
        }
    }

    private void handleIsPrime(String line) {
        // Parse ISPRIME operation (e.g., ISPRIME(N) INTO RESULT)
        String[] parts = line.split("INTO");
        String primeExpr = parts[0].replace("ISPRIME", "").trim();
        String resultVar = parts[1].trim();

        String varName = primeExpr.substring(1, primeExpr.length() - 1); // Extract variable inside ISPRIME()
        int value = resolveValue(varName);
        boolean isPrime = checkPrime(value);

        variables.put(resultVar, isPrime ? 1 : 0); // Store 1 if prime, 0 otherwise
    }

    private void handlePrint(String line) {
        // Extract the variable name from PRINT(RESULT)
        String varName = line.substring(line.indexOf('(') + 1, line.indexOf(')')).trim();
        // Print the value of the variable
        Integer value = variables.get(varName);
        if (value != null) {
            System.out.println(value == 1 ? "PRIME" : "NOT PRIME");
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

    private boolean checkPrime(int n) {
        if (n <= 1) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        PrimeNumberInterpreter interpreter = new PrimeNumberInterpreter();

        // Go-like program to check if a number is prime
        String program = """
            N := 37;
            ISPRIME(N) INTO RESULT;
            PRINT(RESULT);
        """;

        interpreter.eval(program); // Run the interpreter on the Go-like program
    }
}
