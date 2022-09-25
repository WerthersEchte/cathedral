package de.fhkiel.ki.cathedral.gui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

class Menu extends JMenuBar {
  public Menu() {
    JMenu menu;
    JMenuItem menuItem;

    menu = new JMenu("File");
    menu.setMnemonic(KeyEvent.VK_F);
    menu.getAccessibleContext().setAccessibleDescription(
        "File menu");
    add(menu);

    menuItem = new JMenuItem("Exit",
        KeyEvent.VK_E);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(
        KeyEvent.VK_E, ActionEvent.ALT_MASK));
    menuItem.getAccessibleContext().setAccessibleDescription(
        "Exits the program");
    menuItem.addActionListener(e -> System.exit(0));
    menu.add(menuItem);
  }
}
