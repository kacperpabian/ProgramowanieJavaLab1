package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import quizLib.Answers;
import quizLib.Questions;
import quizLib.Student;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;



public class Statistics {

    @FXML
    private Button buttonShowGrades;

    @FXML
    private Button showChart;

    @FXML
    private ListView<?> listViewGrades;

    @FXML
    private BarChart<?, ?> gradesChart;

    @FXML
    private Button generateStatistics;

    @FXML
    private TableView<?> tableStudentView;

    @FXML
    private Label labelAverage;

    @FXML
    private Button buttonChoose;

    List<Student> studentsList;


    private int numberOfQuestions = 8;
    private String templateTest="Resources/Szablon.txt", answersTest="Resources/Odpowiedzi.csv";

    public String getTemplateTest() {
        return templateTest;
    }

    public void setTemplateTest(String templateTest) {
        this.templateTest = templateTest;
    }

    public String getAnswersTest() {
        return answersTest;
    }

    public void setAnswersTest(String answersTest) {
        this.answersTest = answersTest;
    }

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
        studentsList = new ArrayList<>();
        HashMap<Integer, String> correctAnswers;

        File file = new File(getTemplateTest());
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

        file = new File(getAnswersTest());
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
            if(i%9 == 0 && i !=0)
            {
                student.setAnswers(answers);
                studentsList.add(student);
                student = new Student();
                answers = new Answers();
            }
            if(i%9 == 0)
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
            double gradeTemp = getStudentGrade(numberOfCorrectAnswers);
            studentsList.get(i).setGrade(gradeTemp);
        }

        labelAverage.setText(String.valueOf(getAverage()));

        showChart.setDisable(false);


    }

    public void chooseTemplate(ActionEvent event)
    {

        FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String file = dialog.getDirectory() + dialog.getFile();
        setTemplateTest(file);
    }

    public void chooseAnswers(ActionEvent event)
    {

        FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String file = dialog.getDirectory() + dialog.getFile();
        setAnswersTest(file);
    }

    /**
     * Wyswietl wykres
     * @param event
     */
    public void getChart(ActionEvent event)
    {
        XYChart.Series set1 = new XYChart.Series<>();

        set1.getData().add(new XYChart.Data("5.5", countGrades(5.5)));
        set1.getData().add(new XYChart.Data("5.0", countGrades(5.0)));
        set1.getData().add(new XYChart.Data("4.5", countGrades(4.5)));
        set1.getData().add(new XYChart.Data("4.0", countGrades(4.0)));
        set1.getData().add(new XYChart.Data("3.5", countGrades(3.5)));
        set1.getData().add(new XYChart.Data("3.0", countGrades(3.0)));
        set1.getData().add(new XYChart.Data("2.0", countGrades(2.0)));

        gradesChart.getData().addAll(set1);
    }



    /**
     * oddaj liczbę ocen
     * @param grade
     * @return
     */
    private int countGrades (double grade)
    {
        int gradeCounter = 0;
        //System.out.println(studentsList.get(6).getAnswers().getStudentAnswers().values());
        for(int i = 0; i<studentsList.size(); i++)
        {
            if(studentsList.get(i).getGrade() == grade)
            {
                gradeCounter++;
            }
        }
        return gradeCounter;
    }

    /**
     * oddaj hashmape poprawnych odpowiedzi
     * @param questions
     * @return
     */
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

    /**
     * oddaj liczbę poprawnych odpowiedzi studenta
     * @param correctAnswers
     * @param studentAnswers
     * @return
     */
    private int getNumberOfCorrectAnswers(HashMap<Integer, String> correctAnswers, HashMap<Integer, String> studentAnswers)
    {
        int numberOfCorrectAnswers = 0;
        for(int key : correctAnswers.keySet()){

            if(correctAnswers.get(key).equals(studentAnswers.get(key))){
                numberOfCorrectAnswers++;
            }
        }

        //System.out.println(correctAnswers.keySet() + " " + correctAnswers.values());
        //System.out.println(numberOfCorrectAnswers);


        return numberOfCorrectAnswers;
    }

    /**
     * oddaj ocenę studenta po liczbe poprawnych
     * @param correctAnswers
     * @return
     */
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

    /**
     * oddaj średnią
     * @return
     */
    private double getAverage()
    {
        DecimalFormat df2 = new DecimalFormat(".##");
        double sumGrades = 0, averageGrade;
        for(int i =0; i < studentsList.size(); i++)
        {
            sumGrades+= studentsList.get(i).getGrade();
        }
        averageGrade = sumGrades/studentsList.size();
        String avgrade = df2.format(averageGrade).toString();
        avgrade = avgrade.replaceAll(",", ".");
        System.out.println(avgrade);
        averageGrade = Double.parseDouble(avgrade);
        return averageGrade;
    }



}
