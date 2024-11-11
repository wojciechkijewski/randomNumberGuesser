import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class RandomGame {
    final int MIN_BOUND = 0;
    final int MAX_BOUND_EASY = 101;
    final int MAX_BOUND_MEDIUM = 10001;
    final int MAX_BOUND_HARD = 1000001;
    final int WIN_POINT = 100;
    final int BAD_GUESS = -10;
    final int VICTORY = 1;
    final int LOSE = 1;
    final String PC_NAME = "COMPUTER";

    List<Player> players = new ArrayList<>();
    int currentMaxGuess;
    int computerGuess;
    int guessCount = 0;
    int low = MIN_BOUND;
    boolean hasGuessedCorrectly;
    boolean userGoesFirst;
    Random rand = new Random();
    Scanner scanner = new Scanner(System.in);
    Computer computer = new Computer(PC_NAME);
    int numberToGuess;
    int numberOfPlayers;
    boolean multiplayer = false;
    Player currentPlayer;
    int currentPlayerIndex = 0;
    boolean gameRunning = true;

    public void start() {
        while (gameRunning) {
            System.out.println("SINGLE PLAYER (1) OR MULTIPLAYER? (2)");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 1) {
                initializePlayers();
                currentPlayer = players.get(currentPlayerIndex);
                setDifficulty();
                chooseGameMode();
                askForNewGame();
            } else if (choice == 2) {
                multiplayer = true;
                initializePlayers();
                setDifficulty();
                startMultiplayerGame();
                askForNewGame();
            }
            save();
        }
    }

    public void askForNewGame() {
        while (true) {
            System.out.println("NEW GAME? YES/NO");
            String answer = scanner.nextLine().trim().toUpperCase();

            if (answer.equals("YES")) {
                return;
            } else if (answer.equals("NO")) {
                gameRunning = false;
                return;
            } else {
                System.out.println("Invalid input. Please type 'YES' or 'NO'.");
            }
        }
    }

    public void initializePlayers() {
        if (multiplayer) {
            System.out.println("Choose number of Human Players (1-4):");
            numberOfPlayers = Integer.parseInt(scanner.nextLine());
        }  else {numberOfPlayers = 1;}
        for (int i = 0; i < numberOfPlayers; i++) {
            System.out.println("Enter name for Player " + (i + 1) + ":");
            String playerName = scanner.nextLine().toUpperCase();
            Player player = new Player();
            player.setName(playerName);
            players.add(player);
            if (player.fileExists(playerName)) {
                player.load(playerName);
            } else {
                player.save();
            }
        }
    }

    public void startMultiplayerGame() {
        boolean gameWon = false;

        while (!gameWon) {
            Player currentPlayer = players.get(currentPlayerIndex);
            System.out.println(currentPlayer.getName() + "'s turn. Enter your guess:");
            int guess = scanner.nextInt();

            if (guess == numberToGuess) {
                System.out.println("Congratulations " + currentPlayer.getName() + "! You guessed the number correctly.");
                currentPlayer.addScore(WIN_POINT);
                currentPlayer.setNumberWins(1);
                gameWon = true;
                save();
            } else {
                currentPlayer.addScore(BAD_GUESS);
                System.out.println(guess < numberToGuess ? "Too low!" : "Too high!");
                currentPlayer.setNumberLosses(1);
            }
            currentPlayerIndex = (currentPlayerIndex + 1) % numberOfPlayers;
        }
        displayScores();
    }

    public void save(){
        for (Player player : players) {player.save();}
        if (!multiplayer){ computer.save();}
    }

    public void chooseGameMode(){
        System.out.println("Choose Mode: USER/CPU/MIX ");
        String mode = scanner.nextLine().toUpperCase();
        if(mode.equals("USER")) {userGuessingMode();}
        else if(mode.equals("CPU")) {cpuGuessingMode();}
        else if(mode.equals("MIX")) {mixedGuessingMode();}
        else {
            System.out.println("Invalid Mode");
            chooseGameMode();
        }
    }

    public void setDifficulty(){
        System.out.println("Choose Difficulty: EASY/MEDIUM/HARD ");
        String diff = scanner.nextLine().toUpperCase();
        if(diff.equals("HARD")) {
            numberToGuess = rand.nextInt(MIN_BOUND, MAX_BOUND_HARD);
            currentMaxGuess = MAX_BOUND_HARD;}
        else if(diff.equals("EASY")) {
            numberToGuess = rand.nextInt(MIN_BOUND, MAX_BOUND_EASY);
            currentMaxGuess = MAX_BOUND_EASY;}
        else if(diff.equals("MEDIUM")) {
            numberToGuess = rand.nextInt(MIN_BOUND, MAX_BOUND_MEDIUM);
            currentMaxGuess = MAX_BOUND_MEDIUM;}
        else {
            System.out.println("Invalid Difficulty");
            setDifficulty();
        }
        System.out.println("Difficulty Set! " + MIN_BOUND + "-" + currentMaxGuess);
        System.out.println("#### DEBUG NUMBER: " + numberToGuess + " ####");
    }

    public void userGuessingMode() {
        System.out.println("Welcome " + currentPlayer.getName() + "! Let's play the number guessing game.");
        System.out.println("Number randomly chosen between " + MIN_BOUND + " and " + currentMaxGuess);

        hasGuessedCorrectly = false;

        while (!hasGuessedCorrectly) {
            System.out.print("Enter your guess: ");
            int guess = scanner.nextInt();

            if (guess == numberToGuess) {
                System.out.println("Congratulations " +   currentPlayer.getName() + "! You guessed the number correctly.");
                hasGuessedCorrectly = true;
                currentPlayer.addScore(WIN_POINT);
                currentPlayer.setNumberWins(VICTORY);
            } else if (guess < numberToGuess) {
                currentPlayer.addScore(BAD_GUESS);
                System.out.println("Too low! Try guessing a higher number.");
            } else {
                currentPlayer.addScore(BAD_GUESS);
                System.out.println("Too high! Try guessing a lower number.");
            }
        }
    }

    public void mixedGuessingMode(){
        System.out.println("Welcome " + currentPlayer.getName() + " to Mixed Guessing Mode!");
        System.out.println("Number randomly chosen between " + MIN_BOUND + " and " + currentMaxGuess);
        userGoesFirst = rand.nextBoolean();
        System.out.println(userGoesFirst ? "You go first!" : "The computer goes first!");
        hasGuessedCorrectly = false;

        while (!hasGuessedCorrectly) {
            if (userGoesFirst) {
                // User's turn
                System.out.print("Enter your guess: ");
                int guess = scanner.nextInt();

                if (guess == numberToGuess) {
                    System.out.println("Congratulations " + currentPlayer.getName() + "! You guessed the number correctly.");
                    hasGuessedCorrectly = true;
                    currentPlayer.addScore(WIN_POINT);
                    currentPlayer.setNumberWins(VICTORY);
                    computer.setNumberLosses(LOSE);
                } else {
                    currentPlayer.addScore(BAD_GUESS);
                    System.out.println(guess < numberToGuess ? "Too low! Try guessing a higher number." : "Too high! Try guessing a lower number.");
                }
            } else {
                // Computer's turn
                int computerGuess = rand.nextInt(low, currentMaxGuess + 1);
                System.out.println("Computer guesses: " + computerGuess);

                if (computerGuess == numberToGuess) {
                    System.out.println("The computer guessed the number correctly! Game over.");
                    computer.addScore(WIN_POINT);
                    currentPlayer.setNumberLosses(LOSE);
                    computer.setNumberWins(VICTORY);
                    hasGuessedCorrectly = true;
                } else {
                    if (computerGuess < numberToGuess) {
                        System.out.println("Computer's guess is too low.");
                        computer.setScore(BAD_GUESS/2);
                        low = computerGuess + 1;
                    } else {
                        System.out.println("Computer's guess is too high.");
                        computer.setScore(BAD_GUESS/2);
                        currentMaxGuess = computerGuess - 1;
                    }
                }
            }
            userGoesFirst = !userGoesFirst;
        }
    }

    public void cpuGuessingMode() {
        System.out.println("Welcome to CPU Guessing Mode!");
        System.out.println("Please think of a number between " + MIN_BOUND + " and " + currentMaxGuess + " (inclusive).");
        System.out.println("Input number and press Enter when you are ready.");
        numberToGuess = Integer.parseInt(scanner.nextLine());

        do {
            computerGuess = rand.nextInt(low, currentMaxGuess + 1);
            guessCount++;
            System.out.println("Computer guesses: " + computerGuess);
            if (computerGuess > numberToGuess) {
                System.out.println("Guess too high.");
                currentMaxGuess = computerGuess - 1;
            } else if (computerGuess < numberToGuess) {
                System.out.println("Guess too low.");
                low = computerGuess + 1;
            }
        } while (computerGuess != numberToGuess);
        if (WIN_POINT < guessCount*5){
            computer.setNumberLosses(LOSE);
            System.out.println("The computer guessed the number correctly in " + guessCount + " and lost due to too many attempts!");
        }
        else {
            computer.setNumberWins(VICTORY);
            computer.addScore(WIN_POINT - guessCount*5);
            System.out.println("The computer guessed the number correctly in " + guessCount + " attempts!");
        }
    }

    private void displayScores() {
        System.out.println("Final Scores (Best to Worst):");
        players.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));
        for (Player player : players) {
            System.out.println(player.getName() + " - Score: " + player.getScore() +
                    ", Wins: " + player.getNumberWins() +
                    ", Losses: " + player.getNumberLosses());
        }
    }
}
