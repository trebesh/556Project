import java.util.ArrayList;
import java.util.Random;

public class Ant {

    public ArrayList<Integer> Path = new ArrayList<Integer>();
    public static ArrayList<Integer> Pheromones = new ArrayList<Integer>();
    public boolean seenAll = false;

//creates an ant object
    public Ant()
    { }
//adds the next step to the ants path
    public void addToPath(int nextStep)
    {
        if(nextStep != -1) Path.add(nextStep);
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
                    Random rand = new Random();
                    int randStep = rand.nextInt(options.size());
                    //System.out.println("RandStep: " + randStep);
                    nextStep = options.get(randStep);
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
        return this.Path.toString();
    }
}