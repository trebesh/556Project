import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

//A class that store the information of a single ant in a colony, has variables that store the numbers of the universe its seen, the path its taken and whether its seen all numbers yet
//It has methods to add the next step to its path, get its next step, check if a number in a possible set is already in its path, check how many new numbers are in each possible set, print a string value of the ant
public class Ant {

    //variables
    public ArrayList<Integer> seenNumbers = new ArrayList<Integer>();
    public ArrayList<Integer> Path = new ArrayList<Integer>();
    public boolean seenAll = false;

    //creates an ant object
    public Ant(){    }
    //adds the next step to the ants path
    public synchronized void addToPath(int nextStep)
    {
        if(nextStep != -1)
        {
            Path.add(nextStep);
        }
    }
    //returns the ants current path
    public ArrayList<Integer> getPath()
    {
        return Path;
    }
    //gets the next step for the ant
    public synchronized int getNextStep(ArrayList<ArrayList<Integer>> sets, ArrayList<Integer> pheremones)
    {
        ArrayList<Integer> pathWeight = new ArrayList<Integer>();
        ArrayList<Integer> ranges = new ArrayList<Integer>();
        int range = 0;
        Random rand = new Random();
        int moveto;
        int next = -1;

        //if the ant hasnt seen all the numbers in the universe
        if (!seenAll) {
            int nextStep = -1;
            ArrayList<Integer> options = new ArrayList<Integer>();
            //loop through the list of avaliable sets
            for (int i = 0; i < sets.size(); i++) {
                //if it hasnt already gone to one of the sets, add it as a possible option, and get the pheromones at that location
                if (!hasInPath(i)) {
                    options.add(i);
                    pathWeight.add(pheremones.get(i));
                }
                //System.out.println("Options size: " + options.size());
            }

            try {
                //if there are options availiable
                if (options.size() != 0) {

                    ArrayList<Integer> pathDistance = new ArrayList<Integer>();
                    //get how many new numbers are at each posible option
                    pathDistance = getLength(options, sets);
                    //if there are no new options, remove it from the list of possible options
                    for (int i = 0; i<pathDistance.size(); i++)
                    {
                        if(pathDistance.get(i) == 0)
                        {
                            pathDistance.remove(i);
                            options.remove(i);
                            pathWeight.remove(i)
;                        }
                    }

                    //Weight the paths
                    for (int i = 0; i < options.size(); i++){
                        pathWeight.set(i, pathWeight.get(i) + (pathDistance.get(i) * 10));
                    }

                    //Determine the probability ranges
                    for (Integer i:pathWeight) {
                        range += i;
                        ranges.add(range);
                    }

                    if(range < 0) {
                        range = 0;
                        for (int i = 0; i < pheremones.size(); i++){
                            pheremones.set(i, pheremones.get(i)/2);
                        }

                        for (int i = 0; i < sets.size(); i++) {
                            if (!hasInPath(i)) {
                                options.add(i);
                                pathWeight.add(pheremones.get(i));
                            }
                        }
                        if (options.size() != 0) {
                            pathDistance = new ArrayList<Integer>();
                            pathDistance = getLength(options, sets);

                            for (int i = 0; i < pathDistance.size(); i++) {
                                if (pathDistance.get(i) == 0) {
                                    pathDistance.remove(i);
                                    options.remove(i);
                                    pathWeight.remove(i)
                                    ;
                                }
                            }

                            //Weight the paths

                            for (int i = 0; i < options.size(); i++) {
                                pathWeight.set(i, pathWeight.get(i) + (pathDistance.get(i) * 10));
                            }


                            //Determine the probability ranges
                            for (Integer i : pathWeight) {
                                range += i;
                                ranges.add(range);
                            }
                        }
                    }

                    
                    moveto = rand.nextInt(range);

                    //keep adding the ranges of each possible option until we reach the range that the randomly generated number is in
                    for (int i = 0; i < ranges.size() && next == -1; i++){
                        if(moveto < ranges.get(i)){
                            next = i;
                        }
                    }
                    //make that set the next step for the ant and update the values it has seen
                    nextStep = options.get(next);
                    for(int i:sets.get(nextStep))
                    {
                        if(!(seenNumbers.contains(i)))
                        {
                            seenNumbers.add(i);
                        }
                    }
                //if there are no options, the ant has seen everything, and his path is done
                } else seenAll = true;
            } catch (Exception e) {
                System.out.println();
                System.out.println("ERROR in getNextStep");
                System.out.println(e.toString());
                System.out.println(e.getMessage());

            }
            return nextStep;
        }
        else return -1;
    }

    //Checks if a node/set is already in the ants path
    public boolean hasInPath(int target){
        for (int i: Path) {
            if (i == target) return true;
        }
        return  false;
    }

    //Print a string representation of the Ant
    public String toString(){
        return "Path: " + this.Path.toString();
    }

    //check if the ant has seen all the numbers in the universe
    public boolean seenAllNumbers(ArrayList<Integer> Universe, ArrayList<ArrayList<Integer>> sets)
    {
        ArrayList<Integer> seen = new ArrayList<Integer>();
        for(int i: Path)
        {
            for(int j:sets.get(i))
            {
                if(!(seen.contains(j)))
                {
                    seen.add(j);
                }
            }
        }
        Collections.sort(seen);
        if(seen.equals(Universe))
        {
            return true;
        }
        return false;
    }
    //get the amount of numbers in each path that the ant hasnt seen yet
    public ArrayList<Integer> getLength(ArrayList<Integer> options, ArrayList<ArrayList<Integer>> sets)
    {
        ArrayList<Integer> pathDistance = new ArrayList<Integer>();
        for(int i = 0; i<options.size(); i++)
        {
            int score = 0;
            for(int j:sets.get(options.get(i)))
            {
                if(!(seenNumbers.contains(j)))
                {
                    score+=1;
                }
            }
            pathDistance.add(score);
        }
        return pathDistance;
    }

    //randomly assign the ant a start position, only called when there is no pheromones.
    public void startPositions(ArrayList<ArrayList<Integer>> sets)
    {
        Random rand = new Random();
        int randStep = rand.nextInt(sets.size());
        Path.add(randStep);
    }
}