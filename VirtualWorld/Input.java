package VirtualWorld;

import VirtualWorld.Tools.Size;

import javax.swing.*;
import java.awt.*;

class Input extends JFrame {
    private int sizeX, sizeY;

    void sizeInput()
    {
        setTitle("Enter sizes");
        setLocationRelativeTo(null);
        setLayout(null);
        setSize(260,200);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel main = new JLabel("Enter size X and size Y of the gamefield");
        main.setBounds(Size.margin(), 0, 300, 20);
        add(main);

        JLabel x = new JLabel("x:");
        x.setBounds(Size.margin() + 20, 20, 10, 20);
        add(x);

        TextField sX = new TextField(2);
        sX.setBounds(2 * Size.margin() + 30, 20, 50, 20);
        add(sX);

        JLabel y = new JLabel("y:");
        y.setBounds(3 * Size.margin() + 80, 20, 10, 20);
        add(y);

        TextField sY = new TextField(2);
        sY.setBounds(4 * Size.margin() + 90, 20, 50, 20);
        add(sY);

        JButton b = new JButton("ENTER");
        b.setBounds(60, 100, 100, 50);
        b.addActionListener(e -> {
                sizeX = Integer.parseInt(sX.getText());
                sizeY = Integer.parseInt(sY.getText());
                dispose();
                if (sizeX < 4 || sizeX > 50)
                    sizeX = 20;
                if (sizeY < 4 || sizeY > 50)
                    sizeY = 20;
                new World(sizeX, sizeY);
        });
        add(b);

        setVisible(true);
    }
}
