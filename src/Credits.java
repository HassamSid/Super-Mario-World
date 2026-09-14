import javax.swing.*;

public class Credits {
    public static final int SCREEN_WIDTH = 900, SCREEN_HEIGHT = 600;
    String credits = Game.Directory+"\\GameAssets\\GFX\\Credits\\creditsbg.png";
    String addressBackground = Game.Directory+"\\GameAssets\\GFX\\MainMenu\\bgeven.png";

    JPanel panel;
    JLabel background,credit;
    JLayeredPane layer;
    @SuppressWarnings("removal")
    Credits(){
        background = new JLabel();
        background.setIcon(new ImageIcon(addressBackground));
        background.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        background.setVisible(true);

        credit = new JLabel();
        credit.setIcon(new ImageIcon(credits));
        credit.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        credit.setVisible(true);

        layer = new JLayeredPane();
        layer.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        layer.add(background, new Integer(1));
        layer.add(credit, new Integer(2));

        panel = new JPanel();
        panel.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        panel.setLayout(null);
        panel.add(layer);
        panel.setVisible(true);
    }
}