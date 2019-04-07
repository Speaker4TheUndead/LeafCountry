package Players.LeafCountry;

import java.util.*;

/**
 * Created by robertstjacquesjr on 3/6/17.
 */

/**
 * Modified by Johnse Chance
 * Date:04/07/2017
 * RIT
 * Program Name: Graph
 * Description: This is a pieced together graph classes from two different examples from class.
 */
public class Graph <T> {
    private Map<T, Vertex<T>> graph;

    public Graph() {
        graph = new HashMap<>();
    }

    public void addVertex(T data) {
        Vertex<T> vertex = new Vertex<>(data);
        graph.put(data, vertex);
    }

    public void connect(T data1, T data2) {
        Vertex<T> vertex1 = graph.get(data1);
        Vertex<T> vertex2 = graph.get(data2);

        vertex1.addNeighbor(vertex2);
        vertex2.addNeighbor(vertex1);
    }

    public boolean pathExists(T start, T end) {
        Queue<Vertex<T>> vertices = new LinkedList<>();
        Set<Vertex<T>> visited = new HashSet<>();

        Vertex<T> starting = graph.get(start);
        Vertex<T> ending = graph.get(end);

        vertices.add(starting);
        visited.add(starting);

        while(vertices.isEmpty() == false) {
            Vertex<T> vertex = vertices.poll();

            if(vertex == ending) {
                return true;
            }
            else {
                for(Vertex<T> neighbor : vertex.getNeighbors()) {
                    if(visited.contains(neighbor) == false) {
                        visited.add(neighbor);
                        vertices.add(neighbor);
                    }
                }
            }
        }

        return false;
    }


    public void displayShortestPath(T start, T finish) {

        // assumes input check occurs previously
        Vertex startNode, finishNode;
        startNode = graph.get(start);
        finishNode = graph.get(finish);

        // create a distance map that will hold the shortest path distance
        // to each node from the given startNode.  We will just use the
        // maximum Integer value to represent infinity
        Map<Vertex, Integer> distance = new HashMap<>();

        // create a predecessor map that will be used to determine
        // the shortest path to each node from the given startNode.
        // If a node is not yet in the map, that is equivalent to the
        // node not having a predecessor, and not being reachable.
        Map<Vertex, Vertex> predecessors = new HashMap<>();

        djikstra(startNode, distance, predecessors);

        if(distance.get(finishNode) == Integer.MAX_VALUE) {
            System.out.println("No path from " + start + " to " + finish);
        }
        else {
            System.out.println("Minimum distance between " + start + " and " +
                    finish + " is " + String.valueOf(distance.get(finishNode)));

            List<Vertex> path = new LinkedList<Vertex>();
            Vertex n = finishNode;
            while (!n.equals(startNode)) {
                path.add(0, n);
                n = predecessors.get(n);
            }
            path.add(0, startNode);

            System.out.print("Shortest path: ");
            for(Vertex n1 : path) {
                System.out.print(n1 + " ");
            }
            System.out.println();
        }
    }

    public List getShortestpath(T start, T finish) {

        // assumes input check occurs previously
        Vertex startNode, finishNode;
        startNode = graph.get(start);
        finishNode = graph.get(finish);

        // create a distance map that will hold the shortest path distance
        // to each node from the given startNode.  We will just use the
        // maximum Integer value to represent infinity
        Map<Vertex, Integer> distance = new HashMap<>();

        // create a predecessor map that will be used to determine
        // the shortest path to each node from the given startNode.
        // If a node is not yet in the map, that is equivalent to the
        // node not having a predecessor, and not being reachable.
        Map<Vertex, Vertex> predecessors = new HashMap<>();

        djikstra(startNode, distance, predecessors);

        if(distance.get(finishNode) == Integer.MAX_VALUE) {
            System.out.println("No path from " + start + " to " + finish);
            return new LinkedList<Vertex>();
        }
        else {
            List<Vertex> path = new LinkedList<Vertex>();
            Vertex n = finishNode;
            while (!n.equals(startNode)) {
                path.add(0, n);
                n = predecessors.get(n);
            }
            path.add(0, startNode);

            return path;
        }
    }


    public void djikstra(Vertex start, Map<Vertex, Integer> distance, Map<Vertex,Vertex> predecessors){
        // initialize distances - we will use Integer.MAX_VALUE to
        // represent infinity
        for(T name : graph.keySet()) {
            distance.put(graph.get(name), Integer.MAX_VALUE);
        }
        distance.put(start,  0);

        // initialize predecessors - by not yet including any other nodes,
        // they are unvisited and have no predecessor.  Source node is
        // given predecessor of itself.
        predecessors.put(start,  start);

        // our priority queue will just be a list that we search to extract
        // the minimum from at each step (O(n))
        List<Vertex> priorityQ = new LinkedList<>();
        for (T name : graph.keySet()) {
            priorityQ.add(graph.get(name));
        }

        // main loop
        while (!priorityQ.isEmpty()) {
            Vertex U = dequeueMin(priorityQ, distance);

            // return if this node still has distance "infinity" -
            // remaining nodes are inaccessible
            if(distance.get(U) == Integer.MAX_VALUE) {
                return;
            }

            // this loop allows neighbors that have already been finalized
            // to be checked again, but they will never be updated and
            // this doesn't affect overall complexity
            for(Object e : U.getNeighbors()) {
                Vertex n = ((Vertex<T>)e);
                Integer w = (((Node)n.getData()) instanceof ProxyNode)? 1:0;
                // relaxation
                Integer distViaU = distance.get(U) + w;
                if(distance.get(n) > distViaU) {
                    distance.put(n,  distViaU);
                    predecessors.put(n,  U);
                }
            }
        }
    }

    /*
     * Basic implementation of a priority queue that searches for the minimum.
     */
    private Vertex dequeueMin(List<Vertex> priorityQ, Map<Vertex, Integer> distance) {

        Vertex minNode = priorityQ.get(0);  // start off with first one
        for (Vertex n : priorityQ) { // checks first one again...
            if(distance.get(n) < distance.get(minNode)) {
                minNode = n;
            }
        }
        return priorityQ.remove(priorityQ.indexOf(minNode));
    }
}
