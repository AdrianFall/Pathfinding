
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;

/**
 * The AStarAlgorithm class is responsible for finding an optimal path between the given starting and finishing nodes,
 * with the usage of heuristics and path scoring values <br> The A* is an extension of Dijkstra's algorithm
 * whereby it achieves better performance on the number of operations and visited nodes overall leading to a faster path finding,
 * therefore achieving a lower CPU usage.
 * @author Adrian Fall
 *
 */
public class AStarAlgorithm {
	
	/**
	 * Declares an array list for openList, that is for the nodes which
	 * have been examined, as they were the first nodes on the openList and
	 * have been expanded as the surrounding nodes of their parents,
	 * and added to this list, so they will be potentially examined
	 * at later stages.
	 * 
	 */
	private ArrayList<Node> openList;
	
	/**
	 * Declares an array list for the closedList, that is for the nodes which 
	 * 
	 * have expanded their neighbours nodes.
	 */
	private ArrayList<Node> closedList;
	
	/**
	 * Declares a Node instance for the start 
	 */
	private static Node start;
	
	/**
	 * Declares a Node instance for the goal
	 */
	private static Node goal;
	
	/**
	 * Declares a boolean for tracking whether the user wants to allow diagonal path finding
	 */
	private static boolean allowDiagonalPathFinding = true;
	
	/**
	 * Declares an integer variable for the delay (in milliseconds) between the robotic movement.
	 */
	private static int msDelayBetweenRobotMovement;
	
	/**
	 * Declares a boolean for tracking whether the user wants to be shown with the visited nodes (when the algorithm is performs
	 * path-finding visually)
	 */
	private static boolean showVisitedNodes;
	
	/**
	 * Declares an integer variable for the delay (in milliseconds) between visiting the nodes.
	 */
	private static int msDelayBetweenVisitingNodes;
	
	/**
	 * Declares a String variable for the type of the heuristics to be used by the algorithm.
	 */
	private static String heuristicsType;
	
	/**
	 * Declares an integer variable for maintaining the number of operations that the path-finder performs.
	 */
	private static int numberOfOperations = 0;
	
	/**
	 * Declares an integer variable for maintaining the number of visited nodes that the path-finder performs.
	 */
	private static int numberOfVisitedNodes = 0;
	
	/**
	 * The constructor of this class, accepting the nodes and other piece of information that
	 * is needed in order to perform the A* algorithm path find.
	 * @param start - The node which was selected as the start node by the user in the GUI
	 * @param goal - The node which was selected as the goal/finish node by the user in the GUI.
	 * @param allowDiagonalPathFinding - The piece of information determining whether the user allows diagonals or not.
	 * @param msDelayBetweenRobotMovement - The piece of information that determines the delay between the animation of the robot movement.
	 * @param showVisitedNodes - The piece of information that determines whether the user wants to be shown with the visited nodes by the algorithm.
	 * @param msDelayBetweenVisitingNodes - The piece of information that determines the delay between visiting each adjacent node.
	 * @param heuristicsType - The piece of information that determines what type of heuristics will be used by the A* algorithm
	 */
	
	public AStarAlgorithm(Node start, Node goal, boolean allowDiagonalPathFinding, int msDelayBetweenRobotMovement, boolean showVisitedNodes, int msDelayBetweenVisitingNodes, String heuristicsType) {
		AStarAlgorithm.start = start;
		AStarAlgorithm.goal = goal;
		AStarAlgorithm.allowDiagonalPathFinding = allowDiagonalPathFinding;
		AStarAlgorithm.msDelayBetweenRobotMovement = msDelayBetweenRobotMovement;
		AStarAlgorithm.showVisitedNodes = showVisitedNodes;
		AStarAlgorithm.msDelayBetweenVisitingNodes = msDelayBetweenVisitingNodes;
		AStarAlgorithm.heuristicsType = heuristicsType;
	}

