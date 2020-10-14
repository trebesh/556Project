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

public class SCP{

    public static int adjust;
    public static int colonySize = 0;
    public static int generation = 0;
    public static int generationSize = 0;
    public static int numGenerations = 0;
    public static int degrade = 1;

    public static String filename;
    public static BufferedReader reader;
    public static ArrayList<Integer> universe = new ArrayList<Integer>();
    public static ArrayList<Ant> colony = new ArrayList<Ant>();
    public static ArrayList<Integer> set = new ArrayList<Integer>();
    public static ArrayList<ArrayList<Integer>> sets = new ArrayList<ArrayList<Integer>>();
    public static String[] inString;
    public static ArrayList<Integer> bestPath;// = new ArrayList<Integer>();
    public static ArrayList<Integer> pheremones = new ArrayList<Integer>();


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
            

            //Calculate final Results
            int bi = 0;
            int numBest = 0;
            int worstsize = 0;
            int numWorst = 0;
            int averageLength = 0;
            bestPath = colony.get(0).Path;
            for (int i = 0; i < colonySize; i++) {
                if(colony.get(i).Path.size() < bestPath.size()) {
                    bestPath = colony.get(i).Path;
                    bi = i;
                }
                if(colony.get(i).Path.size() <= bestPath.size()) {
                    numBest++;
                }
                if(colony.get(i).Path.size() >= worstsize) {
                    worstsize = colony.get(i).Path.size();
                    numWorst++;
                }
                averageLength += colony.get(i).Path.size();
            }
            averageLength = averageLength/colony.size();

            //Output final results
            System.out.println();
            System.out.println("Final Pheremone strengths: ");
            System.out.println(pheremones);
            System.out.println("Degrading by: " + degrade);
            System.out.println();
            System.out.println("Best Path:");
            System.out.println("(First instance of shortest path - other equally short paths possible)");
            System.out.println("   Ant " + (bi + 1) + " " + bestPath.toString());
            for (int i = 0; i < bestPath.size();i++){
                System.out.print("   " + sets.get(bestPath.get(i)));
            }
            System.out.println();
            System.out.println();
            System.out.println("General Statistics:");
            System.out.println("   Length of Best Path: " + bestPath.size());
            System.out.println("   Number of times best path achieved: " + numBest);
            System.out.println("   Length of worst path: " + worstsize);
            System.out.println("   Number of times worst path seen: " + numWorst);
            System.out.println("   Average path length: " + averageLength);


        }catch (Exception e){
            System.out.println("ERROR in Main: " + e.getMessage());
        }

    }

    public void run(){

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

                //Create a blank pheremone
                pheremones.add(0);
            }
            System.out.println("Sets: " + sets);

            //Create the pheremones
            System.out.println("PheremoneSize: " + pheremones.size());

            //Set the degradation
            degrade = generationSize;

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

    // Runs the current generation through
    public static void runGeneration(){
        adjust = generationSize*generation;
         //Send out the ants to their start points
         for (int i = generationSize -1; i>0; i--) {
            colony.get(i + adjust).startPositions(sets);
        }

        //move the ants out until they have found a valid solution or seen all nodes
        for (int i = 0; i<generationSize;i++) {
            Generation temp = new Generation(i);
            temp.start();
        }

        //Create the pheremone trail
        for (Ant a:colony) {
            for (Integer i: a.Path) {
                pheremones.set(i, pheremones.get(i) + 1);
            }
        }

        generation++;
        System.out.println("Generation: " + generation);

        //Degrade the pheremone trail if applicable
        System.out.println("Pheremones: " + pheremones);
        if(generation > 1){
            for (int i = 0; i < pheremones.size(); i++){
                pheremones.set(i, pheremones.get(i) - degrade);
                if (pheremones.get(i) < 0) pheremones.set(i, 0);
            }
        }
        System.out.println("Pheremones: " + pheremones);

        //printColony();
//        System.out.println("Adjust by " + adjust);
    }

    //Auxillary method to output the colony
    public static void printColony(){
        for (int i = 0; i < colony.size(); i++) {
            System.out.println("Ant " + (i + 1) + " " + colony.get(i).toString());
        }
    }
}

class Generation extends Thread {

    int k;
    public Generation(int i)
    {
        k = i;
    }
    public void run(){
        System.out.println("Thread: " + k);
        while (SCP.colony.get(k + SCP.adjust).seenAll == false && SCP.colony.get(k + SCP.adjust).seenAllNumbers(SCP.universe, SCP.sets) == false){
            //System.out.println("Ant " + (i + adjust));
            SCP.colony.get(k + SCP.adjust).addToPath(SCP.colony.get(k + SCP.adjust).getNextStep(SCP.sets, SCP.pheremones));
        }
    }
}