/*
  Class to produce solutions to the Set Cover Problem by implementing parellel Ant Colony Optimisation, running multiple instances of the same program at the same time.
    ConnorFergusson_1299038_HannahTrebes_1306378
    Command Line:
    javac *.java && java SCP <TEST_FILE>.txt <COLONY_SIZE> <GENERATION_SIZE> <NUMBER_COLONIES>
*/

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

public class SCPColonyParallel{

    //variables that are the same accross all colnies
    public static int numColonies;
    public static CountDownLatch latch2;
    public static int colonySize = 0;
    public static int generationSize = 0;
    public static int numGenerations = 0;
    public static int degrade = 1;
    public static String[] inString;
    public static ArrayList<Integer> set = new ArrayList<Integer>();
    public static ArrayList<ArrayList<Integer>> sets = new ArrayList<ArrayList<Integer>>();
    public static ArrayList<Integer> universe = new ArrayList<Integer>();
    public static ArrayList<Colony> allColonies = new ArrayList<Colony>();
    public static ArrayList<Integer> colonyBi = new ArrayList<Integer>();
    public static ArrayList<Integer> colonyNumBest = new ArrayList<Integer>();
    public static ArrayList<Integer> colonyWorstSize = new ArrayList<Integer>();
    public static ArrayList<Integer> colonyNumWorst = new ArrayList<Integer>();
    public static ArrayList<Integer> colonyAverageLength = new ArrayList<Integer>();

    public static String filename;
    public static BufferedReader reader;

    public static void main(String[] args) {
        try{
            long startTime = System.nanoTime();
            filename = args[0];
            colonySize = Integer.parseInt(args[1]);
            generationSize = Integer.parseInt(args[2]);
            numColonies = Integer.parseInt(args[3]);
            if((colonySize % generationSize) != 0) throw new Exception("Generation size must be an integer factor of colony size.");
            numGenerations = (colonySize/generationSize);

            //Read in the file
            getProblem(filename);

            //Create the first generation of Ants
            createColony();
            try
            {
                //wait for all the program to complete
                latch2.await();
                //print out the path of each ant for each colony
                for(Colony c:allColonies)
                {
                    System.out.println("Final Paths for colony " + c.getK() + ": ");
                    SCPColonyParallel.printColony(c.colony);
                }

                //Output final results, each result is split up for each of the colonies
                System.out.println();
                //print the number of sets used for each ant
                System.out.println("Number of times each node visited: ");
                for(int i = 0; i< allColonies.size(); i++)
                {
                    System.out.println("Colony " + i + ": " + allColonies.get(i).visited);
                }
                System.out.println();
                //print the pheremone strength
                System.out.println("Final Pheremone strengths: ");
                for(int i = 0; i< allColonies.size(); i++)
                {
                    System.out.println("Colony " + i + ": " + allColonies.get(i).pheremones);
                }
                System.out.println();
                //print the amount the pheremones were degrading by
                System.out.println("Degrading by: " + degrade);
                System.out.println();
                //print the best path for each colony
                System.out.println("Best Path:");
                System.out.println("(First instance of shortest path - other equally short paths possible)");
                for(int i = 0; i< allColonies.size(); i++)
                {
                    System.out.println("Colony " + i + ": ");

                    System.out.println("   Ant " + (colonyBi.get(i) + 1) + " " + allColonies.get(i).bestPath.toString());
                    for (int j = 0; j < allColonies.get(i).bestPath.size(); j++) {
                        System.out.println("   " + sets.get(allColonies.get(i).bestPath.get(j)));
                    }
                }
                System.out.println();
                System.out.println();
                System.out.println("General Statistics:");
                System.out.println("   Length of Best Path: ");
                for(int i = 0; i< allColonies.size(); i++)
                {
                    System.out.println("Colony " + i + ": " + allColonies.get(i).bestPath.size());
                }
                System.out.println("   Number of times best path achieved: ");
                for(int i = 0; i< allColonies.size(); i++)
                {
                    System.out.println("Colony " + i + ": " + colonyNumBest.get(i));
                }
                System.out.println("   Length of worst path: ");
                for(int i = 0; i< allColonies.size(); i++)
                {
                    System.out.println("Colony " + i + ": " + colonyWorstSize.get(i));
                }
                System.out.println("   Number of times worst path seen: ");
                for(int i = 0; i< allColonies.size(); i++)
                {
                    System.out.println("Colony " + i + ": " + colonyNumWorst.get(i));
                }
                System.out.println("   Average path length: ");
                for(int i = 0; i< allColonies.size(); i++)
                {
                    System.out.println("Colony " + i + ": " + colonyAverageLength.get(i));
                }
                long endTime = System.nanoTime();
                long totalTime = (endTime -startTime) / 1000000000;
                System.out.println("Took "+(totalTime) + " seconds");
            }
            catch(Exception e)
            {
                System.out.println("error in latch.await");
            }

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

            //Create the pheremones

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
            //create the latch that will countdown when each colony runs every generation
            latch2 = new CountDownLatch(numColonies);
            //for every colony specified to be run
            for(int i=0; i< numColonies; i++) {
                //create a new colony and run it through all generations
                Colony temp = new Colony(i);
                allColonies.add(temp);
                temp.start();
            }

        }catch(Exception e){
            System.out.println(e.toString());
        }
    }