	/**
	 * The A* algorithm which performs the pathfinding in following maner: <br>
	 * 1) Initialise the closed and open lists, adding the start to the open list <br>
	 * 2) While the path has not been calculated and the open list is not empty then: <br>
	 * 3) &nbsp&nbsp&nbspSelect the first node from the open list, then remove it from the open list and add to the closed list <br>
	 * 4) &nbsp&nbsp&nbspObtain the surrounding nodes and check if any of them is the goal node, if yes set path calculated. <br>
	 * 5) &nbsp&nbsp&nbspIf the path has been calculated then trace back the path (from goal to start), and break. <br>
	 * 6) &nbsp&nbsp&nbspExpand each surrounding node, if and only if the surrounding node exists (i.e. is within the bounds of the grid)
	 * and is not the obstacle. <br>
	 * 7) &nbsp&nbsp&nbspSort the open list using the bubble sort algorithm, by the means of the path scoring values.
	 */
	public void calculatePath() {
		System.out.println("Hello from calculatePath()");
		
		boolean pathHasBeenCalculated = false;
		
		//Initialise the closedList and openList as array lists
		closedList = new ArrayList<Node>();
		openList = new ArrayList<Node>();
		
		//Add the start node to the open list
		openList.add(start);
		
		numberOfOperations = 0;
		numberOfVisitedNodes = 0;
		
		//While the path has not been yet calculated
		while (!pathHasBeenCalculated) {
			
			//If OPEN is empty, exit with failure.
			if (openList.isEmpty()) {
				if (!pathHasBeenCalculated) {
					PathfindingGUI.operationsLabel.setText("<html><font color=green> Operations: " + Integer.toString(numberOfOperations) + "</font></html>");
					PathfindingGUI.operationsLabel.paintImmediately(PathfindingGUI.operationsLabel.getVisibleRect());
					
					PathfindingGUI.visitedNodesLabel.setText("<html><font color=green> Visited nodes: " + Integer.toString(numberOfVisitedNodes) + "</font></html>");
					PathfindingGUI.visitedNodesLabel.paintImmediately(PathfindingGUI.visitedNodesLabel.getVisibleRect());
					
					PathfindingGUI.pathFoundLabel.setText("<html><font color=red> Path not found. </font></html>");
					PathfindingGUI.pathFoundLabel.paintImmediately(PathfindingGUI.pathFoundLabel.getVisibleRect());
					
					JOptionPane.showMessageDialog(null, "There is no possible path to be found.");
				}
				break;
			}
			while (!openList.isEmpty()) {
				
				//Select the first node on OPEN, remove it from OPEN, and put it on CLOSED. Call this node s.
				Node s = openList.get(0);
				openList.remove(0);
				closedList.add(s);
				
				//Create the objects of surrounding nodes
				Node east = s.getNodeEast();
				Node west = s.getNodeWest();
				Node north = s.getNodeNorth();
				Node south = s.getNodeSouth();
				Node northEast = s.getNodeNorthEast();
				Node northWest = s.getNodeNorthWest();
				Node southEast = s.getNodeSouthEast();
				Node southWest = s.getNodeSouthWest();
				
				numberOfOperations += 4;
				
				//If the east node exists and is the goal
				if (east != null && east.isTheGoal()) {
					pathHasBeenCalculated = true;
				}
				//Else if the west node exists and is the goal
				else if (west != null && west.isTheGoal()) {
					pathHasBeenCalculated = true;
				}
				//Else if the north node exists and is the goal
				else if (north != null && north.isTheGoal()) {
					pathHasBeenCalculated = true;
				}
				//Else if the south node exists and is the goal
				else if (south != null && south.isTheGoal()) {
					pathHasBeenCalculated = true;
				} 
				if (allowDiagonalPathFinding) {
					numberOfOperations += 4;
					
					if (northEast != null && northEast.isTheGoal()) {
						pathHasBeenCalculated = true;
					}
					else if (southEast != null && southEast.isTheGoal()) {
						pathHasBeenCalculated = true;
					}
					else if (southWest != null && southWest.isTheGoal()) {
						pathHasBeenCalculated = true;
					}
					else if (northWest != null && northWest.isTheGoal()) {
						pathHasBeenCalculated = true;
					}
				}
				
				//If the path has been calculated
				if (pathHasBeenCalculated) {
					traceThePath(s);
					break;
				}//End of if the path has been calculated
				
				
				//Expand the surrounding nodes of the currently looped node
				if (east != null && !east.isTheObstacle() && !closedList.contains(east)) {
					expandNode(east, s,"e");
				}
				
				if (south != null && !south.isTheObstacle() && !closedList.contains(south)) {
					expandNode(south, s,"s");
				}
				
				if (north != null && !north.isTheObstacle() && !closedList.contains(north)) {
					expandNode(north, s,"n");
				}
				
				if (west != null  && !west.isTheObstacle() && !closedList.contains(west)) {
					expandNode(west, s,"w");
				}
				
				
				
				if (allowDiagonalPathFinding) {
					if (northEast != null && !closedList.contains(northEast) && !northEast.isTheObstacle() && !closedList.contains(northEast)) {
						expandNode(northEast, s,"n-e");
					}
					
					if (southEast != null && !closedList.contains(southEast) && !southEast.isTheObstacle() && !closedList.contains(southEast)) {
						expandNode(southEast, s, "s-e");
					}
					
					if (northWest != null && !closedList.contains(northWest) && !northWest.isTheObstacle() && !closedList.contains(northWest)) {
						expandNode(northWest, s, "n-w");
					}
					
					if (southWest != null && !closedList.contains(southWest) && !southWest.isTheObstacle() && !closedList.contains(southWest)) {
						expandNode(southWest, s, "s-w");
					}
				}//End of if allow diagonal path finding
				bubbleSortAlgorithm();
			}//End of while openList is not empty
		}//End of while the path has not been calculated
	}//End of calculatePath method
	
