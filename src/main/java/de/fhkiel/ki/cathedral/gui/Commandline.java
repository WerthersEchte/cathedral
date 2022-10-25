package de.fhkiel.ki.cathedral.gui;

import static de.fhkiel.ki.cathedral.gui.ControlGameProxy.takeTurn;
import static de.fhkiel.ki.cathedral.gui.Util.parseTurn;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
    if (takeTurn(parseTurn(getText().trim()))) {
      setText("");
    }
  }

}
