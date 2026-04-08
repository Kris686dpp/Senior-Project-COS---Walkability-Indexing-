import org.junit.Test;
import static org.junit.Assert.*;
import walkability.*;

import java.util.List;
import java.util.Map;

public class WalkabilityTest {

    // TESTING NODES AND EDGES

    @Test
    public void testAddSingleNode() {
        Graph graph = new Graph();
        Node node = new Node(1, 42.0, 23.0, NodeType.ROAD_NODE);
        graph.addNode(node);

        assertEquals("The graph should only have 1 node", 1, graph.getAllNodes().size());
        assertNotNull("You should be able to get a node from its id", graph.getNode(1));
    }

    @Test
    public void testAddMultipleNodes() {
        Graph graph = new Graph();
        graph.addNode(new Node(1, 42.0, 23.0, NodeType.ROAD_NODE));
        graph.addNode(new Node(2, 42.1, 23.1, NodeType.ROAD_NODE));
        graph.addNode(new Node(3, 42.2, 23.2, NodeType.ROAD_NODE));

        assertEquals("The Graph now should have three nodes", 3, graph.getAllNodes().size());
    }

    @Test
    public void testEdgeDistance() {
        Graph graph = new Graph();
        Node a = new Node(1, 42.0, 23.0, NodeType.ROAD_NODE);
        Node b = new Node(2, 42.1, 23.1, NodeType.ROAD_NODE);
        graph.addNode(a);
        graph.addNode(b);
        graph.addEdge(a, b, 100.0);

        Edge edge = graph.getEdges(a).get(0);
        assertEquals("Edge distance should be 100.0", 100.0, edge.getDistance(), 0.001);
    }

    @Test
    public void testNodeWithNoEdges() {
        Graph graph = new Graph();
        Node a = new Node(1, 42.0, 23.0, NodeType.TRANSIT);
        graph.addNode(a);

        assertEquals("Isolated nodes should not retrun an edge", 0, graph.getEdges(a).size());
    }

    @Test
    public void testGetAllEdges(){
        Graph graph = new Graph();
        Node a = new Node(1,42.0, 23.0, NodeType.ROAD_NODE);
        Node b = new Node(2,42.1, 23.1, NodeType.ROAD_NODE);
        Node c = new Node(3,42.2, 23.2, NodeType.ROAD_NODE);
        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addEdge(a, b, 100);
        graph.addEdge(a, c, 100);
        graph.addEdge(c, b, 100);
        List<Edge> allEdges = graph.getAllEdges();

        assertEquals("The graph should have 6 edges in total (3 edges * 2 reverse edges)", 6, allEdges.size());
    }

    // TESTING ALGORITHMS
    @Test
    public void testDijkstraDirectEdge() {
        Graph graph = new Graph();
        Node a = new Node(1, 42.0, 23.0,NodeType.ROAD_NODE);
        Node b = new Node(2, 42.1, 23.1,NodeType.ROAD_NODE);
        graph.addNode(a);
        graph.addNode(b);
        graph.addEdge(a, b, 6.7);

        Map<Node, Double> distances = Dijkstra.computeShortestPaths(graph,a);
        assertEquals("Direct edge sistance should be 6.7", 6.7, distances.get(b), 0.001);
    }

    @Test
    public void testDijkstraMulti() {
        Graph graph = new Graph();
        Node a = new Node(1, 42.0, 23.0, NodeType.ROAD_NODE);
        Node b = new Node(2, 42.1, 23.1, NodeType.ROAD_NODE);
        Node c = new Node(3, 42.2, 23.2, NodeType.ROAD_NODE);
        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addEdge(a, b, 10.0);
        graph.addEdge(b, c, 20.0);

        Map<Node, Double> distances = Dijkstra.computeShortestPaths(graph, a);
        assertEquals("a to c by b should be 30.0", 30, distances.get(c), 0.001);
    }

    @Test
    public void testFloydWarshallDirectEdge() {
        Graph graph = new Graph();
        Node a = new Node(1, 42.0, 23.0, NodeType.ROAD_NODE);
        Node b = new Node(2, 42.1, 23.1, NodeType.ROAD_NODE);
        graph.addNode(a);
        graph.addNode(b);
        graph.addEdge(a, b, 6.7);

        FloydWarshall.Result result = FloydWarshall.computeAllPairs(graph);
        double[][] dist = result.dist;
        assertEquals("The direct edge between a and b should be 6.7", 6.7, dist[0][1], 0.001);
        assertEquals("The reverse edge, b to a, should also be 6.7", 6.7, dist[1][0], 0.001);


    }

    @Test
    public void testFloydWarshallMulti() {
        Graph graph = new Graph();
        Node a = new Node(1, 0.0, 0.0, NodeType.ROAD_NODE);
        Node b = new Node(2, 0.0, 0.1, NodeType.ROAD_NODE);
        Node c = new Node(3, 0.0, 0.2, NodeType.ROAD_NODE);
        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addEdge(a, b, 10.0);
        graph.addEdge(b, c, 20.0);

        FloydWarshall.Result result = FloydWarshall.computeAllPairs(graph);
        double[][] dist = result.dist;
        assertEquals("A to C by B should be 30.0", 30.0, dist[0][2], 0.001);
        assertEquals("C to  A by B should be 30.0", 30.0, dist[2][0], 0.001);
    }

}
