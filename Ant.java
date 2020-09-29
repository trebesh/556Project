import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

public class Ant {

    public ArrayList<Integer> seenNumbers = new ArrayList<Integer>();
    public ArrayList<Integer> Path = new ArrayList<Integer>();
    public static ArrayList<Integer> Pheromones = new ArrayList<Integer>();
    public boolean seenAll = false;
    public float trailStrength = 0;
    //How much to degrade this ants trail by after each iteration
    public int degradeBy = 10;

    //creates an ant object
    public Ant(int gens)
    {
        trailStrength = gens - 1;
    }
    //adds the next step to the ants path
    public void addToPath(int nextStep)
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
    public int getNextStep(ArrayList<ArrayList<Integer>> sets)//, ArrayList<Integer> pheromones)
    {
        if (!seenAll) {
            int nextStep = -1;
            ArrayList<Integer> options = new ArrayList<Integer>();

            for (int i = 0; i < sets.size(); i++) {
                if (!hasInPath(i)) options.add(i);
                //System.out.println("Options size: " + options.size());
            }
            try {
                if (options.size() != 0) {
                    ArrayList<Integer> pathDistance = new ArrayList<Integer>();
                    pathDistance = getLength(options, sets);
                    int maxDistance = 0;
                    int maxDistancePos = 0;
                    for(int i = 0; i<pathDistance.size(); i++)
                    {
                        if(pathDistance.get(i) > maxDistance)
                        {
                            maxDistance = pathDistance.get(i);
                            maxDistancePos = i;
                        }
                    }
                    nextStep = options.get(maxDistancePos);
                    for(int i:sets.get(nextStep))
                    {
                        if(!(seenNumbers.contains(i)))
                        {
                            seenNumbers.add(i);
                        }
                    }
                } else seenAll = true;
            } catch (Exception e) {
                System.out.println();
                System.out.println("ERROR in getNextStep");
                System.out.println(e.toString());
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
        return "Path: " + this.Path.toString() + " Strength: " + trailStrength;
    }

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

//        for(int i:pathDistance)
//        {
//            System.out.print(i + " ");
//        }
//        System.out.println("");
        return pathDistance;
    }

    public void startPositions(ArrayList<ArrayList<Integer>> sets)
    {
        Random rand = new Random();
        int randStep = rand.nextInt(sets.size());
        Path.add(randStep);
    }

    //Method to degrade the trail of this ant by the proportion declared in degradeBy
    public void degradeTrail(){
        trailStrength = trailStrength - (trailStrength/degradeBy);
    }
}