import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class User {
    private String name;
    private int score;
    private final String saveLocation = "C:\\Users\\wooyt\\IdeaProjects\\randomNumberGuesser\\";
    private int numberWins = 0;
    private int numberLosses = 0;

    public User(){
        this.name = null;
        this.score = 0;
    }
    public User(final String name){this.name = "Computer";}

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveLocation + getName() + ".txt"))) {
            writer.write(getName() + "\n");
            writer.write(Integer.toString(getScore())+ "\n");
            writer.write(Integer.toString(getNumberWins()) + "\n");
            writer.write(Integer.toString(getNumberLosses()));
            System.out.println("Saving player: "+ getName() + " data.");
        } catch (IOException e) {
            System.out.println("Error saving player: "+ getName() + " data.");
            e.printStackTrace();
        }
    }

    public void load(final String username) {
        File file = new File(saveLocation + username + ".txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String name = reader.readLine();
                int score = Integer.parseInt(reader.readLine());
                int numberWins = Integer.parseInt(reader.readLine());
                int numberLoses = Integer.parseInt(reader.readLine());
                setName(name);
                setScore(score);
                setNumberWins(numberWins);
                setNumberLosses(numberLoses);
                System.out.println("Player data loaded.");
            } catch (IOException e) {
                System.out.println("Error loading player data. User does not logged in before.");
                e.printStackTrace();
            }
        }
        System.out.println("Username: " + getName() + " & Score: " + getScore() );
        System.out.println("Wins: " + getNumberWins() + " & Losses: " + getNumberLosses());
    }

    public boolean fileExists(final String username) {
        File file = new File(saveLocation + username + ".txt");
        return file.exists();
    }

    public void setScore(int score) {this.score = score;}
    public int getScore() {return this.score;}
    public void setName(String name) {
        this.name = name.toUpperCase();
    }
    public String getName() {
        return this.name.toUpperCase();
    }
    public void addScore(int amount){
        this.score += amount;
    }
    public int getNumberWins() {return this.numberWins;}
    public void setNumberWins(int numberWins) {this.numberWins += numberWins;}
    public int getNumberLosses() {return numberLosses;}
    public void setNumberLosses(int numberLosses) {this.numberLosses += numberLosses;}
}
