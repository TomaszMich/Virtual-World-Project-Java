package VirtualWorld.Animals;

import VirtualWorld.Animal;
import VirtualWorld.Organism;
import VirtualWorld.World;

import java.util.Random;

public class Turtle extends Animal {

    public Turtle(int positionX, int positionY, World world)
    {
        super(positionX, positionY, world);
        strength = 2;
        initiative = 1;
    }

    @Override
    public void action() {
        super.action();
        Random generator = new Random();
        if (generator.nextInt(100) < 75)
            goBack(this);
    }

    @Override
    public boolean collision(Organism org) {
        if (org.getStrength() < 5) {
            goBack(org);
            return true;
        }
        else
            return super.collision(org);
    }
}
