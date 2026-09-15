import jaco.mp3.player.MP3Player;
import javax.swing.*;
import java.io.File;

public class MainMenu{
    JPanel panel;
    JLayeredPane layer;
    JLabel background;
    JButton start,credit, exit;

    public static final int SCREEN_WIDTH = 900, SCREEN_HEIGHT = 600;
    public static final String btnpath ="/GameAssets/GFX/Buttons/";

    String sfxBtn ="/GameAssets/SFX/bump.mp3";
    String addressBackground ="/GameAssets/GFX/Mainmenu/bg.png";
    String StartButton = btnpath + "Start1-1.png";
    String StartButtonHover = btnpath + "Start1-2.png";
    String StartButtonPress = btnpath + "Start1-3.png";

    String CreditsButton = btnpath + "Credits1-1.png";
    String CreditsButtonHover = btnpath + "Credits1-2.png";
    String CreditsButtonPress = btnpath + "Credits1-3.png";

    String ExitButton = btnpath + "Exit1-1.png";
    String ExitButtonHover = btnpath + "Exit1-2.png";
    String ExitButtonPress = btnpath + "Exit1-2.png";

    MainMenu(){
        Background();
        scenes();
        panel();
        Sound();
    }
    public void Background() {
        background = new JLabel();
        background.setIcon(new ImageIcon(getClass().getResource(addressBackground)));
        background.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        background.setVisible(true);

        start = new JButton();
        start.setIcon(new ImageIcon(getClass().getResource(StartButton)));
        start.setBounds(300, 140, 300, 100);
        start.setText("Start Game");
        start.setBorder(null);
        start.setContentAreaFilled(false);
        start.setOpaque(false);
        start.setVisible(true);
        start.setRolloverIcon(new ImageIcon(getClass().getResource(StartButtonHover)));
        start.setSelectedIcon(new ImageIcon(getClass().getResource(StartButtonPress)));

        credit = new JButton();
        credit.setIcon(new ImageIcon(getClass().getResource(CreditsButton)));
        credit.setBounds(300, 270, 300, 100);
        credit.setText("Credits");
        credit.setBorder(null);
        credit.setContentAreaFilled(false);
        credit.setOpaque(false);
        credit.setVisible(true);
        credit.setRolloverIcon(new ImageIcon(getClass().getResource(CreditsButtonHover)));
        credit.setSelectedIcon(new ImageIcon(getClass().getResource(CreditsButtonPress)));

        exit = new JButton();
        exit.setIcon(new ImageIcon(getClass().getResource(ExitButton)));
        exit.setBounds(300, 400, 300, 100);
        exit.setText("High Score");
        exit.setBorder(null);
        exit.setContentAreaFilled(false);
        exit.setOpaque(false);
        exit.setVisible(true);
        exit.setRolloverIcon(new ImageIcon(getClass().getResource(ExitButtonHover)));
        exit.setSelectedIcon(new ImageIcon(getClass().getResource(ExitButtonPress)));
    }
    public void scenes() {
        layer = new JLayeredPane();
        layer.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        layer.add(background, new Integer(1));
        layer.add(start, new Integer(2));
        layer.add(credit, new Integer(2));
        layer.add(exit, new Integer(2));
    }
    public void panel() {
        panel = new JPanel();
        panel.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        panel.setLayout(null);
        panel.add(layer);
        panel.setVisible(true);
    }

    public void Sound() {
        MP3Player sfx = new MP3Player(new File(getClass().getResource(sfxBtn).getFile()));
        sfx.play();
    }
}