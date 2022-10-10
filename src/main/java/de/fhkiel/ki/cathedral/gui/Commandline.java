package de.fhkiel.ki.cathedral.gui;

import static de.fhkiel.ki.cathedral.gui.GameProxy.takeTurn;
import static de.fhkiel.ki.cathedral.gui.Log.getLog;

import de.fhkiel.ki.cathedral.game.Building;
import de.fhkiel.ki.cathedral.game.Direction;
import de.fhkiel.ki.cathedral.game.Placement;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import javax.swing.AbstractAction;
import javax.swing.JTextField;

class Commandline extends JTextField {

  private final static String promt = "(id OR color buildingname) direction x y";

  Commandline() {
    setToolTipText("Commandline to manually place buildings: " + promt);
    setText(promt);
    addFocusListener(new FocusListener() {

      @Override
      public void focusLost(FocusEvent e) {
        if (getText().isEmpty()) {
          setText(promt);
        }
      }

      @Override
      public void focusGained(FocusEvent e) {
        if (getText().equals(promt)) {
          setText("");
        }
      }
    });
    addActionListener(new AbstractAction() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                          parseCommand();
                        }
                      }
    );
  }

  private void parseCommand() {
    try {
      String[] commands = getText().split(" ");

      Building building = null;
      if (commands.length == 4) {
        try {
          int id = Integer.parseInt(commands[0]);
          Optional<Building> pB = Arrays.stream(Building.values()).filter(b -> b.getId() == id).findAny();
          if (pB.isPresent()) {
            building = pB.get();
          } else {
            getLog().addText("No building with id " + id);
            return;
          }
        } catch (NumberFormatException exception) {
          getLog().addText("Can not parse: " + commands[0]);
          return;
        }
      } else if (commands.length == 5) {
        String bString = commands[0].toLowerCase(Locale.ROOT) + " " + commands[1].toLowerCase(Locale.ROOT);
        Optional<Building> pB = Arrays.stream(Building.values()).filter(b -> b.getName().toLowerCase(Locale.ROOT).equals(bString)).findAny();
        if (pB.isPresent()) {
          building = pB.get();
        } else {
          getLog().addText("No building with color and name " + commands[0] + " " + commands[1]);
          return;
        }
      } else {
        getLog().addText("Not a valid command: " + getText());
        return;
      }

      Direction direction;
      switch (commands[commands.length - 3].toLowerCase(Locale.ROOT)) {
        case "0":
          direction = Direction._0;
          break;
        case "90":
          direction = Direction._90;
          break;
        case "180":
          direction = Direction._180;
          break;
        case "270":
          direction = Direction._270;
          break;
        default:
          getLog().addText("Can not parse direction " + commands[commands.length - 3]);
          return;
      }

      int x;
      try {
        x = Integer.parseInt(commands[commands.length - 2]);
      } catch (NumberFormatException exception) {
        getLog().addText("Can not parse x: " + commands[commands.length - 2]);
        return;
      }

      int y;
      try {
        y = Integer.parseInt(commands[commands.length - 1]);
      } catch (NumberFormatException exception) {
        getLog().addText("Can not parse x: " + commands[commands.length - 1]);
        return;
      }

      if (takeTurn(new Placement(x, y, direction, building))) {
        setText("");
      }

    } catch (Exception exception) {
      getLog().addText("Error parsing command: " + getText() + " -> " + exception.getMessage());
    }
  }

}
