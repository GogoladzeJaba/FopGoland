// File: SimpleInterpreter.java
import java.util.*;
import java.util.regex.*;

public class SimpleInterpreter {
    private final Map<String, Integer> variables = new HashMap<>();

    private enum TokenType {
        NUMBER, IDENTIFIER, ASSIGN, OPERATOR, EOF
    }

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

    public void interpret(String input) {
        Tokenizer tokenizer = new Tokenizer(input);
        Token currentToken = tokenizer.nextToken();

        while (currentToken.type != TokenType.EOF) {
            if (currentToken.type == TokenType.IDENTIFIER) {
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
        return parseTermWithPrecedence(tokenizer, currentToken, 0);
    }

    private int parseTermWithPrecedence(Tokenizer tokenizer, Token currentToken, int precedence) {
        int left = parsePrimary(tokenizer, currentToken); // Parse the first term (number or variable)

        while (true) {
            // Peek the next token without consuming it
            Token nextToken = tokenizer.nextToken();
            if (nextToken.type != TokenType.OPERATOR || getPrecedence(nextToken.value) < precedence) {
                // If no valid operator or lower precedence, exit loop
                tokenizer.pos -= nextToken.value.length(); // Revert tokenizer to "before nextToken"
                break;
            }

            // Advance to the next token and handle higher precedence terms
            int nextPrecedence = getPrecedence(nextToken.value) + 1; // Enforce left-associativity
            Token rightToken = tokenizer.nextToken(); // Get the token after the operator
            int right = parseTermWithPrecedence(tokenizer, rightToken, nextPrecedence);

            // Apply operator to current result
            left = applyOperator(left, right, nextToken.value);
        }

        return left;
    }

    private int parsePrimary(Tokenizer tokenizer, Token currentToken) {
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

    private int getPrecedence(String operator) {
        switch (operator) {
            case "*":
            case "/":
            case "%":
                return 2;
            case "+":
            case "-":
                return 1;
            default:
                return -1;
        }
    }

    private int applyOperator(int left, int right, String operator) {
        switch (operator) {
            case "+": return left + right;
            case "-": return left - right;
            case "*": return left * right;
            case "/":
                if (right == 0) throw new IllegalArgumentException("Division by zero");
                return left / right;
            case "%":
                if (right == 0) throw new IllegalArgumentException("Division by zero");
                return left % right;
            default: throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

    public static void main(String[] args) {
        SimpleInterpreter interpreter = new SimpleInterpreter();

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
