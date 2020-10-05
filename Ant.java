import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

public class Ant {

    public ArrayList<Integer> seenNumbers = new ArrayList<Integer>();
    public ArrayList<Integer> Path = new ArrayList<Integer>();
    public static ArrayList<Integer> Pheromones = new ArrayList<Integer>();
    public boolean seenAll = false;

    //creates an ant object
    public Ant(){    }
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
    public int getNextStep(ArrayList<ArrayList<Integer>> sets, ArrayList<Integer> pheremones)
    {
        ArrayList<Integer> pathWeight = new ArrayList<Integer>();
        ArrayList<Integer> ranges = new ArrayList<Integer>();
        int range = 0;
        Random rand = new Random();
        int moveto;
        int next = -1;

        if (!seenAll) {
            int nextStep = -1;
            ArrayList<Integer> options = new ArrayList<Integer>();

            for (int i = 0; i < sets.size(); i++) {
                if (!hasInPath(i)) {
                    options.add(i);
                    pathWeight.add(pheremones.get(i));
                }
                //System.out.println("Options size: " + options.size());
            }

            try {
                if (options.size() != 0) {
                    ArrayList<Integer> pathDistance = new ArrayList<Integer>();
                    pathDistance = getLength(options, sets);

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
                    //System.out.println("PathWeight: " + pathWeight);
                    for (int i = 0; i < options.size(); i++){
                        pathWeight.set(i, pathWeight.get(i) + (pathDistance.get(i) * 10));
                    }
                    //System.out.println("PathWeight: " + pathWeight);

                    //Determine the probability ranges
                    for (Integer i:pathWeight) {
                        range += i;
                        ranges.add(range);
                    }

                    moveto = rand.nextInt(range);

                    for (int i = 0; i < ranges.size() && next == -1; i++){
                        if(moveto < ranges.get(i)){
                            next = i;
                        }
                    }

                    //System.out.println("Range: " + range);

                    nextStep = options.get(next);
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
        return "Path: " + this.Path.toString();
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
}