package edu.cmu.cs.cs214.hw9.gui;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.SwingConstants;
/*
 * Post length should be at most 150 characters with 50 characters per line
 * When creating a StatusPost object, create an ActionListener that will transition
 * to the poster's profile unless otherwise noted in the comments.
 */
public class StatusPost extends JButton {
	public StatusPost(String name, Date time, String post){
		super();
		setText("<html><center>"+name +" - " +time.toString() + "<br>" + post+"</html>");
		
		setFocusPainted(false);
		setVerticalTextPosition(SwingConstants.TOP);
		setHorizontalTextPosition(SwingConstants.LEFT);
        setMargin(new Insets(0, 0, 0, 0));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
	}
}
