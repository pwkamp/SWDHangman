import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
        BufferedReader reader = new BufferedReader(new FileReader(new File(path).getAbsolutePath()));
        String line = reader.readLine();

        System.out.println(line);
        while (line != null) {
            words.add(line);
            line = reader.readLine();
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
