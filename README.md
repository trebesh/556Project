# 556Project
Repository for the 50% COMPX556 Project. Due 27th October 2020

There are 4 test files provided as part of this documentation, however if you wish to produce your own the format is:
  Each line consists of a set
  Every set can only contain integers
  The integers must be separated by a comma.
  The first line will be treated as the universe, i.e. the union of all the other sets

There are 4 java files associated with this project:
  Ant.java,
  SCP.java,
  SCPParallel.java,
  SCPColonyParallel.java
  
 Ant.java is a class file that is used by the other 3 to store information about the ants.
 
 SCP.java is the serial version of the program,it takes 3 command line arguments:
  The test file,
  The size of the ant colony,
  The generation size
 
A note is that the colony size must be divisble by the generation size, e.g colony:100, generations:10

An example command to run SCP.java follows:

javac *.java && java SCP <TEST_FILE>.txt <COLONY_SIZE> <GENERATION_SIZE>

SCPParallel.java is the first parallel version of the program that runs each generation in paralell,it takes 3 command line arguments:
  The test file,
  The size of the ant colony,
  The generation size
 
A note is that the colony size must be divisble by the generation size, e.g colony:100, generations:10

An example command to run SCPParallel.java follows:

javac *.java && java SCPParallel <TEST_FILE>.txt <COLONY_SIZE> <GENERATION_SIZE>

SCPColonyParallel.java is the second parallel version of the program, it runs multiple colonies in parallel as well as each generation,it takes 4 command line arguments:
  The test file,
  The size of the ant colony,
  The generation size,
  The number of colonies
 
A note is that the colony size must be divisble by the generation size, e.g colony:100, generations:10
An example command to run SCP.java follows:

javac *.java && java SCPColonyParallel <TEST_FILE>.txt <COLONY_SIZE> <GENERATION_SIZE> <NUMBER_COLONIES>
