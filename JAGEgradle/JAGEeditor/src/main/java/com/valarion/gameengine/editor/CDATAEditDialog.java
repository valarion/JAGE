/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 Rubén Tomás Gracia
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ******************************************************************************/
package com.valarion.gameengine.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.valarion.gameengine.editor.XML.AttributeImplementation;

/**
 * Class representing a CDATA text editing window.
 * @author Rubén Tomás Gracia
 *
 */
public class CDATAEditDialog extends JDialog {
	private static final long serialVersionUID = 3135926840544892575L;
	
	private final JPanel contentPanel = new JPanel();
	
	protected JLabel errorLabel;
	
	protected JButton okButton;
	private JTextPane attr;
	
	public static void setAttribute(AttributeImplementation attr) {
		CDATAEditDialog dialog = new CDATAEditDialog(attr);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setTitle("Attribute edit");
		dialog.setModal(true);
		dialog.setVisible(true);
	}

	/**
	 * Launch the application.
	 */
	/**public static void main(String[] args) {
		try {
			CDATAEditDialog dialog = new CDATAEditDialog(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * Create the dialog.
	 */
	public CDATAEditDialog(final AttributeImplementation attribute) {
		setTitle("CDATA edit");
		setBounds(100, 100, 450, 248);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
				contentPanel.setLayout(new BorderLayout(0, 0));
		
				{
					
					attr = new JTextPane();
					JScrollPane jsp = new JScrollPane(attr);
					attr.setSize(100, 100);
					contentPanel.add(jsp);
					attr.setText(attribute != null && attribute.getValue() != null ? attribute.getValue().toString() : "");
					//attr.setColumns(30);
					attr.getDocument().addDocumentListener(new DocumentListener() {
						public void removeUpdate(DocumentEvent e) {
						    test();
						  }
		
						@Override
						public void changedUpdate(DocumentEvent arg0) {
							test();
						}
		
						@Override
						public void insertUpdate(DocumentEvent arg0) {
							test();
						}
						
						public void test() {
							if(attr.getText().length() == 0) {
								okButton.setEnabled(false);
							}
							else {
								
									okButton.setEnabled(true);
							}
						}
					});
				}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							attribute.setValue(attr.getText());
							setVisible(false);
							dispose();
						}
						catch(Exception exception) {
							errorLabel.setText("Invalid value");
							errorLabel.setVisible(true);
						}
					}
				});
				{
					errorLabel = new JLabel("example");
					buttonPane.add(errorLabel);
					errorLabel.setForeground(Color.RED);
					errorLabel.setVisible(false);
				}
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				if(attr.getText().length() == 0) {
					okButton.setEnabled(false);
				}
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