	/**
	 * A method for sorting the open list by the means of the path scoring value, using the bubble sort algorithm.
	 */
	private void bubbleSortAlgorithm() {
		//Bubble sort the openList array list
		for (int k = 0; k<openList.size(); k++) {
			for (int j =0; j < openList.size() -1; j++) {
				if (openList.get(j).getPathScoring() >= openList.get(j+1).getPathScoring()) {
					Node tempNode = openList.get(j);
					openList.set(j, openList.get(j+1));
					openList.set(j+1, tempNode);
				}
			}
		}
		System.out.print("Sorted array list: ");
		for (int i = 0; i < openList.size(); i++) {
			System.out.print(" " + openList.get(i).getPathScoring());
		}
		System.out.println("");
	}//End of bubbleSortAlgorithm method
	
	/**
	 * A method for tracing the path where tracing backward means from goal to start node, and tracing forward from start to goal node. <br>
	 * The method operates by firstly tracing backwards from the 
	 * current node to its parent then continously tracing backwards from the parent
	 * to its parent until there is a parent that has no parent. <br>
	 * Once the path has been traced backwards, then it will be traced forward in easy manner since tracing backward has saved the sequence.
	 * Hence there is only a need to reverse the sequence. <br>
	 * At the end the method will display the distance that was needed to trace the distance.
	 * @param currentNode
	 */
	private void traceThePath(Node currentNode) {
		
		System.out.println("The path has been calculated.");
		PathfindingGUI.operationsLabel.setText("<html><font color=green> Operations: " + Integer.toString(numberOfOperations) + "</font></html>");
		PathfindingGUI.operationsLabel.paintImmediately(PathfindingGUI.operationsLabel.getVisibleRect());
		
		PathfindingGUI.visitedNodesLabel.setText("<html><font color=green> Visited nodes: " + Integer.toString(numberOfVisitedNodes) + "</font></html>");
		PathfindingGUI.visitedNodesLabel.paintImmediately(PathfindingGUI.visitedNodesLabel.getVisibleRect());
		
		PathfindingGUI.pathFoundLabel.setText("<html><font color=orange> Path found. </font></html>");
		PathfindingGUI.pathFoundLabel.paintImmediately(PathfindingGUI.pathFoundLabel.getVisibleRect());
		
		//Trace backwards
		Node node = currentNode.traceBackwards();
		ArrayList<Node> childNodes = new ArrayList<Node>();
		childNodes.add(node);
		
		int counter = 0;
		//While the node exists
		while (node != null) {
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//Trace backwards
			node = node.traceBackwards();
			if (node != null) {
				childNodes.add(node);
			}
			counter++;
			
			PathfindingGUI.pathFoundLabel.setText("<html><font color=orange> Path found over " + (counter + 1) + " nodes. </font></html>");
			PathfindingGUI.pathFoundLabel.paintImmediately(PathfindingGUI.pathFoundLabel.getVisibleRect());
			
			if ((counter % 6) == 0) {
				PathfindingGUI.tracingBackLabel.setText("<html><font color=orange> Tracing path backwards. </font></html>");
				PathfindingGUI.tracingBackLabel.paintImmediately(PathfindingGUI.tracingBackLabel.getVisibleRect());
			} else if ((counter % 6) == 2) {
				PathfindingGUI.tracingBackLabel.setText("<html><font color=orange> Tracing path backwards.. </font></html>");
				PathfindingGUI.tracingBackLabel.paintImmediately(PathfindingGUI.tracingBackLabel.getVisibleRect());
			} else if ((counter % 6) == 4){
				PathfindingGUI.tracingBackLabel.setText("<html><font color=orange> Tracing path backwards... </font></html>");
				PathfindingGUI.tracingBackLabel.paintImmediately(PathfindingGUI.tracingBackLabel.getVisibleRect());
			}
			
		}//End of while the node exists
		
		PathfindingGUI.pathFoundLabel.setText("<html><font color=green> Path found over " + (counter + 1) + " nodes. </font></html>");
		PathfindingGUI.pathFoundLabel.paintImmediately(PathfindingGUI.pathFoundLabel.getVisibleRect());
		
		PathfindingGUI.tracingBackLabel.setText("<html><font color=green> Tracing path backwards </font></html>");
		PathfindingGUI.tracingBackLabel.paintImmediately(PathfindingGUI.tracingBackLabel.getVisibleRect());
		
		System.out.println("About to jump into the for loop for childNodes");
		System.out.println("ChildNodes.size = " + childNodes.size());
		Collections.reverse(childNodes);
		for (int j = 0; j<childNodes.size(); j++) {
			System.out.println("Looping for child nodes: j = " + j);
			childNodes.get(j).drawRobotOnTraceForward(msDelayBetweenRobotMovement);
			
			if ((j % 6) == 0) {
				PathfindingGUI.tracingForwardLabel.setText("<html><font color=orange> Robot tracing forward. </font></html>");
				PathfindingGUI.tracingForwardLabel.paintImmediately(PathfindingGUI.tracingForwardLabel.getVisibleRect());
			} else if ((j % 6) == 2) {
				PathfindingGUI.tracingForwardLabel.setText("<html><font color=orange> Robot tracing forward.. </font></html>");
				PathfindingGUI.tracingForwardLabel.paintImmediately(PathfindingGUI.tracingForwardLabel.getVisibleRect());
			} else if ((j % 6) == 4){
				PathfindingGUI.tracingForwardLabel.setText("<html><font color=orange> Robot tracing forward... </font></html>");
				PathfindingGUI.tracingForwardLabel.paintImmediately(PathfindingGUI.tracingForwardLabel.getVisibleRect());
			}
		}
		PathfindingGUI.tracingForwardLabel.setText("<html><font color=green> Robot tracing forward </font></html>");
		PathfindingGUI.tracingForwardLabel.paintImmediately(PathfindingGUI.tracingForwardLabel.getVisibleRect());
		
		currentNode.drawRobotOnTraceForward(msDelayBetweenRobotMovement);
		goal.setPathForwardFinished(true);
	}//End of traceThePath method
	
