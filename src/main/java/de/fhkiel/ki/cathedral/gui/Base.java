package de.fhkiel.ki.cathedral.gui;

import static de.fhkiel.ki.cathedral.gui.ControlGameProxy.getGame;
import static de.fhkiel.ki.cathedral.gui.ControlGameProxy.ignoreRules;
import static de.fhkiel.ki.cathedral.gui.ControlGameProxy.resetGame;
import static de.fhkiel.ki.cathedral.gui.ControlGameProxy.takeTurn;
import static de.fhkiel.ki.cathedral.gui.ControlGameProxy.undoTurn;
import static de.fhkiel.ki.cathedral.gui.Log.getLog;

import de.fhkiel.ki.cathedral.game.Building;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

class Base extends JFrame {

  private JPanel game;
  private Board board;
  private Pieces pieces;
  private JTabbedPane infoAndAi;

  public Base() {
    super("Cathedral");
  }

  public void create(Settings settings) {
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setSize(900, 600);

    setJMenuBar(new Menu());

    BetterGlassPane glassPane = new BetterGlassPane(this.getRootPane());
    glassPane.setLayout(null);

    JPanel base = new JPanel();
    base.setLayout(new BoxLayout(base, BoxLayout.PAGE_AXIS));

    JScrollPane scrollPane = new JScrollPane(base,
        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    getContentPane().add(scrollPane, BorderLayout.CENTER);

    JPanel gameControls = new JPanel();
    gameControls.setMinimumSize(new Dimension(0, 26));
    gameControls.setPreferredSize(new Dimension(0, 26));
    gameControls.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
    gameControls.setLayout(new BoxLayout(gameControls, BoxLayout.LINE_AXIS));

    Player player = new Player();
    gameControls.add(player);

    Info info = new Info();
    gameControls.add(info);

    JToggleButton rules = new JToggleButton("Rules on");
    rules.setPreferredSize(new Dimension(100, 0));
    rules.addItemListener(itemEvent -> {
      if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
        ignoreRules(true);
        rules.setText("Rules off");
      } else {
        ignoreRules(false);
        rules.setText("Rules on");
      }
    });

    gameControls.add(rules);

    JButton reset = new JButton("Reset");
    reset.addActionListener(e -> {
      resetGame();
      rules.setSelected(false);
    });
    gameControls.add(reset);

    JButton undo = new JButton("Undo");
    undo.addActionListener(e -> undoTurn());
    gameControls.add(undo);

    JPanel space = new JPanel();
    space.setPreferredSize(new Dimension(25, 0));
    gameControls.add(space);

    JButton forfit = new JButton("Forfeit turn");
    forfit.addActionListener(e -> takeTurn(null));
    gameControls.add(forfit);

    Commandline command = new Commandline();
    gameControls.add(command);

    base.add(gameControls);

    game = new JPanel();
    game.setLayout(new BoxLayout(game, BoxLayout.LINE_AXIS));
    game.setBackground(Color.RED);
    game.setPreferredSize(new Dimension(300, 300));


    board = new Board(game);
    game.add(board);

    pieces = new Pieces(game);
    game.add(pieces);

    base.add(game);

    infoAndAi = new JTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

    JPanel border = new JPanel();
    border.setMinimumSize(new Dimension(0, 4));
    border.setPreferredSize(new Dimension(0, 4));
    border.setMaximumSize(new Dimension(Integer.MAX_VALUE, 4));
    border.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY));
    border.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent me) {
        border.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
      }

      @Override
      public void mouseExited(MouseEvent e) {
        border.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      }
    });
    border.addMouseMotionListener(new MouseMotionAdapter() {
      @Override
      public void mouseDragged(MouseEvent e) {
        game.setPreferredSize(new Dimension(0, Math.max(300, game.getPreferredSize().getSize().height + e.getY())));
        game.revalidate();

        infoAndAi.setPreferredSize(new Dimension(0,
            Math.max(150, scrollPane.getViewport().getSize().height - gameControls.getHeight() - border.getHeight() - game.getHeight())));
        infoAndAi.revalidate();
      }
    });
    base.add(border);


    infoAndAi.setPreferredSize(new Dimension(0, 150));

    Log log = getLog();
    JScrollPane scrollLog = new JScrollPane(log,
        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    infoAndAi.addTab("Log", scrollLog);

    GUIDiscord discord = new GUIDiscord(new ControlDiscord(settings), board);
    infoAndAi.addTab("Discord", discord);

    base.add(infoAndAi);


    TimerTask task = new TimerTask() {
      public void run() {
        SwingUtilities.invokeLater(() -> {
              for (Map.Entry<Building, Integer> building : getGame().getBoard().getFreeBuildings().entrySet()) {
                for (int i = 0; i < building.getValue(); ++i) {
                  pieces.add(new Piece(building.getKey(), glassPane, pieces, board));
                }
              }
              pieces.revalidate();
            }
        );
      }
    };
    new Timer("Buildings").schedule(task, 100l);
  }

  void addAI(String name, AI ai) {
    infoAndAi.addTab(name, ai);
  }
}
