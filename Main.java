import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        Component comp = new Component();
        JFrame frame = new JFrame("Flashlight");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(comp);
        frame.pack();

        frame.setVisible(true);


        while (true) {
            comp.update();

            frame.repaint();
        }
    }
}