	/**
	 * A method for expanding the adjacent node in the following manner: <br>
	 * 1) If the adjacent node is on the closed list, do nothing. <br>
	 * 2) Otherwise if the adjacent node is not on the open list, then: <br>
	 * &nbsp&nbsp&nbspAdd the adjacent node to openList, set its parent to the current node <br>
	 * &nbsp&nbsp&nbspcalculate its (adjacent's) values of distance to start node, heuristic value <br>
	 * &nbsp&nbsp&nbspand its path scoring (i.e. distance to start + heuristic). Additionally mark the node as visited. <br>
	 * 3) Otherwise if the adjacent node is on the open list then: <br>
	 * &nbsp&nbsp&nbspif the distance to start of current node is lower than the adjacent's distance to start then: <br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbspSet the adjacent's parent to the current node and re-calculate its values.
	 * @param adjacent - The adjacent node (i.e. the surrounding node of the current)
	 * @param current - The current node (which has been obtained from the first index of the open list)
	 * @param direction - The direction of the adjacent node to the current node.
	 */
	private void expandNode(Node adjacent, Node current, String direction) {
		
		//Increment the number of operations
		//numberOfOperations++;
		
		PathfindingGUI.operationsLabel.setText("<html><font color=orange> Operations: " + Integer.toString(numberOfOperations) + "</font></html>");
		PathfindingGUI.operationsLabel.paintImmediately(PathfindingGUI.operationsLabel.getVisibleRect());
		
		if (!openList.contains(adjacent)) {
			//Add the south node to the openList
			openList.add(adjacent);
			//Set the south's parent node to be the start node
			adjacent.setParentNode(current, direction);
			calculations(adjacent, direction);
			//Mark the south node as visited
			adjacent.setNodeVisited(true);
			showVisitedNode(adjacent);
			
			//Increment the number of visited nodes
			numberOfVisitedNodes++;
			
			PathfindingGUI.visitedNodesLabel.setText("<html><font color=orange> Visited nodes: " + Integer.toString(numberOfVisitedNodes) + "</font></html>");
			PathfindingGUI.visitedNodesLabel.paintImmediately(PathfindingGUI.visitedNodesLabel.getVisibleRect());
			
		} else { //else if the openList contains the adjacent node
			
			//calculations(current, direction);
			
			
			int currentDistanceScore = current.getDistanceFromStart();
			if ((currentDistanceScore) < (adjacent.getDistanceFromStart() - 14)) {
				System.out.println("Tested value was lower.");
				adjacent.setParentNode(current, direction);
				calculations(adjacent, direction);
			}
		}//End of else the openlist contains the adjacent node
	}//End of expandNode method
	
