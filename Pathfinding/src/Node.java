
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * The Node class represents a single instance of a node with references to its 
 * surrounding nodes and its parent node, as well as the position of the instance of this node
 * (whereby its x and y coordinates, as well as column and row). It is also responsible
 * for calculation of the heuristics (estimated distance to goal) and storing the values of:
 * distance to start, heuristics and path scoring (addition of distance to start and heuristics).
 * The node class as well represents the single instance of the node graphically.
 * @author Adrian Fall
 *
 */
public class Node {

	/**
	 * Declares a boolean for tracking whether this instance of the Node is the start.
	 */
	private boolean start;
	
	/**
	 * Declares a boolean for tracking whether this instance of the Node is the goal.
	 */
	private boolean goal;
	
	/**
	 * Declares a boolean for tracking whether this instance of the Node is the obstacle.
	 */
	private boolean obstacle;
	
	/**
	 * Declares a boolean for determining whether the path is being traced back.
	 */
	private boolean onTracebackPath;
	
	/**
	 * Declares the variable for the horizontal position of this instance of node within the grid.
	 */
	private int xPosition;
	
	/**
	 * Declares the variable for the vertical position of this instance of node within the grid.
	 */
	private int yPosition;
	
	//Declare the variables for the location of the nodes (i.e. its column and row numbers)
	/**
	 * Declares the variable for the row location of this instance of node within the grid.
	 */
	private int nodeRow;
	
	/**
	 * Declares the variable for the column location of this node within the grid.
	 */
	private int nodeColumn;
	
	/**
	 * Declares an instance of a parent Node object
	 */
	private Node parent;
	
	/**
	 * Declares an instance of the adjacent Node object.
	 */
	private Node nodeEast, nodeWest, nodeNorth, nodeSouth, nodeNorthEast, nodeNorthWest, nodeSouthEast, nodeSouthWest;
	
	/**
	 * Declares an integer variable for the heuristics value (i.e. the estimated distance to the goal node)
	 */
	private int heuristicValue;
	
	/**
	 * Declares an integer variable for the precise distance to the start node
	 */
	private int distanceToStart;
	
	/**
	 * Declares an integer variable for the path scoring value (i.e. the total cost of movement towards the goal node)
	 */
	private int pathScoring;

	/**
	 * Declares a boolean for tracking whether this instance of the Node has been visited
	 */
	private boolean nodeVisited;
	
	/**
	 * Declares a boolean for tracking whether the user wants to be shown with the visited node.
	 */
	private boolean showVisitedNode;
	
	/**
	 * Declares a boolean for tracking whether this instance of node is being traced forward
	 */
	private boolean onTraceforwardPath;
	
	/**
	 * Declares a String for maintaining the direction of this instance of node from its parent
	 */
	private String nodeDirectionFromParent;
	
	/**
	 * Declares an instance of the BufferedImage object for the robot image facing right.
	 */
	BufferedImage robotFacingRight = null;
	/**
	 * Declares an instance of the BufferedImage object for the robot image facing left.
	 */
	BufferedImage robotFacingLeft = null;
	/**
	 * Declares an instance of the BufferedImage object for the robot image facing up.
	 */
	BufferedImage robotFacingUp = null;
	/**
	 * Declares an instance of the BufferedImage object for the robot image facing down.
	 */
	BufferedImage robotFacingDown = null;
	
	/**
	 * Declares an instance of the BufferedImage object for the lava image.
	 */
	BufferedImage lavaImage = null;
	
	/**
	 * Declares an instance of the BufferedImage object for the finish image.
	 */
	BufferedImage finishImage = null;
	
	/**
	 * Declares a boolean for determining when to draw the images when the path is being forwarded to the goal node.
	 */
	private boolean drawTheImageWhenForwardingPath = false;
	
    /**
     * Declares a boolean for determining when the path forwarding has been finished.
     */
	private boolean pathForwardFinished;
    
