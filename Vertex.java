package Players.LeafCountry;

/**
 * Created by robertstjacquesjr on 3/6/17.
 * implemented by Johnse Chance
 * Date:04/07/2017
 * RIT
 * Program Name: Vertex
 */
import java.util.ArrayList;
import java.util.List;

public class Vertex<T> {

    private T data;

    private List<Vertex<T>> neighbors;

    public Vertex(T data) {
        this.data = data;

        neighbors = new ArrayList<>();
    }

    public T getData() {
        return data;
    }

    public void addNeighbor(Vertex<T> neighbor) {
        neighbors.add(neighbor);
    }

    public List<Vertex<T>> getNeighbors() {
        return neighbors;
    }

}