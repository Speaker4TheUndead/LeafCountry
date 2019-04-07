package Players.LeafCountry;

/**
 * @author: Johnse Chance
 * @date: 03/19/2017
 */


/**
 * Node class used in keeping track of the board.
 */
public class Node {
    /** The id of the owner of this node. 0 = no one, 1 = Player 1, 2 = Player 2**/
    private int id;
    /** Boolean value expressing whether or not the node has currentA flowing through it. **/
    private boolean currentA;
    /** Boolean value expressing whether or not the node has currentB flowing through it. **/
    private boolean currentB;
    /** Lets us Know if the Node is a Root Node **/
    private boolean isRoot = false;
    private String location;
    private int[] xy;
    /** Constructor to be used with root nodes only. i.e. node on the sides of the board**/
    public Node(int id, boolean currentA, boolean currentB){
        this.id = id;
        this.currentA = currentA;
        this.currentB = currentB;
        this.isRoot = true;
    }

    /** Constructor for middle nodes. **/
    public Node(int id){
        this(id, false, false);
    }

    public void setLocation(int x, int y){
        location = "(" + x + ", " + y + ")";
        xy = new int[]{x, y};
    }
    //Getters
    public int getId() {
        return id;
    }

    public boolean hasCurrentA() {
        return currentA;
    }

    public boolean hasCurrentB() {
        return currentB;
    }

    /** Returns whether or not a node has both currents. **/
    public boolean hasCurrBoth() {
        return currentA && currentB;
    }

    /** Gives the current node currentA **/
    public void powerA(){
        this.currentA = true;
    }

    /** Gives the current node currentB **/
    public void powerB(){
        this.currentB = true;
    }

    /** Changes the id of the node to
     * @param id
     */
    public void changeOwner(int id){
        this.id = id;
    }

    /** Getter Method for isRoot Variable **/
    public boolean isRoot(){
        return isRoot;
    }

    @Override
    public String toString(){
        return location + " ";
    }

    @Override
    public Node clone(){
        return new Node(this.getId(), this.currentA, this.currentB);
    }

    public int[] getLocation(){
        return xy;
    }

}
