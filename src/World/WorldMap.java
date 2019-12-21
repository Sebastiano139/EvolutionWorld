package World;
import MapElements.Animal;
import MapElements.Grass;
import MoveParameters.Vector2d;

import java.util.*;

public class WorldMap {
    private int WIDTH;
    private int HEIGHT;
    private int START_ENERGY;
    private int MOVE_ENERGY;
    private int PLANT_ENERGY;
    private double JUNGLE_RATIO;
    private int DAILY_GRASS_ADD;
    private Jungle jungle;
    private int numberOfAnimal;
    private int numberOfGrass;
    private ArrayList<Vector2d> freePositionOnStep = new ArrayList<>();
    private ArrayList<Vector2d> freePositionOnJungle = new ArrayList<>();
    private ArrayList<Grass> grasses = new ArrayList<>();
    private ArrayList<Animal> animals = new ArrayList<>();
    private HashMap<Vector2d, ArrayList<Animal>> animalHashMap = new HashMap<>();
    private HashMap<Vector2d, Grass> grassHashMap = new HashMap<>();
    private static final Comparator<Animal> COMPARATOR = new Comparator<Animal>() {
        public int compare(Animal a, Animal a1) {
            return a.getEnergy() - a1.getEnergy();
        }
    };

    WorldMap(int WIDTH, int HEIGHT, double JUNGLE_RATIO, int START_ENERGY, int MOVE_ENERGY, int PLANT_ENERGY, int numberOfAnimal, int numberOfGrass, int  DAILY_GRASS_ADD) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.JUNGLE_RATIO = JUNGLE_RATIO;
        this.START_ENERGY = START_ENERGY;
        this.MOVE_ENERGY = MOVE_ENERGY;
        this.PLANT_ENERGY = PLANT_ENERGY;
        this.DAILY_GRASS_ADD =  DAILY_GRASS_ADD;
        this.numberOfAnimal = numberOfAnimal;
        this.numberOfGrass = numberOfGrass;
        createJungle();
        getFreePositionForGrasses();
        putAnimalsOnMap(this.numberOfAnimal);
        putGrassesOnMap(this.numberOfGrass/2);
    }

    private void getFreePositionForGrasses() {
        this.freePositionOnStep.clear();
        this.freePositionOnJungle.clear();
        for(int i = 0; i < this.WIDTH; i++)
            for(int j = 0; j < this.HEIGHT; j++) {
                Vector2d vec = new Vector2d(i,j);
                if(this.animalHashMap.get(vec) == null) {
                    if (vec.follows(this.jungle.getLowerLeft()) && vec.precedes(this.jungle.getUpperRight())) {
                        this.freePositionOnJungle.add(vec);
                    } else {
                        this.freePositionOnStep.add(vec);
                    }
                }
            }

    }

    private void createJungle() {
        Vector2d centerPoint = new Vector2d(this.WIDTH/2, this.HEIGHT/2);
        int jungleWIDTH = (int)(Math.sqrt(JUNGLE_RATIO/(1+JUNGLE_RATIO))*(this.WIDTH));
        int jungleHEIGHT = (int)(Math.sqrt(JUNGLE_RATIO/(1+JUNGLE_RATIO))* (this.HEIGHT));
        Vector2d jungleLowerLeft = new Vector2d(centerPoint.x-(jungleWIDTH/2), centerPoint.y).lowerLeft(new Vector2d(centerPoint.x, centerPoint.y-(jungleHEIGHT/2)));
        Vector2d jungleUpperRight = new Vector2d(centerPoint.x+(jungleWIDTH/2), centerPoint.y).upperRight(new Vector2d(centerPoint.x, centerPoint.y+(jungleHEIGHT/2)));
        this.jungle = new Jungle(jungleUpperRight, jungleLowerLeft, this);
    }

    private void putAnimalOnMap( Animal animal ) {
        if( this.grassHashMap.get( animal.getPosition()) == null) {
            this.animalHashMap.computeIfAbsent(animal.getPosition(), k -> new ArrayList<Animal>());
            if(getQuantityOfAnimal(animal.getPosition()) >= 2 ) return;
            this.animalHashMap.get(animal.getPosition()).add( animal );
            this.animalHashMap.get(animal.getPosition()).sort( COMPARATOR );
            this.animals.add(animal);
        }
    }

    private void putAnimalsOnMap( int quantity ) {
        int counter = 0;
        while(counter < quantity) {
            Animal animal = new Animal(new Vector2d( new Random().nextInt(this.WIDTH), new Random().nextInt(this.HEIGHT) ), this.START_ENERGY, this);
            putAnimalOnMap(animal);
            counter++;
        }
    }

    private void putGrassesOnMap(int quantity) {
       int counter = 0;
       while( counter < quantity) {
            if (this.freePositionOnJungle.size() == 0) break;
            int grassIndex = new Random().nextInt(this.freePositionOnJungle.size());
            this.grasses.add(new Grass( this.freePositionOnJungle.get( grassIndex )));
            this.grassHashMap.put(this.freePositionOnJungle.get( grassIndex ) ,new Grass( this.freePositionOnJungle.get( grassIndex )));
            this.freePositionOnJungle.remove(grassIndex);
            counter++;
       }
       counter = 0;
       while( counter < quantity) {
           if (this.freePositionOnStep.size() == 0) break;
           int grassIndex = new Random().nextInt(this.freePositionOnStep.size());
           this.grasses.add(new Grass( this.freePositionOnStep.get( grassIndex )));
           this.grassHashMap.put(this.freePositionOnStep.get( grassIndex ) ,new Grass( this.freePositionOnStep.get( grassIndex )));
           this.freePositionOnStep.remove(grassIndex);
           counter++;
       }
    }

    private void subtractEnergy() {
        for( Animal animal : animals) {
            animal.subtractEnergy(this.MOVE_ENERGY);
        }
    }

    private void cleanDeadAnimals() {
        animals.removeIf(animal -> {
            if(animal.getEnergy() <= 0){
                this.animalHashMap.get(animal.getPosition()).remove(animal);
                if(this.animalHashMap.get(animal.getPosition()).isEmpty()) {
                    this.animalHashMap.remove(animal.getPosition());
                }
                return true;
            }
            return false;
        });
    }

    private void animalReproduction() {
        ListIterator<Animal> iterator = animals.listIterator();
        while(iterator.hasNext()){
            ArrayList<Animal> animalsOnTheSamePosition = this.animalHashMap.get(iterator.next().getPosition());
            if(animalsOnTheSamePosition.size() == 2) {
                if(animalsOnTheSamePosition.get(0).getEnergy() > this.START_ENERGY/2 && animalsOnTheSamePosition.get(1).getEnergy() > this.START_ENERGY/2) {
                    Animal childAnimal = new Animal(animalsOnTheSamePosition.get(0), animalsOnTheSamePosition.get(1), this);
                    animalsOnTheSamePosition.get(0).reproduction();
                    animalsOnTheSamePosition.get(1).reproduction();
                    animalsOnTheSamePosition.add(childAnimal);
                    iterator.add(childAnimal);
                }
            }
        }
    }

    private void feedAnimals() {
        grasses.removeIf(grass -> {
           if( this.animalHashMap.get(grass.getPosition()) != null && !(this.animalHashMap.get(grass.getPosition()).isEmpty()) ) {
               ArrayList<Animal> animalsOnTheSamePlace = this.animalHashMap.get(grass.getPosition());
               int counter = getQuantityOfAnimalsWithTheSameEnergy(animalsOnTheSamePlace);
               if( counter == 1) {
                   animalsOnTheSamePlace.get(0).feedAnimal(this.PLANT_ENERGY);
               } else {
                   int tmp = 0;
                   while(tmp < counter) {
                       animalsOnTheSamePlace.get(tmp).feedAnimal(this.PLANT_ENERGY/counter);
                       tmp++;
                   }
               }
               this.grassHashMap.remove(grass.getPosition());
               return true;
           }
           return false;
        });
    }

    private int getQuantityOfAnimalsWithTheSameEnergy(ArrayList<Animal> animals) {
        int counter = 0;
        Animal stAnimal = animals.get(0);
        while(counter < animals.size() && stAnimal.getEnergy() == animals.get(counter).getEnergy()){
            counter++;
        }
        return counter;
    }

    public void nextDay() {
        cleanDeadAnimals();
        animals.forEach(animal -> animal.move());
        subtractEnergy();
        feedAnimals();
        animalReproduction();
        getFreePositionForGrasses();
        putGrassesOnMap(this.DAILY_GRASS_ADD);
    }


    public boolean positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        ArrayList<Animal> animalsOnOldPos = this.animalHashMap.get(oldPosition);
        animalsOnOldPos.remove(animal);
        this.animalHashMap.put(animal.getPosition(), animalsOnOldPos);
        if(this.animalHashMap.get(oldPosition).isEmpty())
        {
            this.animalHashMap.remove(oldPosition);
            if(animal.isJungle(this.jungle)) freePositionOnJungle.add(animal.getPosition());
            else freePositionOnStep.add(animal.getPosition());
        }
        this.animalHashMap.computeIfAbsent(newPosition, k -> new ArrayList<Animal>());
        ArrayList<Animal> animalsOnNewPos = this.animalHashMap.get(newPosition);
        animalsOnNewPos.add(animal);
        animalsOnNewPos.sort(COMPARATOR);
        if(animal.isJungle(this.jungle)) freePositionOnJungle.remove(animal.getPosition());
        else freePositionOnStep.remove(animal.getPosition());
        return true;
    }


    private int getQuantityOfAnimal(Vector2d position) {
        if(this.animalHashMap.get(position) == null) return 0;
        return this.animalHashMap.get(position).size();
    }

    public HashMap<Vector2d, ArrayList<Animal>> getAnimalHashMap() {
        return this.animalHashMap;
    }

    public HashMap<Vector2d, Grass> getGrassHashMap() {
        return this.grassHashMap;
    }

    public ArrayList<Animal> getAnimals() {
        return this.animals;
    }

    public ArrayList<Vector2d> getFreePosition() {
        return freePositionOnJungle;
    }

    public Jungle getJungle() { return this.jungle; }
    public int getWidth() { return this.WIDTH; }
    public int getHeight() { return this.HEIGHT; }
    public ArrayList<Grass> getGrasses() { return this.grasses; }

}
