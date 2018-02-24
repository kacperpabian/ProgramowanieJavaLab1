package sample;

import javafx.event.ActionEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Statistics {

    public void loadFiles (ActionEvent event) throws FileNotFoundException {
        List<String> szablonFile = new ArrayList<String>();
        List<String> properAnswers = new ArrayList<String>();
        List<String> studentAnswers = new ArrayList<String>();

        File file = new File("Resources\\Szablon.txt");
        Scanner sc = new Scanner(file);


        while(sc.hasNextLine())
        {
            szablonFile.add(sc.nextLine());
        }

        for (int i = 0; i < szablonFile.size(); i++) {

            //System.out.println(szablonFile.get(i));
            if(i%6 == 1)
            {
                properAnswers.add(szablonFile.get(i));
                System.out.println(szablonFile.get(i));
            }
        }
        file = new File("Resources\\Odpowiedzi.csv");
        sc = new Scanner(file);

        while(sc.hasNextLine())
        {
            studentAnswers.add(sc.nextLine());
        }

        for (int i = 0; i < studentAnswers.size(); i++) {

            System.out.println(studentAnswers.get(i));
        }
    }
}
