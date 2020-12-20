# ex2
Directed and weighted graph implementation

### This project is about directed weighted graphs and consists of two parts:

## first part:

The first part describes a number of classes that represent a weighted graph and special algorithms that can be performed on it.
The two main class in this part are:

1) DWGraph_DS
2) DWGraph_Algo

The DWGraph_DS class implements the directed_weighted_graph interface With the help of two more classes NodeData, EdgeData that 
implement additional interfaces that describe edge and vertex properties.
So how is a weighted directed graph actually represented?
Each such graph is represented by 4 HashMaps:
The one-contains the vertices of the graph.
The other-contains its ribs.
The third-contains the list of neighbors for each vertex when it is the source of the rib that connects them.
The fourth-contains the list of neighbors of each vertex when it is the target of the connecting rib between them.
In addition, there are two other variables: edgeSiz,mcCounter.

### The DWGraph_DS class enables the creation of a graph and contains methods such as:

* getNode (int key)-Returns the vertex with the ID, otherwise returns null.

* getEdge (int src, int dest)-Returns the edge (src,dest), null if none

* addNode (node_data n)-Adds a new node to the graph with the given node_data.

* connect (int src, int dest, double w)-Connects an edge with weight w between node src to node dest.

* getV ()-Returns a pointer for the collection representing all the nodes in the graph.

* getE (int node_id)-Returns a pointer for the collection representing all the edges getting out of the given node. 

* removeNode (int key)-Deletes the node from the graph and removes all edges which starts or ends at this node.

* removeEdge (int src, int dest)-Deletes the edge from the graph.

* nodeSize ()-Returns the number of vertices (nodes) in the graph.

* edgeSize ()-Returns the number of edges (assume directional graph).

* getMC ()-Returns the Mode Count - for testing changes in the graph.

### The DWGraph_Algo class that implements the dw_graph_algorithms interface describes special algorithms that are executed on directed 
### weighted graphs and contains methods such as:

* init (directed_weighted_graph g)- performs a graph initialization on which the algorithms are executed.

* getGraph ()-returns the graph on which the algorithms are executed.

* copy ()-Deep copy of the graph using temporary collection, creation of new vertices and the connect method.

