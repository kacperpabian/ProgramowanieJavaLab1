package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

public class Statistics {

    @FXML
    ListView<String> listViewGrades = new ListView<>();

    int numberOfQuestions = 4;

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }
    /**
     * scrollPane pokazujący studenta i jego ocenę
     */


    /**
     * funkcja wywołana po naciśnięciu przycisku Generuj statystyki.
     * Przetwarza plik Szablon oraz odpowiedzi
     * */


    public void loadFiles (ActionEvent event) throws FileNotFoundException {
        List<String> szablonFile = new ArrayList<>();
        List<String> properAnswers = new ArrayList<>();
        List<List<String>> studentAnswers = new ArrayList<>();
        List<Student> studentsList = new ArrayList<>();

        File file = new File("Resources\\Szablon.txt");
        Scanner sc = new Scanner(file);

    /**
     * sczytywanie pliku do listy
     * */
        while(sc.hasNextLine())
        {
            szablonFile.add(sc.nextLine());
        }
    /**
     * pętla generująca liste zawierającą prawidłowe odpowiedzi, sczytane z szablonu
     * */
        for (int i = 0; i < szablonFile.size(); i++) {
            if(i%6 == 1)
            {
                properAnswers.add(szablonFile.get(i));
            }
        }
        file = new File("Resources\\Odpowiedzi.csv");
        sc = new Scanner(file);

        int counter = 0;
        List<String> studentAnswersTemp = new ArrayList<>();

        /**
         * generpwanie podwójnej listy w której w każdej z list, index 0 to Imię nazwisko;index, kolejne to odpowiedzi na zadania
         * */

        while(sc.hasNextLine())
        {

            studentAnswersTemp.add(sc.nextLine());
            counter ++;

            if(counter == 5)
            {
                List<String> answers = new ArrayList<>(studentAnswersTemp);
                studentAnswers.add(answers);
                studentAnswersTemp.clear();
                counter = 0;
            }
        }

        int correctAnswers = 0;
        double grade;
        for (int i = 0; i < studentAnswers.size(); i++) {

            String[] nameSurnameIndex = studentAnswers.get(i).get(0).split(";");

            Student student = new Student();

            student.setName(nameSurnameIndex[0]);
            student.setSurname(nameSurnameIndex[1]);
            student.setIndex(Integer.parseInt(nameSurnameIndex[2]));

            for(int j = 1; j<studentAnswers.get(i).size();j++)
            {
                int m = j-1;

                if(studentAnswers.get(i).get(j).charAt(2) == properAnswers.get(m).charAt(0))
                {
                    System.out.println(studentAnswers.get(i).get(0));
                    correctAnswers++;
                }
            }
            grade = getGrade(correctAnswers);
            student.setGrade(grade);
            studentsList.add(student);
            //String student = studentAnswers.get(i).get(0).replaceAll(";", " ");
            correctAnswers = 0;
        }
//        for(int i = 0; i<studentsList.size(); i++)
//        {
//            System.out.println(studentsList.get(i).getGrade());
//        }

    }

    private double getGrade(int correctAnswers)
    {
        double gradeFactor = ((double)correctAnswers/(double)getNumberOfQuestions());
        if(gradeFactor < 0.55)
        {
            return 2;
        }
        else if(gradeFactor >= 0.55 && gradeFactor < 0.63 )
        {
            return 3;
        }
        else if(gradeFactor >= 0.63 && gradeFactor < 0.72 )
        {
            return 3.5;
        }
        else if(gradeFactor >= 0.72 && gradeFactor < 0.81 )
        {
            return 4;
        }
        else if(gradeFactor >= 0.81 && gradeFactor < 0.9 )
        {
            return 4.5;
        }
        else if(gradeFactor >= 0.9 && gradeFactor < 0.99 )
        {
            return 5;
        }
        else
        {
            return 5.5;
        }
    }
}
