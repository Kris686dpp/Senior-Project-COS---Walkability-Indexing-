package walkability;

// To read XML files
import javax.xml.stream.*;

// To read files from a disc
import java.io.FileInputStream;
import java.io.InputStream;

import java.util.*;

public class OSMParser {
    // Main parse method that will take osm file as input and output a Graph
    public Graph parse(String filePath) throws Exception {

        Graph graph = new Graph(); // The final graph that is going to be returned

        Map<Long, Node> osmNodes = new HashMap<>(); // Temporarily stores nodes by OSM id

        XMLInputFactory factory = XMLInputFactory.newInstance();
        InputStream input = new FileInputStream(filePath);
        XMLStreamReader reader = factory.createXMLStreamReader(input);

        // wayNodeRefs will store the "ways" from the OSM file i.e. sequences of nodes
        List<Long> wayNodeRefs = null;

        /* Will track if the current way is a road as ways can be other structures
        too, such as buildings or parks */
        boolean isRoad = false;

        while (reader.hasNext()) {
            int event = reader.next();

            // Checks if the reader is at the start of an xml tag
            if (event == XMLStreamConstants.START_ELEMENT) {
                // Gets the name of the element
                String elementName = reader.getLocalName();

                // PARSING NODES
                if (elementName.equals("node")) {
                    // Parsing the tag values to the variables of a Node
                    long id = Long.parseLong(reader.getAttributeValue(null, "id"));
                    double latitude = Double.parseDouble(reader.getAttributeValue(null, "lat"));
                    double longitude = Double.parseDouble(reader.getAttributeValue(null, "lon"));

                    Node node = new Node(id, latitude, longitude, NodeType.ROAD_NODE);
                    osmNodes.put(id, node);
                    graph.addNode(node);
                }

                // PARSING WAYS
                else if (elementName.equals("way")) {
                    wayNodeRefs = new ArrayList<>();
                    // isRoad is false until proven otherwise
                    isRoad = false;
                }

                // PARSING NODE REFERENCES
                else if (elementName.equals("nd") && wayNodeRefs != null) {
                    long ref = Long.parseLong(reader.getAttributeValue(null, "ref"));
                    wayNodeRefs.add(ref);
                }

                // PARSING TAGS
                else if (elementName.equals("tag") && wayNodeRefs != null) {
                    String k = reader.getAttributeValue(null, "k");
                    String v = reader.getAttributeValue(null, "v");

                    if ("highway".equals(k)) {
                        isRoad = true;
                    }
                }
            }

            // Checks if it's the end of an element
            else if (event == XMLStreamConstants.END_ELEMENT) {
                String elementName = reader.getLocalName();

                // when the parser reaches </way> it knows that the current way is complete
                if (elementName.equals("way") && wayNodeRefs != null) {
                    // if the way is a road it connects the consecutive nodes
                    if (isRoad) {
                        for (int i = 0; i < wayNodeRefs.size() - 1; i++) {
                            Node from = osmNodes.get(wayNodeRefs.get(i));
                            Node to = osmNodes.get(wayNodeRefs.get(i + 1));

                            if (from != null && to != null) {
                                double distance = haversine(
                                        from.getLatitude(), from.getLongitude(),
                                        to.getLatitude(), to.getLongitude()
                                );
                                graph.addEdge(from, to, distance);
                            }
                        }
                    }

                    // reset only after the whole way is finished
                    wayNodeRefs = null;
                }
            }
        }

        reader.close();
        input.close();

        return graph;
    }

    /* The haversine function calculates the distance between two Nodes
    given only the longitude and latitude and gives back
    the result in meters. It is done using the Haversine Formula */
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000; // radius of Earth in meters

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // Main Haversine formula
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}