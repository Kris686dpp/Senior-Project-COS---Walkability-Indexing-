package walkability;

import javax.xml.stream.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

public class OSMParser {
    public Graph parse(String filePath) throws Exception {

        Graph graph = new Graph();
        Map<Long, Node> allNodes = new HashMap<>(); // Temporarily stores all nodes by OSM id
        Set<Long> roadNodes = new HashSet<>();

        XMLInputFactory factory = XMLInputFactory.newInstance();
        InputStream input = new FileInputStream(filePath);
        XMLStreamReader reader = factory.createXMLStreamReader(input);

        // wayNodeRefs will store the "ways" from the OSM file i.e. sequences of nodes
        List<Long> wayNodeRefs = null;

        // Temporary variables for parsing nodes
        long currentNodeId = -1;
        double currentLat = 0.0;
        double currentLon = 0.0;
        boolean insideNode = false;
        Map<String, String> currentNodeTags = new HashMap<>();

        /* Will track if the current way is a road as ways can be other structures
        too, such as buildings or parks */
        boolean isRoad = false;

        while (reader.hasNext()) {
            int event = reader.next();

            // Checks if the reader is at the start of an xml tag
            if (event == XMLStreamConstants.START_ELEMENT) {
                String elementName = reader.getLocalName();

                // PARSING NODES - collect id, lat, lon but wait for tags before creating node
                if (elementName.equals("node")) {
                    currentNodeId = Long.parseLong(reader.getAttributeValue(null, "id"));
                    currentLat = Double.parseDouble(reader.getAttributeValue(null, "lat"));
                    currentLon = Double.parseDouble(reader.getAttributeValue(null, "lon"));
                    insideNode = true;
                    currentNodeTags.clear();
                }

                // PARSING WAYS
                else if (elementName.equals("way")) {
                    insideNode = false;
                    wayNodeRefs = new ArrayList<>();
                    isRoad = false;
                }

                // PARSING NODE REFERENCES
                else if (elementName.equals("nd") && wayNodeRefs != null) {
                    long ref = Long.parseLong(reader.getAttributeValue(null, "ref"));
                    wayNodeRefs.add(ref);
                }

                // PARSING TAGS - handles both node tags and way tags
                else if (elementName.equals("tag")) {
                    String k = reader.getAttributeValue(null, "k");
                    String v = reader.getAttributeValue(null, "v");

                    if (insideNode) {
                        // Collect tags for the current node to determine its type later
                        currentNodeTags.put(k, v);
                    } else if (wayNodeRefs != null) {
                        // Way tag - check if it is a road
                        if ("highway".equals(k)) {
                            isRoad = true;
                        }
                    }
                }
            }

            // Checks if it's the end of an element
            else if (event == XMLStreamConstants.END_ELEMENT) {
                String elementName = reader.getLocalName();

                // When node element closes we now know all its tags so we can create it
                if (elementName.equals("node") && insideNode) {
                    NodeType type = determineNodeType(currentNodeTags);
                    Node node = new Node(currentNodeId, currentLat, currentLon, type);
                    allNodes.put(currentNodeId, node);
                    insideNode = false;
                }

                // When way element closes
                else if (elementName.equals("way") && wayNodeRefs != null) {
                    if (isRoad) {
                        // Mark all nodes in this road way as road nodes
                        for (Long ref : wayNodeRefs) {
                            roadNodes.add(ref);
                        }

                        // Add edges between consecutive nodes
                        for (int i = 0; i < wayNodeRefs.size() - 1; i++) {
                            Node from = allNodes.get(wayNodeRefs.get(i));
                            Node to = allNodes.get(wayNodeRefs.get(i + 1));

                            if (from != null && to != null) {
                                double distance = Utilities.haversine(
                                        from.getLatitude(), from.getLongitude(),
                                        to.getLatitude(), to.getLongitude()
                                );
                                graph.addEdge(from, to, distance);
                            }
                        }
                    }

                    // Reset only after the whole way is finished
                    wayNodeRefs = null;
                }
            }
        }

        reader.close();
        input.close();

        // Adding the road nodes
        for (Long id : roadNodes) {
            Node node = allNodes.get(id);
            if (node != null) {
                graph.addNode(node);
            }
        }

        // Adding the amenity nodes
        for (Node node : allNodes.values()) {
            if (node.getType() != NodeType.ROAD_NODE) {
                graph.addNode(node);
            }
        }
        // Connect each amenity node to its nearest road node
        // Looping though all the nodes
        for (Node amenity : allNodes.values()) {
            //And checking for only amenity nodes
            if (amenity.getType() == NodeType.ROAD_NODE) continue;

            Node nearest = null;
            double minDist = Double.POSITIVE_INFINITY;

            // Looping through every road node
            for (Long id : roadNodes) {
                Node roadNode = allNodes.get(id);

                // Calculating the straight line distance between the amenity and node
                double amenityDist = Utilities.haversine(
                        amenity.getLatitude(), amenity.getLongitude(),
                        roadNode.getLatitude(), roadNode.getLongitude()
                );
                // If it is smaller than the minimum distance then it gets added as the nearest node
                if (amenityDist < minDist) {
                    minDist = amenityDist;
                    nearest = roadNode;
                }
            }

            // Adding the edge
            if (nearest != null) {
                graph.addEdge(amenity, nearest, minDist);
            }
        }

        return graph;
    }

    /* determineNodeType reads the tags collected from a node element
    and returns the appropriate NodeType based on OSM tag values */
    private NodeType determineNodeType(Map<String, String> tags) {
        String amenity = tags.get("amenity");
        String highway = tags.get("highway");
        String railway = tags.get("railway");
        String shop    = tags.get("shop");
        String leisure = tags.get("leisure");
        String landuse = tags.get("landuse");

        // Checking TRANSIT
        if ("bus_stop".equals(amenity) ||
                "bus_stop".equals(highway) ||
                "ferry_terminal".equals(amenity) ||
                "station".equals(railway) ||
                "tram_stop".equals(railway)) {
            return NodeType.TRANSIT;
        }

        // Checking SHOP
        if ("marketplace".equals(amenity) ||
                "restaurant".equals(amenity) ||
                "cafe".equals(amenity) ||
                "fast_food".equals(amenity) ||
                "pharmacy".equals(amenity) ||
                "bank".equals(amenity) ||
                "supermarket".equals(shop) ||
                "convenience".equals(shop)) {
            return NodeType.SHOP;
        }

        // Checking PARK
        if ("park".equals(leisure) ||
                "playground".equals(leisure) ||
                "garden".equals(leisure) ||
                "park".equals(amenity) ||
                "recreation_ground".equals(landuse)) {
            return NodeType.PARK;
        }

        return NodeType.ROAD_NODE;
    }
}