package file;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class FileAnalyzer {

    private List <String> text;
    private List <String> words;

    public FileAnalyzer(String filename) {
        text = new ArrayList<>();
        words = new ArrayList<>();

        File file = new File(filename);
        try {
            Scanner scanner = null;
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                text.add(line);
                String[] array= line.toLowerCase().replaceAll("(?U)\\pP", "").split("\\s+");
                words.addAll(List.of(array));
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Задание 2.2.1 Подсчет количества различных слов в файле
    // регистр и знаки препинания не учитывается
    public int getNumberOfDistinctWords(){
        Set <String> distinctWords = new HashSet<>(words);
        return distinctWords.size();
    }

    // Задание 2.2.2 Вывод на экран списка различных слов файла, отсортированных по возрастанию их длины (компаратор сначала по длине слова, потом по тексту).
    // а) с помощью Comparator
    public void printSortedWords(){
        Set <String> distinctWords = new HashSet<>(words);
        List<String> list = new ArrayList<String>(distinctWords);
        Collections.sort(list, new Comparator<String>() {
            public int compare(String a, String b) {
                if (a.length() == b.length()){
                    return a.compareTo(b);
                }
                else return a.length() - b.length();
            }
        });
        list.forEach(System.out::println);
    }
    // b) c помощью stream api
    public void printSortedWordsStreamApi(){
        Set <String> distinctWords = new HashSet<>(words);
        distinctWords.stream().sorted((o1, o2)->{
                    if (o1.length() == o2.length()){
                        return o1.compareTo(o2);
                    } else return o1.length() - o2.length();})
                .forEach(System.out::println);
    }

    // Задание 2.2.3 Подсчет количества встреч слов в файле
    // а) стандартными средствами
    public Map<String, Long> countWordsOccurrence() {
        Map<String, Long> map = new HashMap<>();
        for (String w : words) {
            Long n = map.get(w);
            n = (n == null) ? 1 : ++n;
            map.put(w, n);
        }
        return map;
    }
    // b) c помощью stream api
    public Map<String, Long> countWordsOccurrenceStreamApi() {
        Map<String, Long> map = words.stream()
                .collect(Collectors.groupingBy(String::toString, Collectors.counting()));
        return map;
    }

    // Задание 2.2.4 Вывод на экран всех строк файла в обратном порядке.
    public void printReversedLines(){
        ListIterator li = text.listIterator(text.size());
        while(li.hasPrevious()) {
            System.out.println(li.previous());
        }
    }

    // Задание 2.2.5: Реализация своего Iterator для обхода списка в обратном порядке
    public void printReversedLinesWithMyIterator() {
        InverseIterator lines = new InverseIterator(text);
        lines.iterator().forEachRemaining(System.out::println);
    }

    // Задание 2.2.6 Вывод на экран строк, номера которых задаются пользователем в произвольном порядке
    // Номера строк начинаются от 0
    public void printLines(int [] indices){
        for (int i = 0; i < indices.length ; i++) {
            int index = indices[i];
            if ((index < text.size()) && (index >= 0)) {
                System.out.println(text.get(index));
            } else {
                System.out.println("строки " + index + " в файле не существует");
            }
        }
    }
}
