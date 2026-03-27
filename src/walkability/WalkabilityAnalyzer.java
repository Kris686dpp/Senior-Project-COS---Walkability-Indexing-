package walkability;

import java.util.*;

public class WalkabilityAnalyzer {

    // 15 minute walking distance, calculated as 15 mins * 80 m/min
    private static final double MAX_WALKING_DISTANCE = 1200.0;

    private static final double WEIGHT_TRANSIT = 0.3;
    private static final double WEIGHT_SHOP = 0.5;
    private static final double WEIGHT_PARK = 0.2;

    private static final double WEIGHT_AMENITY = 0.7;
    private static final double WEIGHT_CONNECTIVITY = 0.3;

    // Separates the road and amenity nodes from each other


    private static double computeAmenityScore(Graph graph, boolean useFloydWarshall) {
        List<Node> roadNodes = new ArrayList<>();
        List<Node> amenityNodes = new ArrayList<>();

        for (Node node : graph.getAllNodes()) {
            if (node.getType() == NodeType.ROAD_NODE) {
                roadNodes.add(node);
            } else {
                amenityNodes.add(node);
            }
        }

        double totalScore = 0.0;

        if (useFloydWarshall) {
            List<Node> allNodes = new ArrayList<>(graph.getAllNodes());
            Map<Node, Integer> indexMap = new HashMap<>();
            for (int i = 0; i < allNodes.size(); i++) {
                indexMap.put(allNodes.get(i), i);
            }
        }

    }





}
