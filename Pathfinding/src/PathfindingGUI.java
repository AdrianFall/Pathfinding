
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * The PathfindingGUI class sets up the Graphical User Interface for the user,
 * which includes the menu bar and all its menu items, the initialisation of the grid on which the
 * nodes will be represented <br> The GUI allows for customisation of multiple settings
 * of the path-finding nature and delegation to the selected algorithm by the user
 * for the path finding.
 * @author Adrian Fall
 *
 */
public class PathfindingGUI extends Canvas implements Runnable, MouseListener {
	
	private static final long serialVersionUID = 1L;
	/**
	 * Declares a constant variable for the horizontal space of the window
	 */
	private static final int windowWidth = 1050;
	
	/**
	 * Declares a constant variable for the vertical space of the window
	 */
	private static final int windowHeight = 700;
	
	/**
	 * Declares the two dimensional array for maintaining the list of nodes
	 */
	private static Node[][] nodeList;
		
	/**
	 * Declares a Node instance for the start 
	 */
	private static Node start;
	
	/**
	 * Declares a Node instance for the goal
	 */
	private static Node goal;
	
	//Declare a boolean for determining if path has been calculated
	/**
	 * Declares a boolean for the purpose of determining whether the path has been calculated
	 */
	private boolean pathCalculated;
	
	//Declare a PathFindingGUI object for the running thread
	/**
	 * Declares a threaded pathfinding GUI object
	 */
	private static PathfindingGUI threadGUI;
	
	/**
	 * Declares an instance of the A* algorithm object
	 */
	private static AStarAlgorithm aStar;
	
	/**
	 * Declares an instance of the Dijkstra algorithm object
	 */
	private static DijkstraAlgorithm dijkstra;
	
	
	/**
	 * Declares a boolean for tracking whether the user wants to be shown with the visited nodes (when the algorithm is performs
	 * path-finding visually)
	 */
	private static boolean showVisitedNodes = true;
	
	/**
	 * Declares a boolean for tracking whether the user wants to allow diagonal path finding
	 */
	private static boolean allowDiagonalPathFinding = true;
	
	/**
	 * Declares an integer variable for the delay (in milliseconds) between visiting the nodes.
	 */
	private static int msDelayBetweenVisitingNodes = 10;

	/**
	 * Declares an integer variable for the delay (in milliseconds) between the robotic movement.
	 */
	private static int msDelayBetweenRobotMovement = 100;
	
	/**
	 * Declares a String variable for the type of the algorithm that the user has choosen to use, by default it is A*
	 */
	private static String algorithmType = "astar";
	
	/**
	 * Declares a String variable for the type of the heuristics that the user has choosen to use, by default it is Manhattan
	 */
	private static String heuristicsType = "manhattan";
	
	/**
	 * Declares an instance of the JLabel object, for displaying the number of operations.
	 */
	public static JLabel operationsLabel;
	
	/**
	 * Declares an instance of the JLabel object, for displaying the number of visited nodes.
	 */
	public static JLabel visitedNodesLabel;
	
	/**
	 * Declares an instance of the JLabel object, for displaying whether the path-finder has found a path.
	 */
	public static JLabel pathFoundLabel;
	
	/**
	 * Declares an instance of the JLabel object, for displaying the information about the path being traced back.
	 */
	public static JLabel tracingBackLabel;
	
	/**
	 * Declares an instance of the JLabel object, for displaying the information about the path being traced forward.
	 */
	public static JLabel tracingForwardLabel;
	
	/**
	 * Declares an instance of the JLabel object, for displaying the information about the grid being loaded.
	 */
	public static JLabel gridLoadingLabel;
	
	/**
	 * Declares a JLabel object for the image of the start node legend
	 */
	private static JLabel startNodeImageLegend = new JLabel();
	
	private static JFrame f;
	
	private static JLabel legendsInfoLabel;
	
	static JLabel startNodeLabel = new JLabel("Start");
	
	static JLabel goalNodeImageLegend = new JLabel();
	
	static JLabel goalNodeLabel = new JLabel("Goal");
	
	static JLabel obstacleNodeImageLegend = new JLabel();
	
	static JLabel obstacleNodeLabel = new JLabel("Obstacle");
	
	static JLabel visitedNodeImageLegend = new JLabel();
	
	static JLabel visitedNodeLabel = new JLabel("Visited");
	
	static JLabel traversedNodeImageLegend = new JLabel();
	
	static JLabel traversedNodeLabel = new JLabel("Traversed");
	//TODO
	
	/**
	 * The main method which initialises the frame (window) all its components, their bounds (locations), 
	 * mouse and actions listeners.
	 * @param args
	 */
	public static void main (String[] args) {
		//Create and initialise a frame object
		f = new JFrame("Pathfinding");
		//Set the default operation when user closes the window (frame)
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Set the size of the window
		f.setSize(windowWidth, windowHeight);
		//Do not allow resizing of the window
		f.setResizable(false);
		//Set the position of the window to be in middle of the screen when program is started
		f.setLocationRelativeTo(null);
		//Declare and initialise the dijkstra object
		PathfindingGUI gui = new PathfindingGUI();
		
		//Set the layout manager to null
		f.setLayout(null);
		//Set the bounds of the dijkstra object, so that some space is left for the menu bar
		gui.setBounds(0, 20, 800, 580);
		
		//Add the dijkstra object to the frame
		f.add(gui);
		
		//Call the setUpWindowComponents method
		setUpWindowComponents(f);
		
		//Call the setUpMenuBar method
		setUpMenuBar(f);
		
		//Start the GUI thread
		threadGUI = gui;
		gui.start();
		f.setVisible(true);
	}//End of main method
	
