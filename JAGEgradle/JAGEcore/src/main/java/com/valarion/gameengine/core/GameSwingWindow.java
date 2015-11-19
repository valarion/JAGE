package com.valarion.gameengine.core;

import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import java.awt.BorderLayout;
import java.awt.DisplayMode;

import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.awt.event.ActionEvent;

public class GameSwingWindow {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameSwingWindow window = new GameSwingWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GameSwingWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		final JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setBounds(12, 115, 408, 22);
		frame.getContentPane().add(comboBox);
		
		String lastresolution = null;
		List<DisplayMode> displaymodes = Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayModes());
		Collections.reverse(displaymodes);
		for(DisplayMode d : displaymodes) {
			String newresolution = d.getWidth()+"x"+d.getHeight();
			if(! newresolution.equalsIgnoreCase(lastresolution)) {
				comboBox.addItem(newresolution);
				lastresolution = newresolution;
				
			}
		}
		
		final JCheckBox chckbxFullScreen = new JCheckBox("FullScreen");
		chckbxFullScreen.setSelected(true);
		chckbxFullScreen.setBounds(12, 51, 113, 25);
		frame.getContentPane().add(chckbxFullScreen);
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fs = Boolean.toString(chckbxFullScreen.isSelected());
				String res[] = ((String) comboBox.getSelectedItem()).split("x");
				
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				GameCore.main(new String[]{res[0],res[1],fs});
			}
		});
		btnOk.setBounds(12, 215, 97, 25);
		frame.getContentPane().add(btnOk);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
		btnCancel.setBounds(323, 215, 97, 25);
		frame.getContentPane().add(btnCancel);
	}
}
