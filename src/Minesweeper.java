import java.awt.*;
import java.awt.event.*;
import java.io.Console;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Minesweeper {

    private class Tile extends JButton {
        int r;
        int c;
        // 0 = hidden, 1 = shown, 2 = flagged
        int status = 0;
        int texture = 9;

        public Tile(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    int tileSize = 32;
    int numRows = 12;
    int numCols = 12;
    int boardWidth = numCols * tileSize;
    int boardHeight = numRows * tileSize;
    int mineCount = 10;

    JFrame window = new JFrame("Minesweeper");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    Tile[][] board = new Tile[numRows][numCols];
    ImageIcon[] textures = new ImageIcon[14];
    ArrayList<Tile> mineList;
    Random random = new Random();

    int tilesClicked = 0;
    boolean gameOver = false;

    public Minesweeper() throws Exception {

        for (int i = 0; i < textures.length; i++) {
            textures[i] = new ImageIcon(
                    String.format("D:\\Programming things\\Java Things\\Minesweeper\\src\\textures\\%d.png", i));
        }

        // window.setVisible(true);
        window.setSize(boardWidth, boardHeight);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.BOLD, 25));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Minesweeper");
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        window.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(numRows, numCols));
        boardPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        window.add(boardPanel);

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                Tile tile = new Tile(r, c);
                board[r][c] = tile;

                tile.setFocusable(false);
                tile.setIcon(textures[tile.texture]);
                tile.setPreferredSize(new Dimension(tileSize, tileSize));

                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (gameOver) {
                            return;
                        }
                        Tile tile = (Tile) e.getSource();

                        // Left Click
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (tile.status == 0) {
                                if (mineList.contains(tile)) {
                                    tile.status = 1;
                                    revealMines();
                                } else {
                                    checkNeighbors(tile);
                                }
                            }
                        }

                        // Right Click
                        if (e.getButton() == MouseEvent.BUTTON3) {
                            if (tile.status == 0) {
                                tile.status = 2;
                                tile.texture = 10;
                                tile.setIcon(textures[tile.texture]);
                            } else {
                                if (tile.status == 2) {
                                    tile.status = 0;
                                    tile.texture = 9;
                                    tile.setIcon(textures[tile.texture]);
                                }
                            }
                        }
                    }
                });

                boardPanel.add(tile);
            }
        }

        setMines();

        window.pack();
        window.setVisible(true);
    }

    void setMines() {
        mineList = new ArrayList<Tile>();

        int mineLeft = mineCount;
        while (mineLeft > 0) {
            int r = random.nextInt(numRows);
            int c = random.nextInt(numCols);

            Tile tile = board[r][c];
            if (!mineList.contains(tile)) {
                mineList.add(tile);
                mineLeft--;
            }
        }
    }

    void revealMines() {
        for (int i = 0; i < mineList.size(); i++) {
            Tile tile = mineList.get(i);

            if (tile.status == 0) {
                tile.texture = 12;
            }
            tile.status = 1;
            tile.setIcon(textures[tile.texture]);

            gameOver = true;
            textLabel.setText("Boom! Game Over!");
        }
    }

    void checkMine(Tile tile) {
        int bombsFound = checkNeighbors(tile);
        tile.texture = bombsFound;
        System.out.println(bombsFound);
        tile.status = 1;
        tile.setIcon(textures[tile.texture]);

        if (tilesClicked == (numRows * numCols) - mineList.size()) {
            gameOver = true;
            textLabel.setText("Victory! Yippee!");
        }
    }

    int checkNeighbors(Tile tile) {
        tilesClicked++;

        int numberFound = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (!(i == 0 && j == 0)) {
                    try {
                        Tile beingChecked = board[tile.r + i][tile.c + j];
                        if (mineList.contains(beingChecked)) {
                            numberFound++;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        if (numberFound == 0) {
            tile.status = 1;
            tile.texture = 0;
            tile.setIcon(textures[tile.texture]);
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (!(i == 0 && j == 0)) {
                        try {
                            Tile beingChecked = board[tile.r + i][tile.c + j];
                            if (beingChecked.status == 0) {
                                checkMine(beingChecked);
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                        }
                    }
                }
            }
        }

        return numberFound;

    }

}
