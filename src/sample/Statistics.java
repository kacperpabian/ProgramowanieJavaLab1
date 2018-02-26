package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;
import quizLib.Answers;
import quizLib.Questions;
import quizLib.Student;


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
        List<Questions> questions = new ArrayList<>();
        List<Student> studentsList = new ArrayList<>();
        HashMap<Integer, String> correctAnswers;

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
        Questions question = new Questions();
        for (int i = 0; i < szablonFile.size(); i++) {
            if(i%6 == 0 && i !=0)
            {
                question = new Questions();
            }
            if(i%6==0)
            {
                String[] numberAndQuestion = szablonFile.get(i).split("_");
                question.setNumber(Integer.parseInt(numberAndQuestion[0]));
                question.setQuestion(numberAndQuestion[1]);
            }
            else if(i%6 == 1)
            {
                question.setCorrectAnswer(szablonFile.get(i));
            }
            else
            {
                String[] numberAndAnswer = szablonFile.get(i).split("_");
                question.getAnswers().put(numberAndAnswer[0], numberAndAnswer[1]);
            }
            if(i%6 == 5)
            {
                questions.add(question);
            }
        }

        correctAnswers = getCorrectAnswersHash(questions);

        file = new File("Resources\\Odpowiedzi.csv");
        sc = new Scanner(file);

        List<String> studentAnswersTemp = new ArrayList<>();
        /**
         * generpwanie podwójnej listy w której w każdej z list, index 0 to Imię nazwisko;index, kolejne to odpowiedzi na zadania
         * */

        while(sc.hasNextLine())
        {
            studentAnswersTemp.add(sc.nextLine());
        }

        Student student = new Student();
        Answers answers = new Answers();
        for (int i = 0; i < studentAnswersTemp.size(); i++)
        {
            if(i%5 == 0 && i !=0)
            {
                student.setAnswers(answers);
                studentsList.add(student);
                student = new Student();
                answers = new Answers();
            }
            if(i%5 == 0)
            {
                String[] nameSurnameIndex = studentAnswersTemp.get(i).split(";");
                student.setName(nameSurnameIndex[0]);
                student.setSurname(nameSurnameIndex[1]);
                student.setIndex(Integer.parseInt(nameSurnameIndex[2]));
            }
            else
            {
                String[] questionAnswer = studentAnswersTemp.get(i).split(";");
                answers.getStudentAnswers().put(Integer.parseInt(questionAnswer[0]), questionAnswer[1]);
            }
        }

        for(int i=0; i<studentsList.size(); i++)
        {
            int numberOfCorrectAnswers;
            numberOfCorrectAnswers = getNumberOfCorrectAnswers(correctAnswers, studentsList.get(i).getAnswers().getStudentAnswers());
            studentsList.get(i).setGrade(getStudentGrade(numberOfCorrectAnswers));
        }


    }

    private HashMap<Integer,String> getCorrectAnswersHash(List<Questions> questions)
    {
        HashMap<Integer, String> correctAnswers = new HashMap<>();
        for(int i = 0; i < questions.size();i++)
        {
            int m= i+1;
            correctAnswers.put(m, questions.get(i).getCorrectAnswer());
        }
        return correctAnswers;
    }
    private int getNumberOfCorrectAnswers(HashMap<Integer, String> correctAnswers, HashMap<Integer, String> studentAnswers)
    {
        int numberOfCorrectAnswers = 0;
        for(int key : correctAnswers.keySet()){

            if(correctAnswers.get(key).equals(studentAnswers.get(key))){
                numberOfCorrectAnswers++;
            }
        }
        return numberOfCorrectAnswers;
    }
    private double getStudentGrade(int correctAnswers)
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
