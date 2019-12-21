package World;

import Gui.WorldVisualisation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class World {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            private JsonParser config;
            private WorldMap world;
            @Override
            public void run() {
                this.config = new JsonParser();
                world = new WorldMap(this.config.getWidth(), this.config.getHeight(), this.config.getJungleRatio(), this.config.getStartEnergy(), this.config.getMoveEnergy()
                        , this.config.getPlantEnergy(), this.config.getNumberOfAnimals(), this.config.getNumberOfGrass(), this.config.getDailyGrassNumber());
                JFrame frame = new JFrame("Symulacja Rozwoju");
                frame.setSize(world.getWidth()*10, world.getHeight()*10);
                frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                WorldVisualisation worldVisualisation = new WorldVisualisation(world);
                worldVisualisation.setSize(new Dimension(1, 1));
                frame.add(worldVisualisation);
                Timer timer = new Timer(20, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        worldVisualisation.repaint();
                        world.nextDay();
                    }
                });
                timer.setRepeats(true);
                timer.start();
            }
        });
    }
}
