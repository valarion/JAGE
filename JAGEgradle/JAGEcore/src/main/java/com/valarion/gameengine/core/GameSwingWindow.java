package com.valarion.gameengine.core;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;

public class GameSwingWindow {

	private JFrame frmJustanothergameengine;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameSwingWindow window = new GameSwingWindow();
					window.frmJustanothergameengine.setVisible(true);
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
		frmJustanothergameengine = new JFrame();
		frmJustanothergameengine.setTitle("JustAnotherGameEngine");
		frmJustanothergameengine.setBounds(100, 100, 450, 300);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) ((dimension.getWidth() - frmJustanothergameengine.getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - frmJustanothergameengine.getHeight()) / 2);
	    frmJustanothergameengine.setLocation(x, y);
		frmJustanothergameengine.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmJustanothergameengine.getContentPane().setLayout(null);
		
		final JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setBounds(12, 115, 408, 22);
		frmJustanothergameengine.getContentPane().add(comboBox);
		
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
		frmJustanothergameengine.getContentPane().add(chckbxFullScreen);
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fs = Boolean.toString(chckbxFullScreen.isSelected());
				String res[] = ((String) comboBox.getSelectedItem()).split("x");
				
				frmJustanothergameengine.dispatchEvent(new WindowEvent(frmJustanothergameengine, WindowEvent.WINDOW_CLOSING));
				GameCore.main(new String[]{res[0],res[1],fs});
			}
		});
		btnOk.setBounds(12, 215, 97, 25);
		frmJustanothergameengine.getContentPane().add(btnOk);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmJustanothergameengine.dispatchEvent(new WindowEvent(frmJustanothergameengine, WindowEvent.WINDOW_CLOSING));
			}
		});
		btnCancel.setBounds(323, 215, 97, 25);
		frmJustanothergameengine.getContentPane().add(btnCancel);
	}
}
