/*
 * Created on Feb 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.flexdock.windowing.test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.flexdock.windowing.View;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

/**
 * @author Christopher Butler
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WindowTest {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(WindowsLookAndFeel.class.getName());
		} catch(Exception e) {
		}
		
		JFrame f = new JFrame();
		f.setSize(600, 500);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		View view = new View("Test View");
		view.setIcon("org/flexdock/windowing/test/titlebar/msvs001.png");
		view.addAction(new EmptyAction("close"));
		view.addAction(new EmptyAction("pin"));
		
		p.add(view, BorderLayout.CENTER);
		f.setContentPane(p);
		f.setVisible(true);

//		view.getTitlebar().setActive(true);
	}
	
	private static class EmptyAction extends AbstractAction {
		private EmptyAction(String name) {
			putValue(Action.NAME, name);
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	
}
