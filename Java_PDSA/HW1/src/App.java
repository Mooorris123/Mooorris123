import edu.princeton.cs.algs4.UF;

class ImageSegmentation {

    private int largestColor;
    private int largestSize;
    private int segmentCount;
    private int[][] image;
    private int N;
    UF uuuuuuuuuuf ;
    private int[] size;
    private int[] color;
    private int root;

    public ImageSegmentation(int L, int[][] inputImage) {
        // Initialize a N-by-N image
        N = L;
        /*this*/image = inputImage;
        segmentCount = 0;
        largestColor = 0;
        largestSize = 0;
        uuuuuuuuuuf = new UF(N*N);
        size = new int[N*N];
        color = new int[N*N];
        for (int i = 0; i < N * N; i++) {
            int row = i / N;
            int col = i % N;
        color[i] = image[row][col];
        size[i]=0;
        }
    }
    public int countDistinctSegments() {
        // Count the number of distinct segments in the image.
        //segmentCount = N * N;
        for (int i = 0; i  <N ; i++) {
            for (int j = 0; j <N ; j++) {
                if (image[i][j] != 0) {
                    int current = i * N + j;
                    //root =uuuuuuuuuuf.find(current);
                    if (j < N-1  && image[i][j + 1] == image[i][j]) {
                        uuuuuuuuuuf.union(current, current + 1);
                        root =uuuuuuuuuuf.find(current);
                       // size[root]++;
                        //segmentCount--;
                    }
                    if (i < N-1 && image[i + 1][j] == image[i][j]) {
                        uuuuuuuuuuf.union(current, current + N);
                        root =uuuuuuuuuuf.find(current);
                        //size[root]++;
                       // segmentCount--;
                    }
                    /*if (i < N - 1 && image[i + 1][j] == image[i][j]&&j < N - 1 && image[i][j + 1] == image[i][j]) {
                        //root =uuuuuuuuuuf.find(current);
                        size[root]--;
                       // segmentCount++;
                    }*/
                    /*if (i == N-1 && j == N-1) {
                        root =uuuuuuuuuuf.find(current);
                    }*/
                 } //else if (image[i][j]==0) {
                //     //segmentCount--;
                //     root=0;
                // }
               // System.out.println(root);
            }
        }
        for (int i = 0; i < N * N; i++) {
            // System.out.println(uuuuuuuuuuf.find(i));
              root =uuuuuuuuuuf.find(i);
              if (color[i] != 0) {
                if (root==i) {
                   segmentCount++;
                }
                size[root]++;
                if (size[root] > largestSize || (size[root] == largestSize && color[root] < largestColor)) {
                    largestSize = size[root];
                    largestColor = color[root];
                }
              }
            }
        //System.out.println(uuuuuuuuuuf.find(8));
        return segmentCount;
    }
    public int[] findLargestSegment() {
        // Find the largest connected segment and return an array
        // containing the number of pixels and the color of the segment.
      return new int[]{largestSize, largestColor};
}
    // private object mergeSegment (object
        // Maybe you can use user-defined function to
        // facilitate you implement mergeSegment method.
    // }
    public static void main(String args[]) {

        // Example 1:
        int[][] inputImage1 = {
            {0, 0, 0},
            {0, 1, 1},
            {0, 0, 1}
        };

        System.out.println("Example 1:");
        ImageSegmentation s = new ImageSegmentation(3, inputImage1);
        System.out.println("Number of Distinct Segments: " + s.countDistinctSegments());

        int[] largest = s.findLargestSegment();
        System.out.println("Size of the Largest Segment: " + largest[0]);
        System.out.println("Color of the Largest Segment: " + largest[1]);


        // Example 2:
        int[][] inputImage2 = {
            {0, 0, 0, 3, 0},
            {0, 2, 3, 3, 0},
            {1, 2, 2, 0, 0},
            {1, 2, 2, 1, 1},
            {0, 0, 1, 1, 1}
        };

        System.out.println("\nExample 2:");

        s = new ImageSegmentation(5, inputImage2);
        System.out.println("Number of Distinct Segments: " + s.countDistinctSegments());

        largest = s.findLargestSegment();
        System.out.println("Size of the Largest Segment: " + largest[0]);
        System.out.println("Color of the Largest Segment: " + largest[1]);
    }
}
            //    {0, 0, 0, 3, 0, 1},
            //    {0, 2, 3, 3, 0, 1},
            //    {1, 2, 2, 0, 0, 1},
            //    {1, 2, 2, 1, 1, 1},
            //    {0, 0, 1, 1, 1, 1},
            //    {0, 0, 1, 1, 1, 1},