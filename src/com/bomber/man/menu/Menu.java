package com.bomber.man.menu;

import com.bomber.man.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.security.Key;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * Created by Murspi on 28.03.2017.
 */
public class Menu extends JPanel {

    private Image image = null;

    public Rectangle playButton = new Rectangle(300, 220, 200, 50);
    public Rectangle helpButton = new Rectangle(300, 290, 200, 50);
    public Rectangle editorButton = new Rectangle(300, 360, 200, 50);
    public Rectangle quitButton = new Rectangle(300, 430, 200, 50);


    Main main;

    public Menu(Main main) {
        this.main = main;
        addMouseListener(new MouseInput(main));

    }

    public void paint(Graphics g) {
        if (image == null)
            image = getImage("res/drawables/menu1.png");
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(image, 0, 0, 800, 600, this);
        Font fnt0 = new Font("arial", Font.BOLD, 40);
        g.setFont(fnt0);
        g.setColor(Color.white);

        g.drawString("Play", playButton.x + 55, playButton.y + 40);
        g2d.draw(playButton);
        g.drawString("Settings", helpButton.x + 35, helpButton.y + 40);
        g2d.draw(helpButton);
        g.drawString("Quit", quitButton.x + 55, quitButton.y + 40);
        g2d.draw(quitButton);
        g.drawString("Editor", editorButton.x + 45, editorButton.y + 40);
        g2d.draw(editorButton);


    }

    public Image getImage(String path) {
        Image tempImage = null;
        try {
            tempImage = Toolkit.getDefaultToolkit().getImage(path);
        } catch (Exception e) {
            System.out.println("Error");
        }


        return tempImage;
    }
}



