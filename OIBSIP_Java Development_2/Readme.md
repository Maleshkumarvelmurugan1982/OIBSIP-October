# Guess The Number Game

## Objective
A console-based number guessing game where players attempt to guess a randomly generated number within a limited number of attempts. The game features multiple difficulty levels, a scoring system, hints, and multi-round gameplay.

## Game Features

### Difficulty Levels
- **Easy**: Guess numbers 1-50 with 10 attempts
- **Medium**: Guess numbers 1-100 with 7 attempts  
- **Hard**: Guess numbers 1-200 with 5 attempts

### Gameplay Mechanics
- **3 rounds per game** with cumulative scoring
- **2 hints per round** (each hint costs 10 points)
- **Proximity feedback**: VERY HOT, HOT, WARM, COLD, FREEZING
- **Dynamic scoring**: Higher base scores for harder difficulties, penalties for extra attempts and hints

### Hint System
Three types of hints available:
1. Whether the number is even or odd
2. A range where the number falls (1/4 of total range)
3. Divisibility by 3, 5, 7, or 10

## How to Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Command line/terminal access

### Compilation
```bash
javac GuessTheNumber.java
```

### Execution
```bash
java GuessTheNumber
```

## Implementation Details

### Tools & Technologies Used
- **Language**: Java
- **Core Libraries**: 
  - `java.util.Random` - Random number generation
  - `java.util.Scanner` - User input handling
- **Design Pattern**: Object-oriented with encapsulated game state

### Key Components
1. **Difficulty Configuration**: Constants for range limits and attempt counts
2. **Game State Management**: Tracks score, rounds, hints, and attempts
3. **Input Validation**: Handles invalid inputs gracefully
4. **Scoring Algorithm**: Base score minus penalties for attempts and hints
5. **Performance Rating**: Percentage-based evaluation system

## Scoring System

### Base Scores
- Easy: 80 points per round
- Medium: 100 points per round
- Hard: 150 points per round

### Penalties
- Each additional attempt: -10 points
- Each hint used: -10 points
- Minimum score per round: 10 points

### Performance Ratings
- 90%+: EXCELLENT! You're a master guesser!
- 75-89%: GREAT JOB! Very impressive!
- 60-74%: GOOD WORK! Keep practicing!
- 40-59%: NOT BAD! Room for improvement!
- Below 40%: KEEP TRYING! Practice makes perfect!

## Outcome
A fully functional, interactive command-line game that provides:
- Engaging user experience with clear feedback
- Multiple difficulty levels for varied challenge
- Strategic hint system for assistance
- Comprehensive scoring and performance tracking
- Replay capability for extended gameplay

The game successfully demonstrates core programming concepts including loops, conditionals, user input handling, random number generation, and score calculation algorithms.
