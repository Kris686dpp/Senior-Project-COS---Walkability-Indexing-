import org.junit.Test;
import static org.junit.Assert.*;
import walkability.*;
import java.util.Map;

public class WalkabilityTest {

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

        double[][] dist = FloydWarshall.computeAllPairs(graph);
        assertEquals("A to C by B should be 30.0", 30.0, dist[0][2], 0.001);
        assertEquals("C to  A by B should be 30.0", 30.0, dist[2][0], 0.001);
    }

}
