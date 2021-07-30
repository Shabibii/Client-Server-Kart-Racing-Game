/**
 *
 * @author 1819364
 */ 
import javax.swing.JFrame; // use JFrame to present information window 
import java.awt.Font; // set font of text in labels
import java.awt.FlowLayout; // specifies how components are arranged
import javax.swing.JLabel; // displays text and images
import javax.swing.SwingConstants; // common constants used with Swing
import javax.swing.Icon; // interface used to manipulate images
import javax.swing.ImageIcon; // loads images

public class RaceTrackInfoFrame extends JFrame
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    private JLabel descriptionTextLabel;
    private JLabel kartLabel;

    public RaceTrackInfoFrame(int selectedkart)
    {
        super( "ARU KART SPORT 2021" );
        setLayout( new FlowLayout() ); // set frame layout

        // JLabel constructor with description text as argument
        descriptionTextLabel = new JLabel( "<html>WELCOME DRIVER<br><br>Some tips:<br>\u26D4 Stay on the road<br>\uD83D\uDE97 Karts may not collide<br>\u23F0 Close and race!<br>i) Increase speed with key press<br>i) Shortcut: 'M' (MUTE)</html>" );
        descriptionTextLabel.setFont( new Font( "DialogInput", 1, 14 ) ); // set font, make bold, resize to 14
        add( descriptionTextLabel ); // add descriptionTextLabel to JFrame

        // Get icon image based on selected kart
        Icon kartIcon = selectedkart == 0 ? 
        new ImageIcon( getClass().getResource( "C1_ARU\\kartAru0.png" ) )
        :
        new ImageIcon( getClass().getResource( "C2_OPP\\kartOpp0.png" ) );

        String kartName = selectedkart == 0 ? "ARU" : "OPP";
        
        kartLabel = new JLabel();
        kartLabel.setText( "<html>Kart " + kartName + " keys<br>\u2191 | \u2193 | \u2192 | \u2190<html>" );
        kartLabel.setFont( new Font( "Monospaced", 1, 18 ) );
        kartLabel.setIcon( kartIcon );
        kartLabel.setHorizontalTextPosition( SwingConstants.CENTER );
        kartLabel.setVerticalTextPosition( SwingConstants.BOTTOM );
        add( kartLabel ); // add kart label to JFrame
    }
}