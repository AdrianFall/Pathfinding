Pathfinding
===========
The application is a path finding robot which finds the optimal (shortest) path between the given start and finish points, on a grid based Graphical User Interface. The robot has been provided with the two different algorithms: the A* and Dijkstra, which guarantee to obtain an optimal path. When the robot is provided with A* algorithm, there is an option of choosing between the Manhattan and Chebyshev heuristics. As additional information for the user, the application shows the following information when a path is being calculated: number of operations performed, number of visited nodes, information whether a path was found and additional information when the path is being traversed and forwarded.

Deployment instructions
=======================
The application has been developed and fully tested with Java Runtime Environment 7 and JDK (Java Development Kit) 1.7 version. Additionally the application has been fully tested with JDK compliance 1.6, however the application has turned out to be incompatible with the JDK 1.5 and earlier versions.

Running the application
=======================
The following describes how to run the application in three different approaches:
Using Windows console to run the source files (i.e. .java files):

•	Tell the system where to find the JDK, by setting the path e.g. "set path=C:\Program Files\Java\jdk1.7.0_45\bin"

•	Compile the .java files using the javac syntax, e.g. “javac ClassName.java” or to compile all .java files in the current directory use “javac *.java”

•	Run the PathfindingGUI.class using the java syntax, i.e. by typing “java PathfindingGUI” in the Windows Console.
