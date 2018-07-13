package VirtualWorld;

import VirtualWorld.Animals.*;
import VirtualWorld.Plants.*;

import java.util.Random;

public abstract class Organism {

    protected int strength;
    protected int initiative;
    protected int positionX;
    protected int positionY;
    private int age;
    int movedTo;
    protected int step;
    protected final World world;

    Organism(int positionX, int positionY, World world) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.world = world;
        this.age = 0;
        this.movedTo = -1;
        this.step = 1;
    }

    abstract void action();
    abstract void movement(int direction, int step);
    public boolean collision(Organism org)
    {
        if (this.strength > org.getStrength()) {
            world.removeOrganism(org);
            return true;
        }
        else {
            world.removeOrganism(this);
            return false;
        }
    }

    void draw()
    {
        world.drawOrganism(positionX, positionY, String.valueOf(this.getClass().getSimpleName()));
    }

    protected void reproduce()
    {
        Random generator = new Random();
        int place = generator.nextInt(4), x = positionX, y = positionY, fail = 0;

        while (x == positionX && y == positionY && fail < 4)
        {
            switch (place)
            {
                case 0:
                    if (!world.isThereOrganism(x, y - 1) && y != 0)
                        y--;
                    break;
                case 1:
                    if (!world.isThereOrganism(x, y + 1) && y != world.getSizeY() - 1)
                        y++;
                    break;
                case 2:
                    if (!world.isThereOrganism(x - 1, y) && x != 0)
                        x--;
                    break;
                case 3:
                    if (!world.isThereOrganism(x + 1, y) && x != world.getSizeX() - 1)
                        x++;
                    break;
            }
            fail++;
            place = (place + 1) % 4;
        }
        if (fail <= 4)
        {
            if (this instanceof Antelope)
                world.addOrganism(new Antelope(x, y, world));
            else if (this instanceof Fox)
                world.addOrganism(new Fox(x, y, world));
            else if (this instanceof Sheep)
                world.addOrganism(new Sheep(x, y, world));
            else if (this instanceof Turtle)
                world.addOrganism(new Turtle(x, y, world));
            else if (this instanceof Wolf)
                world.addOrganism(new Wolf(x, y, world));
            else if (this instanceof Dandelion)
                world.addOrganism(new Dandelion(x, y, world));
            else if (this instanceof DeadlyNightshade)
                world.addOrganism(new DeadlyNightshade(x, y, world));
            else if (this instanceof Grass)
                world.addOrganism(new Grass(x, y, world));
            else if (this instanceof Guarana)
                world.addOrganism(new Guarana(x, y, world));
            else if (this instanceof Hogweed)
                world.addOrganism(new Hogweed(x, y, world));
        }
        world.infoBoard("Created new "+String.valueOf(this.getClass().getSimpleName())+".");
    }

    public int getStrength() {
        return strength;
    }
    public int getInitiative() {
        return initiative;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public int getMovedTo() {
        return movedTo;
    }

    public int getAge() {
        return age;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setMovedTo(int movedTo) {
        this.movedTo = movedTo;
    }

    public void increaseAge() { age++; }

    public void strengthPlus3() {strength += 3;}
}
