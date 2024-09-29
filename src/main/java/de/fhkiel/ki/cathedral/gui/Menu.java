package de.fhkiel.ki.cathedral.gui;

import static de.fhkiel.ki.cathedral.gui.ControlGameProxy.getGame;
import static de.fhkiel.ki.cathedral.gui.ControlGameProxy.takeTurn;
import static de.fhkiel.ki.cathedral.utility.DataManagement.loadGame;
import static de.fhkiel.ki.cathedral.utility.DataManagement.saveGame;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.JFileChooser;
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

    menuItem = new JMenuItem("Load Game",
        KeyEvent.VK_L);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(
        KeyEvent.VK_L, InputEvent.ALT_DOWN_MASK));
    menuItem.getAccessibleContext().setAccessibleDescription(
        "Loads a game from a given file");
    menuItem.addActionListener(e -> {
          JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
          if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            loadGame(fileChooser.getSelectedFile().getAbsolutePath()).getTurns().stream()
                .skip(1).forEachOrdered(turn -> takeTurn(turn.getAction()));
          }
        });
    menu.add(menuItem);

    menuItem = new JMenuItem("Save Game",
        KeyEvent.VK_S);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(
        KeyEvent.VK_S, InputEvent.ALT_DOWN_MASK));
    menuItem.getAccessibleContext().setAccessibleDescription(
        "Saves a game to a given file");
    menuItem.addActionListener(e -> {
      JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
      if(fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
        saveGame(getGame(), fileChooser.getSelectedFile().getAbsolutePath());
      }
    });
    menu.add(menuItem);

    menu.addSeparator();

    menuItem = new JMenuItem("Exit",
        KeyEvent.VK_E);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(
        KeyEvent.VK_E, InputEvent.ALT_DOWN_MASK));
    menuItem.getAccessibleContext().setAccessibleDescription(
        "Exits the program");
    menuItem.addActionListener(e -> System.exit(0));
    menu.add(menuItem);
  }
}
