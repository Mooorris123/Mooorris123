import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.Point2D;
import java.io.FileNotFoundException;
import java.io.FileReader;
import edu.princeton.cs.algs4.ResizingArrayStack;
import com.google.gson.*;

import java.util.Comparator;

class ObservationStationAnalysis {

    private ArrayList<Point2D> inputArray;
    private Point2D[] sites;
    private Point2D[] convexhull;
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
        }
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
        int n = polygon.size();
        Point2D[] Polygon = new Point2D[n];
        while (!polygon.isEmpty()) {
            Polygon[n - 1] = polygon.pop();
            n--;
        }
        // find the convex hull sites

        state = 1;
        return Polygon;
    }

    public Point2D[] findFarthestStations() {
        if (state == 0) {
            convexhull = findConvexHull(sites);
        }
        int mid = convexhull.length / 2;
        double dis = 0;
        Point2D[] farthest = new Point2D[2]; // Example
        for (int i = 0, j = mid; i < mid + 1 && j < convexhull.length;) {
            if (convexhull[i].distanceSquaredTo(convexhull[j]) > dis) {
                dis = convexhull[i].distanceSquaredTo(convexhull[j]);
                farthest[0] = convexhull[i];
                farthest[1] = convexhull[j];
            }
            if (j == convexhull.length - 1) {
                if ((convexhull[i + 1].x() - convexhull[i].x()) * (convexhull[0].y() - convexhull[j].y())
                        - (convexhull[i + 1].y() - convexhull[i].y())
                                * (convexhull[0].x() - convexhull[j].x()) < 0) {
                    i++;
                } else if ((convexhull[i + 1].x() - convexhull[i].x()) * (convexhull[0].y() - convexhull[j].y())
                        - (convexhull[i + 1].y() - convexhull[i].y())
                                * (convexhull[0].x() - convexhull[j].x()) == 0) {
                    i++;
                    j++;
                } else
                    j++;
            } else {
                if ((convexhull[i + 1].x() - convexhull[i].x()) * (convexhull[j + 1].y() - convexhull[j].y())
                        - (convexhull[i + 1].y() - convexhull[i].y())
                                * (convexhull[j + 1].x() - convexhull[j].x()) < 0) {
                    i++;
                } else if ((convexhull[i + 1].x() - convexhull[i].x()) * (convexhull[j + 1].y() - convexhull[j].y())
                        - (convexhull[i + 1].y() - convexhull[i].y())
                                * (convexhull[j + 1].x() - convexhull[j].x()) == 0) {
                    i++;
                    j++;
                } else
                    j++;
            }
        }
        // Arrays.sort(farthest, Point2D.R_ORDER);
        // // find the farthest two stations
        // Point2D origin = new Point2D(0, 0);
        // if (farthest[0].distanceSquaredTo(origin) == farthest[1].distanceSquaredTo(origin)
        //         && farthest[0].y() > farthest[1].y()) {
        //     Point2D temp = farthest[0];
        //     farthest[0] = farthest[1];
        //     farthest[1] = temp;
        // }
        if (Double.compare(farthest[0].distanceSquaredTo(new Point2D(0, 0)), farthest[1].distanceSquaredTo(new Point2D(0, 0))) == 0) {
            if (farthest[0].y() > farthest[1].y()) {
                // Swap if A's y-coordinate is greater than B's
                Point2D temp = farthest[0];
                farthest[0] = farthest[1];
                farthest[1] = temp;
            }
        } else {
            // Sort based on the distance to (0, 0) if they are not equal
            if (farthest[0].distanceSquaredTo(new Point2D(0, 0)) > farthest[1].distanceSquaredTo(new Point2D(0, 0))) {
                // Swap if A is farther from the origin than B
                Point2D temp = farthest[0];
                farthest[0] = farthest[1];
                farthest[1] = temp;
            }
        }
        state = 1;
        return farthest; // it should be sorted (ascendingly) by polar radius; please sort (ascendingly)
                         // by y coordinate if there are ties in polar radius.
    }

    public double coverageArea() {
        double area = 0.0;
        // calculate the area surrounded by the existing stations
        for (int i = 0; i < convexhull.length; i++) {
            if (i == convexhull.length - 1) {
                area += convexhull[i].x() * convexhull[0].y() - convexhull[i].y() *
                        convexhull[0].x();
            } else
                area += convexhull[i].x() * convexhull[i + 1].y() - convexhull[i].y() *
                        convexhull[i + 1].x();
        }
        return Math.abs(area * 0.5);
    }

    public void addNewStation(Point2D newStation) {
        Point2D[] add;
        if (state == 1) {
            add = Arrays.copyOf(convexhull, convexhull.length + 1);
            add[convexhull.length] = newStation;
        } else {
            add = Arrays.copyOf(sites, sites.length + 1);
            add[sites.length] = newStation;
        }
        sites = add;
        state = 0;
    }

    public static void main(String[] args) throws Exception {

        ArrayList<Point2D> stationCoordinates = new ArrayList<>();
        stationCoordinates.add(new Point2D(0, 0));
        stationCoordinates.add(new Point2D(5, 0));
        stationCoordinates.add(new Point2D(0, 5));
        stationCoordinates.add(new Point2D(5, 5));
        stationCoordinates.add(new Point2D(0, 4));
        stationCoordinates.add(new Point2D(1, 1));
        stationCoordinates.add(new Point2D(2, 2));

        ObservationStationAnalysis Analysis = new ObservationStationAnalysis(stationCoordinates);
        System.out.println("Farthest Station A: " + Analysis.findFarthestStations()[0]);
        System.out.println("Farthest Station B: " + Analysis.findFarthestStations()[1]);
        System.out.println("Coverage Area: " + Analysis.coverageArea());

        System.out.println("Add Station (6, 7): ");
        Analysis.addNewStation(new Point2D(6, 7));

        System.out.println("Add Station (10, 0): ");
        Analysis.addNewStation(new Point2D(10, 0));

        System.out.println("Add Station (3, 3): ");
        Analysis.addNewStation(new Point2D(3, 3));

        System.out.println("Farthest Station A: " + Analysis.findFarthestStations()[0]);
        System.out.println("Farthest Station B: " + Analysis.findFarthestStations()[1]);
        System.out.println("Coverage Area: " + Analysis.coverageArea());
    }
}

