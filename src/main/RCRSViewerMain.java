package main;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

import main.RCRSViewer.MenuListener;

public class RCRSViewerMain {
	public static void main(String args[]) {
		RCRSViewer rcrs = new RCRSViewer();
		rcrs.setSize(500, 500);
		rcrs.init();

		MenuCreator menu;
		String[] menuList = { "File", "Open LogFile", "Connect Server", "s",
				"Preference", "s", "Quit", "n", "Help" };
		menu = new MenuCreator(menuList, rcrs.getMenuListener());

		JFrame frame = new JFrame("Test");
		frame.setSize(rcrs.width, rcrs.height);
		frame.add(rcrs);

		frame.setMenuBar(menu.getMenu());
		frame.setResizable(true);
		frame.setVisible(true);

		JFrame frame2 = new JFrame("Test");
		frame2.setSize(rcrs.width, rcrs.height);
		frame2.setMenuBar(menu.getMenu());
		frame2.setResizable(true);
		frame2.setVisible(true);
	}

}