	/**
	 * A method for setting up the window's components which include:
	 * labels, buttons, check boxes and its action listeners.
	 * @param f - The window to be set up.
	 */
	private static void setUpWindowComponents(JFrame f) {
		
		//Create and initialise a JLabel object for the information about the Options
		JLabel optionsLabel = new JLabel("Options");
		//Set bounds for the optionsLabel
		optionsLabel.setBounds(850, 30, 200, 25);
		
		//Create and initialise JCheckBox object for the user to specify whether to allow diagonal path finding
		final JCheckBox checkBoxAllowDiagonalPathFinding = new JCheckBox("Allow Diagonal");
		//Set the checkBoxAllowDiagonalPathFinding to be either ticked or unticked. By default it's ticked.
		if (allowDiagonalPathFinding) {
			checkBoxAllowDiagonalPathFinding.setSelected(true);
		} else {
			checkBoxAllowDiagonalPathFinding.setSelected(false);
		}
		//Set the bounds for checkBoxAllowDiagonalPathFinding
		checkBoxAllowDiagonalPathFinding.setBounds(850, 50, 200, 25);
		
		//Create and initialise JCheckBox object for the user to specify whether he wants to be shown with the visited nodes
		final JCheckBox checkBoxVisitedNodes = new JCheckBox("Show Visited Nodes");
		//Set the checkBoxVisitedNodes to be either ticked or unticked. By default it's ticked.
		if (showVisitedNodes) {
			checkBoxVisitedNodes.setSelected(true);
		} else {
			checkBoxVisitedNodes.setSelected(false);
		}
		//Set the bounds for checkBoxVisitedNodes
		checkBoxVisitedNodes.setBounds(850, 80, 200, 25);
		
		//Declare and intialize a JLabel object for the information of the algorithm selection
		JLabel algorithmSelectionInfoLabel = new JLabel("Algorithm selection:");
		//Set the bounds of the algorithmSelectionInfoLabel
		algorithmSelectionInfoLabel.setBounds(850, 115, 200, 25);
		
		//Declare and initialize a JCheckBox object for selection of the A* Algorithm
		final JCheckBox aStarAlgorithmCheckbox = new JCheckBox("A* Algorithm");
		//Set the bounds of the aStarAlgorithmSelection
		aStarAlgorithmCheckbox.setBounds(850, 140, 200, 25);
		//Set the aStarAlgorithmSelection checkbox as ticked by default (Since A* algorithm is the default algorithm of the program)
		aStarAlgorithmCheckbox.setSelected(true);
		
		//Declare and initialize a JCheckBox object for the selection of the Dijkstra Algorithm
		final JCheckBox dijkstraAlgorithmCheckbox = new JCheckBox("Dijkstra Algorithm");
		//Set the bounds of the dijkstraAlgorithmCheckbox
		dijkstraAlgorithmCheckbox.setBounds(850, 170, 200, 25);
		
		//Declare and initialize a JLabel object for the information of the Heuristics selection
		JLabel heuristicsSelectionInfoLabel = new JLabel("Heuristics selection:");
		//Set the bounds of the heuristicsSelectionInfoLabel
		heuristicsSelectionInfoLabel.setBounds(850, 200, 200, 25);
		
		//Declare and initialize a JCheckBox object for selection of the Manhattan Heuristics
		final JCheckBox manhattanHeuristicsCheckbox = new JCheckBox("Manhattan");
		//Set the bounds of the manhattanHeuristicsCheckbox
		manhattanHeuristicsCheckbox.setBounds(850, 225, 200, 25);
		//Set the manhattanHeuristicsCheckbox as ticked by default
		manhattanHeuristicsCheckbox.setSelected(true);
		
		//Declare and initialize a JCheckBox object for selection of the Chebyshev Heuristics
		final JCheckBox chebyshevHeuristicsCheckbox = new JCheckBox("Chebyshev");
		chebyshevHeuristicsCheckbox.setBounds(850, 255, 200, 25);
		
		//Declare and initialize a JLabel object for the information that Dijkstra doesn't use a heuristics
		final JLabel dijkstraHeuristicInfoLabel = new JLabel("<html><font color=red> Dijkstra doesn't use any heuristics</font></html>");
		//Set the bounds of the dijkstraHeuristicInfoLabel
		dijkstraHeuristicInfoLabel.setBounds(825, 225, 200, 25);
		//Set the label to not visible by default
		dijkstraHeuristicInfoLabel.setVisible(false);
		
		//Initialize a JLabel object for the information about operations
		operationsLabel = new JLabel("");
		//Set the bounds of the operationsLabel
		operationsLabel.setBounds(850, 290, 200, 25);
		
		//Initialize a JLabel object for the information about visited nodes
		visitedNodesLabel = new JLabel("");
		//Set the bounds of the visitedNodesLabel
		visitedNodesLabel.setBounds(850, 320, 200, 25);
		
		//Initialize a JLabel object for the information about path being found
		pathFoundLabel = new JLabel("");
		//Set the bounds of the pathFound
		pathFoundLabel.setBounds(850, 350, 200, 25);
		
		//Initialize a JLabel object for the information about tracing back
		tracingBackLabel = new JLabel("");
		//Set the bounds of the tracingBackLabel
		tracingBackLabel.setBounds(850, 380, 200, 25);
		
		//Initialize a JLabel object for the information about tracing forward
		tracingForwardLabel = new JLabel("");
		//Set the bounds of the tracingForwardLabel
		tracingForwardLabel.setBounds(850, 410, 200, 25);
		
		//Initialize a JLabel object for the information about the grid being loaded
		gridLoadingLabel = new JLabel("Please wait while the grid is being loaded...");
		//Set the bounds of the gridLoadingLabel
		gridLoadingLabel.setBounds(100, 600, 300, 25);
		
		//Initialize a JLabel object for the legends label info
		legendsInfoLabel = new JLabel("Legends:");
		legendsInfoLabel.setBounds(25, 610, 200, 25);
		legendsInfoLabel.setVisible(false);
		
		startNodeLabel.setBounds(120, 635, 36, 25);
		startNodeLabel.setVisible(false);
		
		goalNodeImageLegend.setVisible(false);
		goalNodeLabel.setBounds(175, 635, 36, 25);
		goalNodeLabel.setVisible(false);
		
		obstacleNodeImageLegend.setVisible(false);
		obstacleNodeLabel.setBounds(235, 635, 60, 25);
		obstacleNodeLabel.setVisible(false);
		
		visitedNodeImageLegend.setVisible(false);
		visitedNodeLabel.setBounds(310, 635, 60, 25);
		visitedNodeLabel.setVisible(false);
		
		traversedNodeImageLegend.setVisible(false);
		traversedNodeLabel.setBounds(380, 635, 60, 25);
		traversedNodeLabel.setVisible(false);
		//TODO
		
		
		//Declare and initialize a JButton object for the calculation path.
		JButton calculatePathButton = new JButton("Calculate Path");
		//Set bounds of the calculatePathButton
		calculatePathButton.setBounds(850, 450, 150, 50);
		
		//Declare and initialize a JButton object for creating new grid.
		JButton createNewGridButton = new JButton("New Grid");
		//Set bounds of the createNewGridButton
		createNewGridButton.setBounds(850, 510, 150, 50);
		
		//Add an action listener for the checkBoxAllowDiagonalPathFinding
		checkBoxAllowDiagonalPathFinding.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checkBoxAllowDiagonalPathFinding.isSelected()) {
					System.out.println("Setting the allowDiagonalPathFinding to true");
					allowDiagonalPathFinding = true;
				} else {
					System.out.println("Setting the allowDiagonalPathFinding to false");
					allowDiagonalPathFinding = false;
				}
			}
		});//End of ActionListener for checkBoxAllowDiagonalPathFinding
		
		//Add an action listener to the checkBoxVisitedNodes
		checkBoxVisitedNodes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checkBoxVisitedNodes.isSelected()) {
					System.out.println("Setting the showVisitedNodes to true");
					showVisitedNodes = true;
				} else {
					System.out.println("Setting the showVisitedNodes to false");
					showVisitedNodes = false;
				}
			}
		});//End of ActionListener for checkBoxVisitedNodes
		
		//Add the action listener for the aStarAlgorithmCheckbox
		aStarAlgorithmCheckbox.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				if (aStarAlgorithmCheckbox.isSelected()) {
					dijkstraAlgorithmCheckbox.setSelected(false);
					algorithmType = "astar";
					manhattanHeuristicsCheckbox.setVisible(true);
					chebyshevHeuristicsCheckbox.setVisible(true);
					dijkstraHeuristicInfoLabel.setVisible(false);
				} else {
					aStarAlgorithmCheckbox.setSelected(true);
					algorithmType = "astar";
				}
			}
			
		});//End of action listener for aStarAlgorithmCheckbox
		
		//Add the action listener for the dijkstraAlgorithmCheckbox
		dijkstraAlgorithmCheckbox.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				if (dijkstraAlgorithmCheckbox.isSelected()) {
					aStarAlgorithmCheckbox.setSelected(false);
					algorithmType = "dijkstra";
					manhattanHeuristicsCheckbox.setVisible(false);
					chebyshevHeuristicsCheckbox.setVisible(false);
					dijkstraHeuristicInfoLabel.setVisible(true);
				} else {
					dijkstraAlgorithmCheckbox.setSelected(true);
					algorithmType = "dijkstra";
				}
			}
			
		});//End of action listener for dijkstraAlgorithmCheckbox
		
		//Add the action listener for the manhattanHeuristicsCheckbox
		manhattanHeuristicsCheckbox.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				if (manhattanHeuristicsCheckbox.isSelected()) {
					chebyshevHeuristicsCheckbox.setSelected(false);
					heuristicsType = "manhattan";
				} else {
					manhattanHeuristicsCheckbox.setSelected(true);
					heuristicsType = "manhattan";
				}
			}
		});//End of action listener for manhattanHeuristicsCheckbox
		
		//Add the action listener for the chebyshevHeuristicsCheckbox
		chebyshevHeuristicsCheckbox.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				if (chebyshevHeuristicsCheckbox.isSelected()) {
					manhattanHeuristicsCheckbox.setSelected(false);
					heuristicsType = "chebyshev";
				} else {
					chebyshevHeuristicsCheckbox.setSelected(true);
					heuristicsType = "chebyshev";
				}
			}
		});//End of action listener for chebyshevHeuristicsCheckbox
		
		//Add action listener for the calculatePathButton
		calculatePathButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Clear the information regarding algorithm steps/operations
				PathfindingGUI.operationsLabel.setText("");
				PathfindingGUI.operationsLabel.paintImmediately(PathfindingGUI.operationsLabel.getVisibleRect());
				
				PathfindingGUI.visitedNodesLabel.setText("");
				PathfindingGUI.visitedNodesLabel.paintImmediately(PathfindingGUI.visitedNodesLabel.getVisibleRect());
				
				PathfindingGUI.pathFoundLabel.setText("");
				PathfindingGUI.pathFoundLabel.paintImmediately(PathfindingGUI.pathFoundLabel.getVisibleRect());
				
				PathfindingGUI.tracingBackLabel.setText("");
				PathfindingGUI.tracingBackLabel.paintImmediately(PathfindingGUI.tracingBackLabel.getVisibleRect());
				
				PathfindingGUI.tracingForwardLabel.setText("");
				PathfindingGUI.tracingForwardLabel.paintImmediately(PathfindingGUI.tracingForwardLabel.getVisibleRect());
				
				if (threadGUI.pathCalculated) {
					//Loop through the nodeList length for the nodes in columns
					for (int i = 0; i < nodeList.length; i++) {
						//Inner loop through the nodeList length for nodes in rows
						for (int n = 0; n < nodeList[i].length; n++) {
							if (nodeList[i][n].getNodeVisited()) {
								nodeList[i][n].setNodeVisited(false);
							} else if (nodeList[i][n].isTheGoal()) {
								nodeList[i][n].setGoalNodeReached(false);
							} else if (nodeList[i][n].isTheStart()) {
								nodeList[i][n].setShowImageAtStartNode(true);
							}
						}
					}
				}
				//If the start or goal node is null
				if (PathfindingGUI.start == null || PathfindingGUI.goal == null) {
					JOptionPane.showMessageDialog(null, "In order to calculate the path, remember to allocate the start and goal nodes.", "No start or goal node", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				//If the selected algorithm is A* algorithm
				if (algorithmType.equals("astar")) {
					 
					//Initialise the AStarAlgorithm object
					aStar = new AStarAlgorithm(start, goal, allowDiagonalPathFinding, msDelayBetweenRobotMovement, showVisitedNodes, msDelayBetweenVisitingNodes, heuristicsType);
					//Call the method of aStar object to calculate the path
					aStar.calculatePath();
					threadGUI.pathCalculated = true;
				} else if (algorithmType.equals("dijkstra")) {
					//initialise the DijkstraAlgorithm object
					dijkstra = new DijkstraAlgorithm(start, goal, allowDiagonalPathFinding, msDelayBetweenRobotMovement, showVisitedNodes, msDelayBetweenVisitingNodes);
					//Call the method of dijkstra to calculate the path
					dijkstra.calculatePath();
					threadGUI.pathCalculated = true;
				}
			}
		});//End of action listener for calculatePathButton
		
		//Add an action listener for the createNewGridButton
		createNewGridButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent args0) {
				//Clear the information regarding algorithm steps/operations
				PathfindingGUI.operationsLabel.setText("");
				PathfindingGUI.operationsLabel.paintImmediately(PathfindingGUI.operationsLabel.getVisibleRect());
				
				PathfindingGUI.visitedNodesLabel.setText("");
				PathfindingGUI.visitedNodesLabel.paintImmediately(PathfindingGUI.visitedNodesLabel.getVisibleRect());
				
				PathfindingGUI.pathFoundLabel.setText("");
				PathfindingGUI.pathFoundLabel.paintImmediately(PathfindingGUI.pathFoundLabel.getVisibleRect());
				
				PathfindingGUI.tracingBackLabel.setText("");
				PathfindingGUI.tracingBackLabel.paintImmediately(PathfindingGUI.tracingBackLabel.getVisibleRect());
				
				PathfindingGUI.tracingForwardLabel.setText("");
				PathfindingGUI.tracingForwardLabel.paintImmediately(PathfindingGUI.tracingForwardLabel.getVisibleRect());
				threadGUI.initialiseNodesOnGrid();
				threadGUI.pathCalculated = false;
			}
		});
		
		//Add the components to the frame
		f.add(algorithmSelectionInfoLabel);
		f.add(aStarAlgorithmCheckbox);
		f.add(dijkstraAlgorithmCheckbox);
		f.add(heuristicsSelectionInfoLabel);
		f.add(manhattanHeuristicsCheckbox);
		f.add(chebyshevHeuristicsCheckbox);
		f.add(dijkstraHeuristicInfoLabel);
		f.add(checkBoxAllowDiagonalPathFinding);
		f.add(optionsLabel);
		f.add(checkBoxVisitedNodes);
		f.add(operationsLabel);
		f.add(visitedNodesLabel);
		f.add(pathFoundLabel);
		f.add(tracingBackLabel);
		f.add(tracingForwardLabel);
		f.add(calculatePathButton);
		f.add(createNewGridButton);
		f.add(gridLoadingLabel);
		f.add(legendsInfoLabel);
		f.add(startNodeLabel);
		f.add(goalNodeImageLegend);
		f.add(goalNodeLabel);
		f.add(obstacleNodeImageLegend);
		f.add(obstacleNodeLabel);
		f.add(visitedNodeImageLegend);
		f.add(visitedNodeLabel);
		f.add(traversedNodeImageLegend);
		f.add(traversedNodeLabel);
		//TODO
	}
	
	/**
	 * A method for setting up the menu bars, menu items, its action listeners for
	 * calling other methods to create the grid and calculate path via action listeners on the menu items, 
	 * showing settings and help windows via action listeners on the menu items, and the action listeners
	 * for the mouse clicks on the grid (so start, obstacle and goal nodes can be allocated)
	 * @param f - The window to be set up.
	 */
	private static void setUpMenuBar(final JFrame f) {
		//Declare and initialise the bar object
		JMenuBar bar = new JMenuBar();
		//Set the bounds of the bar object
		bar.setBounds(0, 0, windowWidth, 20);
		//Add the bar object to the frame
		f.add(bar);
		
		//Declare and initialise the options object to be placed on the bar as a menu
		JMenu options = new JMenu("Options");
		//Add the options object to the bar as a menu
		bar.add(options);
		//Declare and initialise the quit object as a menu item (to be selectable from drop down menu of options) 
		JMenuItem quit = new JMenuItem("Quit");
		//Declare and initialise the createNewGrid object as a menu item (to be selectable from drop down menu of options)
		JMenuItem createNewGrid = new JMenuItem("Create New Grid");
		//Declare and initialise the calculatePath object as a menu item (to be selectable from drop down menu of options)
		JMenuItem calculatePath = new JMenuItem("Calculate The Path");
		
		//Declare and initialise the settings object to be placed on the bar as a menu
		JMenu settings = new JMenu("Settings");
		//Add the settings object to the bar menu
		bar.add(settings);
		//Declare and initialise the showVisitedNodes object as a menu item (to be selectable from drop down menu of settings)
		JMenuItem calculatePathSettings = new JMenuItem("Calculate Path Settings");
		
		//Declare and initialise the help object to be placed on the bar as a menu
		JMenu help = new JMenu("Help");
		//Add the help object to the bar menu
		bar.add(help);
		//Declare and initialise the aboutProgram object as a menu item (to be selectable from drop down menu of help)
		JMenuItem aboutProgram = new JMenuItem("About Program");
		
		
		//Add an action listener to the quit object, so that when selected it terminates the program
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Terminate the program
				System.exit(0);
			}
		});
		
		//Add an action listener to the createNewGrid object
		createNewGrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Clear the information regarding algorithm steps/operations
				PathfindingGUI.operationsLabel.setText("");
				PathfindingGUI.operationsLabel.paintImmediately(PathfindingGUI.operationsLabel.getVisibleRect());
				
				PathfindingGUI.visitedNodesLabel.setText("");
				PathfindingGUI.visitedNodesLabel.paintImmediately(PathfindingGUI.visitedNodesLabel.getVisibleRect());
				
				PathfindingGUI.pathFoundLabel.setText("");
				PathfindingGUI.pathFoundLabel.paintImmediately(PathfindingGUI.pathFoundLabel.getVisibleRect());
				
				PathfindingGUI.tracingBackLabel.setText("");
				PathfindingGUI.tracingBackLabel.paintImmediately(PathfindingGUI.tracingBackLabel.getVisibleRect());
				
				PathfindingGUI.tracingForwardLabel.setText("");
				PathfindingGUI.tracingForwardLabel.paintImmediately(PathfindingGUI.tracingForwardLabel.getVisibleRect());
				threadGUI.initialiseNodesOnGrid();
				threadGUI.pathCalculated = false;
			}
		});
		
		//Add an action listener to the calculatePath object
		calculatePath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Clear the information regarding algorithm steps/operations
				PathfindingGUI.operationsLabel.setText("");
				PathfindingGUI.operationsLabel.paintImmediately(PathfindingGUI.operationsLabel.getVisibleRect());
				
				PathfindingGUI.visitedNodesLabel.setText("");
				PathfindingGUI.visitedNodesLabel.paintImmediately(PathfindingGUI.visitedNodesLabel.getVisibleRect());
				
				PathfindingGUI.pathFoundLabel.setText("");
				PathfindingGUI.pathFoundLabel.paintImmediately(PathfindingGUI.pathFoundLabel.getVisibleRect());
				
				PathfindingGUI.tracingBackLabel.setText("");
				PathfindingGUI.tracingBackLabel.paintImmediately(PathfindingGUI.tracingBackLabel.getVisibleRect());
				
				PathfindingGUI.tracingForwardLabel.setText("");
				PathfindingGUI.tracingForwardLabel.paintImmediately(PathfindingGUI.tracingForwardLabel.getVisibleRect());
				
				if (threadGUI.pathCalculated) {
					//Loop through the nodeList length for the nodes in columns
					for (int i = 0; i < nodeList.length; i++) {
						//Inner loop through the nodeList length for nodes in rows
						for (int n = 0; n < nodeList[i].length; n++) {
							if (nodeList[i][n].getNodeVisited()) {
								nodeList[i][n].setNodeVisited(false);
							} else if (nodeList[i][n].isTheGoal()) {
								nodeList[i][n].setGoalNodeReached(false);
							} else if (nodeList[i][n].isTheStart()) {
								nodeList[i][n].setShowImageAtStartNode(true);
							}
						}
					}
				}
				//If the start or goal node is null
				if (PathfindingGUI.start == null || PathfindingGUI.goal == null) {
					return;
				}
				
				//If the selected algorithm is A* algorithm
				if (algorithmType.equals("astar")) {
					 
					//Initialise the AStarAlgorithm object
					aStar = new AStarAlgorithm(start, goal, allowDiagonalPathFinding, msDelayBetweenRobotMovement, showVisitedNodes, msDelayBetweenVisitingNodes, heuristicsType);
					//Call the method of aStar object to calculate the path
					aStar.calculatePath();
					threadGUI.pathCalculated = true;
				} else if (algorithmType.equals("dijkstra")) {
					//initialise the DijkstraAlgorithm object
					dijkstra = new DijkstraAlgorithm(start, goal, allowDiagonalPathFinding, msDelayBetweenRobotMovement, showVisitedNodes, msDelayBetweenVisitingNodes);
					//Call the method of dijkstra to calculate the path
					dijkstra.calculatePath();
					threadGUI.pathCalculated = true;
				}
			}
		});
		
		//Add an action listener to the calculatePathSettings object
		calculatePathSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Create and initialise a frame object
				final JFrame frame = new JFrame("Calculate Path Settings");
				//Set the default operation when user closes the window (frame)
				frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				//Set the size of the window
				frame.setSize(300, 400);
				//Do not allow resizing of the window
				frame.setResizable(false);
				//Set the position of the window to be in middle of the screen
				frame.setLocationRelativeTo(null);
				
				//Create and initialise a JLabel for indicating to the user that the delay between visiting nodes can be changed.
				JLabel delayBetweenVisitingNodesLabel = new JLabel("Delay between visiting nodes in milliseconds:");
				
				//Create and initialise a JLabel for indicating the user that the delay between the robot moving can be changed.
				JLabel delayBetweenRobotMovingLabel = new JLabel("Delay between robot movement in milliseconds: ");
				
				//Create and initialise a JTextField for the user to specify the amount of delay in milliseconds between visiting the nodes
				final JTextField delayBetweenVisitingNodesTextField = new JTextField(Integer.toString(msDelayBetweenVisitingNodes));
				
				//Create and initialise a JTextField for the user to specify the amount of delay in milliseconds between robot movement
				final JTextField delayBetweenRobotMovementTextField = new JTextField(Integer.toString(msDelayBetweenRobotMovement));
				
				//Create and initialise a JButton for the user to accept the changes for delaying between visited nodes
				JButton acceptDelayChangesButton = new JButton("Accept changes");
				
				//Create and initialise a JButton for the user to accept the changes for delaying the robot movement
				JButton acceptDelayRobotMovementButton = new JButton("Accept changes");
				
				//Add an actionListener for the acceptDelayChangesButton
				acceptDelayChangesButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int delayInMilliseconds = msDelayBetweenVisitingNodes;
						try {
							//Obtain the inputted delay from the text field
							delayInMilliseconds = Integer.parseInt(delayBetweenVisitingNodesTextField.getText());
						} catch (Exception e1){
							JOptionPane.showMessageDialog(frame, "The input must be an integer", "Wrong input", JOptionPane.ERROR_MESSAGE);
						}
						
						//If the delay in milliseconds is between 0 and 100
						if (delayInMilliseconds >= 0 && delayInMilliseconds <= 400) {
							//Change the msDelayBetweenVisitingNodes
							msDelayBetweenVisitingNodes = delayInMilliseconds;
						} else {
							JOptionPane.showMessageDialog(frame, "The delay must be between 0 and 400 milliseconds.");
						}
					}//End of actionPerformed
				});//End of action listener for acceptDelayChangesButton
				
				//Add an action listener for the acceptDelayRobotMovementButton
				acceptDelayRobotMovementButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e1) {
						int delayInMilliseconds = msDelayBetweenRobotMovement;
						try {
							delayInMilliseconds = Integer.parseInt(delayBetweenRobotMovementTextField.getText());
						} catch (Exception e2) {
							JOptionPane.showMessageDialog(frame, "The input must be an integer", "Wrong input", JOptionPane.ERROR_MESSAGE);
						}
						
						//If the delay in milliseconds is between 100 and 600
						if (delayInMilliseconds >= 100 && delayInMilliseconds <= 600) {
							//Change the msDelayBetweenRobotMovement
							msDelayBetweenRobotMovement = delayInMilliseconds;
						} else {
							JOptionPane.showMessageDialog(frame, "The delay must be between 100 and 600 milliseconds.");
						}
					}//End of action performed
				});//End of action listener for the acceptDelayRobotMovementButton
				
				
				//Create and initialise the JPanel object
				JPanel panel = new JPanel();
				//Set the panel layout to null, so it allows for custom positioning of its components (via the means of setBounds of each components)
				panel.setLayout(null);
				
				//Add the delayBetweenVisitingNodesLabel to the panel
				panel.add(delayBetweenVisitingNodesLabel);
				//Position the delayBetweenVisitingNodesLabel
				delayBetweenVisitingNodesLabel.setBounds(20, 85, 275, 25);
				
				//Add the delayBetweenRobotMovingLabel to the panel
				panel.add(delayBetweenRobotMovingLabel);
				//Position the delayBetweenRobotMovingLabel
				delayBetweenRobotMovingLabel.setBounds(20, 145, 275, 25);
				
				//Add the delayBetweenVisitingNodesTextField to the panel
				panel.add(delayBetweenVisitingNodesTextField);
				//Position the delayBetweenVisitingNodesTextField
				delayBetweenVisitingNodesTextField.setBounds(20, 105, 80, 25);
				
				//Add the delayBetweenRobotMovementTextField to the panel
				panel.add(delayBetweenRobotMovementTextField);
				//Position the delayBetweenRobotMovementTextField
				delayBetweenRobotMovementTextField.setBounds(20, 170, 80, 25);
				
				//Add the acceptDelayChangesButton to the panel
				panel.add(acceptDelayChangesButton);
				//Position the acceptDelayChangesButton
				acceptDelayChangesButton.setBounds(110, 105, 130, 25);
				
				//Add the acceptDelayRobotMovementButton to the panel
				panel.add(acceptDelayRobotMovementButton);
				//Position the acceptDelayRobotMovementButton
				acceptDelayRobotMovementButton.setBounds(110, 170, 130, 25);
				
				//Add the panel to the frame
				frame.add(panel);
				//Set the frame to be visible
				frame.setVisible(true);
				
			}//End of actionPerformed method for calculatePathSettings
		});//End of ActionListener for calculatePathSettings
		
		//Add an action listener to the aboutProgram object
		aboutProgram.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Create and initialise a frame object
				JFrame frame = new JFrame("About Program");
				//Set the default operation when user closes the window (frame)
				frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				//Set the size of the window
				frame.setSize(300, 400);
				//Do not allow resizing of the window
				frame.setResizable(false);
				//Set the position of the window to be in middle of the screen
				frame.setLocationRelativeTo(null);
				
				//Create and initialise the JPanel object
				JPanel panel = new JPanel();
				//Set the panel layout
				panel.setLayout(new GridLayout(2,2));
				
				//Create and initialise a label objects
				JLabel labelTitle = new JLabel("About the Pathfinding Program");
				JLabel labelMouseSettings = new JLabel("<html> The Mouse Settings: <br>Left mouse button = Placement of obstacle node <br>" +
						"Right mouse button = Placement of start/goal node <br><br> The Program Settings: <br> Show/Hide visited nodes <br> "
						+ " Allow/Disable diagonal path finding <br> Set delay between visiting nodes <br> Set delay between robot movement</html>");
				JLabel labelAuthor = new JLabel("Author: Adrian Fall");
				//Center the position of the JLabels
				labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
				labelMouseSettings.setHorizontalAlignment(SwingConstants.CENTER);
				labelAuthor.setHorizontalAlignment(SwingConstants.CENTER);
				
				//Add the labels to the panel
				panel.add(labelTitle);
				panel.add(labelMouseSettings);
				//panel.add(labelAuthor);
				
				//Add the panel to the frame
				frame.add(panel);
				//Set the frame to be visible
				frame.setVisible(true);
			}//End of actionPerformed method for aboutProgram 
		});//End of ActionListener for aboutProgram
		
		//Add the quit object to the list of drop down items of options
		options.add(quit);
		//Add the createNewGrid object to the list of drop down items of options
		options.add(createNewGrid);
		//Add the calculatePath object to the list of drop down items of options
		options.add(calculatePath);
		
		//Add the calculatePathSettings object to the list of drop down items of settings
		settings.add(calculatePathSettings);
		
		//Add the aboutProgram object to the list of drop down items of the help
		help.add(aboutProgram);
		
	}//End of setUpMenu method
		
	/**
	 * A method for starting a new thread, so it takes care
	 * of the grid, its initialisation and rendering.
	 */
	public void start() {
		new Thread(this).start();
		System.out.println("Thread started");
	}
	
	/**
	 * A method for running the thread which initialises the nodes
	 * and their positions on the grid, creating buffered strategy
	 * which organises the memory allocation of the canvas, creates
	 * the 2D graphics object and delegates it to the render method
	 */
	public void run() {
		
		//Call the initialise method to initialise the grid for the nodes.
		initialiseGrid();

		f.add(startNodeImageLegend);
		initialiseNodesOnGrid();
		
		showLegends();

		System.out.println("Thread running.");
		while(true) {
			
			//Create the bufferStrategy object for organizing memory of the canvas
			BufferStrategy buffStrategy = getBufferStrategy();
			
			//If the bufferedStrategy has not been yet created with buffers
			if (buffStrategy == null) {
				System.out.println("Creating strategy with 2 buffers");
				//Create the strategy with 2 buffers
				createBufferStrategy(2);
				//Continue from the beginning of while loop
				continue;
			}
			//Create the Graphics2D object that will be used for rendering
			Graphics2D g2d = (Graphics2D) buffStrategy.getDrawGraphics();
			//Call the render method
			render(g2d);
			//Make the next available buffer visible
			buffStrategy.show();
			
			//Delay the thread
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}//End of run method
	

	
	private void showLegends() {
		//TODO
		startNodeImageLegend.setIcon(new javax.swing.ImageIcon(getClass().getResource("start.png")));  
		startNodeImageLegend.setBounds(120, 610, 36, 25);
		
		legendsInfoLabel.setVisible(true);
		startNodeLabel.setVisible(true);
		
		goalNodeImageLegend.setIcon(new javax.swing.ImageIcon(getClass().getResource("goal.png")));  
		goalNodeImageLegend.setBounds(170, 610, 36, 25);
		goalNodeImageLegend.setVisible(true);
		goalNodeLabel.setVisible(true);
		
		obstacleNodeImageLegend.setIcon(new javax.swing.ImageIcon(getClass().getResource("obstacle.png")));
		obstacleNodeImageLegend.setBounds(240, 610, 36, 25);
		obstacleNodeImageLegend.setVisible(true);
		
		obstacleNodeLabel.setVisible(true);
		
		visitedNodeImageLegend.setIcon(new javax.swing.ImageIcon(getClass().getResource("visited.png")));
		visitedNodeImageLegend.setBounds(310, 610, 36, 25);
		visitedNodeImageLegend.setVisible(true);
		visitedNodeLabel.setVisible(true);
		
		traversedNodeImageLegend.setIcon(new javax.swing.ImageIcon(getClass().getResource("traversed.png")));
		traversedNodeImageLegend.setBounds(390, 610, 36, 25);
		traversedNodeImageLegend.setVisible(true);
		traversedNodeLabel.setVisible(true);
		

	}

	/**
	 * A method for initialising the grid of node list.
	 * The grid is initialised based on two dimensional array
	 * of the node list, where the first dimension corresponds 
	 * to the number of columns and the second dimension
	 * to the number of rows of possible nodes on the grid.
	 * Additionally adds the mouse listener to the grid, so
	 * that the nodes (obstacle, start and goal) can be allocated
	 * on the grid.
	 */
	public void initialiseGrid() {

		System.out.println("Hello from initialise");
		//Request focus for this component for the inputs and window.
		requestFocus();
		//Set the pathCalculated boolean to false, since we are just initialising the nodes and their positions
		pathCalculated = false;
		
		//Initialise and set the size of the nodeList, first dimension corresponds to the number of columns
		//and second dimension to the number of rows
		nodeList = new Node[19][13];
		System.out.println("nodeList.length (number of columns) = " + nodeList.length);
		System.out.println("nodeList[] length (number of rows) = " + nodeList[0].length);
		
		//Add the mouse listener
		addMouseListener(this);
	}
	
	/**
	 * A method for initialising the nodes on the grid, setting
	 * its position (column and row). When nodes are created on 
	 * the grid, each of them will be initialised with 8-way surrounding
	 * nodes
	 */
	//A method for initialising the nodes
	public void initialiseNodesOnGrid() {

		//Set the start and goal nodes to null
		start = null;
		goal = null;
		
		//Loop through the nodeList length for the nodes in columns
		for (int i = 0; i < nodeList.length; i++) {
			//Inner loop through the nodeList length for nodes in rows
			for (int n = 0; n < nodeList[i].length; n++) {
				
				//Create the node
				System.out.println("Creating node at column [" + i + "] row" + " [" + n + "]");
				nodeList[i][n] = new Node(i, n).setXPos(20 + i * 40).setYPos(20 + n * 40);
			}
		}
		
		//Loop through the nodeList length for the nodes in columns
		for (int column = 0; column < nodeList.length; column++) {
			//Inner loop through the nodeList length for nodes in rows
			for (int row = 0; row < nodeList[column].length; row++) {
				//Declare variables for the directions of the nodes
				int eastDirection = column + 1;
				int westDirection = column - 1;
				int northDirection = row - 1;
				int southDirection = row + 1;
				
				
				//Declare and initialise the surrounding nodes
				Node east = null, west = null, north = null, south = null, northEast = null, northWest = null, southEast = null, southWest = null;
				
				//If the eastDirection is within the bounds of the grid
				if (eastDirection >= 0 && eastDirection < nodeList.length) {
					//Set the surrounding east node
					east = nodeList[eastDirection][row];
				}
				//If the westDirection is within the bounds of the grid
				if (westDirection >= 0 && westDirection < nodeList.length) {
					//Set the surrounding west node
					west = nodeList[westDirection][row];
				}
				//If the northDirection is within the bounds of the grid 
				if (northDirection >= 0 && northDirection < nodeList[column].length) {
					//Set the surrounding west node
					north = nodeList[column][northDirection];
				}
				//If the southDirection is within the bounds of the grid
				if (southDirection >= 0 && southDirection < nodeList[column].length) {
					//Set the surrounding south node
					south = nodeList[column][southDirection];
				}
				//If the northDirection and eastDirection is within the bounds of the grid, hence the northEast direction will be within the bounds as well
				if (northDirection >= 0 && northDirection < nodeList[column].length && eastDirection >= 0 && eastDirection < nodeList.length) {
					//Set the surrounding northEast node
					northEast = nodeList[eastDirection][northDirection];
				}
				//If the northDirection and westDirection is within the bounds of the grid, hence the northWest direction will be within the bounds as well
				if (northDirection >= 0 && northDirection < nodeList[column].length && westDirection >= 0 && westDirection < nodeList.length) {
					//Set the surrounding northWest node
					northWest = nodeList[westDirection][northDirection];
				}
				//If the southDirection and eastDirection is within the bounds of the grid, hence the southEast direction will be within the bounds as well
				if (southDirection >= 0 && southDirection < nodeList[column].length && eastDirection >= 0 && eastDirection < nodeList.length) {
					//Set the surrounding southEast node
					southEast = nodeList[eastDirection][southDirection];
				}
				//If the southDirection and westDirection is within the bounds of the grid, hence the southWest direction will be within the bounds as well
				if (southDirection >= 0 && southDirection < nodeList[column].length && westDirection >= 0 && westDirection < nodeList.length) {
					southWest = nodeList[westDirection][southDirection];
				}
				
				//Set the nearby directions of the currently looped node
				nodeList[column][row].setNearbyNodes(east, west, north, south, northEast, northWest, southEast, southWest);
			}
		}
		
		gridLoadingLabel.setVisible(false);
		
	}//End of InitialiseNodes method
	
	/**
	 * A method for rendering the graphics to be drawn onto the graphics 2d object,
	 * hence it renders the grid with the node graphical representation.
	 * @param g2d - The graphics 2d object to be drawn onto.
	 */
	public void render(Graphics2D g2d) {
		
		//Set the background color and bounds
		g2d.setColor(new Color(136,0,0));
		g2d.fillRect(0, 0, 800, 600);
		
		//Loop through the nodeList length for the nodes in columns
		for (int i = 0; i < nodeList.length; i++) {
			//Inner loop through the nodeList length for the nodes in rows
			for (int n = 0; n < nodeList[i].length; n++) {
				
				//Render the node through the Node class
				nodeList[i][n].render(g2d);
			}//End of loop for nodes in rows
		}//End of loop for nodes in columns
	}//End of render method

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * A method for handling the mouse events when mouse has been pressed down
	 * on the grid, so that it allows an start, goal or obstacle node to
	 * be placed on the grid. 
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		//If the path has been already calculated
		if (pathCalculated) {
			JOptionPane.showMessageDialog(null, "Please create a new grid (in Options) to draw a new path.");
			return;
		} else {
			//Obtain the pressed button and store it in mouseButton
			int mouseButton = e.getButton();
					
			//Create a node object and call the method to get its position (i.e. its row and column)
			final Node node = getNodePosition(e.getX(), e.getY());
			
			//If the node object exists
			if (node != null) {
				boolean nodeIsObstacle = false;
				if (node.isTheObstacle()) nodeIsObstacle = true;
				//Clear the clicked node, in case it has been assigned already to any type of node
				node.clearNode();
				
				//If the clicked mouse button is the left button
				if (mouseButton == 1) {
					System.out.println("node is the goal" + (node == goal));
					if (!nodeIsObstacle && !(node == goal) && !(node == start)){
					//Set the clicked node to be the obstacle
					node.setTheObstacle(); 
					} else if (node == goal) {
						goal = node;
						node.setTheGoal(true);
					} else if (node == start) {
						start = node;
						node.setTheStart();
					}
					
				}
				
				//If the clicked mouse button is the right button
				if (mouseButton == 3) {
					
					//Initialise the JPopupMenu for showing the menu when right mouse button is clicked
					JPopupMenu menu = new JPopupMenu("Menu");
					//Initialise the JMenuItem for selecting the start node from the menu
					JMenuItem startNodeItem = new JMenuItem("Start Node");
					//Initialise the JMenuItem for selecting the goal node from the menu
					JMenuItem goalNodeItem = new JMenuItem("Goal Node");
				    //Add the JMenuItems to the menu
				    menu.add(startNodeItem);
				    menu.add(goalNodeItem);
				    //Show the menu at the coordinates of where the mouse button has been clicked
				    menu.show(e.getComponent(), e.getX(), e.getY());
				    
				    //Add an action listener for the startNodeItem
				    startNodeItem.addActionListener(new ActionListener() {
				    
						@Override
						public void actionPerformed(ActionEvent e1) {
							//If the start Node exists
							if (start != null) {
								//Clear the goal
								start.clearNode();
								
							}
							//Set the clicked node to be the start
							node.setTheStart();
							//Set the start node with the current node
							start = node;
						}
				    	
				    });//End of action listener for the startNodeItem
				    
				    goalNodeItem.addActionListener(new ActionListener() {
				    	
				    	@Override
				    	public void actionPerformed(ActionEvent e2) {
				    		//If the goal Node exists
				    		if (goal != null) {
				    			//Clear the goal
				    			goal.clearNode();
				    			
				    		}
				    		//Set the clicked node to be the goal
				    		node.setTheGoal(true);
				    		//Set the goal node with the current node
				    		goal = node;
				    	}
				    });//End of action listener for the goalNodeItem
				}//End of if the clicked mouse is the right button
			}//End of if the node object exists
		}//End of else (i.e. path is not calculated)
	}//End of mousePressed method
	
	/**
	 * A method for determining the positon of the node in
	 * the two dimensional array (i.e. its column and row)
	 * based on the clicked mouse coordinates (i.e. x and y)
	 * @param mouseX - The coordinate of where the mouse button was clicked, i.e. the width
	 * @param mouseY - The coordinate of where the mouse button was clicked, i.e. the height
	 * @return - If the mouse coordinate were within the bounds of the grid, then returns the node at the specific column and row position. Otherwise returns null
	 */
	public Node getNodePosition(int mouseX, int mouseY) {
		
		mouseX -= 20;
		mouseY -= 20;
		mouseX /= 40;
		mouseY /= 40;
		
		//If the mouse coordinates are within the bounds of any node on the grid
		if (mouseX >= 0 && mouseY >= 0 && mouseX < nodeList.length && mouseY < nodeList[mouseX].length) {
			return nodeList[mouseX][mouseY];
		} else {
			return null;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
