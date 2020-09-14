package com.company;

public class Ant {

    ArrayList<int> Path = new ArrayList<int>[];
    ArrayList<int> Pheromones = new ArrayList<int>[];
//creates an ant object
    public Ant()
    {

    }
//adds the next step to the ants path
    public static void AddToPath(int nextStep)
    {
        Path.add(nextStep);
    }
//returns the ants current path
    public static ArrayList<int> getPath()
    {
        return path;
    }
//gets the next step for the ant
    public static int getNextStep(Universe universe, ArrayList<int> pheromones)
    {
        ArrayList<int> options = new ArrayList<int>[];
        int nextStep = -1;
        for(int i=0; i<universe.length(); i++)
        {
            int found = 0;
            for(int j: Path)
            {
                if(i = j)
                {
                    found = 1;
                }
            }
            if (found = 0)
            {
                options.add(i);
            }
        }
        Random rand = new Random();
        int randStep = rand.nextInt(options.length());
        nextStep = options.get(randStep);
        return nextStep;
    }
}