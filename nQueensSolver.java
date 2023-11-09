// Ray Kumar
// CS 203
// Project 1
// Prof. Turini

import java.util.Arrays;
import java.util.Random;

public class nQueensSolver {

    // Global Variables
    public static int[] board,D1,D2;
    public static int N;

    public static void main(String[] args) {
        // Input Size
        N = 8;
        try{
            solveIterativeRepair();
            boardPrint();
        }
        catch (Exception e){
            System.out.println(e +"Input is incorrect");
        }

    }

    public static void solveIterativeRepair() throws Exception {

        if(N<=3) {throw new Exception("Can't solve for N <= 3");}

        // Initialize the Board & Diagonals
        initBoard(N);

        // Initalize the Counters and attacks
        int count = 0;
        int swapcounter;
        int attacks = getAttacks();

        do {
            // Check if generated board is a solution
            if (attacks == 0) {break;}
            swapcounter = 0;
            for (int i = 0; i < N - 1; i++) {
                for (int j = i + 1; j < N; j++) {

                    // Initialize Row & Col variables for readibility
                    int iRow = board[i];
                    int iCol = i;
                    int jRow = board[j];
                    int jCol = j;

                    // Calculate D1 & D2 indexes
                    int iD1 = calcD1(iRow,iCol);
                    int jD1 = calcD1(jRow,jCol);
                    int iD2 = calcD2(iRow,iCol);
                    int jD2 = calcD2(jRow,jCol);

                    // Check if queen is under attack
                    if (( D1[iD1] > 1 ) || ( D1[jD1] > 1 ) || ( D2[iD2] > 1 ) || ( D2[jD2] > 1) ) {
                        // Get attacks before and after the swap
                        int oldAttacks = getAttacks();
                        swapQueens(i, j);
                        int newAttacks = getAttacks();

                        // Swap back to original board if collisions arent reduced
                        if (newAttacks >= oldAttacks) {
                            swapQueens(i, j);
                        }
                        else {
                            swapcounter++;
                        }
                    }//END IF
                }//END FOR
            }// END NESTED FOR

            attacks = getAttacks();

            // COUNTERS FOR DEBUGGING
            //System.out.println("Iteration: "+ count);
            //System.out.println("Swaps: " +swapcounter );
            //System.out.println("Collisions: " + attacks);
            //System.out.println();

            if(swapcounter == 0 && attacks > 0){
                count++;
                initBoard(N);
                //System.out.println("===================");
            }
        } while(attacks > 0);
    }//END SOLVE ITERATIVE REPAIR

    public static void swapQueens(int i, int j){
        //Decrement the Diagonals from the old position
        D1[calcD1(board[i],i)]--;
        D1[calcD1(board[j],j)]--;
        D2[calcD2(board[i],i)]--;
        D2[calcD2(board[j],j)]--;

        // Swap the queens on the Board
        int temp = board[i];
        board[i] = board[j];
        board[j] = temp;

        //Increment the Diagonals from the new position
        D1[calcD1(board[i],i)]++;
        D1[calcD1(board[j],j)]++;
        D2[calcD2(board[i],i)]++;
        D2[calcD2(board[j],j)]++;
    }

    public static void boardPrint(){
        // Used to print out the board.
        for( int i = 0; i < N; i++){
            for( int j =0; j < N; j++){
                // Print 1 or . depending on queen position
                String out = board[j]  == i ? " 1 " : " . ";
                System.out.print(out);
            }
           System.out.println(" ");
        }
        System.out.println(" ");

    }
    public static void initBoard(int n){
        int[] array = new int[n];
        Random rand = new Random();
        D1 =  new int[(2*n)-1];
        D2 =  new int[(2*n)-1];

        // Populate the Array
        for (int i = 0; i < n; i++) {
            array[i] = i;
        }

        // Shuffle the Array
        for (int i = n - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);

            // Swap array[i] and array[j]
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }

        board = array;
        initDiagonals();

    }
    public static void initDiagonals(){
        Arrays.fill(D1,0);
        Arrays.fill(D2,0);
        // Increment indexes of the queens
        for(int i = 0; i < N;i++){
            D1[calcD1(board[i],i)]++;
            D2[calcD2(board[i],i)]++;
        }
    }
    public static int getAttacks(){
        int conflict = 0;
        for( int i = 0; i < D1.length; i++){
            conflict += D1[i] > 1 ? D1[i]-1 : 0;
            conflict += D2[i] > 1 ? D2[i]-1 : 0;
        }
        return conflict;
    }
    public static void aPrint(int[] array){
        //Used to print out D1 & D2 Arrays
        for( int i =0; i < array.length; i++){
            System.out.print( array[i] + "  ");
        }
        System.out.println();
    }
    public static int calcD1(int row, int col){return (row+col);}
    public static int calcD2(int row, int col){return ((row-col)+(N-1));}
    public static void tester(int n){
        // called to get the time for Emperical Analysis
        N = n;
        int i =0;
        while(i < 11) {
            long start = System.nanoTime();
            try {solveIterativeRepair();
            } catch (Exception e) { }
            long end = System.nanoTime();
            System.out.println((end - start) * 1e-6);
            i++;
        }

    }

}// END MAIN