* isConnected ()-
a Boolean function that checks whether a graph is strongly connected graph using a BFS algorithm (since BFS is an algorithm that performs a
lateral search and scans the graph in a way that ensures that any node that is in the same binding element of the
initial node is checked which helps us check if the whole graph is connected or not by reversing the direction of the ribs and activating it again on the same vertex.

* shortestPathDist (int src, int dest)-
The sum of the weights of the requested route is returned after using the Dijkstra algorithm which makes sure this is the most effective 
route,if there is no route at all it will be returned -1.

* shortestPath (int src, int dest)-
After using the Dijkstra algorithm a list With the most effective route is returned containing the requested route starting
from the source vertex to the destination vertex.

* Save () -
Save the graph (which we initialized using the init) to a file in a functionable location.
The graph will be saved in Json format thus allowing other programmers to load the graph in their software well.
(To enable saving and loading as requested by us, we implemented the function without using the gson library,
And actually we implemented the function using loops)

* load () -
Allows you to load a graph from a text file in Json format.
If the graph is loaded successfully - we will initialize the graph found in DWGraph_Algo to be the above graph.
Otherwise an error message will be thrown and nothing will change.

### Functions that helped perform the various algorithms:

* bfs () -
A lateral search algorithm that allows us to test whether it is possible to reach from a particular vertex on the graph towards all the other vertices.
The function gets a source vertex and from there the search begins.
This algorithm is used by the function isConnected which aims to test whether the initialized graph in DWGraph_Algo is a strong link or not.

* restoreNodes ()
A function that returns the Info of the vertices to their original state.
It is used in the isConnected algorithm.
Complexity: O (n) where n is the number of vertices on the graph.

* dfs () -
An in-depth search algorithm that allows us to go over the graph (and search it).
Similar to the bfs algorithm we choose a random vertex from which we will start the search,
But unlike bfs this algorithm is an in-depth search, meaning not going through all the neighbors of the vertex first,
But choose a neighbor we have not visited and recursively continue to search for a neighbor we have not visited
Until the algorithm is stuck / blocked by a neighbor we have already visited.
When such a thing collapses we return to a vertex that has a neighbor that we have not yet visited.
In addition each vertex holds a field that tells when we visited it, and the realization is through a stack.
Complexity: O (V + E) where V is the number of vertices on the graph and E is the number of sides.

* tarjan () -
An algorithm that allows you to find binding components.
The realization is done by a stack, and a call to the dfs algorithm.
Complexity: O (| E | * | V |) where E is the number of sides and V is the number of vertices on the graph.

* getComp () -
Activates the Tarjan algorithm and returns a list of binding elements on the graph.

* fromJsonToGraph () -
A function that allows you to load a graph from a String object of Json format.
Unlike the load function this load does not change the initialized graph in DWGraph_Algo, but returns a new graph to the user.
This function helps in the second part of the project.

 * oneCallShrtPathD () -
Similar to the ShortestPathDist function returns the weight of the most efficient / cheapest way from a source vertex to a destination vertex, without reusing the dijkstra algorithm.
pay attention! To use this function you must first use their algorithm dijkstra with the requested vertex and then you can use this function and search
The efficient ways without using dijkstra again.
O (n) where n is the number of vertices on the graph

* shortCurrPath () -
Similar to the shortestPath function returns the path itself (Node_data List) of the most efficient / cheapest path from a source vertex to a destination vertex, without reusing the dijkstra algorithm.
pay attention! To use this function you must first use their algorithm dijkstra with the requested vertex and then you can use this function and search
The efficient ways without using dijkstra again.
O (n) where n is the number of vertices on the graph

## second part:

The second part of the project presents us to a game called the Pokemon game, in which there is a graph with Pokemon and agents on it, the goal of the agents is to catch as many Pokemon as possible with as few calls for moves as possible to assign the game to play.
The goal of the second part of the project is to find an effective solution so that we get the highest score.
In order to meet this requirement, we used the departments given to us by the lecturers / practitioners and made changes accordingly.

### The departments are:

* Agent-
This class contains the features that each Pokemon Agent needs.
Some of the functions in this class are:
* update () -The purpose of this function is to update the current agent with new values.

* setNextNode () -Defines to the agent who will be the next vertex to which he will move.

* Pokemon-
This class contains all the features that each Pokemon needs.
Some of the functions in this class are:
* init_from_json () -This function receives a String in Json format and creates a Pokemon object

* get_edge () -Returns the rib on which the Pokemon is located.

* Arena-
The "map" on which the game is played, contains a graph, Pokemon agents and more.
The class contains the following object variables:
directed_weighted_graph gg
List <Agent> agents
List <Pokemon> pokemons
List <String> info
Point3D MIN
Point3D MAX
Some of the functions in this class are:
* setGraph (directed_weighted_graph g) -This function initializes the arena with the graph it receives.

* getAgents (String aa, directed_weighted_graph gg) -This function receives a String and Graph, and returns a list of agents.
The String is in Json format.
* json2Pokemons (String fs) -In the same way this function receives a String in Json format and returns an array of Pokemon.
 
* updateEdge (Pokemon fr, directed_weighted_graph g) -Assigns the corresponding side to the given Pokemon.
(By auxiliary function called isOnEdge)
* isOnEdge (geo_location p, geo_location src, geo_location dest) -Using three positions checks if a particular Pokemon is on any rib.

* GraphRange (directed_weighted_graph g) -Returns the size of the graph as a Range2D object.

* MyFrame-
The graphical display of the game, with the help of this class we can watch (visually) the game in real time.
The class contains the following object variables:
Arena ar
gameClient.util.Range2Range w2f
### Some of the functions in this class are:

* update (Arena ar) -Updates the Arena in Frame

* updateFrame () -Updates the Frame, among other things adjusts the size of the graph and the Arena to the size of the window.

* drawGraph (Graphics g) -Draws the graph on all the sides and vertices

* drawPokemons (Graphics g) -Draws all the Pokemon on the graph and in addition above each registered Pokemon its value.

* drawAgants (Graphics g) -Draws all the agents on the graph and in addition above each registered agent the score he has in real time.

* SimpleGui-
Displays a graphical window for the login screen, in which the user is required to enter his ID number and the stage at which he wants to play.

* Range
Represents the distance of a one-dimensional object.

* Range2D-
Represents two-dimensional distance, consisting of two forms of one-dimensional object.

* Range2Range-
Allows you to convert distances and locations between the Frame and the real world

* Point3D-
Represents a three-dimensional point in space, Athens is used primarily to know where agents and Pokemon are located on the graph.

* Ex2-
The class that represents the entire game, presents it visually and contains the algorithms we implemented.

# enjoy!

