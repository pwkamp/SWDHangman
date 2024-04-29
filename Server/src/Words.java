import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Words {
    ArrayList<String> words = new ArrayList<>();

    public Words(String path) {
        try {
            this.addWordsFromFile(path);
        } catch (IOException e) {
            System.out.println("Error reading file: " + path);
        }
    }

    public void addWordsFromFile(String path) throws IOException {
        Scanner reader = new Scanner(Paths.get(path));
        String line;

        while (reader.hasNext()) {
            line = reader.nextLine();
            words.add(line.toLowerCase());
        }
    }

    public void displayWords() {
        for (String word : words) {
            System.out.println(word);
        }
    }

    public String getRandomWord() {
        int randIndex = (int) (Math.random() * words.size());
        return words.get(randIndex);
    }

}
