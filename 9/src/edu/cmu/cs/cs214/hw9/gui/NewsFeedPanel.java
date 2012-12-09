package edu.cmu.cs.cs214.hw9.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Date;
import java.util.List;

import javax.swing.JTextField;

import edu.cmu.cs.cs214.hw9.backend.ClientHandler;
import edu.cmu.cs.cs214.hw9.backend.Status;

public class NewsFeedPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	private FacelookAppGUI container;
	public NewsFeedPanel(final String email, final FacelookAppGUI a) {
		super();
		container = a;
		this.setBackground(Color.decode("#3b5998"));
		this.setPreferredSize(new Dimension(770,539));
		setLayout(null);
		
		ClientHandler ch = new ClientHandler();
		
		String name = ch.getUserInfo(email);
		
		JLabel lblFacelook = new JLabel("Facelook");
		lblFacelook.setFont(new Font("Lucida Fax", Font.PLAIN, 32));
		lblFacelook.setForeground(Color.WHITE);
		lblFacelook.setBounds(12, 13, 199, 32);
		add(lblFacelook);
		
		JButton profile = new JButton(name);
		profile.setBounds(12, 47, 150, 25);
		profile.setFont(new Font("Lucida Fax", Font.PLAIN, 12));
		add(profile);
		
		//==================================================/
		profile.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				container.replace(new ProfilePanel(email, email, a));
				
			}
			
		});
		//==================================================/
		
		
		JButton btnFriends = new JButton("Friends");
		btnFriends.setBounds(172, 47, 97, 25);
		btnFriends.setFont(new Font("Lucida Fax", Font.PLAIN, 12));
		add(btnFriends);
		
		
		//==================================================/
		btnFriends.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				container.replace(new FriendListPanel(email, a));
			}
			
		});
		//==================================================/
		
		
		JLabel lblNewsFeed = new JLabel("News Feed");
		lblNewsFeed.setFont(new Font("Lucida Fax", Font.PLAIN, 20));
		lblNewsFeed.setForeground(Color.WHITE);
		lblNewsFeed.setBounds(12, 87, 166, 40);
		add(lblNewsFeed);
		
		JPanel panel = new JPanel();
		panel.setBounds(22, 147, 719, 379);
		add(panel);
		panel.setLayout(new GridLayout(5, 2, 5, 5));
		
		List<Status> posts = ch.getFriendUpdates(email);
		
		for (Status post : posts) {
			StatusPost p = new StatusPost(post.getPoster().getFullname(), post.getTimestamp(), post.getMessage());
			final String friendEmail = post.getPoster().getFullname();
			p.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					container.replace(new ProfilePanel(friendEmail, email, a));
				}
				
			});
			
			panel.add(p);
		}
		
		final JTextField txtExample = new JTextField();
		txtExample.setText("example@example.com");
		txtExample.setBounds(432, 13, 166, 22);
		add(txtExample);
		txtExample.setColumns(10);
		txtExample.addFocusListener(new FocusListener() {

	        public void focusGained(FocusEvent e) {
	            if (txtExample.getText().equals("example@example.com")) {
	            	txtExample.setText("");
	            }
	        }

	        public void focusLost(FocusEvent e) {
	            if ("".equalsIgnoreCase(txtExample.getText().trim())) {
	            	txtExample.setText("example@example.com");
	            }
	        }
				
		});
		
		JButton btnFindUser = new JButton("Find User");
		btnFindUser.setBounds(598, 13, 97, 22);
		add(btnFindUser);
		
		
		//==================================================/
		btnFindUser.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = new ClientHandler().getUserInfo(txtExample.getText());
				if (name != null) {
					container.replace(new ProfilePanel(txtExample.getText(), email, a));
				}
				else {
					JFrame f = new JFrame();
					f.add(new JLabel("Unknown User!"));
					f.setVisible(true);
				}
			}
			
		});
		//==================================================/

	}
}
