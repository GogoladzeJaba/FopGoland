import java.util.HashMap;
import java.util.Map;
import java.util.*;

public class SumOfFirstNthNumber {



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
                // Handle print statements (e.g., PRINT(SUM))
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
            // If the value is a variable (like SET SUM TO N), get its value
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

            // Execute the loop
            for (int i = start; i <= end; i++) {
                // Find next line inside the loop to execute (handle ADD operation)
                String nextLine = getNextLine();
                if (nextLine != null && nextLine.contains("ADD")) {
                    handleAdd(nextLine, i); // Perform addition inside the loop
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

            // Retrieve current value of SUM and add the current loop value (i)
            int currentSum = variables.getOrDefault(varName, 0);
            currentSum += value;
            variables.put(varName, currentSum);
        }

        private void handlePrint(String line) {
            // Extract the variable name from PRINT(SUM)
            String varName = line.substring(line.indexOf('(') + 1, line.indexOf(')')).trim();
            // Print the value of the variable
            System.out.println(variables.get(varName));
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
            SimpleInterpreter interpreter = new SimpleInterpreter();

            // GoLanf-like program to sum the first N numbers
            String program = """
            SET N TO 6;
            SET SUM TO 0;
            FOR I FROM 1 TO N;
                ADD I TO SUM;
            END FOR;
            PRINT(SUM);
        """;

            interpreter.eval(program); // Run the interpreter on the GoLand-like program
        }
    }



