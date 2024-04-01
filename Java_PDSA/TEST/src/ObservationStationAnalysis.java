import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.ResizingArrayStack;

class ObservationStationAnalysis {
    private ArrayList<Point2D> inputArray;
    private int n;
    private Point2D[] forpolarorder;
    private double area;
    private Point2D head;

    public ObservationStationAnalysis(ArrayList<Point2D> stations) {
        // you can do something in Constructor or not
        inputArray = stations;
        // sorting();
    }

    public void sorting() {
        n = inputArray.size();
        forpolarorder = new Point2D[n];
        int startp = 0;
        for (int i = 0; i < n; i++) {
            forpolarorder[i] = inputArray.get(i);
            if (forpolarorder[startp].y() > forpolarorder[i].y()) {
                startp = i;
            } else if (forpolarorder[startp].y() == forpolarorder[i].y()) {
                if (forpolarorder[startp].x() > forpolarorder[i].x()) {
                    startp = i;
                }
            }
        }
        Point2D first = new Point2D(forpolarorder[startp].x() - 0.01, forpolarorder[startp].y());
        head = forpolarorder[startp];
        Arrays.sort(forpolarorder, first.polarOrder());
    }

    public Point2D[] findFarthestStations() {
        sorting();
        Point2D[] farthest1 = new Point2D[2]; // Example
        ResizingArrayStack<Integer> stack = new ResizingArrayStack<Integer>();
        int k;
        stack.push(0);
        stack.push(1);

        for (int i = 2; i < n; i++) {
            int temp = stack.pop();
            if (!stack.isEmpty()) {
                // System.out.println(i);
                k = Point2D.ccw(forpolarorder[stack.peek()], forpolarorder[temp], forpolarorder[i]);
                while (k == -1) {
                    temp = stack.pop();
                    if (stack.isEmpty()) {
                        break;
                    } else
                        k = Point2D.ccw(forpolarorder[stack.peek()], forpolarorder[temp], forpolarorder[i]);
                }
            }
            stack.push(temp);
            stack.push(i);
        }
        int m = stack.size();
        int mid = m / 2;// low:0~mid-1 ; up:mid~stack.size()-1
        Point2D[] polygon = new Point2D[m];

        if (forpolarorder[0] != head) {
            polygon = new Point2D[m + 1];
            polygon[0] = head;
        }
        int c = polygon.length;
        area = 0.0;
        int lastlace = stack.peek();
        while (!stack.isEmpty()) {
            int counting = stack.pop();
            polygon[c - 1] = forpolarorder[counting];
            c--;
            if (forpolarorder[0] != head && stack.isEmpty()) {
            }
            if (stack.isEmpty()) {
                if (forpolarorder[0] != head) {
                    area += forpolarorder[counting].y() * polygon[0].x()
                            - forpolarorder[counting].x() * polygon[0].y()

                            + polygon[0].y() * forpolarorder[lastlace].x()
                            - polygon[0].x() * forpolarorder[lastlace].y();
                } else {
                    area += forpolarorder[counting].y() * forpolarorder[lastlace].x()
                            - forpolarorder[counting].x() * forpolarorder[lastlace].y();
                }
            } else {
                area += forpolarorder[counting].y() * forpolarorder[stack.peek()].x()
                        - forpolarorder[counting].x() * forpolarorder[stack.peek()].y();
            }
        }
        mid = polygon.length / 2;
        double a = 0;
        for (int i = 0, j = mid; i < mid + 2 && j < polygon.length;) {
            if (i != j + 1 && i != j - 1) {
                // System.out.println("OK");
                if (polygon[i].distanceTo(polygon[j]) > a) {
                    a = polygon[i].distanceTo(polygon[j]);
                    farthest1[0] = polygon[i];
                    farthest1[1] = polygon[j];
                } else {
                    farthest1[0] = farthest1[0];
                    farthest1[1] = farthest1[1];
                }
            }
            if (j != polygon.length - 1) {
                if ((polygon[i + 1].x() - polygon[i].x())
                        * (polygon[j + 1].y() - polygon[j].y())
                        - (polygon[i + 1].y() - polygon[i].y())
                                * (polygon[j + 1].x() - polygon[j].x()) < 0)
                    i++;
                else
                    j++;
            } else {
                if ((polygon[i + 1].x() - polygon[i].x())
                        * (polygon[0].y() - polygon[j].y())
                        - (polygon[i + 1].y() - polygon[i].y())
                                * (polygon[0].x() - polygon[j].x()) < 0)
                    i++;
                else
                    j++;
            }
        }

        // Rorder
        // find the farthest two stations
        Arrays.sort(farthest1, Point2D.R_ORDER);
        // System.out.println(farthest[1]);
        return farthest1; // it should be sorted (ascendingly) by polar radius; please sort (ascendingly)
                          // by y coordinate if there are ties in polar radius.
    }

    public double coverageArea() {
        area = Math.abs(area) * 0.5;
        return area;
    }

    public void addNewStation(Point2D newStation) {

        inputArray.add(newStation);

        // sorting();
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