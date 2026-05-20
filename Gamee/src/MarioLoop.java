import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MarioLoop extends JPanel implements Runnable {

    // Window
    private final int WIDTH = 900;
    private final int HEIGHT = 500;

    // Thread
    private Thread thread;
    private boolean running = true;

    // Player
    private float playerX = 100;
    private float playerY = 300;

    private float velX = 0;
    private float velY = 0;

    private final float SPEED = 5f;
    private final float GRAVITY = 0.6f;
    private final float JUMP_FORCE = -12f;

    private boolean left;
    private boolean right;
    private boolean jump;

    private boolean onGround = false;

    // Ground
    private final int groundY = 350;

    // Camera
    private int cameraX = 0;

    // Score
    private int score = 0;

    // Enemy
    private float enemyX = 700;
    private float enemyY = 310;

    private float enemySpeed = 3;

    private final int enemyLeft = 600;
    private final int enemyRight = 900;

    public MarioLoop() {

        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        setFocusable(true);

        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                int key = e.getKeyCode();

                if (key == KeyEvent.VK_A)
                    left = true;

                if (key == KeyEvent.VK_D)
                    right = true;

                if (key == KeyEvent.VK_SPACE)
                    jump = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {

                int key = e.getKeyCode();

                if (key == KeyEvent.VK_A)
                    left = false;

                if (key == KeyEvent.VK_D)
                    right = false;

                if (key == KeyEvent.VK_SPACE)
                    jump = false;
            }
        });

        start();
    }

    public void start() {

        thread = new Thread(this);

        thread.start();
    }

    @Override
    public void run() {

        final double UPDATE_CAP = 1.0 / 60.0;

        long lastTime = System.nanoTime();

        double unprocessed = 0;

        while (running) {

            long now = System.nanoTime();

            unprocessed += (now - lastTime) / 1_000_000_000.0;

            lastTime = now;

            while (unprocessed >= UPDATE_CAP) {

                updateGame();

                repaint();

                unprocessed -= UPDATE_CAP;
            }

            try {
                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateGame() {

        // Movement
        velX = 0;

        if (left)
            velX = -SPEED;

        if (right)
            velX = SPEED;

        playerX += velX;

        // Jump
        if (jump && onGround) {

            velY = JUMP_FORCE;

            onGround = false;
        }

        // Gravity
        velY += GRAVITY;

        playerY += velY;

        // Ground collision
        if (playerY >= groundY) {

            playerY = groundY;

            velY = 0;

            onGround = true;
        }

        // Camera
        cameraX = (int) playerX - 200;

        if (cameraX < 0)
            cameraX = 0;

        // Distance score
        score = (int) playerX / 10;

        // Enemy movement
        enemyX += enemySpeed;

        if (enemyX <= enemyLeft || enemyX >= enemyRight) {

            enemySpeed *= -1;
        }

        // Collision
        Rectangle player =
                new Rectangle((int) playerX, (int) playerY, 50, 50);

        Rectangle enemy =
                new Rectangle((int) enemyX, (int) enemyY, 50, 50);

        if (player.intersects(enemy)) {

            playerX = 100;
            playerY = 300;

            score = 0;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // SKY LOOP
        for (int i = -1; i < 20; i++) {

            if (i % 2 == 0)
                g2d.setColor(new Color(120, 200, 255));
            else
                g2d.setColor(new Color(100, 180, 240));

            g2d.fillRect(
                    i * WIDTH - (cameraX % WIDTH),
                    0,
                    WIDTH,
                    HEIGHT
            );
        }

        // Mountains
        g2d.setColor(Color.GRAY);

        for (int i = 0; i < 20; i++) {

            int mx = i * 300 - (cameraX / 2);

            g2d.fillPolygon(
                    new int[]{
                            mx,
                            mx + 120,
                            mx + 240
                    },
                    new int[]{
                            350,
                            180,
                            350
                    },
                    3
            );
        }

        // Ground
        g2d.setColor(new Color(60, 180, 75));

        g2d.fillRect(0, 400, WIDTH, 100);

        // Ground blocks loop
        for (int i = -10; i < 100; i++) {

            g2d.setColor(new Color(150, 75, 0));

            g2d.fillRect(
                    i * 64 - (cameraX % 64),
                    350,
                    64,
                    64
            );

            g2d.setColor(Color.BLACK);

            g2d.drawRect(
                    i * 64 - (cameraX % 64),
                    350,
                    64,
                    64
            );
        }

        // Player
        g2d.setColor(Color.RED);

        g2d.fillRect(
                (int) playerX - cameraX,
                (int) playerY,
                50,
                50
        );

        // Head
        g2d.setColor(new Color(255, 220, 180));

        g2d.fillOval(
                (int) playerX - cameraX + 5,
                (int) playerY - 20,
                40,
                40
        );

        // Enemy
        g2d.setColor(Color.BLACK);

        g2d.fillRect(
                (int) enemyX - cameraX,
                (int) enemyY,
                50,
                50
        );

        g2d.setColor(Color.WHITE);

        g2d.drawString(
                "GOOMBA",
                (int) enemyX - cameraX + 2,
                (int) enemyY + 28
        );

        // Score Bar
        g2d.setColor(Color.BLACK);

        g2d.fillRoundRect(20, 20, 220, 50, 20, 20);

        g2d.setColor(Color.WHITE);

        g2d.setFont(new Font("Arial", Font.BOLD, 24));

        g2d.drawString(
                "Distance: " + score,
                35,
                53
        );

        // Controls
        g2d.drawString(
                "A/D Move | SPACE Jump",
                580,
                40
        );
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Mario Loop");

        MarioLoop game = new MarioLoop();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(game);

        frame.pack();

        frame.setResizable(false);

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
}