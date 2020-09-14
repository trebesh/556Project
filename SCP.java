/*
  Class to produce solutions to the Set Cover Problem by implementing parellel Ant Colony Optimisation.
    ConnorFergusson_1299038_HannahTrebes_1306378
*/

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class SCP {

    public static String filename;
    public static BufferedReader reader;
    public static ArrayList<Integer> universe;

    public static void main(String[] args) {
        try{
            filename = args[0];

            //Read in the file
            getProblem(filename);
        }catch (Exception e){
            System.out.println("Arguments not in correct format");
            System.out.println("Try: FileName.txt");
        }

    }

    public static void getProblem(String filename){
        try {
            //Get the problem instance
            reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();

            //Assume the first line contains the Universe
            universe = Integer.parseInt(line.split(","));

            while (line != null){

            }

        }catch(FileNotFoundException e){System.out.println("File Not Found : " + filename);}
        catch(Exception e){System.out.println("ERROR in getProblem");}
    }
}
