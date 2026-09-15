import jaco.mp3.player.MP3Player;

import javax.swing.*;
import java.io.File;

public abstract class Mobs { //Abstraction

    public static final String imgpath ="/GameAssets/GFX/Enemy/";

    private String sfxDie = "/GameAssets/SFX/grow.mp3"; //Encapsulation

    abstract void Sound(); //Abstraction

    public String getSfxDie() { //Encapsulation
        return sfxDie;
    }
}

class Goomba extends Mobs { //Inheritance

    JLabel Goomba;

    String goomba = imgpath + "goomb.png";
    String goombaRight = imgpath + "rgoomba.gif";
    String goombaLeft = imgpath + "lgoomba.gif";
    String goombaPress = imgpath + "goombdie.png";

    Goomba() {
        Goomba = new JLabel();
        Goomba.setIcon(new ImageIcon(getClass().getResource(goomba)));
        Goomba.setBounds(524, 480, 50, 50);
        Goomba.setOpaque(false);
        Goomba.setVisible(true);
    }

    @Override //Polymorphism
    void Sound() {
        MP3Player sfx = new MP3Player(new File(getClass().getResource(getSfxDie()).getFile()));
        sfx.play();
    }
}