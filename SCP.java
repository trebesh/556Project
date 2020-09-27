/*
  Class to produce solutions to the Set Cover Problem by implementing parellel Ant Colony Optimisation.
    ConnorFergusson_1299038_HannahTrebes_1306378
*/

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

public class SCP {

    public static int colonySize = 0;
    public static int generation = 0;
    public static int generationSize = 0;

    public static String filename;
    public static BufferedReader reader;
    public static ArrayList<Integer> universe = new ArrayList<Integer>();
    public static ArrayList<Ant> colony = new ArrayList<Ant>();
    public static ArrayList<Integer> set = new ArrayList<Integer>();
    public static ArrayList<ArrayList<Integer>> sets = new ArrayList<ArrayList<Integer>>();
    public static String[] inString;

    public static void main(String[] args) {
        try{
            filename = args[0];
            colonySize = Integer.parseInt(args[1]);
            generationSize = Integer.parseInt(args[2]);
            //Read in the file
            getProblem(filename);

            //Create the first generation of Ants
            createColony();

            //Run generation till all ants have visited all nodes
            runGeneration(generation);
            printColony();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    // Reads in the problem Instance to be dealt with
    public static void getProblem(String filename){
        String line = "";
        try {
            //Get the problem instance
            reader = new BufferedReader(new FileReader(filename));
            line = reader.readLine();

            //Assume the first line contains the Universe
            inString = line.split(",");

            for (int i = 0; i < inString.length; i++) {
                universe.add(Integer.parseInt(inString[i]));
            }
            Collections.sort(universe);
            System.out.println("Universe:" + universe);

            //Gather in the sets
            line = reader.readLine();
            while (line != null){
                inString = line.split(",");
                for (int i = 0; i < inString.length; i++) {
                    set.add(Integer.parseInt(inString[i]));
                }
                //System.out.println("Set: " + set);
                sets.add(set);
                set = new ArrayList<Integer>();
                line = reader.readLine();
            }
            System.out.println("Sets: " + sets);

        }catch(FileNotFoundException e){System.out.println("File Not Found : " + filename);}
        catch(Exception e){
            System.out.println("ERROR in getProblem");
            System.out.println("File Name: " + filename);
            System.out.println("Universe: " + universe);
            System.out.println("Line: " + line);
            System.out.println("InString: " + inString.toString());
            System.out.println();
            System.out.println(e.toString());
        }
    }

    // Creates the colony and first generation of Ants
    public static void createColony(){
        try {
            //Figure out how big the colony and each generation should be - relative to the number of sets in the search space
            // Naieve - one ant for every set in sets, one generation
            
            //Create the colony
            while (colony.size() < colonySize) {
                colony.add(new Ant());
            }

        }catch(Exception e){
            System.out.println("ERROR in createColony");
            System.out.println("Colony: " + colony);
            System.out.println();
            System.out.println(e.toString());
        }
    }

    // Runs a generation through till there are no places that each hasn't visited
    public static void runGeneration(int gen){
         //Send out the first generation (gen 0) 1 step
         for (int i=(generation + generationSize - 1); i>generation; i--) {
            colony.get(i).startPositions(sets);
        }


        for (int i = gen; i<gen+generationSize;i++) {
            while (colony.get(i).seenAll == false && colony.get(i).seenAllNumbers(universe, sets) == false){
                colony.get(i).addToPath(colony.get(i).getNextStep(sets));
            }
        }
    }


    //Auxillary method to output the colony
    public static void printColony(){
        for (int i = 0; i < colony.size(); i++) {
            System.out.println("Ant " + (i + 1)  + " has path " + colony.get(i).toString());
        }
    }
}