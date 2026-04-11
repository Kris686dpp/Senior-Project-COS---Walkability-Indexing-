package walkability;

public class Utilities {
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
