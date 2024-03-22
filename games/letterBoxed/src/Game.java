import java.util.*;
import java.io.*;
public class Game {

    public static void main(String [] args) throws IOException {
        Scanner input = new Scanner(System.in);

        System.out.println("welcome to letter boxed");
        System.out.println("=======================");

        System.out.println();
        System.out.println("enter the game board [north],[east],...");
        String board = input.next();

        String [] boardList = board.split(",");
        Table myTable = new Table(boardList[0], boardList[1], boardList[2], boardList[3]);

        System.out.println();
        System.out.println(myTable);

        System.out.println();
        System.out.println("enter the number of steps");
        int steps = input.nextInt();

        String [] mySolution = myTable.alternateSolution(steps);

        System.out.println();

        String printable = myTable.buildString(mySolution);
        System.out.println(printable);
    }
}