    // Runs the current generation through, takes an integer "k" for the current colony that is being run
    public static void runGeneration(int k){
        allColonies.get(k).adjust = generationSize*allColonies.get(k).generation;
        System.out.println(allColonies.get(k).adjust);
        //Send out the ants to their start points
        for (int i = generationSize -1; i>0; i--) {
            allColonies.get(k).getColony().get(i + allColonies.get(k).adjust).startPositions(sets);
        }
        SCPColonyParallel.allColonies.get(k).latch = new CountDownLatch(generationSize);
        //move the ants out until they have found a valid solution or seen all nodes
        for (int i = 0; i<generationSize;i++) {
            ColonyGeneration temp = new ColonyGeneration(i, k);
            temp.start();
        }
        try {
            //wait for the latch of that colony to reach zero
            SCPColonyParallel.allColonies.get(k).latch.await();
        }
        catch(Exception e)
        {
            System.out.println("error in latch.await");
        }

        //Create the pheremone trail
        for (int i = 0; i<generationSize;i++) {
            Ant a = allColonies.get(k).getColony().get(i + allColonies.get(k).adjust);
            for (Integer l: a.Path) {
                allColonies.get(k).getPheremones().set(l, allColonies.get(k).getPheremones().get(l) + 1);
                allColonies.get(k).getVisited().set(l, allColonies.get(k).getVisited().get(l) + 1);
            }
        }

        allColonies.get(k).generation +=1;
        //Degrade the pheremone trail if applicable

        if(allColonies.get(k).generation > 1){
            for (int i = 0; i < allColonies.get(k).getPheremones().size(); i++){
                allColonies.get(k).getPheremones().set(i, allColonies.get(k).getPheremones().get(i) - degrade);
                if (allColonies.get(k).getPheremones().get(i) < 0) allColonies.get(k).getPheremones().set(i, 0);
            }
        }

    }

    //Auxillary method to output the colony
    public static void printColony(ArrayList<Ant> colony){
        for (int i = 0; i < colony.size(); i++) {
            System.out.println("Ant " + (i + 1) + " " + colony.get(i).toString());
        }
    }
}

//this is a class that is used to generate a new generation for a specific colony, extends the thread method so that it can be used as a thread
class ColonyGeneration extends Thread {

    int k;
    int colony;
    //create a new generation, taking integers for the current generation number and what colony it is for
    public ColonyGeneration(int i, int colonyNum)
    {
        k = i;
        colony = colonyNum;
    }
    //the method that will run a generation in a thread for that colony
    public void run(){
        System.out.println(" Generation Thread: " + k + " for colony: " + colony);
        while (SCPColonyParallel.allColonies.get(colony).getColony().get(k + SCPColonyParallel.allColonies.get(colony).adjust).seenAll == false
                && SCPColonyParallel.allColonies.get(colony).getColony().get(k + SCPColonyParallel.allColonies.get(colony).adjust).seenAllNumbers(SCPColonyParallel.universe, SCPColonyParallel.sets) == false){

            SCPColonyParallel.allColonies.get(colony).getColony().get(k + SCPColonyParallel.allColonies.get(colony).adjust).addToPath(SCPColonyParallel.allColonies.get(colony).getColony().get(k + SCPColonyParallel.allColonies.get(colony).adjust).getNextStep(SCPColonyParallel.sets, SCPColonyParallel.allColonies.get(colony).getPheremones()));
        }
        SCPColonyParallel.allColonies.get(colony).latch.countDown();
    }
}

//a class for creating a new colony, extends thread so it can be run as a thread
class Colony extends Thread{
    //an int varible for what number colony this is
    int k;
    //variables that are specific to each colony
    public ArrayList<Ant> colony = new ArrayList<Ant>();
    public ArrayList<Integer> bestPath;
    public ArrayList<Integer> pheremones = new ArrayList<Integer>();
    public ArrayList<Integer> visited = new ArrayList<Integer>();
    public CountDownLatch latch;
    public int adjust;
    public int generation = 0;

    //create a new colony, takes an integer to say what number this colony is
    public Colony(int i) {
        k=i;
        for(int j = 0; j<SCPColonyParallel.sets.size(); j++)
        {
            pheremones.add(0);
            visited.add(0);
        } 
    }
    //retuns the number this colony is
    public int getK()
    {
        return k;
    }
    //return the visited variable
    public ArrayList<Integer> getVisited() {
        return visited;
    }

    //returns the the array that contains the ants in the colony
    public ArrayList<Ant> getColony() {
        return colony;
    }

    //returns the array containing the pheremones for this colony
    public ArrayList<Integer> getPheremones() {
        return pheremones;
    }

    //the method that will execute when a new colony thread is told to start
    public void run() {
        //adds a new ant to the colony until the  colony size has been reached
        while (colony.size() < SCPColonyParallel.colonySize) {
            colony.add(new Ant());
        }

        //Run generations till all ants have visited all nodes
        for (int i = 0; i < SCPColonyParallel.numGenerations; i++) SCPColonyParallel.runGeneration(k);

        //Output results in the form of the colony
        System.out.println();

        //Calculate final Results
        int bi = 0;
        int numBest = 0;
        int worstsize = 0;
        int numWorst = 0;
        int averageLength = 0;
        bestPath = colony.get(0).Path;
        for (int i = 0; i < SCPColonyParallel.colonySize; i++) {
            if (colony.get(i).Path.size() < bestPath.size()) {
                bestPath = colony.get(i).Path;
                bi = i;
            }
            if (colony.get(i).Path.size() <= bestPath.size()) {
                numBest++;
            }
            if (colony.get(i).Path.size() >= worstsize) {
                worstsize = colony.get(i).Path.size();
                numWorst++;
            }
            averageLength += colony.get(i).Path.size();
        }
        averageLength = averageLength / colony.size();
        SCPColonyParallel.colonyBi.add(bi);
        SCPColonyParallel.colonyNumBest.add(numBest);
        SCPColonyParallel.colonyWorstSize.add(worstsize);
        SCPColonyParallel.colonyNumWorst.add(numWorst);
        SCPColonyParallel.colonyAverageLength.add(averageLength);


        SCPColonyParallel.latch2.countDown();
    }
}