	/**
	 * A method for performing the calculations of the given node. <br>
	 * To calculate the distance from start value, it obtains the distance of its parent node and adds the distance to its parent based on the direction. <br>
	 * For example if the given node is in non-diagonal direction to its parent, then its distance will be calculated as follows: parent distance + 10. <br>
	 * On the other hand if the given node is in diagonal direction to its parent, then its distance will be calculated as follows: parent distance + 14 <br>
	 * Then the node calculates its heuristics value based on the selection of the user in the GUI. <br>
	 * Once both of the above values are calculated, the node will be delegates to calculate its path scoring, which simply adds both of above values together.
	 * @param n - The node to perform the calculations on.
	 * @param direction - The direction of the node from its parent.
	 */
	private void calculations(Node n, String direction) {
		if (direction.equals("e") || direction.equals("s") || direction.equals("w") || direction.equals("n")) {
			n.setDistanceFromStart((n.getParent().getDistanceFromStart())+10);
		} else {
			n.setDistanceFromStart((n.getParent().getDistanceFromStart())+14);
		}
		
		if (heuristicsType.equals("manhattan")) {
			System.out.println("Using Manhattan heuristics.");
			n.calculateManhattanHeuristics(goal, 10);
		} else if (heuristicsType.equals("chebyshev")) {
			System.out.println("Using Chebyshev Heuristics");
			n.calculateChebyshevHeuristics(goal, 10);
		}
		n.calculatePathScoring(n.getHeuristics(), n.getDistanceFromStart());
	}//End of calculations method
	
	/**
	 * A method for determining whether the user wants to be visually represented with the visiting of nodes.
	 * The method also allows for user defined delay between visiting each node.
	 * @param n - The node to perform the actions on.
	 */
	private void showVisitedNode(Node n) {
		if (showVisitedNodes) {
			n.setShowVisitedNode(true);
			//delay for user specified amount of milliseconds (otherwise by default 10ms) so a good visual presentation of the visited nodes is shown
			try {
				TimeUnit.MILLISECONDS.sleep(msDelayBetweenVisitingNodes);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			n.setShowVisitedNode(false);
		}
	}//End of showVisitedNode method
}
