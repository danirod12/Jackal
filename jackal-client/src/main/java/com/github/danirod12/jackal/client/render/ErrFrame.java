package com.github.danirod12.jackal.client.render;

import javax.swing.*;
import java.awt.*;

public class ErrFrame {

    private final JFrame frame;
    private final JTextArea area = new JTextArea();

    public ErrFrame() {

        frame = new JFrame("Errors logger");

        Dimension dimension = new Dimension(800, 700);
        frame.setSize(dimension);
        area.setForeground(Color.RED);
        
        frame.add(new JScrollPane(area));
        frame.setVisible(true);

    }

    public void log(String string) {
        area.append("\n" + string);
    }

    public boolean isClosed() {
        return !frame.isVisible();
    }

}
