import java.io.IOException;
import java.util.Scanner;

public class Morphology {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
//        String text = "Повторное появление леопарда в Осетии позволяет предположить," +
//                " что леопард постоянно обитает в некоторых районах Северного Кавказа.";
        Lemmatizer lemmatizer = new Lemmatizer(text);
        lemmatizer.getLemmas();
    }
}