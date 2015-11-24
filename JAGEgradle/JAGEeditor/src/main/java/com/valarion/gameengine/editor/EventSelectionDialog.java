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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import com.valarion.gameengine.editor.XML.DefinableXML;
import com.valarion.gameengine.editor.XML.EmptyNodeImplementation;
import com.valarion.gameengine.editor.XML.NodeDefinition;
import com.valarion.gameengine.editor.XML.XMLDefinition;
import com.valarion.gameengine.editor.XML.XMLImplementation;

/**
 * Class that represents a child selection window for a given node.
 * @author Rubén Tomás Gracia
 *
 */
public class EventSelectionDialog extends JDialog {
	private static final long serialVersionUID = -160003573757341895L;

	private final JPanel contentPanel = new JPanel();
	
	protected XMLDefinition ret = null;
	
	protected JList<XMLDefinition> list;
	
	protected static JButton okButton;
	
	public static XMLDefinition getEvent(Object parent, String title) {
		if(parent instanceof XMLImplementation || parent instanceof EmptyNodeImplementation) {
			LinkedList<XMLDefinition> posibilities = new LinkedList<XMLDefinition>();
			if(parent instanceof XMLImplementation) {
				XMLImplementation p = (XMLImplementation) parent;
				for(int i=0;i<p.getCustomNodesCount();i++) {
					XMLDefinition n = p.getCustomNode(i);
					if(p.canAddChild(n.getName())) {
						posibilities.add(n);
					}
				}
				
				for (int i = 0; i < p.getSharedNodesCount(); i++) {
					NodeDefinition n = p.getSharedNode(i);
					if (p.canAddChild(n.getType().getSimpleName())) {
						Set<Class<?>> set = Editor.instance().getInheritedclasses().get(n.getType());
						if (set != null) {
							for (Class<?> c : set) {
								try {
									/*XMLDefinition def = new XMLDefinition(c.getSimpleName());
									def.setSuperClass(n.getType());
									posibilities.add(def);*/
									XMLDefinition def = ((DefinableXML) c.newInstance()).getNodeDefinition();
									if (def != null) {
										def.setSuperClass(n.getType());
										posibilities.add(((DefinableXML) c.newInstance()).getNodeDefinition());
									}

								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
			else {
				EmptyNodeImplementation p = (EmptyNodeImplementation) parent;
				Set<Class<?>> set = Editor.instance().getInheritedclasses().get(p.getType());
				if(set != null) {
					for (Class<?> c : set) {
						try {
							XMLDefinition def = ((DefinableXML) c.newInstance()).getNodeDefinition();
							if (def != null) {
								def.setSuperClass(p.getType());
								posibilities.add(((DefinableXML) c.newInstance()).getNodeDefinition());
							}
						} catch (InstantiationException | IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			EventSelectionDialog dialog = new EventSelectionDialog(posibilities.toArray(new XMLDefinition[0]));
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setTitle(title);
			dialog.setModal(true);
			dialog.setVisible(true);
			return dialog.ret;
		}
		return null;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			EventSelectionDialog dialog = new EventSelectionDialog(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public EventSelectionDialog(XMLDefinition posibilities[]) {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				
				if (posibilities != null) {
					list = new JList<XMLDefinition>(posibilities);
					list.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				} else {
					list = new JList<XMLDefinition>();

				}
				list.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						okButton.setEnabled(true);
						if (arg0.getClickCount() == 2) {
							ret = list.getSelectedValue();
							setVisible(false);
							dispose();
						}
					}
				});
				scrollPane.setViewportView(list);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setEnabled(false);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						XMLDefinition def = list.getSelectedValue();
						if(def != null) {
							ret = def;
							setVisible(false);
							dispose();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						ret = null;
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
