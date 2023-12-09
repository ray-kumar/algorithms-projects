// Ray Kumar
// CS 203
// Project 2
// Prof. Turini


// Used to create the strip
import java.util.ArrayList;
// Used to generate random points
import java.util.Random;




public class Solver2DClosestPair {

    public static void main(String[] args) {

    solveByDivideNConquer(8);

    }

    // Used to setup P&Q, perform the merge sort and call the efficientClosestPair()
    public static void solveByDivideNConquer(int n){

            // Initialize the P & Q arrays
            Point[] p = new Point[n];
            Point[] q = new Point[n];

            // Generate random points
            populateArrays(p, q, n);

            // Mergesort P using the x Values
            MergesortX(p, 0, p.length - 1);

            // Mergesort Q using the y Values
            MergesortY(q, 0, p.length - 1);

            // Call the efficientClosestPair
            Pair closestPair = efficientClosestPair(p, q);

            // Print the closest Points
            System.out.println("Closest Pair of Points");
            System.out.println(closestPair);

    }

    // Divide and Conquer implementation of finding the closest pair
    public static Pair efficientClosestPair(Point[] p, Point[] q){

        Pair closestPair = new Pair();

        if(p.length < 4){

            // Initalize distance variables
            double minDist = Double.MAX_VALUE;
            double currDist = 0;

            // Double for loop to parse through the combinations
            for (int i = 0; i < p.length - 1; i++) {
                for (int j = i + 1; j < p.length; j++) {

                    // Get the distance
                    currDist = calcDistSq(p[i],p[j]);

                    // Change the max distance value and set the closest pair
                    if (currDist < minDist){
                        // Update the min Distance and closest pair
                        minDist = currDist;
                        closestPair.p1 = p[i];
                        closestPair.p2 = p[j];
                        closestPair.dist = Math.sqrt(currDist);
                    }
                }
            }//END DOUBLE FOR
            return closestPair;
        }
        else {

            // Get the length of the left and right arrays
            int lenOfLeft = p.length/2;
            int lenOfRight = p.length - lenOfLeft;

            // Initialize pLeft & pRight Arrays
            Point[] pLeft  = new Point[lenOfLeft];
            Point[] pRight = new Point[lenOfRight];

            // Initialize qLeft & qRight Arrays
            Point[] qLeft  = new Point[lenOfLeft];
            Point[] qRight = new Point[lenOfRight];

            // Populate the pLeft & pRight arrays
            for(int i=0;i<p.length;i++){
                if (i< lenOfLeft){
                    pLeft[i] = p[i];
                    p[i].setFlag(true);
                    }
                else{
                    pRight[i- lenOfLeft] = p[i];
                    p[i].setFlag(false);
                    }
            }

            // Populate the qLeft & qRight array
            int leftIndex =0;
            int rightIndex =0;
            for(int i = 0; i < q.length;i++){

                if(q[i].flag){
                    qLeft[leftIndex++] = q[i];
                }
                else{
                    qRight[rightIndex++] = q[i];
                }
            }

            // Get the closest Pair and the distance (Can be converted to distance as needed)
            Pair pairRight = efficientClosestPair(pRight,qRight);
            Pair pairLeft = efficientClosestPair(pLeft,qLeft);

            // Set the closest Pair to the min of left and right
            closestPair = pairRight.dist < pairLeft.dist ? pairRight : pairLeft;

            // Get the smallest distance and the midpoint of p
            double d = closestPair.dist;
            double m = p[(p.length - 1)/ 2].x;


            // Initialize the strip
            Point[] s = new Point[p.length];

            // Populate the strip
            int num =0;
            for(int i =0; i< p.length;i++){

                if(Math.abs(q[i].x -m) < d ){
                    s[num++]=q[i];

                }
            }

            // Initialize Dminsq
            double dMinSq = Math.pow(d,2);

            // Perform Strip Search
            for(int i =0;i < (num - 1); i++){
                int k = i + 1;
                // Check if the points are in the box
                while( (k < (num )) && (Math.pow(s[k].y - s[i].y,2) < dMinSq) ){

                    // Calculate the new distance
                    double newDMinSq = calcDistSq(s[i],s[k]);

                    // Compare the old and new distance
                    if(newDMinSq < dMinSq){
                        // Update the min Distance and closest pair
                        dMinSq = newDMinSq;
                        closestPair.p1 = s[i];
                        closestPair.p2 = s[k];
                        closestPair.dist = Math.sqrt(newDMinSq);
                    }
                    k++;
                }

            }// END FOR
            return closestPair;
        }
    }

    // Used to check the right answer
    public static Pair bruteForceSolve(Point[] p){
        Pair closestPoints = new Pair();
        double minDist = Double.MAX_VALUE;
        double currDist = 0;

        // Double for loop to parse through the combinations
        for (int i = 0; i < p.length - 1; i++) {
            for (int j = i + 1; j < p.length; j++) {

                // Get the distance
                currDist = calcDistSq(p[i],p[j]);

                // Change the max distance value and set the closest pair
                if (currDist < minDist){
                    minDist = currDist;
                    closestPoints.p1 = p[i];
                    closestPoints.p2 = p[j];
                    closestPoints.dist = Math.sqrt(currDist);

                }
            }
        }
       return closestPoints;
    }


