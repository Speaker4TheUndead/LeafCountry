package Players.LeafCountry;

import Interface.Coordinate;
import Interface.PlayerMove;

/**
 * Johnse Chance
 * Date:04/08/2017
 * RIT
 * Program Name: ProxyBoard
 * Description: This class is meant to be used for determining the fewestSegmentsToVictory.
 */
public class ProxyBoard extends Board {
    /** Constructor calls the copy constructor of the Board Class**/
    public ProxyBoard(Board b){
        super(b);
    }
    /** Method for replacing Nodes with Proxy Nodes**/
    public void processMove(PlayerMove move, int id){
        Coordinate c = move.getCoordinate();
        //Make Sure the move is valid
        if((c.getRow() + c.getCol()) % 2 != 0 || c.getRow() <= 0 || c.getRow() >= actualSize - 1
                || c.getCol() <= 0 || c.getCol() >= actualSize - 1 ){
            System.out.println("Invalid Move Coordinates: " + c);
        }else{
            nodes[c.getRow()][c.getCol()] = new ProxyNode(id);
            nodes[c.getRow()][c.getCol()].setLocation(c.getRow(), c.getCol());
        }
    }

    /** Return all the nodes of the board.**/
    public Node[][] getNodes(){
        return nodes;
    }
}
