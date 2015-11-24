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
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.valarion.gameengine.editor.XML.AttributeImplementation;
import com.valarion.gameengine.editor.XML.DefinableXML;
import com.valarion.gameengine.editor.XML.EmptyNodeImplementation;
import com.valarion.gameengine.editor.XML.XMLDefinition;
import com.valarion.gameengine.editor.XML.XMLImplementation;

/**
 * Class representing an event tree editing window.
 * @author Rubén Tomás Gracia
 *
 */
public class EventTreeWindow {

	private JFrame frame;

	protected static JTree eventTree;

	protected static DefaultMutableTreeNode selected;

	protected static JMenuItem mntmMoveUp;
	protected static JMenuItem mntmMoveDown;
	protected static JMenuItem mntmAddBefore;
	protected static JMenuItem mntmAddAfter;
	protected static JMenuItem mntmAddChildStart;
	protected static JMenuItem mntmAddChildEnd;
	protected static JMenuItem mntmChange;
	protected static JMenuItem mntmRemove;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EventTreeWindow window = new EventTreeWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public EventTreeWindow() throws InstantiationException, IllegalAccessException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private void initialize() throws InstantiationException, IllegalAccessException {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JScrollPane eventTreeScrollPane = new JScrollPane();
		frame.getContentPane().add(eventTreeScrollPane, BorderLayout.CENTER);

		eventTree = new JTree(
				new DefaultTreeModel(((DefinableXML)(Editor.instance().getEventsclasses().get("RootNode").newInstance())).getNodeDefinition().createImplementationTree(), true));
		eventTree.setUI(new BasicTreeUI() {
			protected boolean shouldPaintExpandControl(final TreePath path, final int row, final boolean isExpanded,
					final boolean hasBeenExpanded, final boolean isLeaf) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				return node.getChildCount() > 0;
			}
		});
		eventTree.setToggleClickCount(0);
		eventTreeScrollPane.setViewportView(eventTree);

		JPopupMenu rightClickMenu = new JPopupMenu();
		addPopup(eventTree, rightClickMenu);