    // Used to Print the points out for debugging
    public static void printPoints(Point[] points){
        for(int i =0; i < points.length;i++){
            System.out.println(points[i].x + "," + points[i].y);
        }
        System.out.println();
    }

    // Used to simplify getting the distance by just inputting points
    public static double calcDistSq(Point one, Point two){
        int xDiff = Math.abs(one.x- two.x);
        int yDiff = Math.abs(one.y- two.y);;
        return(Math.pow(xDiff,2)+Math.pow(yDiff,2));
    }


    // Used to Generate non-repeating points
    public static void populateArrays(Point[] p, Point[] q , int n) {

        Random random = new Random();

        // Domain and Range for the points
        int maxX = (int) Math.pow(n,2);
        int maxY = (int) Math.pow(n,2);
        ArrayList<Point> points = new ArrayList<Point>(n);

        // Find random points
        for (int i = 0; i < n;)  {
            int x = random.nextInt(maxX);
            int y = random.nextInt(maxY);

            // Check for duplicates
            boolean isDuplicate = false;
            for (Point point : points) {
                if (point.x == x && point.y == y) {
                    isDuplicate = true;
                    break;
                }
            }

            if (!isDuplicate) {
                // Create a new point and add it to P & Q
                Point newPoint = new Point(x,y);
                p[i] = newPoint;
                q[i] = newPoint;
                points.add(newPoint);
                i++;
            }
        }
    }

    // Custom Class to store coordinate values
    public static class Point {
        // X & Y Coordinates
        public int x,y;
        // Used for flags
        public boolean flag;

        // Constructor
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
            this.flag = false;
        }
        // custom toString function
        @Override
        public String toString(){
            return ("(" + x + "," + y + ")" );
        }

        // Used to set the flag
        public Point setFlag(boolean val){
            this.flag = val;
            return this;
        }

    }
    // Custom class to store points
    public static class Pair {

        // Default constructors
        public Point p1 = new Point(0,0);
        public Point p2 = new Point(0,0);
        public double dist;

        // custom toString function
        @Override
        public String toString(){
            return ("(" + p1.x + "," + p1.y + ") (" + p2.x +"," + p2.y + ") Dist: " + dist);
        }
    }

    // Merge sort function for X
    public static void MergesortX(Point[] arr, int left, int right) {

        if (left < right) {
            int mid = left + (right - left) / 2;

            // sort first and second halves
            MergesortX(arr, left, mid);
            MergesortX(arr, mid+1, right);

            // merge the sorted halves
            MergeArraysX(arr, left, mid, right);

        }

    }

    // Merge array function for merge sort x
    public static void MergeArraysX(Point[] arr, int left, int mid, int right) {

        int t1 = mid - left + 1;
        int t2 = right - mid;

        Point[] leftArr = new Point[t1];
        Point[] rightArr = new Point[t2];

        for (int i = 0; i < t1; i++) {
            leftArr[i] = arr[left + i];
        }

        for (int j = 0; j < t2; j++) {
            rightArr[j] = arr[mid + 1 + j];
        }

        int i = 0;
        int j = 0;
        // Initial index of merged subarray
        int k = left;
        while (i < t1 && j < t2) {
            if (leftArr[i].x <= rightArr[j].x) {
                arr[k] = leftArr[i];
                i++;
            }
            else {
                arr[k] = rightArr[j];
                j++;
            }
            k++;
        }

        // Copy remaining elements of L[] if any
        while (i < t1) {
            arr[k] = leftArr[i];
            i++;
            k++;
        }

        // Copy remaining elements of R[] if any
        while (j < t2) {
            arr[k] = rightArr[j];
            j++;
            k++;
        }
    }

    // Merge sort function for Y
    public static void MergesortY(Point[] arr, int left, int right) {

        if (left < right) {
            int mid = left + (right - left) / 2;

            // sort first and second halves
            MergesortY(arr, left, mid);
            MergesortY(arr, mid+1, right);

            // merge the sorted halves
            MergeArraysY(arr, left, mid, right);

        }

    }

    // Merge function for mergesort y
    public static void MergeArraysY(Point[] arr, int left, int mid, int right) {

        int t1 = mid - left + 1;
        int t2 = right - mid;

        Point[] leftArr = new Point[t1];
        Point[] rightArr = new Point[t2];

        for (int i = 0; i < t1; i++) {
            leftArr[i] = arr[left + i];
        }

        for (int j = 0; j < t2; j++) {
            rightArr[j] = arr[mid + 1 + j];
        }

        int i = 0;
        int j = 0;
        int k = left;
        while (i < t1 && j < t2) {
            if (leftArr[i].y <= rightArr[j].y) {
                arr[k] = leftArr[i];
                i++;
            }
            else {
                arr[k] = rightArr[j];
                j++;
            }
            k++;
        }

        // Copy remaining elements of L[] if any
        while (i < t1) {
            arr[k] = leftArr[i];
            i++;
            k++;
        }

        // Copy remaining elements of R[] if any
        while (j < t2) {
            arr[k] = rightArr[j];
            j++;
            k++;
        }
    }

}// END divideNConquer
