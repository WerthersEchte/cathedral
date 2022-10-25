package de.fhkiel.ki.cathedral.gui;

import static de.fhkiel.ki.cathedral.game.Color.Black;
import static de.fhkiel.ki.cathedral.game.Color.White;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

class GUIDiscord extends JPanel implements ControlDiscord.Listener {

  private final JButton join;
  private JTextArea discordConsole;

  private JPasswordField token;
  private JComboBox<String> channelList, gameList;


  private JLabel mStateConnected, mStateHostingGame, mStateJoiningGame, mStateAutoJoinGame, mStateRunningGame;

  JPanel mGameBoard;
  private ControlDiscord discordconnection;

  public GUIDiscord(ControlDiscord discordconnection, JPanel gameBoard) {
    this.discordconnection = discordconnection;
    discordconnection.register(this);

    mGameBoard = gameBoard;

    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

    JPanel connection = new JPanel();
    connection.setLayout(new BoxLayout(connection, BoxLayout.LINE_AXIS));
    connection.setMinimumSize(new Dimension(0, 25));
    connection.setPreferredSize(new Dimension(0, 25));
    connection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

    JToggleButton connectionToggle = new JToggleButton("Connect");
    connectionToggle.setPreferredSize(new Dimension(100, 0));
    connectionToggle.addItemListener(itemEvent -> {
      setToken();
      connectionToggle.setSelected(this.discordconnection.connect(connectionToggle.isSelected()));
      if (connectionToggle.isSelected()) {
        connectionToggle.setText("Disconnect");
      } else {
        connectionToggle.setText("Connect");
      }
    });
    connection.add(connectionToggle);

    connection.add(new JLabel(" Token: "));
    token = new JPasswordField(discordconnection.getToken());
    token.addActionListener(a -> setToken());
    connection.add(token);

    add(connection);

    JPanel state = new JPanel();
    state.setLayout(new BoxLayout(state, BoxLayout.LINE_AXIS));
    state.setMinimumSize(new Dimension(0, 25));
    state.setPreferredSize(new Dimension(0, 25));
    state.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

    state.add(mStateConnected = new JLabel("Connected"));
    mStateConnected.setForeground(java.awt.Color.RED);
    state.add(new JLabel(" | "));
    state.add(mStateHostingGame = new JLabel("Hosting"));
    mStateHostingGame.setForeground(java.awt.Color.RED);
    state.add(Box.createRigidArea(new Dimension(5, 0)));
    state.add(mStateAutoJoinGame = new JLabel("AutoJoin"));
    mStateAutoJoinGame.setForeground(java.awt.Color.RED);
    state.add(Box.createRigidArea(new Dimension(5, 0)));
    state.add(mStateJoiningGame = new JLabel("Joining"));
    mStateJoiningGame.setForeground(java.awt.Color.RED);
    state.add(Box.createRigidArea(new Dimension(5, 0)));
    state.add(mStateRunningGame = new JLabel("Running"));
    mStateRunningGame.setForeground(java.awt.Color.RED);

    state.add(new JLabel(" |  Playing color: "));
    JToggleButton colorBlack = new JToggleButton("Black");
    colorBlack.setSelected(true);
    JToggleButton colorWhite = new JToggleButton("White");
    state.add(colorBlack);
    state.add(colorWhite);
    colorBlack.addItemListener(itemEvent -> {
      if (colorBlack.isSelected()) {
        colorWhite.setSelected(false);
        discordconnection.setPlayingColor(Black);
      } else {
        colorWhite.setSelected(true);
        discordconnection.setPlayingColor(White);
      }
    });
    colorWhite.addItemListener(itemEvent -> {
      if (colorWhite.isSelected()) {
        colorBlack.setSelected(false);
        discordconnection.setPlayingColor(White);
      } else {
        colorBlack.setSelected(true);
        discordconnection.setPlayingColor(Black);
      }
    });

    add(state);

    JPanel channel = new JPanel();
    channel.setLayout(new BoxLayout(channel, BoxLayout.LINE_AXIS));
    channel.setMinimumSize(new Dimension(0, 25));
    channel.setPreferredSize(new Dimension(0, 25));
    channel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

    channel.add(new JLabel("Channel to play in: "));
    channelList = new JComboBox<>();
    channel.add(channelList);

    add(channel);

    JPanel game = new JPanel();
    game.setLayout(new BoxLayout(game, BoxLayout.LINE_AXIS));
    game.setMinimumSize(new Dimension(0, 25));
    game.setPreferredSize(new Dimension(0, 25));
    game.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

    JButton resetAll = new JButton("X");
    resetAll.setPreferredSize(new Dimension(25, 25));
    resetAll.setMaximumSize(new Dimension(25, 25));
    resetAll.setMargin(new Insets(0, 0, 0, 0));
    resetAll.addActionListener(e -> this.discordconnection.resetAll());
    game.add(resetAll);

    JButton gameStart = new JButton("Start Game");
    gameStart.addActionListener(e -> this.discordconnection.startGame());
    game.add(gameStart);

    game.add(new JLabel("  ||  "));

    JToggleButton gameJoin = new JToggleButton("Autojoin Game");
    gameJoin.addItemListener(itemEvent -> setAutoJoin(gameJoin.isSelected()));
    game.add(gameJoin);

    game.add(Box.createRigidArea(new Dimension(5, 0)));

    join = new JButton("Join Game: ");
    join.addActionListener(itemEvent -> discordconnection.join((String) gameList.getSelectedItem()));
    join.setEnabled(false);
    game.add(join);
    gameList = new JComboBox<>();
    game.add(gameList);

    add(game);

    JPanel send = new JPanel();
    send.setLayout(new BoxLayout(send, BoxLayout.LINE_AXIS));
    send.setMinimumSize(new Dimension(0, 25));
    send.setPreferredSize(new Dimension(0, 25));
    send.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

    JTextField toSendToDiscord = new JTextField("");

    JButton sendToDiscord = new JButton("Send:");
    sendToDiscord.setPreferredSize(new Dimension(50, 0));
    sendToDiscord.setMargin(new Insets(0, 0, 0, 0));
    sendToDiscord.addActionListener(e -> sendToDiscord(toSendToDiscord.getText()));
    send.add(sendToDiscord);

    toSendToDiscord.addActionListener(a -> sendToDiscord(toSendToDiscord.getText()));
    send.add(toSendToDiscord);

    add(send);

    discordConsole = new JTextArea();
    JScrollPane scrollConsole = new JScrollPane(discordConsole,
        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    add(scrollConsole);
  }

  private void sendToDiscord(String text) {
    if (!text.isBlank() && !text.isEmpty()) {
      discordconnection.sendMessage(text);
    }
  }

  private void setToken() {
    StringBuilder token = new StringBuilder("");
    for (char c : this.token.getPassword()) {
      token.append(c);
    }
    discordconnection.setToken(token.toString());
  }

  private void setAutoJoin(boolean on) {
    discordconnection.autoJoin(on);
    join.setEnabled(!on && gameList.getItemCount() > 0);
    gameList.setEnabled(!on);
  }

  private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH.mm.ss.SSS");

  void addText(String text) {
    discordConsole.append(dateFormat.format(new Date()) + " " + text + (text.endsWith("\n") ? "" : "\n"));
    discordConsole.setCaretPosition(discordConsole.getDocument().getLength());
  }

  @Override
  public void info(String info) {
    SwingUtilities.invokeLater(() -> addText(info));
  }

  @Override
  public void channelChanged() {
    Arrays.stream(channelList.getActionListeners()).forEach(actionListener -> channelList.removeActionListener(actionListener));
    channelList.removeAllItems();
    for (String channel : discordconnection.getChannelList()) {
      channelList.addItem(channel);
    }
    channelList.setSelectedItem(discordconnection.getSelectedChannel());
    channelList.addItemListener(e -> discordconnection.setSelectedChannel((String) channelList.getSelectedItem()));
  }

  @Override
  public void joinableGamesChanged() {
    String old = (String) gameList.getSelectedItem();

    gameList.removeAllItems();
    for (String game : discordconnection.getJoinableGames()) {
      gameList.addItem(game);
    }
    if (discordconnection.getJoinableGames().contains(old)) {
      gameList.setSelectedItem(old);
    } else {
      discordconnection.getJoinableGames().stream().findFirst().ifPresent(g -> gameList.setSelectedItem(g));
    }
    join.setEnabled(!discordconnection.getState().contains(ControlDiscord.State.AutoJoinGame) && gameList.getItemCount() > 0);
  }

  @Override
  public void stateChanged() {
    if (discordconnection.getState().contains(ControlDiscord.State.Connected)) {
      mStateConnected.setForeground(java.awt.Color.GREEN);
    } else {
      mStateConnected.setForeground(java.awt.Color.RED);
    }
    if (discordconnection.getState().contains(ControlDiscord.State.HostingGame)) {
      mStateHostingGame.setForeground(java.awt.Color.GREEN);
    } else {
      mStateHostingGame.setForeground(java.awt.Color.RED);
    }
    if (discordconnection.getState().contains(ControlDiscord.State.AutoJoinGame)) {
      mStateAutoJoinGame.setForeground(java.awt.Color.GREEN);
    } else {
      mStateAutoJoinGame.setForeground(java.awt.Color.RED);
    }
    if (discordconnection.getState().contains(ControlDiscord.State.JoiningGame)) {
      mStateJoiningGame.setForeground(java.awt.Color.GREEN);
    } else {
      mStateJoiningGame.setForeground(java.awt.Color.RED);
    }
    if (discordconnection.getState().contains(ControlDiscord.State.RunningGame)) {
      mStateRunningGame.setForeground(java.awt.Color.GREEN);
    } else {
      mStateRunningGame.setForeground(java.awt.Color.RED);
    }
  }

  @Override
  public BufferedImage gameState() {
    int w = mGameBoard.getWidth();
    int h = mGameBoard.getHeight();
    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    Graphics2D g = bi.createGraphics();
    mGameBoard.paint(g);
    g.dispose();
    return bi;
  }
}
