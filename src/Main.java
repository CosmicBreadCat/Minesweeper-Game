import java.util.Arrays;
public class Main {
    public static void main(String[] args) {
        Minesweeper board = new Minesweeper();

        String[][] matrix = board.CreateBoard();

        //print to console
        for (int i = 0; i < 10; i++) {
            System.out.println(Arrays.deepToString(matrix[i]));
        }
    }
}

