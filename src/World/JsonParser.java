package World;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class JsonParser {
    private int width, height, moveEnergy, plantEnergy, startEnergy, numberOfAnimals, numberOfGrass, dailyGrassNumber;
    private double jungleRatio;

    public JsonParser() {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader("parameters.json")) {

            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            System.out.println(jsonObject);
            this.width = ((Number) jsonObject.get("width")).intValue();
            if(this.width < 0) throw new IllegalArgumentException("Width cannot be lesser than 0");
            this.height = ((Number) jsonObject.get("height")).intValue();
            if(this.height < 0) throw new IllegalArgumentException("Height cannot be lesser than 0");
            this.moveEnergy = ((Number) jsonObject.get("moveEnergy")).intValue();
            this.plantEnergy = ((Number) jsonObject.get("plantEnergy")).intValue();
            this.startEnergy = ((Number) jsonObject.get("startEnergy")).intValue();
            this.numberOfAnimals = ((Number) jsonObject.get("numberOfAnimals")).intValue();
            this.jungleRatio = ((Number) jsonObject.get("jungleRatio")).doubleValue();
            this.numberOfGrass = ((Number) jsonObject.get("numberOfGrass")).intValue();
            this.dailyGrassNumber = ((Number) jsonObject.get("dailyGrass")).intValue();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getMoveEnergy() {
        return this.moveEnergy;
    }

    public int getPlantEnergy() {
        return this.plantEnergy;
    }

    public int getStartEnergy() {
        return this.startEnergy;
    }

    public double getJungleRatio() {
        return this.jungleRatio;
    }

    public int getNumberOfAnimals() {
        return this.numberOfAnimals;
    }

    public int getNumberOfGrass() {
        return this.numberOfGrass;
    }

    public int getDailyGrassNumber() {
        return this.dailyGrassNumber;
    }
    }
