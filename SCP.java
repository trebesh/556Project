/*
  Class to produce solutions to the Set Cover Problem by implementing parellel Ant Colony Optimisation.
    ConnorFergusson_1299038_HannahTrebes_1306378

    Command Line:
    javac *.java && java SCP <TEST_FILE>.txt <COLONY_SIZE> <GENERATION_SIZE>
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
    public static int numGenerations = 0;

    public static String filename;
    public static BufferedReader reader;
    public static ArrayList<Integer> universe = new ArrayList<Integer>();
    public static ArrayList<Ant> colony = new ArrayList<Ant>();
    public static ArrayList<Integer> set = new ArrayList<Integer>();
    public static ArrayList<ArrayList<Integer>> sets = new ArrayList<ArrayList<Integer>>();
    public static String[] inString;
    public static ArrayList<Integer> bestPath = new ArrayList<Integer>();


    public static void main(String[] args) {
        try{
            filename = args[0];
            colonySize = Integer.parseInt(args[1]);
            generationSize = Integer.parseInt(args[2]);
            if((colonySize % generationSize) != 0) throw new Exception("Generation size must be an integer factor of colony size.");
            numGenerations = (colonySize/generationSize);

            //Read in the file
            getProblem(filename);

            //Create the first generation of Ants
            createColony();

            //Run generation till all ants have visited all nodes
            for (int i = 0; i < numGenerations; i++)runGeneration();

            //Output results in the form of the colony
            System.out.println();
            System.out.println("Final Paths: ");
            printColony();

            //Output final Results
            int bi = 0;
            for (int i = bi; i < colonySize; i++) {
                if(colony.get(i).Path.size() < bestPath.size()) {
                    bestPath = colony.get(i).Path;
                    bi = i;
                }
            }
            System.out.println();
            System.out.println("Optimal Path:");
            System.out.println("Ant " + bi + " " + colony.get(bi).toString());


        }catch (Exception e){
            System.out.println("ERROR in Main: " + e.getMessage());
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
                colony.add(new Ant(numGenerations));
            }

        }catch(Exception e){
            System.out.println("ERROR in createColony");
            System.out.println("Colony: " + colony);
            System.out.println();
            System.out.println(e.toString());
        }
    }

    // Runs the current generation through
    public static void runGeneration(){
        int adjust = generationSize*generation;
         //Send out the ants to their start points
         for (int i = generationSize -1; i>0; i--) {
            colony.get(i + adjust).startPositions(sets);
        }

        //move the ants out until they have found a valid solution or seen all nodes
        for (int i = 0; i<generationSize;i++) {
            while (colony.get(i + adjust).seenAll == false && colony.get(i + adjust).seenAllNumbers(universe, sets) == false){
                colony.get(i + adjust).addToPath(colony.get(i + adjust).getNextStep(sets, colony));
            }
        }

        //Decrease the trail strength of previous generations
        degradeTrails(adjust);

        generation++;
        System.out.println("Generation: " + generation);
        printColony();
//        System.out.println("Adjust by " + adjust);
    }

    public static void degradeTrails(int upto){
        for (int i = 0; i < upto; i++){
            colony.get(i).degradeTrail();
        }

    }


    //Auxillary method to output the colony
    public static void printColony(){
        for (int i = 0; i < colony.size(); i++) {
            System.out.println("Ant " + (i + 1) + " " + colony.get(i).toString());
        }
    }
}