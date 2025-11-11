import java.util.Random;
import java.util.Scanner;

public class GuessTheNumber {
    private static final int EASY_RANGE = 50;
    private static final int MEDIUM_RANGE = 100;
    private static final int HARD_RANGE = 200;
    
    private static final int EASY_ATTEMPTS = 10;
    private static final int MEDIUM_ATTEMPTS = 7;
    private static final int HARD_ATTEMPTS = 5;
    
    private static final int MAX_ROUNDS = 3;
    private static final int MAX_HINTS = 2;
    
    private int totalScore;
    private int currentRound;
    private int maxRange;
    private int maxAttempts;
    private int hintsRemaining;
    private String difficultyName;
    private Scanner scanner;
    private Random random;
    
    public GuessTheNumber() {
        this.totalScore = 0;
        this.currentRound = 0;
        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }
    
    public void startGame() {
        displayWelcome();
        selectDifficulty();
        
        for (currentRound = 1; currentRound <= MAX_ROUNDS; currentRound++) {
            playRound();
        }
        
        displayFinalScore();
        askPlayAgain();
    }
    
    private void displayWelcome() {
        System.out.println("==========================================");
        System.out.println("     WELCOME TO GUESS THE NUMBER!        ");
        System.out.println("==========================================");
    }
    
    private void selectDifficulty() {
        System.out.println("\nSELECT DIFFICULTY LEVEL:");
        System.out.println("1. EASY   - Numbers 1-50,  10 attempts, 2 hints");
        System.out.println("2. MEDIUM - Numbers 1-100, 7 attempts,  2 hints");
        System.out.println("3. HARD   - Numbers 1-200, 5 attempts,  2 hints");
        
        int choice = 0;
        boolean validChoice = false;
        
        while (!validChoice) {
            System.out.print("\nEnter your choice (1-3): ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= 3) {
                    validChoice = true;
                } else {
                    System.out.println("Please enter 1, 2, or 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
        
        switch (choice) {
            case 1:
                maxRange = EASY_RANGE;
                maxAttempts = EASY_ATTEMPTS;
                difficultyName = "EASY";
                break;
            case 2:
                maxRange = MEDIUM_RANGE;
                maxAttempts = MEDIUM_ATTEMPTS;
                difficultyName = "MEDIUM";
                break;
            case 3:
                maxRange = HARD_RANGE;
                maxAttempts = HARD_ATTEMPTS;
                difficultyName = "HARD";
                break;
        }
        
        System.out.println("\nDifficulty set to: " + difficultyName);
        System.out.println("Game Rules:");
        System.out.println("   - Guess a number between 1 and " + maxRange);
        System.out.println("   - You have " + maxAttempts + " attempts per round");
        System.out.println("   - Total rounds: " + MAX_ROUNDS);
        System.out.println("   - Hints available: " + MAX_HINTS + " per round");
        System.out.println("   - Type 'hint' to get a clue (costs points!)");
        System.out.println("   - Type 'exit' to quit the game anytime");
        System.out.println("   - Fewer attempts = More points!\n");
    }
    
    private void playRound() {
        System.out.println("\n=============================================");
        System.out.println("         ROUND " + currentRound + " of " + MAX_ROUNDS + " [" + difficultyName + "]");
        System.out.println("=============================================");
        
        int targetNumber = random.nextInt(maxRange) + 1;
        int attempts = 0;
        boolean hasWon = false;
        hintsRemaining = MAX_HINTS;
        
        while (attempts < maxAttempts && !hasWon) {
            attempts++;
            int attemptsLeft = maxAttempts - attempts + 1;
            
            System.out.print("\nAttempt " + attempts + "/" + maxAttempts);
            if (hintsRemaining > 0) {
                System.out.print(" (Hints: " + hintsRemaining + ")");
            }
            System.out.print(" - Enter guess, 'hint', or 'exit': ");
            
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("hint")) {
                if (hintsRemaining > 0) {
                    giveHint(targetNumber, attempts);
                    hintsRemaining--;
                    attempts--;
                } else {
                    System.out.println("No hints remaining!");
                    attempts--;
                }
                continue;
            }
            
            int userGuess;
            try {
                userGuess = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number, 'hint', or 'exit'.");
                attempts--;
                continue;
            }
            
            if (userGuess < 1 || userGuess > maxRange) {
                System.out.println("Please enter a number between 1 and " + maxRange);
                attempts--;
                continue;
            }
            
            if (userGuess == targetNumber) {
                hasWon = true;
                int roundScore = calculateScore(attempts, MAX_HINTS - hintsRemaining);
                totalScore += roundScore;
                
                System.out.println("\nCORRECT! You guessed it!");
                System.out.println("You found the number in " + attempts + " attempts!");
                System.out.println("Round Score: " + roundScore + " points");
                System.out.println("Total Score: " + totalScore + " points");
            } else {
                int difference = Math.abs(userGuess - targetNumber);
                
                if (userGuess < targetNumber) {
                    System.out.print("Too LOW! ");
                } else {
                    System.out.print("Too HIGH! ");
                }
                
                if (difference <= 5) {
                    System.out.println("VERY HOT! You're extremely close!");
                } else if (difference <= 10) {
                    System.out.println("HOT! Getting closer!");
                } else if (difference <= 20) {
                    System.out.println("WARM! You're in the zone!");
                } else if (difference <= 30) {
                    System.out.println("COLD! Not quite there...");
                } else {
                    System.out.println("FREEZING! Far away!");
                }
                
                if (attemptsLeft > 1) {
                    System.out.println("You have " + (attemptsLeft - 1) + " attempts remaining.");
                }
            }
        }
        
        if (!hasWon) {
            System.out.println("\nOut of attempts! The number was: " + targetNumber);
            System.out.println("Total Score: " + totalScore + " points");
        }
    }
    
    private void giveHint(int targetNumber, int currentAttempt) {
        int hintType = random.nextInt(3);
        
        switch (hintType) {
            case 0:
                if (targetNumber % 2 == 0) {
                    System.out.println("HINT: The number is EVEN");
                } else {
                    System.out.println("HINT: The number is ODD");
                }
                break;
            case 1:
                int rangeSize = maxRange / 4;
                int lowerBound = ((targetNumber - 1) / rangeSize) * rangeSize + 1;
                int upperBound = Math.min(lowerBound + rangeSize - 1, maxRange);
                System.out.println("HINT: The number is between " + lowerBound + " and " + upperBound);
                break;
            case 2:
                int[] divisors = {3, 5, 7, 10};
                int randomDivisor = divisors[random.nextInt(divisors.length)];
                if (targetNumber % randomDivisor == 0) {
                    System.out.println("HINT: The number is divisible by " + randomDivisor);
                } else {
                    System.out.println("HINT: The number is NOT divisible by " + randomDivisor);
                }
                break;
        }
        
        System.out.println("Hint used! -10 points from round score.");
    }
    
    private int calculateScore(int attempts, int hintsUsed) {
        int baseScore = 0;
        
        switch (difficultyName) {
            case "EASY":
                baseScore = 80;
                break;
            case "MEDIUM":
                baseScore = 100;
                break;
            case "HARD":
                baseScore = 150;
                break;
        }
        
        int attemptPenalty = (attempts - 1) * 10;
        int hintPenalty = hintsUsed * 10;
        
        return Math.max(baseScore - attemptPenalty - hintPenalty, 10);
    }
    
    private void displayFinalScore() {
        System.out.println("\n=============================================");
        System.out.println("           GAME OVER!");
        System.out.println("=============================================");
        System.out.println("Final Score: " + totalScore + " points");
        
        int maxPossibleScore = 0;
        switch (difficultyName) {
            case "EASY":
                maxPossibleScore = MAX_ROUNDS * 80;
                break;
            case "MEDIUM":
                maxPossibleScore = MAX_ROUNDS * 100;
                break;
            case "HARD":
                maxPossibleScore = MAX_ROUNDS * 150;
                break;
        }
        
        System.out.println("Maximum Possible Score: " + maxPossibleScore + " points");
        
        double percentage = (totalScore * 100.0) / maxPossibleScore;
        System.out.println("Performance: " + String.format("%.1f", percentage) + "%");
        
        System.out.println("\n" + getRating(percentage));
    }
    
    private String getRating(double percentage) {
        if (percentage >= 90) {
            return "EXCELLENT! You're a master guesser!";
        } else if (percentage >= 75) {
            return "GREAT JOB! Very impressive!";
        } else if (percentage >= 60) {
            return "GOOD WORK! Keep practicing!";
        } else if (percentage >= 40) {
            return "NOT BAD! Room for improvement!";
        } else {
            return "KEEP TRYING! Practice makes perfect!";
        }
    }
    
    private void askPlayAgain() {
        System.out.print("\nWould you like to play again? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        
        if (response.equals("yes") || response.equals("y")) {
            totalScore = 0;
            currentRound = 0;
            System.out.println("\n=============================================\n");
            startGame();
        } else {
            System.out.println("\nThanks for playing! Goodbye!");
            scanner.close();
        }
    }
    
    public static void main(String[] args) {
        GuessTheNumber game = new GuessTheNumber();
        game.startGame();
    }
}
