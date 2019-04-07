package Players.LeafCountry;

import Interface.Coordinate;
import Interface.PlayerMove;

import java.util.*;

/**
 * Johnse Chance
 * Date:04/07/2017
 * RIT
 * Program Name: DjiktrasNonsense
 * Description: This is a class designed specifically to calculate the fewestSegmentsToVictory
 */
public class DjiktrasNonsense {
    /** ProxyBoard to be filled. **/
    private ProxyBoard board;
    private int id;
    /**
     * Constructor
     * @param b Board to be Proxied
     * @param l List of all valid moves to be Made
     * @param id The id of the owner of the ProxyNodes.
     */
    public DjiktrasNonsense(Board b, List<PlayerMove> l, int id){
        board = new ProxyBoard(b);
        floodBoard(l, id);
        this.id = id;
    }

    /**
     * Method used to flood the board with ProxyNodes
     * @param l The List of valid moves to be made
     * @param id The id of the owner of the ProxyNodes.
     */
    private void floodBoard(List<PlayerMove> l, int id){
        for(PlayerMove p : l){
            board.processMove(p, id);
        }
    }

    /**
     * Method used to calculate the fewest amount of moves that
     * are required to win for the player id specified in the Constructor.
     * @return The number of moves required.
     */
    public int calculateMoves(){
        Graph<Node> graph = this.toGraph();
        Node[][] nodes = board.getNodes();
        Node alpha = new Node(0);
        Node omega = new Node(0);
        graph.addVertex(alpha);
        graph.addVertex(omega);
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                if(nodes[i][j].isRoot() && nodes[i][j].hasCurrentA()){
                    graph.connect(alpha, nodes[i][j]);
                }else if(nodes[i][j].isRoot() && nodes[i][j].hasCurrentB()){
                    graph.connect(omega, nodes[i][j]);
                }
            }
        }
        LinkedList<Vertex> path = (LinkedList<Vertex>) graph.getShortestpath(alpha, omega);
        return countMoves(path);
    }

    /**
     * Return the list of best moves possible
     * @return Arralist of best Moves
     */
    public ArrayList getMoves(){
        Graph<Node> graph = this.toGraph();
        Node[][] nodes = board.getNodes();
        Node alpha = new Node(0);
        Node omega = new Node(0);
        graph.addVertex(alpha);
        graph.addVertex(omega);
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                if(nodes[i][j].isRoot() && nodes[i][j].hasCurrentA()){
                    graph.connect(alpha, nodes[i][j]);
                }else if(nodes[i][j].isRoot() && nodes[i][j].hasCurrentB()){
                    graph.connect(omega, nodes[i][j]);
                }
            }
        }
        LinkedList<Vertex> path = (LinkedList<Vertex>) graph.getShortestpath(alpha, omega);
        ArrayList<PlayerMove> moves = new ArrayList<>();
        //Remove Alpha and Omega
        path.remove(0);
        path.remove(path.getLast());

        path.forEach(v -> {
            int[] xy = ((Node)v.getData()).getLocation();
            if( xy != null && nodes[xy[0]][xy[1]] instanceof ProxyNode) {
                Coordinate coord = new Coordinate(xy[0], xy[1]);
                PlayerMove move = new PlayerMove(coord, this.id);
                moves.add(move);
            }
        });

        return moves;
    }

    /**
     * Method used to return the board as a Graph.
     * @return The Board as a Graph.
     */
    private Graph<Node> toGraph(){
        Graph<Node> output = new Graph<>();
        Node[][] nodes = board.getNodes();
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                output.addVertex(nodes[i][j]);
            }
        }
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                HashMap<String,Node> surrounding = new HashMap();
                HashMap<String, Coordinate> sides = new HashMap<>();
                sides.put("top", new Coordinate(i - 1, j));
                sides.put("bottom", new Coordinate(i + 1, j));
                sides.put("left", new Coordinate(i, j - 1));
                sides.put("right", new Coordinate(i, j + 1));

                //Fill in the neighoring nodes as null if the node at coord c is at any of the extremes.
                if(i == 0){
                    surrounding.put("top", null);
                }
                if(i == board.getActualSize() - 1){
                    surrounding.put("bottom", null);
                }
                if(j == 0){
                    surrounding.put("left", null);
                }
                if(j == board.getActualSize() - 1){
                    surrounding.put("right", null);
                }
                for (String side:
                        sides.keySet()) {
                    if(!surrounding.containsKey(side)){
                        surrounding.put(side, nodes[sides.get(side).getRow()][sides.get(side).getCol()]);
                    }
                }
                //Update the node at this coordinate.
                for (Map.Entry entry:
                        surrounding.entrySet()) {
                    if(entry.getValue() != null && ((Node)entry.getValue()).getId() == nodes[i][j].getId()){
                        output.connect(nodes[i][j], (Node)entry.getValue());
                    }
                }
            }
        }
        return output;
    }

    /**
     * Method used to count the moves that are to be made from the shortest path.
     * @param l The Shortest Path Represented as a Linked List of Vertices
     * @return The length of the path - the nodes already in place.
     */
    public int countMoves(LinkedList<Vertex> l){
        int total = 0;
        for (Vertex v:
             l) {
            if(v.getData() instanceof ProxyNode){
                total += 1;
            }
        }
        return total;
    }
}