    /**
     * Declares a boolean for determining whether to stop showing the image (of robot) at the start node, by default true
     */
	private boolean showImageAtStart = true;
	
    
    /**
     * Declares a boolean for determining whether the goal node was reached.
     */
	private boolean goalReached;
	
	
	/**
	 * The constructor which initialises this class. It obtains the column and row
	 * position of this node and stores it globally in this class. It also loads the image of the 
	 * robot that will potentially be used by this instance of the node for graphical repesentation.
	 * @param column - The position in the column of the grid of nodes
	 * @param row - The position in the row of the grid of nodes
	 * @throws IOException 
	 */
	public Node(int column, int row) {
		start = false;
		goal = false;
		obstacle = false;
		
		//Set the row and column of the node
		nodeRow = row;
		nodeColumn = column;
		
		//robotFacingLeft =new ImageIcon(getClass().getResource("/images/quit.png"));
		try {
			robotFacingLeft = ImageIO.read(Node.class.getResourceAsStream("robotFacingLeft.png"));
			robotFacingDown = ImageIO.read(Node.class.getResourceAsStream("robotFacingDown.png"));
			robotFacingUp = ImageIO.read(Node.class.getResourceAsStream("robotFacingUp.png"));
			robotFacingRight = ImageIO.read(Node.class.getResourceAsStream("robotFacingRight.png"));
			lavaImage = ImageIO.read(Node.class.getResourceAsStream("Lava.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	/**
	 * Setter for the parent node
	 * @param node - The node to be set as the parent of this instance of the node
	 * @param nodeDirection - The direction of this instance of the node from its parent
	 */
	public void setParentNode(Node node, String nodeDirection) {
		parent = node;
		nodeDirectionFromParent = nodeDirection;
	}
	
	/**
	 * Getter for the parent of this instance of node.
	 * @return - Node of the parent
	 */
	public Node getParent() {
		return parent;
	}
	
	/**
	 * Getter for the the direction of this node from its parent 
	 * @return - A string with the representation of the direction from its parent
	 */
	//Getter for nodeDirectionFromParent
	public String getNodeDirectionFromParent() {
		return nodeDirectionFromParent;
	}
	
	/**
	 * Setter for the horizontal (width) position of this instance of the node
	 * @param xPos - The horizontal (width) position of this node
	 * @return - integer with the horizontal (width) position of this node
	 */
	public Node setXPos(int xPos) {
		xPosition = xPos;
		return this;
	}
	
	/**
	 * Setter for the vertical (height) position of this instance of the node
	 * @param yPos - The vertical (height) position of this node
	 * @return - integer with the vertical (height) position of this node
	 */
	public Node setYPos(int yPos) {
		yPosition = yPos;
		return this;
	}
	
	/**
	 * Getter for the node row in the grid of nodes.
	 * @return - integer with the specific node row in the grid of nodes.
	 */
	public int getNodeRow() {
		return nodeRow;
	}
	
	/**
	 * Getter for the node column in the grid of nodes
	 * @return - integer with the specific node column in the grid of nodes 
	 */
	public int getNodeColumn() {
		return nodeColumn;
	}
	
	/**
	 * Setter for the goal node, hence setting this instance of the node to be the goal node.
	 */
	public void setTheGoal(boolean isGoal) {
		if (isGoal) {
			goal = true;
		} else {
			goal = false;
		}
	}
	
	/**
	 * Getter for the goal node, hence gets whether this instance of the node is the goal node.
	 * @return - boolean representing whether this node is the goal.
	 */
	public boolean isTheGoal() {
		return goal;
	}
	
	/**
	 * Setter for the obstacle node, hence setting this instance of the node to be the obstacle node.
	 */
	public void setTheObstacle() {
		obstacle = true;
	}
	
	/**
	 * Getter for the obstacle node, hence gets whether this instance of the node is the obstacle node.
	 * @return - boolean representing whether this node is the obstacle.
	 */
	public boolean isTheObstacle() {
		return obstacle;
	}
	
	/**
	 * Setter for the start node, hence setting this instance of the node to be the start node.
	 */
	public void setTheStart() {
		start = true;
	}
	
	/**
	 * Getter for the start node, hence gets whether this instance of the node is the start node.
	 * @return - boolean representing whether this node is the start.
	 */
	public boolean isTheStart() {
		return start;
	}
	
	/**
	 * A method for rendering the graphics to be drawn onto the graphics 2d object, 
	 * hence it renders the instance of this node, so it is drawn with
	 * graphics at its specific coordinates position.
	 * @param g2d - The graphics 2d object to be drawn onto.
	 */
	//Method for rendering the graphics to draw the squares on the screen (representing nodes)
	public void render(Graphics2D g2d) {
		//Set the color and draw the rectangle
		g2d.setColor(Color.BLACK);
		g2d.drawRect(xPosition, yPosition, 40, 40);
		
		//Set the colors for the nodes
		if (onTracebackPath && !start) {
			g2d.setColor(Color.GREEN);
		} else if (nodeVisited && showVisitedNode && !start) {
			g2d.setColor(Color.CYAN);
		} else if (start) {
			g2d.setColor(Color.BLUE);
		} else {
			g2d.setColor(Color.lightGray);
		}
		//Fill the rectangle leaving 1 pixel for the borders
		g2d.fillRect(xPosition + 1, yPosition + 1, 38, 38);
		
		if (obstacle) {
			
			//g2d.setColor(Color.RED);
			g2d.drawImage(lavaImage, xPosition, yPosition, 40, 40, null);
		} else if (goal) {
			try {
				finishImage = ImageIO.read(Node.class.getResourceAsStream("finish.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			g2d.setColor(Color.ORANGE);
			g2d.fillRect(xPosition + 1, yPosition + 1, 38, 38);
			if (!goalReached) {
				g2d.drawImage(finishImage, xPosition, yPosition, 40, 40, null);
			} else {
				g2d.drawImage(finishImage, xPosition, yPosition, 40, 10, null);
			}
		}
		/**
		if (pathScoring != 0) {
			g2d.setColor(Color.BLACK);
			g2d.drawString(Integer.toString(pathScoring), xPosition+14, yPosition+14);
			g2d.drawString(Integer.toString(distanceToStart), xPosition+14, yPosition+25);
		}
		*/
		
		//If the node is a start node and the program didn't yet stop showing the image at start
		if (start && showImageAtStart) {
			try {
				robotFacingDown = ImageIO.read(Node.class.getResourceAsStream("robotFacingDown.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			g2d.drawImage(robotFacingDown, xPosition, yPosition, 45, 45, null);
		}
		
		//If the path is being forwarded
		if (onTraceforwardPath) {
			//Check the direction from the parent, and draw the correct robot image to animate the robot moving to the goal node
			if (nodeDirectionFromParent == "e" && drawTheImageWhenForwardingPath) {
				g2d.drawImage(robotFacingRight, xPosition, yPosition, 50, 50, null);
			} else if (nodeDirectionFromParent == "w" && drawTheImageWhenForwardingPath) {
				g2d.drawImage(robotFacingLeft, xPosition, yPosition, 50, 50, null);
			} else if (nodeDirectionFromParent == "n" && drawTheImageWhenForwardingPath) {
				g2d.drawImage(robotFacingUp, xPosition, yPosition, 50, 50, null);
			} else if (nodeDirectionFromParent == "s" && drawTheImageWhenForwardingPath) {
				g2d.drawImage(robotFacingDown, xPosition, yPosition, 50, 50, null);
			} else if (nodeDirectionFromParent == "s-e" && drawTheImageWhenForwardingPath) {
				g2d.drawImage(robotFacingDown, xPosition, yPosition, 50, 50, null);
			} else if (nodeDirectionFromParent == "s-w" && drawTheImageWhenForwardingPath) {
				g2d.drawImage(robotFacingDown, xPosition, yPosition, 50, 50, null);
			} else if (nodeDirectionFromParent == "n-e" && drawTheImageWhenForwardingPath) {
				g2d.drawImage(robotFacingUp, xPosition, yPosition, 50, 50, null);
			} else if (nodeDirectionFromParent == "n-w" && drawTheImageWhenForwardingPath) {
				g2d.drawImage(robotFacingUp, xPosition, yPosition, 50, 50, null);
			} 
		}
		
		if (pathForwardFinished && goalReached) {
			g2d.drawImage(robotFacingDown, xPosition, yPosition, 50, 50, null);
		}
	}
	/**
	 * A method for clearing this instance of node to be blank again.
	 * Hence setting that is is not a start, goal or obstacle anymore.
	 * This also ensures that it will be stopped being drawn on the graphics.
	 */
	public void clearNode() {
		start = false;
		goal = false;
		obstacle = false;
	}
	
	/**
	 * A method for tracing backwards (the idea of getting back from goal to start node)
	 * from the current instance of the node to its parent node.
	 * @return - The parent of the instance of this node
	 */
	//A method for tracing backwards from the goal node to the start node
	public Node traceBackwards() {
		
		onTracebackPath = true;
		return parent;
	}
	
	
	/**
	 * A method for displaying the robot image when tracing the path forward 
	 * (the idea of getting from start to goal node).
	 * 
	 * @param delay - The delay before the robot disappears.
	 */
	public void drawRobotOnTraceForward(int delay) {
		int delayThread = delay;
		onTraceforwardPath = true;
		drawTheImageWhenForwardingPath = true;
		try {
			Thread.sleep(delayThread);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		showImageAtStart = false;
		drawTheImageWhenForwardingPath = false;
	}
	
	/**
	 * Setter for the boolean whether the path forwarding has been finished for this instance of the node.
	 * @param pathForwardFinished - boolean whether the path forwarding has been finished.
	 */
	public void setPathForwardFinished(boolean pathForwardFinished) {
		this.pathForwardFinished = pathForwardFinished;
		if (pathForwardFinished) {
			goalReached = true;
		}
	}
	
	/**
	 * Setter for the surrounding nodes of this instance of the node.
	 * @param east - Node to the east from this instance of the node.
	 * @param west - Node to the west from this instance of the node.
	 * @param north - Node to the north from this instance of the node.
	 * @param south - Node to the south from this instance of the node.
	 * @param northEast - Node to the north-east from this instance of the node.
	 * @param northWest - Node to the north-west from this instance of the node.
	 * @param southEast - Node to the south-east from this instance of the node.
	 * @param southWest - Node to the south-west from this instance of the node.
	 */
	public void setNearbyNodes(Node east, Node west, Node north, Node south, Node northEast, Node northWest, Node southEast, Node southWest) {
		nodeEast = east;
		nodeWest = west;
		nodeNorth = north;
		nodeSouth = south;
		nodeNorthEast = northEast;
		nodeNorthWest = northWest;
		nodeSouthEast = southEast;
		nodeSouthWest = southWest;
	}

	/**
	 * Getter for the east node to the instance of the node.
	 * @return - Node to the east of this instance of the node.
	 */
	public Node getNodeEast() {
		return nodeEast;
	}
	
	/**
	 * Getter for the west node to the instance of the node
	 * @return - Node to the west of this instance of the node.
	 */
	public Node getNodeWest() {
		return nodeWest;
	}
	
	/**
	 * Getter for the north node to the instance of the node.
	 * @return - Node to the north of this instance of the node.
	 */
	public Node getNodeNorth() {
		return nodeNorth;
	}
	
	/**
	 * Getter for the south node to the instance of the node
	 * @return - Node to the south of this instance of the node
	 */
	public Node getNodeSouth() {
		return nodeSouth;
	}
	
	/**
	 * Getter for the north-east node to the instance of the node.
	 * @return - Node to the north-east of this instance of the node.
	 */
	public Node getNodeNorthEast() {
		return nodeNorthEast;
	}

	/**
	 * Getter for the north-west node to the instance of the node.
	 * @return - Node to the north-west of this instance of the node.
	 */
	public Node getNodeNorthWest() {
		return nodeNorthWest;
	}
	
	/**
	 * Getter for the south-east node to the instance of the instance of this node.
	 * @return - Node to the south-east of this instance of the node.
	 */
	public Node getNodeSouthEast() {
		return nodeSouthEast;
	}
	
	/**
	 * Getter for the south-west node to the instance of the instance of this node.
	 * @return - Node to the south-west of this instance of the node.
	 */
	public Node getNodeSouthWest() {
		return nodeSouthWest;
	}
	
	/**
	 * A method for performing the Manhattan Heuristics calculation, providing the estimate distance
	 * from this instance of the goal to the goal node.<br>The calculation is performed by obtaining
	 * the difference of the rows of this instance of node and the goal's, and adding it with the 
	 * difference of the columns of this instance of node and the goal's. At the end the obtained 
	 * addition of the differences is multiplied by 10 for the optimal results.
	 * @param goal - Node of the goal
	 * @param scale - integer of the scale factor for the heuristic value (so the estimated distance is multiplied by it)
	 */
	public void calculateManhattanHeuristics(Node goal, int scale) {
		
		//Calculate the absolute value of the rows and columns to the goal node, and add them up
		heuristicValue = Math.abs(nodeRow - goal.nodeRow) + Math.abs(nodeColumn - goal.nodeColumn);
		heuristicValue *= scale;
	}
	
	public void calculateChebyshevHeuristics(Node goal, int scale) {
		int x = Math.abs(nodeRow - goal.nodeRow);
		int y = Math.abs(nodeColumn - goal.nodeColumn);
		//Calculate the heuristics value
		//movement_cost*math.max(math.abs(node.x-end.x),math.abs(node.y-end.y))
		heuristicValue = scale * Math.max(x, y);
		
		//heuristicValue = scale * (x + y) + (scale*2 - 2 * scale) * Math.min(x, y);
	}
	
	/**
	 * Getter for the heuristics value (estimated distance from this node to the goal node).
	 * @return - integer with the heuristics value.
	 */
	public int getHeuristics() {
		return heuristicValue;
	}
	
	/**
	 * Setter for the distance from the this instance of the node to
	 * the start node.
	 * @param distance - integer with the distance value.
	 */
	public void setDistanceFromStart(int distance) {
		distanceToStart = distance;
	}
	
	/**
	 * Getter for the distance from this instance of node to
	 * the start node.
	 * @return - integer with the distance value.
	 */
	public int getDistanceFromStart() {
		return distanceToStart;
	}
	
	/**
	 * A method for calculating the path scoring value of this instance of the node, by the means
	 * of addition of the heuristics value and the distance to start value of this node.
	 * @param heuristic - integer with the heuristics value of this instance of the node.
	 * @param distance - integer with the distance to start value of this instance of the node.
	 */
	public void calculatePathScoring(int heuristic, int distance) {
		pathScoring = heuristic + distance;
	}
	
	/**
	 * Getter for the path scoring value of the instance of this node.
	 * @return - integer with the path scoring value of this node.
	 */
	public int getPathScoring() {
		return pathScoring;
	}
	
	/**
	 * Setter for the boolean whether the instance of this node has
	 * been visited.
	 */
	public void setNodeVisited(boolean visited) {
		if (visited) {
			nodeVisited = visited;
		} else {
			nodeVisited = false;
			onTracebackPath = false;
			pathScoring = 0;
		}
	}
	
	/**
	 * Getter for the boolean whether the instance of this node
	 * has been visited.
	 * @return - boolean whether visited or not.
	 */
	public boolean getNodeVisited() {
		return nodeVisited;
	}
	
	/**
	 * Setter for the boolean whether the goal node 
	 * has been reached (i.e. path has been calculated)
	 */
	public void setGoalNodeReached(boolean goalReached) {
		this.goalReached = goalReached;
	}
	
	
	/**
	 * Setter for the the boolean determining whether the
	 * visited node should be drawn on the graphics.
	 * @param show - boolean with the information whether the node should be drawn onto the graphics.
	 */
	public void setShowVisitedNode(boolean show) {
		showVisitedNode = show;
	}
	
	/**
	 * Setter for the boolean determining whether to
	 * show the image at the start node
	 */
	public void setShowImageAtStartNode(boolean show) {
		showImageAtStart = show;
	}
}
