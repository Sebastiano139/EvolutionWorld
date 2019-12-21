package MapElements;

import java.util.*;

public class Genes {
    private ArrayList<Integer> genesArray = new ArrayList<Integer>();

    public Genes() {
        int[] counterOfSpecificGene = new int[8];
        for(int i = 0; i < 32; i++ ){
           this.genesArray.add(new Random().nextInt(8));
           counterOfSpecificGene[this.genesArray.get(i)]++;
        }
        for(int i = 0; i < 8; i++ ) {
            if(counterOfSpecificGene[i] == 0) rebalancedGenes(counterOfSpecificGene, i);
        }
        Collections.sort(this.genesArray);
    }

    @Override
    public String toString() {
        return "genes: "+this.genesArray;
    }

    private void rebalancedGenes(int[] counterOfSpecificGenes, int index) {
        int removedElement = new Random().nextInt(8);
        while(removedElement == index || counterOfSpecificGenes[removedElement]<=1)removedElement = new Random().nextInt(8);
        this.genesArray.remove(removedElement);
        this.genesArray.add(index);
        Collections.sort(this.genesArray);
    }

    public Genes(Genes parentA, Genes parentB) {
        int[] counterOfSpecificGene = new int[8];
        int stDivisionPoint =  new Random().nextInt(32);
        int ndDivisionPoint = new Random().nextInt(32);
        if(stDivisionPoint > ndDivisionPoint) swap(stDivisionPoint, ndDivisionPoint);
        while(stDivisionPoint == ndDivisionPoint) ndDivisionPoint = new Random().nextInt(32);
        int matchingOrder = new Random().nextInt(2);
        // If matching order = 0 |parentA genes|parentB genes|parentA genes|
        // If matching order = 1 |parentB genes|parentA genes|parentB genes|
        for(int i=0; i<32; i++ ){
            if(matchingOrder == 0) {
                if(i <= stDivisionPoint) this.genesArray.add((int)parentA.getGenesArray().get(i));
                else if(i > stDivisionPoint && i <= ndDivisionPoint) this.genesArray.add((int)parentB.getGenesArray().get(i));
                else  this.genesArray.add((int)parentA.getGenesArray().get(i));
                counterOfSpecificGene[this.genesArray.get(i)]++;
            } else {
                if(i <= stDivisionPoint) this.genesArray.add((int)parentB.getGenesArray().get(i));
                else if(i > stDivisionPoint && i <= ndDivisionPoint) this.genesArray.add((int)parentA.getGenesArray().get(i));
                else  this.genesArray.add((int)parentB.getGenesArray().get(i));
                counterOfSpecificGene[this.genesArray.get(i)]++;
            }
        }
        for(int i = 0; i < 8; i++ ) {
            if(counterOfSpecificGene[i] == 0) rebalancedGenes(counterOfSpecificGene, i);
        }
        Collections.sort(this.genesArray);
    }

    private void swap(int a, int b)
    {
        int temp = a;
        a = b;
        b = temp;
    }

    public int getDirection() { return this.genesArray.get(new Random().nextInt(32)); }
    public ArrayList getGenesArray() { return this.genesArray; };

}
