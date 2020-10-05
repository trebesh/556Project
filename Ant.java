import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
import java.util.Random;

public class Ant {

    public ArrayList<Integer> seenNumbers = new ArrayList<Integer>();
    public ArrayList<Integer> Path = new ArrayList<Integer>();
    public boolean seenAll = false;
    //How much to degrade this ants trail by after each iteration
    public int degradeBy = 10;

    //creates an ant object
    public Ant()
    {
        
    }
    //adds the next step to the ants path
    public int addToPath(int nextStep)
    {
        if(nextStep != -1)
        {
            Path.add(nextStep);
            return nextStep;
        }
        return -1;
    }
    //returns the ants current path
    public ArrayList<Integer> getPath()
    {
        return Path;
    }
    //gets the next step for the ant
    public int getNextStep(ArrayList<ArrayList<Integer>> sets, ArrayList<Ant> colony, ArrayList<Integer> pheromones)//, ArrayList<Integer> pheromones)
    {
        if (!seenAll) {
            int nextStep = -1;
            int totalSteps = 0;
            ArrayList<Integer> options = new ArrayList<Integer>();

            for (int i = 0; i < sets.size(); i++) {
                if (!hasInPath(i)) options.add(i);
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
                        }
                    }
                    int optionNumber = 0;
                    for(int i: options)
                    {
                        if(pheromones.get(i) != 0)
                        {
                            pathDistance.set(optionNumber, (pathDistance.get(optionNumber) * pheromones.get(i)));
                        }
                    }
                    for(int k = 0; k<pathDistance.size(); k++)
                    {
                        totalSteps += pathDistance.get(k);
                    }

                    Random rand = new Random();
                    int index = rand.nextInt(totalSteps);
                    int sum = 0;
                    int i=0;
                    while(sum < index ) 
                    {
                        sum = sum + pathDistance.get(i);
                        if(sum <= index)
                        {
                            i++;
                        }
                    }
                    nextStep = i;


                    for(int l:sets.get(nextStep))
                    {
                        if(!(seenNumbers.contains(l)))
                        {
                            seenNumbers.add(l);
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
            if(options.size() != sets.size())
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
            else
            {
                pathDistance.add(1);
            }
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
