package QuantTTT;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/**
 * Created by ryanleung on 11/18/17.
 */
public class XOButton extends JButton {

    public String marks;
    public XOButton() {
        marks = "";

    }

    public void classify(String classification) {
        marks = classification;
    }

}
