package VirtualWorld;

import VirtualWorld.Animals.*;
import VirtualWorld.Plants.*;
import VirtualWorld.Tools.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

public class World extends JFrame implements KeyListener {

    private int sizeX, sizeY, humanDirection, ability, lines;
    private final Vector<Organism> organisms = new Vector<>(0);
    private final JPanel panel = new JPanel();
    private final JPanel board = new JPanel();
    private JButton b;
    private JDialog pop;
    private final Image[] pics = new Image[11];

    public World(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.humanDirection = -1;
        this.ability = -1;

        createOrganisms();
        loadImages();
        createWindow();
        drawAllOrganisms();
        setVisible(true);
    }

    private void createWindow() {
        setSize(Size.image() * sizeX + 3 * Size.margin() + Size.infoBoard(), Size.image() * sizeY + 3 * Size.margin() + 100);
        setLocationRelativeTo(null); //centers window one the screen
        setDefaultCloseOperation(EXIT_ON_CLOSE); //closes aplication on clicking X
        setTitle("Virtual World - Tomasz Michalski 171890");
        setFocusable(true);
        requestFocus();
        addKeyListener(this);
        setLayout(null);

        //JPanel with organisms
        panel.setLayout(null);
        panel.setBounds(Size.margin(), Size.margin(), Size.image() * sizeX, Size.image() * sizeY);
        panel.setBackground(Color.white);
        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int x = e.getX() / Size.image();
                int y = e.getY() / Size.image();
                if (!isThereOrganism(x, y))
                    addDialog(x, y);
            }
            @Override
            public void mouseClicked(MouseEvent e) { }
            @Override
            public void mousePressed(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }
        });
        add(panel);

        //JPanel as InfoBoard
        board.setLayout(null);
        board.setBounds(Size.margin() * 2 + sizeX * Size.image(), Size.margin(), Size.infoBoard(), Size.image() * sizeY);
        add(board);

        //JButton
        b = new JButton("Make turn");
        b.setBounds(Size.margin(), Size.image() * sizeY + 2 * Size.margin(), 100, 40);
        b.addActionListener(e -> executeTur());
        add(b);

        //JMenuBar
        JMenuBar mb = new JMenuBar();
        JMenu menu = new JMenu("Game");
        JMenuItem save = new JMenuItem("Save game");
        JMenuItem load = new JMenuItem("Load game");
        save.addActionListener(e -> save());
        load.addActionListener(e -> load());
        menu.add(save);
        menu.add(load);
        mb.add(menu);
        setJMenuBar(mb);
    }

    private void executeTur() {
        lines = 0;
        board.removeAll();
        requestFocus();
        sort("age", organisms);
        sort("initiative", organisms);

        for (int i = 0; i < organisms.size(); i++) {
            if (organisms.get(i).getAge() != 0)
                organisms.get(i).action();
        }
        increaseAges();
        panel.removeAll();
        drawAllOrganisms();
        panel.revalidate();
        panel.repaint();

        if (ability >= 0) {
            if (ability < 5)
                infoBoard("Ability is on");
            ability++;
        }
        if (ability == 10)
            ability = -1;
    }

    private void save() {
        String name;
        PrintWriter out = null;
        try {
            out = new PrintWriter("src/save.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (out != null) {
            out.print(sizeX + "\n" + sizeY + "\n" + organisms.size());
            for (int i = 0; i < organisms.size(); i++) {
                name = String.valueOf(organisms.get(i).getClass().getSimpleName());
                out.print("\n" + name + "\n" + organisms.get(i).getPositionX() + "\n" + organisms.get(i).getPositionY() + "\n");
                out.print(organisms.get(i).getStrength() + "\n" + organisms.get(i).getMovedTo() + "\n" + organisms.get(i).getAge());
            }
            out.close();
        }
        lines = 0;
        board.removeAll();
        infoBoard("Your game has been saved");
    }

    private void load() {
        Scanner sc = null;
        String name;
        int quantity, posX, posY, str, mvd, age;
        try {
            sc = new Scanner(new File("src/save.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (sc != null) {
            organisms.clear();
            sizeX = sc.nextInt();
            sizeY = sc.nextInt();
            quantity = sc.nextInt();

            for (int i = 0; i < quantity; i++) {
                name = sc.next();
                posX = sc.nextInt();
                posY = sc.nextInt();
                str = sc.nextInt();
                mvd = sc.nextInt();
                age = sc.nextInt();
                loadOrganism(name, posX, posY, str, mvd, age);
            }
            sc.close();
        }
        setSize(40 * sizeX + 100, Size.image() * sizeY + 130);
        panel.setBounds(Size.margin(), Size.margin(), Size.image() * sizeX, Size.image() * sizeY);
        board.setBounds(Size.margin() * 2 + sizeX * Size.image(), Size.margin(), 200, Size.image() * sizeY);
        b.setBounds(Size.margin(), Size.image() * sizeY + 2 * Size.margin(), 100, 40);

        panel.removeAll();
        drawAllOrganisms();
        panel.revalidate();
        panel.repaint();
        lines = 0;
        board.removeAll();
        infoBoard("Your game has been loaded");
    }

    private void addDialog(int posX, int posY) {
        pop = new JDialog(this, "Add organism", true);
        pop.setSize(400, 100);
        pop.setLayout(null);
        pop.setLocationRelativeTo(this);
        String names[] = {"Antelope", "Fox", "Human", "Sheep", "Turtle", "Wolf", "Dandelion", "DeadlyNightshade", "Grass", "Guarana", "Hogweed"};
        JComboBox<String> cb = new JComboBox<>(names);
        cb.setBounds(Size.margin(), Size.margin(), 200, 30);
        pop.add(cb);
        JButton addButton = new JButton("ADD");
        addButton.addActionListener(e -> {
            String name = cb.getItemAt(cb.getSelectedIndex());
            addOrganismByName(name, posX, posY);
            drawOrganism(posX, posY, name);
            panel.revalidate();
            panel.repaint();
            pop.dispose();
        });
        addButton.setBounds(250, Size.margin(), 100, 30);
        pop.add(addButton);
        pop.setVisible(true);
    }

    void infoBoard(String message) {
        if (lines < sizeY) {
            JLabel l = new JLabel(message);
            l.setBounds(0, lines * Size.image(), super.getWidth(), Size.image());
            board.add(l);
            board.repaint();
            lines++;
        }
    }

    private void drawAllOrganisms() {
        for (int i = 0; i < organisms.size(); i++) {
            organisms.get(i).draw();
        }
    }

    void drawOrganism(int posX, int posY, String name) {
        Image image = getImage(name);
        if (image != null) {
            JLabel picLabel = new JLabel(new ImageIcon(image));
            picLabel.setBounds(posX * Size.image(), posY * Size.image(), Size.image(), Size.image());
            panel.add(picLabel);
        }
    }

    private void loadOrganism(String name, int posX, int posY, int str, int mvd, int age) {
        Organism temp;

        switch (name) {
            case "Antelope":
                temp = new Antelope(posX, posY, this);
                break;
            case "Fox":
                temp = new Fox(posX, posY, this);
                break;
            case "Human":
                temp = new Human(posX, posY, this);
                break;
            case "Sheep":
                temp = new Sheep(posX, posY, this);
                break;
            case "Turtle":
                temp = new Turtle(posX, posY, this);
                break;
            case "Wolf":
                temp = new Wolf(posX, posY, this);
                break;
            case "Dandelion":
                temp = new Dandelion(posX, posY, this);
                break;
            case "DeadlyNightshade":
                temp = new DeadlyNightshade(posX, posY, this);
                break;
            case "Grass":
                temp = new Grass(posX, posY, this);
                break;
            case "Guarana":
                temp = new Guarana(posX, posY, this);
                break;
            case "Hogweed":
                temp = new Hogweed(posX, posY, this);
                break;
            default:
                temp = new Sheep(posX, posY, this);
                break;
        }
        temp.setAge(age);
        temp.setStrength(str);
        temp.setMovedTo(mvd);
        organisms.add(temp);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
                setHumanDirection(Direction.Up());
                break;
            case KeyEvent.VK_DOWN:
                setHumanDirection(Direction.Down());
                break;
            case KeyEvent.VK_LEFT:
                setHumanDirection(Direction.Left());
                break;
            case KeyEvent.VK_RIGHT:
                setHumanDirection(Direction.Right());
                break;
            case KeyEvent.VK_A:
                if (ability == -1)
                    ability = 0;
                break;
            case KeyEvent.VK_SPACE:
                executeTur();
                break;
        }
    }

    private void loadImages() {
        BufferedImage antelope, fox, human, sheep, turtle, wolf, dandelion, dn, grass, guarana, hogweed;
        try {
            antelope = ImageIO.read(new File("src/Img/Antelope.png"));
            Image newAntelope = antelope.getScaledInstance(Size.image(), Size.image(), Image.SCALE_DEFAULT);
            pics[0] = newAntelope;
            fox = ImageIO.read(new File("src/Img/Fox.png"));
            Image newFox = fox.getScaledInstance(Size.image(), Size.image(), Image.SCALE_DEFAULT);
            pics[1] = newFox;
            human = ImageIO.read(new File("src/Img/Human.png"));
            Image newHuman = human.getScaledInstance(Size.image(), Size.image(), Image.SCALE_DEFAULT);
            pics[2] = newHuman;
            sheep = ImageIO.read(new File("src/Img/Sheep.png"));
            Image newSheep = sheep.getScaledInstance(Size.image(), Size.image(), Image.SCALE_DEFAULT);
            pics[3] = newSheep;
            turtle = ImageIO.read(new File("src/Img/Turtle.png"));
            Image newTurtle = turtle.getScaledInstance(Size.image(), Size.image(), Image.SCALE_DEFAULT);
            pics[4] = newTurtle;
            wolf = ImageIO.read(new File("src/Img/Wolf.png"));
            Image newWolf = wolf.getScaledInstance(Size.image(), Size.image(), Image.SCALE_DEFAULT);
            pics[5] = newWolf;
            dandelion = ImageIO.read(new File("src/Img/Dandelion.png"));
            Image newDandelion = dandelion.getScaledInstance(Size.image(), Size.image(), Image.SCALE_DEFAULT);
            pics[6] = newDandelion;
            dn = ImageIO.read(new File("src/Img/DeadlyNightshade.png"));
            Image newDN = dn.getScaledInstance(Size.image(), Size.image(), Image.SCALE_DEFAULT);
            pics[7] = newDN;
            grass = ImageIO.read(new File("src/Img/Grass.png"));
            Image newGrass = grass.getScaledInstance(Size.image(), Size.image(), Image.SCALE_DEFAULT);
            pics[8] = newGrass;
            guarana = ImageIO.read(new File("src/Img/Guarana.png"));
            Image newGuarana = guarana.getScaledInstance(Size.image(), Size.image(), Image.SCALE_DEFAULT);
            pics[9] = newGuarana;
            hogweed = ImageIO.read(new File("src/Img/Hogweed.png"));
            Image newHogweed = hogweed.getScaledInstance(Size.image(), Size.image(), Image.SCALE_DEFAULT);
            pics[10] = newHogweed;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Image getImage(String name) {
        switch (name) {
            case "Antelope":
                return pics[0];
            case "Fox":
                return pics[1];
            case "Human":
                return pics[2];
            case "Sheep":
                return pics[3];
            case "Turtle":
                return pics[4];
            case "Wolf":
                return pics[5];
            case "Dandelion":
                return pics[6];
            case "DeadlyNightshade":
                return pics[7];
            case "Grass":
                return pics[8];
            case "Guarana":
                return pics[9];
            case "Hogweed":
                return pics[10];
            default:
                return null;
        }
    }

    private void addOrganismByName(String name, int posX, int posY) {
        switch (name) {
            case "Antelope":
                addOrganism(new Antelope(posX, posY, this));
                break;
            case "Fox":
                addOrganism(new Fox(posX, posY, this));
                break;
            case "Human":
                addOrganism(new Human(posX, posY, this));
                break;
            case "Sheep":
                addOrganism(new Sheep(posX, posY, this));
                break;
            case "Turtle":
                addOrganism(new Turtle(posX, posY, this));
                break;
            case "Wolf":
                addOrganism(new Wolf(posX, posY, this));
                break;
            case "Dandelion":
                addOrganism(new Dandelion(posX, posY, this));
                break;
            case "DeadlyNightshade":
                addOrganism(new DeadlyNightshade(posX, posY, this));
                break;
            case "Grass":
                addOrganism(new Grass(posX, posY, this));
                break;
            case "Guarana":
                addOrganism(new Guarana(posX, posY, this));
                break;
            case "Hogweed":
                addOrganism(new Hogweed(posX, posY, this));
                break;
        }
    }

    public void removeOrganism(Organism org) {
        infoBoard(String.valueOf(org.getClass().getSimpleName()) + " died.");
        organisms.remove(org);
    }

    void addOrganism(Organism org) {
        organisms.add(org);
    }

    boolean isThereOrganism(int x, int y) {
        for (int i = 0; i < organisms.size(); i++) {
            if (organisms.get(i).getPositionX() == x && organisms.get(i).getPositionY() == y)
                return true;
        }
        return false;
    }

    public int findOrganism(int x, int y) {
        for (int i = 0; i < organisms.size(); i++) {
            if (organisms.get(i).getPositionX() == x && organisms.get(i).getPositionY() == y)
                return i;
        }
        return -1;
    }

    public int checkForCollision(Organism org) {
        for (int i = 0; i < organisms.size(); i++) {
            if (organisms.get(i).getPositionX() == org.getPositionX() && organisms.get(i).getPositionY() == org.getPositionY() && organisms.get(i) != org)
                return i;
        }
        return -1;
    }

    private void increaseAges()
    {
        for (int i = 0; i < organisms.size(); i++)
            organisms.get(i).increaseAge();
    }

    private void createOrganisms() {
        int[] a = generateXandY();
        int count = 0, type = 0, num = 0;
        Random generator = new Random();

        addOrganism(new Human(a[0], a[1], this));

        while (count < Fill.p20(sizeX, sizeY)) {
            a = generateXandY();
            if (num >= 20)
                type = generator.nextInt(10);

            switch (type) {
                case 0:
                    addOrganism(new Hogweed(a[0], a[1], this));
                    break;
                case 1:
                    addOrganism(new Wolf(a[0], a[1], this));
                    break;
                case 2:
                    addOrganism(new Sheep(a[0], a[1], this));
                    break;
                case 3:
                    addOrganism(new Fox(a[0], a[1], this));
                    break;
                case 4:
                    addOrganism(new Turtle(a[0], a[1], this));
                    break;
                case 5:
                    addOrganism(new Antelope(a[0], a[1], this));
                    break;
                case 6:
                    addOrganism(new Grass(a[0], a[1], this));
                    break;
                case 7:
                    addOrganism(new Dandelion(a[0], a[1], this));
                    break;
                case 8:
                    addOrganism(new Guarana(a[0], a[1], this));
                    break;
                case 9:
                    addOrganism(new DeadlyNightshade(a[0], a[1], this));
                    break;
            }
            count++;
            if (num < 20) {
                type = (type + 1) % 10;
                num++;
            }
        }
        for (int i = 0; i < organisms.size(); i++) {
            organisms.get(i).increaseAge();
        }
    }

    private int[] generateXandY() {
        int[] coordinates = new int[2];
        Random generator = new Random();

        do {
            coordinates[0] = generator.nextInt(sizeX);
            coordinates[1] = generator.nextInt(sizeY);
        } while (isThereOrganism(coordinates[0], coordinates[1]));

        return coordinates;
    }

    private void sort(final String field, List<Organism> itemLocationList) {
        Collections.sort(itemLocationList, new Comparator<>() {
            @Override
            public int compare(Organism o1, Organism o2) {
                if (field.equals("initiative")) {
                    return o2.getInitiative() - o1.getInitiative();
                }
                if (field.equals("age")) {
                    return o2.getAge() - o1.getAge();
                }
                return 0;
            }
        });
    }

    public Organism getOneOrganism(int i) {
        return organisms.get(i);
    }

    int getSizeX() { return sizeX; }

    int getSizeY() { return sizeY; }

    public int getHumanDirection() {
        return humanDirection;
    }

    public void setHumanDirection(int humanDirection) {
        this.humanDirection = humanDirection;
    }

    public int getAbility() {
        return ability;
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) { }
}