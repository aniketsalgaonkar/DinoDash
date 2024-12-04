import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        int width = 750;
        int heigth = 250;

        JFrame frame = new JFrame("DinoDash");

//        frame.setVisible(true);
        frame.setSize(width,heigth);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DinoDash dinoDash = new DinoDash();
        frame.add(dinoDash);
        frame.pack();
        dinoDash.requestFocus();
        frame.setVisible(true);
    }
}