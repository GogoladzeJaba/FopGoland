// File: SimpleInterpreter.java
import java.util.*;
import java.util.regex.*;

public class SimpleInterpreter {
    // Variable storage
    private final Map<String, Integer> variables = new HashMap<>();

    // Token types
    private enum TokenType {
        NUMBER, IDENTIFIER, ASSIGN, OPERATOR, EOF
    }

    // Token structure
    private static class Token {
        TokenType type;
        String value;

        Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return type + " (" + value + ")";
        }
    }

    // Tokenizer (Lexical Analysis)
    private static class Tokenizer {
        private final String input;
        private int pos = 0;
        private final Pattern tokenPatterns = Pattern.compile(
                "\\s*(\\d+|[a-zA-Z_][a-zA-Z_0-9]*|:=|[+\\-*/%]|.)");

        Tokenizer(String input) {
            this.input = input;
        }

        Token nextToken() {
            if (pos >= input.length()) {
                return new Token(TokenType.EOF, "");
            }

            Matcher matcher = tokenPatterns.matcher(input);
            if (matcher.find(pos) && matcher.start() == pos) {
                String tokenValue = matcher.group(1);
                pos = matcher.end();

                if (tokenValue.matches("\\d+")) {
                    return new Token(TokenType.NUMBER, tokenValue);
                } else if (tokenValue.matches("[a-zA-Z_][a-zA-Z_0-9]*")) {
                    return new Token(TokenType.IDENTIFIER, tokenValue);
                } else if (tokenValue.equals(":=")) {
                    return new Token(TokenType.ASSIGN, tokenValue);
                } else if ("+-*/%".contains(tokenValue)) {
                    return new Token(TokenType.OPERATOR, tokenValue);
                }
            }
            throw new IllegalArgumentException("Unexpected token at position " + pos);
        }
    }

    // Execution Engine
    public void interpret(String input) {
        Tokenizer tokenizer = new Tokenizer(input);
        Token currentToken = tokenizer.nextToken();

        while (currentToken.type != TokenType.EOF) {
            if (currentToken.type == TokenType.IDENTIFIER) {
                // Handle variable assignment
                String varName = currentToken.value;
                currentToken = tokenizer.nextToken();

                if (currentToken.type == TokenType.ASSIGN) {
                    currentToken = tokenizer.nextToken();

                    int value = parseExpression(tokenizer, currentToken);
                    variables.put(varName, value);
                    System.out.println("Assigned " + varName + " = " + value);
                } else {
                    throw new IllegalArgumentException("Expected ':=' after identifier");
                }
            } else {
                throw new IllegalArgumentException("Unexpected token: " + currentToken.value);
            }

            currentToken = tokenizer.nextToken();
        }
    }

    private int parseExpression(Tokenizer tokenizer, Token currentToken) {
        // Parse a single expression (e.g., 5 + 3)
        int result = parseTerm(tokenizer, currentToken);

        currentToken = tokenizer.nextToken();
        while (currentToken.type == TokenType.OPERATOR && "+-".contains(currentToken.value)) {
            String operator = currentToken.value;
            currentToken = tokenizer.nextToken();

            int nextTerm = parseTerm(tokenizer, currentToken);
            if (operator.equals("+")) {
                result += nextTerm;
            } else if (operator.equals("-")) {
                result -= nextTerm;
            }
            currentToken = tokenizer.nextToken();
        }
        return result;
    }

    private int parseTerm(Tokenizer tokenizer, Token currentToken) {
        // Parse a single term (e.g., 5 * 3)
        if (currentToken.type == TokenType.NUMBER) {
            return Integer.parseInt(currentToken.value);
        } else if (currentToken.type == TokenType.IDENTIFIER) {
            if (!variables.containsKey(currentToken.value)) {
                throw new IllegalArgumentException("Undefined variable: " + currentToken.value);
            }
            return variables.get(currentToken.value);
        } else {
            throw new IllegalArgumentException("Expected number or variable, got: " + currentToken.value);
        }
    }

    public static void main(String[] args) {
        SimpleInterpreter interpreter = new SimpleInterpreter();

        // Example usage
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your code (type 'exit' to quit):");
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            try {
                interpreter.interpret(input);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
