package de.fhkiel.ki.cathedral.gui;

import static de.fhkiel.ki.cathedral.game.Color.*;
import static de.fhkiel.ki.cathedral.gui.ControlGameProxy.register;
import static de.fhkiel.ki.cathedral.gui.Util.asInputStream;
import static de.fhkiel.ki.cathedral.gui.Util.gameColorToFontcolor;
import static de.fhkiel.ki.cathedral.gui.Util.gameColorToString;

import de.fhkiel.ki.cathedral.game.Color;
import de.fhkiel.ki.cathedral.game.Placement;
import de.fhkiel.ki.cathedral.game.Turn;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import discord4j.core.util.OrderUtil;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

class GUIDiscord extends JPanel implements ControlDiscord.Listener {

  private JTextArea discordConsole;

  private JPasswordField token;
  private JComboBox<String> channelList;


  private JLabel mStateHostingGame, mStateJoiningGame, mStateAutoJoinGame, mStateRunningGame;

  JPanel mGameBoard;
  private ControlDiscord discordconnection;
  public GUIDiscord( ControlDiscord discordconnection, JPanel gameBoard) {
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
      if(connectionToggle.isSelected()){
        connectionToggle.setText("Disconnect");
      } else {
        connectionToggle.setText("Connect");
      }
      this.discordconnection.connect(connectionToggle.isSelected());
    });
    connection.add(connectionToggle);

    connection.add(new JLabel(" Token: "));
    token = new JPasswordField(discordconnection.getToken());
    connection.add(token);

    add(connection);

    JPanel state = new JPanel();
    state.setLayout(new BoxLayout(state, BoxLayout.LINE_AXIS));
    state.setMinimumSize(new Dimension(0, 25));
    state.setPreferredSize(new Dimension(0, 25));
    state.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

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

    JButton gameStart = new JButton("Start Game");
    gameStart.addActionListener(e -> this.discordconnection.startGame());
    game.add(gameStart);


    JToggleButton gameJoin = new JToggleButton("Autojoin Game");
    gameJoin.addItemListener(itemEvent -> discordconnection.autoJoin(gameJoin.isSelected()));
    game.add(gameJoin);

    game.add(new JLabel("Color: "));
    JToggleButton colorBlack = new JToggleButton("Black");
    colorBlack.setSelected(true);
    JToggleButton colorWhite = new JToggleButton("White");
    game.add(colorBlack);
    game.add(colorWhite);
    colorBlack.addItemListener(itemEvent -> {
      if(colorBlack.isSelected()){
        colorWhite.setSelected(false);
        discordconnection.setPlayingColor(Black);
      } else {
        colorWhite.setSelected(true);
        discordconnection.setPlayingColor(White);
      }
    });
    colorWhite.addItemListener(itemEvent -> {
      if(colorWhite.isSelected()){
        colorBlack.setSelected(false);
        discordconnection.setPlayingColor(White);
      } else {
        colorBlack.setSelected(true);
        discordconnection.setPlayingColor(Black);
      }
    });

    add(game);

    discordConsole = new JTextArea();
    JScrollPane scrollConsole = new JScrollPane(discordConsole,
        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    add(scrollConsole);
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
    channelList.removeAllItems();
    for(String channel : discordconnection.getChannelList()){
      channelList.addItem(channel);
    }
    channelList.setSelectedItem(discordconnection.getSelectedChannel());
  }

  @Override
  public void stateChanged() {
    if(discordconnection.getState().contains(ControlDiscord.State.HostingGame)){
      mStateHostingGame.setForeground(java.awt.Color.GREEN);
    } else {
      mStateHostingGame.setForeground(java.awt.Color.RED);
    }
    if(discordconnection.getState().contains(ControlDiscord.State.AutoJoinGame)){
      mStateAutoJoinGame.setForeground(java.awt.Color.GREEN);
    } else {
      mStateAutoJoinGame.setForeground(java.awt.Color.RED);
    }
    if(discordconnection.getState().contains(ControlDiscord.State.JoiningGame)){
      mStateJoiningGame.setForeground(java.awt.Color.GREEN);
    } else {
      mStateJoiningGame.setForeground(java.awt.Color.RED);
    }
    if(discordconnection.getState().contains(ControlDiscord.State.RunningGame)){
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
