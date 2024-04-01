import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.ResizingArrayStack;

class ObservationStationAnalysis {

    private ArrayList<Point2D> inputArray;
    private Point2D[] sites;
    private Point2D[] convex;
    private Point2D[] farthest1 = new Point2D[2];
    private int state = 0;

    public ObservationStationAnalysis(ArrayList<Point2D> stations) {
        // you can do something in Constructor or not
        inputArray = stations;
        sites = new Point2D[inputArray.size()];
        inputArray.toArray(sites);
    }

    private Point2D[] findConvexHull(Point2D[] points) {
        Point2D rightest = points[0];
        for (int i = 1; i < points.length; i++) {
            if (points[i].y() < rightest.y()) {
                rightest = points[i];
            } else if (points[i].y() == rightest.y() && points[i].x() > rightest.x()) {
                rightest = points[i];
            }
        } // find the starting p
        Point2D startp = new Point2D(rightest.x() - 0.000001, rightest.y());
        Arrays.sort(points, startp.polarOrder());
        // sort the inputs to polarorder
        ResizingArrayStack<Point2D> polygon = new ResizingArrayStack<>();
        polygon.push(points[0]);
        polygon.push(points[1]);
        for (int i = 2; i < points.length; i++) {
            Point2D angle = polygon.pop();
            if (!polygon.isEmpty()) {
                while (Point2D.ccw(polygon.peek(), angle, points[i]) != 1) {
                    angle = polygon.pop();
                    if (polygon.isEmpty()) {
                        break;
                    }
                }
            }
            polygon.push(angle);
            polygon.push(points[i]);
        }
        // find the convex hull sites
        int n = polygon.size();
        Point2D[] Polygon = new Point2D[n];
        while (!polygon.isEmpty()) {
            Polygon[n - 1] = polygon.pop();
            n--;
        }
        // store them to an array
        state = 1;
        return Polygon;
    }

    private Point2D[] findfar(Point2D[] convexhull) {

        double dis = 0;
        Point2D[] farthest = new Point2D[2];
        for (int i = 0; i < convexhull.length; i++) {
            for (int j = i + 1; j < convexhull.length; j++) {
                if (convexhull[i].distanceSquaredTo(convexhull[j]) > dis) {
                    dis = convexhull[i].distanceSquaredTo(convexhull[j]);
                    farthest[0] = convexhull[i];
                    farthest[1] = convexhull[j];
                }
            }
        }
        // int mid = convexhull.length / 2;
        // for (int i = 0, j = mid; i < mid + 1 && j < convexhull.length;) {
        // if (convexhull[i].distanceSquaredTo(convexhull[j]) > dis) {
        // dis = convexhull[i].distanceSquaredTo(convexhull[j]);
        // farthest[0] = convexhull[i];
        // farthest[1] = convexhull[j];
        // }
        // if (j == convexhull.length - 1) {
        // if ((convexhull[i + 1].x() - convexhull[i].x()) * (convexhull[0].y() -
        // convexhull[j].y())
        // - (convexhull[i + 1].y() - convexhull[i].y())
        // * (convexhull[0].x() - convexhull[j].x()) < 0) {
        // i++;
        // } else if ((convexhull[i + 1].x() - convexhull[i].x()) * (convexhull[0].y() -
        // convexhull[j].y())
        // - (convexhull[i + 1].y() - convexhull[i].y())
        // * (convexhull[0].x() - convexhull[j].x()) == 0) {
        // i++;
        // j++;
        // } else
        // j++;
        // } else {
        // if ((convexhull[i + 1].x() - convexhull[i].x()) * (convexhull[j + 1].y() -
        // convexhull[j].y())
        // - (convexhull[i + 1].y() - convexhull[i].y())
        // * (convexhull[j + 1].x() - convexhull[j].x()) < 0) {
        // i++;
        // } else if ((convexhull[i + 1].x() - convexhull[i].x()) * (convexhull[j +
        // 1].y() - convexhull[j].y())
        // - (convexhull[i + 1].y() - convexhull[i].y())
        // * (convexhull[j + 1].x() - convexhull[j].x()) == 0) {
        // i++;
        // j++;
        // } else
        // j++;
        // } // use rotating cailper to count farthest pair
        // }

        Arrays.sort(farthest, Point2D.R_ORDER);
        // find the farthest two stations
        Point2D origin = new Point2D(0, 0);
        if (farthest[0].distanceSquaredTo(origin) == farthest[1].distanceSquaredTo(origin)
                && farthest[0].y() > farthest[1].y()) {
            Point2D temp = farthest[0];
            farthest[0] = farthest[1];
            farthest[1] = temp;
        }
        return farthest;
    }

    public Point2D[] findFarthestStations() {
        if (state == 0) {
            convex = findConvexHull(sites);
            farthest1 = findfar(convex);
        }
        state = 1;
        return farthest1; // it should be sorted (ascendingly) by polar radius; please sort (ascendingly)
                          // by y coordinate if there are ties in polar radius.
    }

    public double coverageArea() {
        double area = 0.0;
        // calculate the area surrounded by the existing stations
        for (int i = 0; i < convex.length; i++) {
            if (i == convex.length - 1) {
                area += convex[i].x() * convex[0].y() - convex[i].y() *
                        convex[0].x();
            } else
                area += convex[i].x() * convex[i + 1].y() - convex[i].y() *
                        convex[i + 1].x();
        }
        // use shoelace thereom
        return Math.abs(area * 0.5);
    }

    public void addNewStation(Point2D newStation) {
        Point2D[] add;
        if (state == 1) {
            add = Arrays.copyOf(convex, convex.length + 1);// for first added station
            add[convex.length] = newStation;
        } else {
            add = Arrays.copyOf(sites, sites.length + 1);// for the other added stations
            add[sites.length] = newStation;
        }
        sites = add;
        state = 0;
    }

    public static void main(String[] args) throws Exception {

        ArrayList<Point2D> stationCoordinates = new ArrayList<>();
        stationCoordinates.add(new Point2D(0, 0));
        stationCoordinates.add(new Point2D(0, 1));
        stationCoordinates.add(new Point2D(0, 5));
        stationCoordinates.add(new Point2D(0, 5));
        stationCoordinates.add(new Point2D(0, 4));
        stationCoordinates.add(new Point2D(0, 3));
        stationCoordinates.add(new Point2D(1, 2));

        ObservationStationAnalysis Analysis = new ObservationStationAnalysis(stationCoordinates);
        System.out.println("Farthest Station A: " + Analysis.findFarthestStations()[0]);
        System.out.println("Farthest Station B: " + Analysis.findFarthestStations()[1]);
        System.out.println("Coverage Area: " + Analysis.coverageArea());

        System.out.println("Add Station (6, 7): ");
        Analysis.addNewStation(new Point2D(0, 7));

        System.out.println("Add Station (10, 0): ");
        Analysis.addNewStation(new Point2D(0, 10));

        System.out.println("Add Station (3, 3): ");
        Analysis.addNewStation(new Point2D(0, 3));

        System.out.println("Farthest Station A: " +
                Analysis.findFarthestStations()[0]);
        System.out.println("Farthest Station B: " +
                Analysis.findFarthestStations()[1]);
        System.out.println("Coverage Area: " + Analysis.coverageArea());
    }
}