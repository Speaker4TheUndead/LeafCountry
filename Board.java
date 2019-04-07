package Players.LeafCountry;

import Interface.Coordinate;
import Interface.PlayerMove;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Johnse Chance
 * @date: 03/19/2017
 */

/**
 * Board Class that retains all information about the board, and the nodes inside.
 */
public class Board {
    /** 2D Array of nodes used to keep the status of the board.**/
    protected Node[][] nodes;

    /** The actual size of the whole board. **/
    protected int actualSize;

    public Board(int dim){
        this.actualSize = (2*dim) + 1;
        nodes =  new Node[actualSize][actualSize];
        //Initialize Game Board
        //Create The Roots For Player 2
        //A nodes
        for (int j = 0; j < actualSize; j++) {
            if(j % 2 != 0) {
                nodes[0][j] = new Node(2, true, false);
                nodes[actualSize - 1][j] = new Node(2, false, true);
            }else{
                nodes[0][j] = new Node(0, false, false);
                nodes[actualSize - 1][j] = new Node(0);
            }
            nodes[0][j].setLocation(0, j);
            nodes[actualSize - 1][j].setLocation(actualSize - 1, j);
        }

        //Create The Roots For Player 1
        //A Nodes
        for (int j = 0; j < actualSize; j++) {
            if(j % 2 != 0) {
                nodes[j][0] = new Node(1, true, false);
                nodes[j][actualSize - 1] = new Node(1, false, true);
            }else{
                nodes[j][0] = new Node(0, false, false);
                nodes[j][actualSize - 1] = new Node(0);
            }
            nodes[j][0].setLocation(j, 0);
            nodes[j][actualSize - 1].setLocation(j, actualSize - 1);

        }

        //Create Checkerboarded inside nodes.
        for (int i = 1; i < actualSize - 1; i++) {
            for (int j = 1; j < actualSize - 1; j++) {
                if(i % 2 !=0 && j % 2 == 0){
                    nodes[i][j] = new Node(1);
                }else if(j % 2 !=0 && i % 2 == 0){
                    nodes[i][j] = new Node(2);
                }else{
                    nodes[i][j] = new Node(0);
                }
                nodes[i][j].setLocation(i, j);
            }
        }
    }

    /**
     * Copy Constructor, Mainly used for proxies.
     * @param b original board to copy.
     */
    public Board(Board b){
        actualSize = b.getActualSize();
        nodes = b.carbonCopy();
    }

    /**
     * Updates the "current" flowing across the board, through the nodes. If a node is connected to one side of the
     * board it will have either currentA or currentB passing through it. If it gains a current, it'll update its
     * surrounding nodes of the same id so that they may continue the flow of the current. If any node has both currentA
     * and currenB flowing through it then it means that the owner of that node has won.
     * @param c The Coordinate of the node to update.
     */
    public void updateNodes(Coordinate c){
        //If there nodes surrounding this one without its current, then give them its current
        //If you had to update any, call this function on them
        int row = c.getRow();
        int col = c.getCol();
        HashMap<String,Node> surrounding = new HashMap();
        HashMap<String, Coordinate> sides = new HashMap<>();
        sides.put("top", new Coordinate(row - 1, col));
        sides.put("bottom", new Coordinate(row + 1, col));
        sides.put("left", new Coordinate(row, col - 1));
        sides.put("right", new Coordinate(row, col + 1));
        //Variables to determine whether a change has occurred in the current of this node.
        boolean prevA = nodes[row][col].hasCurrentA();
        boolean prevB = nodes[row][col].hasCurrentB();
        //Fill in the neighoring nodes as null if the node at coord c is at any of the extremes.
        if(row == 0){
            surrounding.put("top", null);
        }
        if(row == actualSize - 1){
            surrounding.put("bottom", null);
        }
        if(col == 0){
            surrounding.put("left", null);
        }
        if(col == actualSize - 1){
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
            if(entry.getValue() != null && ((Node)entry.getValue()).getId() == nodes[row][col].getId()){
                if(((Node)entry.getValue()).hasCurrentA() ){
                    nodes[row][col].powerA();
                }
                if(((Node)entry.getValue()).hasCurrentB() ){
                    nodes[row][col].powerB();
                }
            }
        }
        //Update the Surrounding nodes recursively (If a change was made to this node.)
        if(prevA != nodes[row][col].hasCurrentA() || prevB != nodes[row][col].hasCurrentB()){
            for (Map.Entry entry:
                    surrounding.entrySet()) {
                if(entry.getValue() != null &&((Node)entry.getValue()).getId() == nodes[row][col].getId()){
                    updateNodes(sides.get(entry.getKey()));
                }
            }
        }
    }

    /**
     * Processes a move to make sure it is valid or not, if the former it, will update the game board to reflect
     * its effect on the game.
     * @param move The move to be processed
     */
    public void processMove(PlayerMove move){
        Coordinate c = move.getCoordinate();
        //Make Sure the move is valid
        if((c.getRow() + c.getCol()) % 2 != 0 || c.getRow() <= 0 || c.getRow() >= actualSize - 1
                || c.getCol() <= 0 || c.getCol() >= actualSize - 1 ){
            System.out.println("Invalid Move Coordinates: " + c);
        }else{
            nodes[c.getRow()][c.getCol()].changeOwner(move.getPlayerId());
            updateNodes(c);
        }
    }

    /**
     * Checks every node on one side of the board to determine whether or not they have both currentA and currentB,
     * which indicates a win.
     * @param id
     * @return Whether or not the player with the id of id has won.
     */
    public boolean hasWon(int id){
        if(id == 2){
            for (int i = 1; i < actualSize - 1; i+=2) {
                if(nodes[0][i].hasCurrBoth()){
                    return true;
                }
            }
        }else if(id == 1){
            for (int i = 1; i < actualSize - 1; i+=2) {
                if(nodes[i][0].hasCurrBoth()){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Creates a deep copy of the board at its current state.
     * @return copy
     */
    public Node[][] carbonCopy(){
        Node[][] copy = new Node[nodes.length][nodes.length];
        for (int y=0; y < copy.length; y++){
            for (int x=0; x < copy.length; x++){
                copy[y][x] = nodes[y][x].clone();
            }
        }
        return copy;
    }


    public int getActualSize() {
        return actualSize;
    }

    public Node getNode(int x, int y){
        return nodes[x][y];
    }
    /** Method for Debugging Purposes**/
    public void displayBoard(){
        for (int i = 0; i < actualSize; i++) {
            for (int j = 0; j < actualSize; j++) {
                System.out.print(nodes[i][j]);
            }
            System.out.println();
        }
    }
}
