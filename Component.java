import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.*;

public class Component extends JComponent {
    private int x;
    private int y;
    private int targetX;
    private int targetY;

    private flashlight2 flashLight;


    private ArrayList<Rectangle> rects;

    public Component() {
        rects = new ArrayList<>();

        rects.add(new Rectangle(500, 40, 40, 40));
        rects.add(new Rectangle(20, 170, 40, 40));
        rects.add(new Rectangle(400, 300, 40, 40));

        rects.add(new Rectangle(30, 240, 40, 40));
        rects.add(new Rectangle(458, 200, 40, 40));
        rects.add(new Rectangle(120, 49, 40, 40));
        flashLight = new flashlight2(rects);
        this.addMouseMotionListener(new MouseHandler(flashLight));

        x = 100;
        y = 100;

        this.setPreferredSize(new Dimension(640, 480));

    }

    public void update(){
        flashLight.update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        flashLight.draw(g2d);
        // Draw the line
        g2d.setColor(Color.RED);
    }
}