		mntmMoveUp = new JMenuItem("Move up");
		mntmMoveUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (selected != null && selected.getParent() != null
						&& selected.getParent() instanceof DefaultMutableTreeNode
						&& selected.getPreviousSibling() != null) {
					int startposition = selected.getParent().getIndex(selected) - 1;
					((DefaultMutableTreeNode) selected.getParent()).insert(selected, startposition);
					selected = null;
					updateTree();
				}
			}
		});
		rightClickMenu.add(mntmMoveUp);

		mntmMoveDown = new JMenuItem("Move down");
		mntmMoveDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selected != null && selected.getParent() != null
						&& selected.getParent() instanceof DefaultMutableTreeNode
						&& selected.getNextSibling() != null) {
					int startposition = selected.getParent().getIndex(selected);
					((DefaultMutableTreeNode) selected.getParent()).insert(selected.getNextSibling(), startposition);
					selected = null;
					updateTree();
				}
			}
		});
		rightClickMenu.add(mntmMoveDown);

		JSeparator separator = new JSeparator();
		rightClickMenu.add(separator);

		mntmAddBefore = new JMenuItem("Add before");
		mntmAddBefore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selected != null && selected.getParent() != null
						&& selected.getParent() instanceof DefaultMutableTreeNode
						&& ((DefaultMutableTreeNode) selected.getParent())
								.getUserObject() instanceof XMLImplementation) {
					Object uo = ((DefaultMutableTreeNode) selected.getParent()).getUserObject();
					XMLDefinition def = EventSelectionDialog.getEvent(uo, "Add before");
					DefaultMutableTreeNode implementation = (def != null ? def.createImplementationTree() : null);
					if (implementation != null) {
						((DefaultMutableTreeNode) selected.getParent()).insert(implementation,
								((DefaultMutableTreeNode) selected.getParent()).getIndex(selected));
						updateTree();
					}
				}
			}
		});
		rightClickMenu.add(mntmAddBefore);

		mntmAddAfter = new JMenuItem("Add after");
		mntmAddAfter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selected != null && selected.getParent() != null
						&& selected.getParent() instanceof DefaultMutableTreeNode
						&& ((DefaultMutableTreeNode) selected.getParent())
								.getUserObject() instanceof XMLImplementation) {
					Object uo = ((DefaultMutableTreeNode) selected.getParent()).getUserObject();
					XMLDefinition def = EventSelectionDialog.getEvent(uo, "Add after");
					DefaultMutableTreeNode implementation = (def != null ? def.createImplementationTree() : null);
					if (implementation != null) {
						((DefaultMutableTreeNode) selected.getParent()).insert(implementation,
								((DefaultMutableTreeNode) selected.getParent()).getIndex(selected) + 1);
						updateTree();
					}
				}
			}
		});
		rightClickMenu.add(mntmAddAfter);

		JSeparator separator_1 = new JSeparator();
		rightClickMenu.add(separator_1);

		mntmAddChildStart = new JMenuItem("Add child start");
		mntmAddChildStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (selected != null && selected.getUserObject() instanceof XMLImplementation) {
					Object uo = selected.getUserObject();
					XMLDefinition def = EventSelectionDialog.getEvent(uo, "Add child start");
					DefaultMutableTreeNode implementation = (def != null ? def.createImplementationTree() : null);
					if (implementation != null) {
						selected.insert(implementation, 0);
						eventTree.expandPath(new TreePath(selected.getPath()));
						updateTree();
					}
				}
			}
		});
		rightClickMenu.add(mntmAddChildStart);

		mntmAddChildEnd = new JMenuItem("Add child end");
		mntmAddChildEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selected != null && selected.getUserObject() instanceof XMLImplementation) {
					Object uo = selected.getUserObject();
					XMLDefinition def = EventSelectionDialog.getEvent(uo, "Add child end");
					DefaultMutableTreeNode implementation = (def != null ? def.createImplementationTree() : null);
					if (implementation != null) {
						selected.insert(implementation, selected.getChildCount());
						eventTree.expandPath(new TreePath(selected.getPath()));
						updateTree();
					}
				}
			}
		});
		rightClickMenu.add(mntmAddChildEnd);

		JSeparator separator_2 = new JSeparator();
		rightClickMenu.add(separator_2);

		mntmRemove = new JMenuItem("Remove");
		mntmRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (selected != null && selected.getParent() != null
						&& selected.getParent() instanceof DefaultMutableTreeNode) {
					DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selected.getParent();
					int index = parent.getIndex(selected);
					parent.remove(selected);
					Object child = null;
					if (selected.getUserObject() instanceof XMLImplementation) {
						XMLImplementation uo = (XMLImplementation) selected.getUserObject();
						if (uo.getSuperClass() != null) {
							child = ((XMLImplementation) parent.getUserObject()).refreshChildrenCount(parent)
									.mustAddChild(uo.getSuperClass().getSimpleName());

						} else {
							child = ((XMLImplementation) parent.getUserObject()).refreshChildrenCount(parent)
									.mustAddChild(uo.getName());
						}
					} else if (selected.getUserObject() instanceof EmptyNodeImplementation) {
						child = ((XMLImplementation) parent.getUserObject()).refreshChildrenCount(parent).mustAddChild(
								((EmptyNodeImplementation) selected.getUserObject()).getType().getSimpleName());
					}
					if (child != null) {
						if (child instanceof XMLDefinition) {
							parent.insert(((XMLDefinition) child).createImplementationTree(), index);
						} else {
							parent.insert(new DefaultMutableTreeNode(child), index);
						}
					}
					updateTree();
				}
			}
		});

		mntmChange = new JMenuItem("Change");
		mntmChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selected != null && selected.getParent() != null
						&& selected.getParent() instanceof DefaultMutableTreeNode) {
					int index = selected.getParent().getIndex(selected);
					DefaultMutableTreeNode parent = ((DefaultMutableTreeNode) selected.getParent());
					Object uo = ((DefaultMutableTreeNode) selected.getParent()).getUserObject();
					((DefaultMutableTreeNode) selected.getParent()).remove(selected);
					XMLDefinition def = EventSelectionDialog.getEvent(uo, "Change");
					DefaultMutableTreeNode implementation = (def != null ? def.createImplementationTree() : null);
					if (implementation != null) {
						parent.insert(implementation, index);
						updateTree();
					} else {
						parent.insert(selected, index);
					}
				}
			}
		});
		rightClickMenu.add(mntmChange);

		JSeparator separator_3 = new JSeparator();
		rightClickMenu.add(separator_3);
		rightClickMenu.add(mntmRemove);
	}

	protected static void updateTree() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				eventTree.updateUI();
				eventTree.setUI(new BasicTreeUI() {
					protected boolean shouldPaintExpandControl(final TreePath path, final int row, final boolean isExpanded,
							final boolean hasBeenExpanded, final boolean isLeaf) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
						return node.getChildCount() > 0;
					}
				});
			}
		});
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				} else {
					if (e.getClickCount() == 2) {
						TreePath path = eventTree.getPathForLocation(e.getX(), e.getY());
						if (path != null) {
							DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
							Object userobject = node.getUserObject();

							if (userobject instanceof XMLImplementation) {
								selected = node;
								if (selected != null) {
									DefaultMutableTreeNode parent = ((DefaultMutableTreeNode) selected.getParent());
									if (parent != null && parent instanceof DefaultMutableTreeNode
											&& parent.getUserObject() instanceof XMLImplementation
											&& ((XMLImplementation) parent.getUserObject()).refreshChildrenCount(parent)
													.canAddChildren()) {
										Object uo = ((DefaultMutableTreeNode) selected.getParent()).getUserObject();
										XMLDefinition def = EventSelectionDialog.getEvent(uo, "Add after");
										DefaultMutableTreeNode implementation = (def != null
												? def.createImplementationTree() : null);
										if (implementation != null) {
											((DefaultMutableTreeNode) selected.getParent()).insert(implementation,
													((DefaultMutableTreeNode) selected.getParent()).getIndex(selected)
															+ 1);
											updateTree();
										}
									}
								}
								/*
								 * else if(selected != null &&
								 * selected.getUserObject() instanceof
								 * XMLImplementation) { Object uo =
								 * selected.getUserObject(); XMLDefinition def =
								 * EventSelectionDialog.getEvent(uo);
								 * DefaultMutableTreeNode implementation = (def
								 * != null ? def.createImplementationTree() :
								 * null); if (implementation != null) {
								 * selected.insert(implementation,
								 * selected.getChildCount()); updateTree(); } }
								 */
							}
							else if (userobject instanceof AttributeImplementation) {
								AttributeImplementation uo = (AttributeImplementation)userobject;
								if (uo.getName().equals("CDATA")) {
									CDATAEditDialog.setAttribute((AttributeImplementation) userobject);
								} else {
									AttributeEditDialog.setAttribute((AttributeImplementation) userobject);
								}
								updateTree();
							}
						}
					}
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				TreePath path = eventTree.getPathForLocation(e.getX(), e.getY());
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				eventTree.setSelectionPath(path);
				if (node.getAllowsChildren()) {
					selected = node;
					DefaultMutableTreeNode prev = node.getPreviousSibling();
					mntmMoveUp.setEnabled(false);
					mntmMoveDown.setEnabled(false);
					mntmAddBefore.setEnabled(false);
					mntmAddAfter.setEnabled(false);
					mntmAddChildStart.setEnabled(false);
					mntmAddChildEnd.setEnabled(false);
					mntmChange.setEnabled(false);
					mntmRemove.setEnabled(false);
					if (prev != null && prev.getAllowsChildren()) {
						mntmMoveUp.setEnabled(true);
					}
					DefaultMutableTreeNode next = node.getNextSibling();
					if (next != null && next.getAllowsChildren()) {
						mntmMoveDown.setEnabled(true);
					}
					DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
					if (parent != null && parent.getUserObject() instanceof XMLImplementation
							&& ((XMLImplementation) parent.getUserObject()).refreshChildrenCount(parent).canAddChildren()) {
						mntmAddAfter.setEnabled(true);
						mntmAddBefore.setEnabled(true);
					}
					if (selected != null && selected.getUserObject() instanceof XMLImplementation
							&& ((XMLImplementation) selected.getUserObject()).refreshChildrenCount(selected)
									.canAddChildren()) {
						mntmAddChildStart.setEnabled(true);
						mntmAddChildEnd.setEnabled(true);
					}
					if (parent != null && parent.getUserObject() instanceof XMLImplementation) {
						mntmRemove.setEnabled(true);
						mntmChange.setEnabled(true);
					}
					popup.show(e.getComponent(), e.getX(), e.getY());
				} else {
					selected = null;
				}
			}
		});
	}
}
