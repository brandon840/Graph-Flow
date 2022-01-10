package assign3;

import java.util.*;

public class BellmanFord{

    private int[] distances = null;
    private int[] predecessors = null;
    private int source;

    class BellmanFordException extends Exception{
        public BellmanFordException(String str){
            super(str);
        }
    }

    class NegativeWeightException extends BellmanFordException{
        public NegativeWeightException(String str){
            super(str);
        }
    }

    class PathDoesNotExistException extends BellmanFordException{
        public PathDoesNotExistException(String str){
            super(str);
        }
    }

    BellmanFord(WGraph g, int source) throws NegativeWeightException{
        /* Constructor, input a graph and a source
         * Computes the Bellman Ford algorithm to populate the
         * attributes 
         *  distances - at position "n" the distance of node "n" to the source is kept
         *  predecessors - at position "n" the predecessor of node "n" on the path
         *                 to the source is kept
         *  source - the source node
         *
         *  If the node is not reachable from the source, the
         *  distance value must be Integer.MAX_VALUE
         */
        this.source = source;
        this.distances = new int[g.getNbNodes()];
        this.predecessors = new int[g.getNbNodes()];

        //Initialize the graph
        for (int n=0;n<g.getNbNodes();n++){
                distances[n] = Integer.MAX_VALUE;
                predecessors[n] = -1;
        }
        this.distances[source] = 0;

        //Edge relaxation
        for(int i=0;i<g.getNbNodes()-1;i++) {
            for (Edge e : g.getEdges()) {
                if (distances[e.nodes[0]] + e.weight < distances[e.nodes[1]]) {
                    distances[e.nodes[1]] = distances[e.nodes[0]] + e.weight;
                    predecessors[e.nodes[1]] = e.nodes[0];
                }
            }
        }

        //Check for negative cycles
        for (Edge e : g.getEdges()) {
            if (distances[e.nodes[0]] + e.weight < distances[e.nodes[1]]) {
                throw new NegativeWeightException("There is a negative weight cycle!");
            }
        }

    }

    public int[] shortestPath(int destination) throws PathDoesNotExistException{
        /* Returns the list of nodes along the shortest path from
         * the object source to the input destination
         * If not path exists an Error is thrown
         */
        int n = destination;
        ArrayList<Integer> path = new ArrayList<Integer>();
        path.add(destination);
        while (predecessors[n] != this.source){
            if (predecessors[n] == -1){
                throw new PathDoesNotExistException("Unreachable node");
            }
            path.add(0,predecessors[n]);
            n=predecessors[n];
        }
        path.add(0,source);

        int[] pathArray = new int[path.size()];
        for(int i=0;i<pathArray.length;i++){
            pathArray[i]=path.get(i);
        }

        return pathArray;
    }

    public void printPath(int destination){
        /* Print the path in the format s->n1->n2->destination
         * if the path exists, else catch the Error and
         * prints it
         */
        try {
            int[] path = this.shortestPath(destination);
            for (int i = 0; i < path.length; i++){
                int next = path[i];
                if (next == destination){
                    System.out.println(destination);
                }
                else {
                    System.out.print(next + "-->");
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public static void main(String[] args) throws NegativeWeightException, PathDoesNotExistException {
        //String file = args[0];
        WGraph g = new WGraph("C:\\Users\\BRANDON MA\\Personal Projects\\Assignment 3\\src\\assign3\\bf1.txt");
        try{
            BellmanFord bf = new BellmanFord(g, g.getSource());
            bf.printPath(g.getDestination());
        }
        catch (Exception e){
            System.out.println(e);
        }

   } 
}

