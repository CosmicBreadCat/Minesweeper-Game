import java.util.Objects;
import java.util.Random;
import java.util.ArrayList;

public class Minesweeper {
    Random rand = new Random();

    private String[][] CreateMatrix(){
        String[][] matrix = new String[10][10];
        for (int i = 0; i < 10; i++) {
            //Create initial matrix values
            for (int j = 0; j < 10; j++) {
                matrix[i][j] = ".";
            }
        }
        return matrix;
    }

    private String[][] GenBombLoc(){
        //Create Bomb locations
        String[][] bombLoc = new String[10][2];
        for (int i = 0; i < 10; i++) {
            boolean flag = true;
            while (flag){
                boolean check = false;
                String[] loc = {String.valueOf(rand.nextInt(10)), String.valueOf(rand.nextInt(10))};
                //makes sure there are no dupes
                for (String[] strings : bombLoc) {
                    if (Objects.equals(strings[0], loc[0]) && Objects.equals(strings[1], loc[1])) {
                        check = true;
                        break;
                    }
                }
                if(!check){
                    flag = false;
                    bombLoc[i] = loc;
                }
            }
        }
        return bombLoc;
    }

    private String[][] AddBombs(String[][] matrix, String[][] bombLoc){
        //Add bombs from list into matrix
        for (int i = 0; i < 10; i++) {
            int x = Integer.parseInt(bombLoc[i][0]);
            int y = Integer.parseInt(bombLoc[i][1]);
            matrix[x][y] = "X";
        }
        return matrix;
    }

    private String[][] GetNeighbours(String[] coord, int height, int length){
        ArrayList<String[]> neighbours = new ArrayList<>();

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int x = Integer.parseInt(coord[0]);
                int y = Integer.parseInt(coord[1]);


                //Makes sure neighbour isn't out of bounds
                String[] neighbour = {String.valueOf(x + i), String.valueOf(y + j)};
                if ((0 < (x + i)) && ((x + i) < height)) {
                    if ((0 < (y + j)) && ((y + j) < length)) {
                        if (!(i == 0 && j == 0)) {
                            neighbours.add(neighbour);
                        }
                    }
                }

            }
        }
        String[][] output = new String[height][2];
        neighbours.toArray(output);
        return output;
    }

    private String[][] AddNumbers(String[][] matrix){
        int bombNumber = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                String[] coord = {String.valueOf(i), String.valueOf(j)};
                String[][] neighbours = GetNeighbours(coord, matrix.length, matrix[i].length);

                for (String[] neighbour : neighbours) {
                    try {
                        int[] intNeighbour = {Integer.parseInt(neighbour[0]), Integer.parseInt(neighbour[1])};

                        if (Objects.equals(matrix[intNeighbour[0]][intNeighbour[1]], "X")) {
                            bombNumber += 1;
                        }
                    }
                    catch (Exception ignored){};
                }
                matrix[i][j] = String.valueOf(bombNumber);
            }
        }

        return matrix;
    }

    public String[][] CreateBoard(){
        String[][] matrix = AddBombs(CreateMatrix(), GenBombLoc());

        return matrix;
    }

}
