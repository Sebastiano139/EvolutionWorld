package Gui;

import MapElements.Animal;
import MapElements.Grass;
import World.WorldMap;

import javax.swing.*;
import java.awt.*;


public class WorldVisualisation extends JPanel {

    public WorldMap map;

    public WorldVisualisation(WorldMap map) {
        this.map = map;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = this.map.getWidth()*10;
        int height = this.map.getHeight()*10;
        g.setColor(Color.yellow);
        g.fillRect(0, 0, width, height);
        g.setColor(new Color(0, 102, 0));
        g.fillRect(this.map.getJungle().getLowerLeft().x*10, this.map.getJungle().getLowerLeft().y*10 ,(this.map.getJungle().getUpperRight().x*10-this.map.getJungle().getLowerLeft().x*10)
                ,(this.map.getJungle().getUpperRight().y*10 - this.map.getJungle().getLowerLeft().y*10));
        //draw MapElements.Grass
        for (Grass grass : this.map.getGrasses()) {
            g.setColor(Color.GREEN);
            g.fillRect(grass.getPosition().x*10, grass.getPosition().y*10, 10, 10);
        }

        for (Animal animal : this.map.getAnimals()) {
            if(animal.getEnergy() <= 20) g.setColor(new Color(255, 204, 204));
            if(animal.getEnergy() > 20) g.setColor(new Color(255, 102, 102));
            if(animal.getEnergy() > 40) g.setColor(new Color(255, 77, 77));
            if(animal.getEnergy() > 60) g.setColor(new Color(255, 51, 51));
            if(animal.getEnergy() > 80) g.setColor(new Color(230, 0, 0));
            if(animal.getEnergy() > 100) g.setColor(new Color(204, 0, 0));

            g.fillRect(animal.getPosition().x*10, animal.getPosition().y*10, 10, 10);
        }
    }
}