class OutputFormat {
    ArrayList<Point2D> stations;
    ObservationStationAnalysis OSA;
    Point2D[] farthest;
    double area;
    Point2D[] farthestNew;
    double areaNew;
    ArrayList<Point2D> newStations;
}

class TestCase {
    int Case;
    int score;
    ArrayList<OutputFormat> data;
}

class test_ObservationStationAnalysis {
    public static void main(String[] args) {
        Gson gson = new Gson();
        int num_ac = 0;
        int i = 1;

        try {
            // TestCase[] testCases = gson.fromJson(new FileReader(args[0]),
            // TestCase[].class);
            TestCase[] testCases = gson.fromJson(new FileReader(args[0]), TestCase[].class);
            for (TestCase testCase : testCases) {
                System.out.println("Sample" + i + ": ");
                i++;
                for (OutputFormat data : testCase.data) {
                    ObservationStationAnalysis OSA = new ObservationStationAnalysis(data.stations);
                    Point2D[] farthest;
                    double area;
                    Point2D[] farthestNew;
                    double areaNew;

                    farthest = OSA.findFarthestStations();
                    area = OSA.coverageArea();

                    if (data.newStations != null) {
                        for (Point2D newStation : data.newStations) {
                            OSA.addNewStation(newStation);
                        }
                        farthestNew = OSA.findFarthestStations();

                        areaNew = OSA.coverageArea();
                    } else {
                        farthestNew = farthest;
                        areaNew = area;
                    }

                    if (farthest[0].equals(data.farthest[0]) && farthest[1].equals(data.farthest[1])
                            && Math.abs(area - data.area) < 0.0001
                            && farthestNew[0].equals(data.farthestNew[0]) && farthestNew[1].equals(data.farthestNew[1])
                            && Math.abs(areaNew - data.areaNew) < 0.0001) {
                        System.out.println("AC");
                        num_ac++;
                    } else {
                        System.out.println("WA");
                        System.out.println("Ans-farthest: " + Arrays.toString(data.farthest));
                        System.out.println("Your-farthest:  " + Arrays.toString(farthest));
                        System.out.println("Ans-area:  " + data.area);
                        System.out.println("Your-area:  " + area);

                        System.out.println("Ans-farthestNew: " + Arrays.toString(data.farthestNew));
                        System.out.println("Your-farthestNew:  " + Arrays.toString(farthestNew));
                        System.out.println("Ans-areaNew:  " + data.areaNew);
                        System.out.println("Your-areaNew:  " + areaNew);
                        System.out.println("");
                    }
                }
                System.out.println("Score: " + num_ac + "/ 8");
            }

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}