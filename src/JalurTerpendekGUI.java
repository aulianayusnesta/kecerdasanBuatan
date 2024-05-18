import javax.swing.*;
import java.awt.*;
import java.util.*;

class Node implements Comparable<Node> {
    public int x, y, jarak;

    public Node(int x, int y, int jarak) {
        this.x = x;
        this.y = y;
        this.jarak = jarak;
    }

    @Override
    public int compareTo(Node lainnya) {
        return Integer.compare(this.jarak, lainnya.jarak);
    }
}

public class JalurTerpendekGUI extends JFrame {
    private static final int SIZE = 10;
    private static final char START = 'S';
    private static final char GOAL = 'G';
    private static final char WALL = 'X';
    private static final char PATH = '.';
    private static final char VISITED = '*';
    private static final int CELL_SIZE = 50;

    private char[][] peta = {
            {'S', '.', '.', '.', 'X', '.', '.', '.', '.', '.'},
            {'.', 'X', '.', 'X', '.', 'X', 'X', 'X', 'X', '.'},
            {'.', 'X', '.', '.', '.', '.', '.', '.', 'X', '.'},
            {'.', '.', 'X', 'X', 'X', '.', 'X', '.', '.', '.'},
            {'X', '.', '.', '.', '.', 'X', '.', 'X', 'X', '.'},
            {'.', 'X', '.', 'X', '.', '.', '.', 'X', '.', '.'},
            {'.', '.', '.', '.', 'X', 'X', 'X', 'X', 'X', '.'},
            {'.', 'X', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', 'G', '.'},
    };

    public JalurTerpendekGUI() {
        setTitle("Pencarian Jalur Terpendek");
        setSize(SIZE * CELL_SIZE, SIZE * CELL_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new PetaPanel());
        int startX = 0, startY = 0, goalX = 9, goalY = 8;
        tampilkanPeta("Peta Awal");
        temukanJalurTerpendek(startX, startY, goalX, goalY);
        tampilkanPeta("Peta Setelah Pencarian Jalur Terpendek");
    }

    private class PetaPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (peta[i][j] == START) {
                        g.setColor(Color.GREEN);
                    } else if (peta[i][j] == GOAL) {
                        g.setColor(Color.RED);
                    } else if (peta[i][j] == WALL) {
                        g.setColor(Color.BLACK);
                    } else if (peta[i][j] == VISITED) {
                        g.setColor(Color.YELLOW);
                    } else if (peta[i][j] == PATH) {
                        g.setColor(Color.BLUE);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    g.setColor(Color.GRAY);
                    g.drawRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    public void temukanJalurTerpendek(int startX, int startY, int goalX, int goalY) {
        int n = peta.length;
        int m = peta[0].length;
        int[][] jarak = new int[n][m];
        for (int[] baris : jarak) Arrays.fill(baris, Integer.MAX_VALUE);
        jarak[startX][startY] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(startX, startY, 0));

        while (!pq.isEmpty()) {
            Node sekarang = pq.poll();
            int x = sekarang.x;
            int y = sekarang.y;
            int dist = sekarang.jarak;

            if (x == goalX && y == goalY) {
                tandaiJalur(startX, startY, goalX, goalY, jarak);
                return;
            }

            for (int i = 0; i < 4; i++) {
                int newX = x + dx[i];
                int newY = y + dy[i];

                if (newX >= 0 && newX < n && newY >= 0 && newY < m && peta[newX][newY] != WALL) {
                    int newDist = dist + 1;
                    if (newDist < jarak[newX][newY]) {
                        jarak[newX][newY] = newDist;
                        pq.add(new Node(newX, newY, newDist));
                        peta[newX][newY] = VISITED;
                    }
                }
            }
            repaint();
        }
    }

    private void tandaiJalur(int startX, int startY, int goalX, int goalY, int[][] jarak) {
        int x = goalX;
        int y = goalY;
        while (x != startX || y != startY) {
            peta[x][y] = PATH;
            for (int i = 0; i < 4; i++) {
                int newX = x + dx[i];
                int newY = y + dy[i];
                if (newX >= 0 && newX < peta.length && newY >= 0 && newY < peta[0].length) {
                    if (jarak[newX][newY] == jarak[x][y] - 1) {
                        x = newX;
                        y = newY;
                        break;
                    }
                }
            }
        }
        peta[startX][startY] = START;
        peta[goalX][goalY] = GOAL;
        repaint();
    }

    private static final int[] dx = {-1, 1, 0, 0};
    private static final int[] dy = {0, 0, -1, 1};

    private void tampilkanPeta(String judul) {
        System.out.println(judul);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(peta[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("\nLegenda:");
        System.out.println("S = Start (Titik Awal)");
        System.out.println("G = Goal (Titik Tujuan)");
        System.out.println("X = Dinding/Rumah (Tidak Dapat Dilalui)");
        System.out.println(". = Jalan (Dapat Dilalui)");
        System.out.println("* = Jalan yang Telah Dilalui");
        System.out.println();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JalurTerpendekGUI frame = new JalurTerpendekGUI();
            frame.setVisible(true);
        });
    }
}
