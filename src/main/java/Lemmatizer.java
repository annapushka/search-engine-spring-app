import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.io.IOException;
import java.util.*;

public class Lemmatizer {
    private String text;
    private List<String> generalWords;
    private HashMap<String, Integer> wordsCounts;

    public Lemmatizer(String text) {
        this.text = text;
        this.wordsCounts = new HashMap<>();
        this.generalWords = new ArrayList<>();
    }

    public void getLemmas() throws IOException {
        String[] words = text.toLowerCase().replaceAll("[^А-Яа-я]+", " ")
                .split("\\s");
        for(int i = 0; i < words.length; i++) {
            isAuxiliaryPart(words[i]);
        }
        generalWords.forEach(word -> {
            int count = 1;
            if(wordsCounts.containsKey(word)){
                count++;
            }
            wordsCounts.put(word, count);
        });
        printString();
    }

    public void printString() {
        wordsCounts.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        });
    }

    public void isAuxiliaryPart(String word) throws IOException {
        LuceneMorphology luceneMorph = new RussianLuceneMorphology();
        List<String> wordBaseForms = luceneMorph.getMorphInfo(word);
        for (String form : wordBaseForms) {
            if (form.contains("СОЮЗ") || form.contains("ЧАСТ")
                    || form.contains("ПРЕДЛ") || form.contains("МЕЖД")) {
                continue;
            } else if(form.contains("ед") || form.contains("ИНФИНИТИВ")){
                String[] paths = form.split("\\|");
                generalWords.add(paths[0]);
            }
        }
    }
}
