package de.fhkiel.ki.cathedral.gui;

import static de.fhkiel.ki.cathedral.gui.ControlGameProxy.getGame;
import static de.fhkiel.ki.cathedral.gui.ControlGameProxy.register;
import static de.fhkiel.ki.cathedral.gui.Util.gameColorToFontcolor;
import static de.fhkiel.ki.cathedral.gui.Util.gameColorToPaint;

import de.fhkiel.ki.cathedral.ai.Agent;
import de.fhkiel.ki.cathedral.game.Placement;
import de.fhkiel.ki.cathedral.game.Turn;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

class AI extends JPanel implements ControlGameProxy.Listener {

  private final Agent agent;
  private final int turnTime = 30; // TODO implement timers
  private final int bonusTime = 120;
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH.mm.ss.SSS");
  JLabel turnTimeLabel;
  JTextArea aiConsole;
  private boolean autoPlayBlack = false;
  private boolean autoPlayWhite = false;
  private boolean turnIsCalculated = false;

  public AI(Agent agent) {
    this.agent = agent;

    register(this);
    agent.initialize(getGame().copy(), new PrintStream(new ConsoleStream(this)));

    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

    JPanel controls = new JPanel();
    controls.setLayout(new BoxLayout(controls, BoxLayout.LINE_AXIS));
    controls.setMinimumSize(new Dimension(0, 50));
    controls.setPreferredSize(new Dimension(0, 50));
    controls.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

    JButton stop = new JButton("Stop");
    stop.setMinimumSize(new Dimension(100, 50));
    stop.setPreferredSize(new Dimension(100, 50));
    stop.setMaximumSize(new Dimension(100, 50));
    stop.addActionListener(e -> AI.this.agent.stop());
    controls.add(stop);

    JButton takeTurn = new JButton("Take turn");
    takeTurn.setMinimumSize(new Dimension(100, 50));
    takeTurn.setPreferredSize(new Dimension(100, 50));
    takeTurn.setMaximumSize(new Dimension(100, 50));
    takeTurn.addActionListener(e -> takeTurn());
    controls.add(takeTurn);

    JPanel autoplay = new JPanel();
    autoplay.setPreferredSize(new Dimension(200, 0));
    TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Autoplay");
    title.setTitleJustification(TitledBorder.CENTER);
    autoplay.setBorder(title);
    autoplay.setLayout(new BoxLayout(autoplay, BoxLayout.LINE_AXIS));

    JToggleButton autoPlayBlackButton = new JToggleButton("Black");
    autoPlayBlackButton.setPreferredSize(new Dimension(100, 0));
    autoPlayBlackButton.setBackground(gameColorToPaint(de.fhkiel.ki.cathedral.game.Color.Black));
    autoPlayBlackButton.setForeground(gameColorToFontcolor(de.fhkiel.ki.cathedral.game.Color.Black));
    autoPlayBlackButton.addItemListener(itemEvent -> {
      autoPlayBlack = autoPlayBlackButton.isSelected();
      autoTurn();
    });
    autoplay.add(autoPlayBlackButton);

    JToggleButton autoPlayWhiteButton = new JToggleButton("White");
    autoPlayWhiteButton.setPreferredSize(new Dimension(100, 0));
    autoPlayWhiteButton.setBackground(gameColorToPaint(de.fhkiel.ki.cathedral.game.Color.White));
    autoPlayWhiteButton.setForeground(gameColorToFontcolor(de.fhkiel.ki.cathedral.game.Color.White));
    autoPlayWhiteButton.addItemListener(itemEvent -> {
      autoPlayWhite = autoPlayWhiteButton.isSelected();
      autoTurn();
    });
    autoplay.add(autoPlayWhiteButton);

    controls.add(autoplay);

    JButton evaluate = new JButton("Evaluate turn");
    evaluate.setMinimumSize(new Dimension(120, 50));
    evaluate.setPreferredSize(new Dimension(120, 50));
    evaluate.setMaximumSize(new Dimension(120, 50));
    evaluate.addActionListener(e -> evaluateTurn());
    controls.add(evaluate);

    turnTimeLabel = new JLabel(" Time for turn in s: 0");
    controls.add(turnTimeLabel);

    controls.add(Box.createHorizontalGlue());

    add(controls);

    this.agent.guiElement().ifPresent(this::add);

    aiConsole = new JTextArea();
    JScrollPane scrollConsole = new JScrollPane(aiConsole,
        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    add(scrollConsole);
  }

  void addText(String text) {
    aiConsole.append(dateFormat.format(new Date()) + " " + text + (text.endsWith("\n") ? "" : "\n"));
    aiConsole.setCaretPosition(aiConsole.getDocument().getLength());
  }

  private void evaluateTurn() {
    addText(agent.evaluateLastTurn(getGame().copy()));
  }

  private synchronized void takeTurn() {
    if (turnIsCalculated || getGame().isFinished()) {
      return;
    }

    turnIsCalculated = true;
    long start = System.currentTimeMillis();
    new Thread(() -> {
      try {
        Optional<Placement> possiblePlacement = agent.calculateTurn(getGame().copy(), turnTime, bonusTime);
        turnIsCalculated = false;
        possiblePlacement.ifPresentOrElse(ControlGameProxy::takeTurn, () -> ControlGameProxy.takeTurn(null));
      } catch (Exception exception) {
        SwingUtilities.invokeLater(() -> addText("Exception taking turn: " + exception.getMessage()));
        turnIsCalculated = false;
      }
    }).start();
    new Thread(() -> {
      while (turnIsCalculated) {
        SwingUtilities.invokeLater(() -> turnTimeLabel.setText(" Time for turn in s: " + ((System.currentTimeMillis() - start) / 100 / 10.0)));
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          // no
        }
      }
    }).start();
  }

  private void autoTurn() {
    if (
        autoPlayBlack && getGame().getCurrentPlayer() == de.fhkiel.ki.cathedral.game.Color.Black ||
            autoPlayWhite && (
                getGame().getCurrentPlayer() == de.fhkiel.ki.cathedral.game.Color.White ||
                    getGame().getCurrentPlayer() == de.fhkiel.ki.cathedral.game.Color.Blue
            )
    ) {
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        // no
      }
      takeTurn();
    }
  }

  @Override
  public void newGame() {
    autoTurn();
  }

  @Override
  public void newPlacement(Placement placement) {
    autoTurn();
  }

  @Override
  public void undoTurn(Turn turn) {
    autoTurn();
  }

  @Override
  public void gameChanged() {
    autoTurn();
  }

  @Override
  public void failedPlacement(Placement placement) {
    autoTurn();
  }

  @Override
  public void noPlacement() {
    autoTurn();
  }

  @Override
  public void undoTurn() {
    autoTurn();
  }

  private static class ConsoleStream extends OutputStream {

    private final StringBuilder buffer;
    private final AI ai;

    public ConsoleStream(AI ai) {
      buffer = new StringBuilder();
      this.ai = ai;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
      ai.addText(new String(b, off, len));
    }

    @Override
    public void write(int b) throws IOException {
      char c = (char) b;
      String value = Character.toString(c);
      if (value.equals("\n")) {
        ai.addText(buffer.toString());
        buffer.delete(0, buffer.length());
      } else {
        buffer.append(value);
      }
    }
  }
}
