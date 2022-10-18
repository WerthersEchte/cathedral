package de.fhkiel.ki.cathedral.gui;

import static de.fhkiel.ki.cathedral.game.Color.Black;
import static de.fhkiel.ki.cathedral.game.Color.Blue;
import static de.fhkiel.ki.cathedral.game.Color.None;
import static de.fhkiel.ki.cathedral.game.Color.White;
import static de.fhkiel.ki.cathedral.gui.ControlGameProxy.getGame;
import static de.fhkiel.ki.cathedral.gui.ControlGameProxy.takeTurn;
import static de.fhkiel.ki.cathedral.gui.Util.asInputStream;
import static de.fhkiel.ki.cathedral.gui.Util.gameColorToString;
import static de.fhkiel.ki.cathedral.gui.Util.parseTurn;

import de.fhkiel.ki.cathedral.game.Color;
import de.fhkiel.ki.cathedral.game.Placement;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.core.util.OrderUtil;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import reactor.core.publisher.Flux;

public class ControlDiscord implements ControlGameProxy.Listener {

  private static final String JOIN_GAME = "joining game: playing ";
  private static final String JOIN_REJECT = "can not join as ";
  private static final String JOIN_ACCEPT = "plays as ";
  private static final String GAME_START = "Game starts with ";
  private static final String START_GAME = "Starting a new game. Playing the color ";
  private static final long TIMEOUT = 5000L;
  public static final String TURN_START = "Turn: ";
  public static final String TURN_PASS = "pass";
  public static final int DELAY = 500;

  public interface Listener{
    void info(String info);
    void channelChanged();
    void stateChanged();
    BufferedImage gameState();
  }
  private Listener mListener;

  void register(Listener listener) {
    mListener = listener;
  }
  void unregister() {
    mListener = null;
  }

  private void info(String info){
    if(mListener != null){
      mListener.info(info);
    }
  }

  private void channelChanged(){
    if(mListener != null){
      mListener.channelChanged();
    }
  }

  public ControlDiscord(Settings settings) {
    ControlGameProxy.register(this);

    mToken = settings.token;
    mSelectedChannel = settings.default_channel;
  }

  private String mToken = "";

  public void setToken(String token) {
    mToken = token;
  }

  public String getToken() {
    return mToken;
  }

  private Color mPlayingColor = Black;

  public Color getPlayingColor() {
    return mPlayingColor;
  }

  public void setPlayingColor(Color playingColor) {
    mPlayingColor = playingColor;
  }

  private GatewayDiscordClient connection = null;
  boolean connect(boolean connect) {
    if(connect && connection == null){
      connection = DiscordClient.create(mToken)
          .gateway()
          .setInitialPresence(ignore -> ClientPresence.online(ClientActivity.of(Activity.Type.CUSTOM, "Setting up", null)))
          .login()
          .block();

      Optional.ofNullable(connection).ifPresentOrElse(
          c -> {
            c.getSelf().blockOptional().ifPresent( u -> info("Connected as " + u.getUsername()));

            generateValues();
            getChannel();
            addDefaultDiscordListener();
          },
          () -> info("Could not connect to Discord!")
      );

    } else if(connection != null) {
      connection.logout().block();
      connection = null;
    }

    return connection != null;
  }

  private String selfInMessage = "";

  private void generateValues() {
    selfInMessage = "<@" + connection.getSelfId().asString() + ">";
  }

  private final Map<String, Snowflake> channelByGuildAndName = new HashMap<>();
  List<String> getChannelList(){
    return new ArrayList<>(channelByGuildAndName.keySet());
  }

  private String mSelectedChannel = "";

  public String getSelectedChannel() {
    return mSelectedChannel;
  }

  public void setSelectedChannel(String aSelectedChannel) {
    this.mSelectedChannel = aSelectedChannel;
  }

  private void getChannel() {
    channelByGuildAndName.clear();
    connection.getGuilds().collectList().blockOptional().orElse(new ArrayList<>()).forEach(
        g -> OrderUtil.orderGuildChannels(g.getChannels()).collectList().blockOptional().orElse(new ArrayList<>()).forEach(
            c -> {
              if(c instanceof TextChannel t){
                channelByGuildAndName.put(g.getName() + "::" + c.getName(), c.getId());
              }
            }
        ));
    if(mSelectedChannel == null || mSelectedChannel.isBlank() || !channelByGuildAndName.containsKey(mSelectedChannel)){
      mSelectedChannel = channelByGuildAndName.keySet().stream().findFirst().orElse("");
    }
    info("Found channel: " + channelByGuildAndName.keySet().stream().reduce((s, s2) -> s + "; " + s2).orElse("NO CHANNEL"));
    channelChanged();
  }

  private Flux<Message> subscribeToDiscord(){
    return connection.getEventDispatcher().on(MessageCreateEvent.class)
        .filter(e -> e.getMember().map(m -> !connection.getSelfId().equals(m.getId())).orElse(false))
        .map(MessageCreateEvent::getMessage)
        .filter(message -> message.getChannelId().equals(channelByGuildAndName.get(mSelectedChannel)));
  }

  private void addDefaultDiscordListener() {
    // on new/open joinable game
    subscribeToDiscord().subscribe(this::onNewJoinableGame);
    // on automatically joining a game
    subscribeToDiscord().filter(m -> isInState(State.AutoJoinGame)).subscribe(this::onAutoJoinGame);
    // on joining a game
    subscribeToDiscord().filter(m -> isInState(State.JoiningGame)).subscribe(this::onJoiningGame);
    // on hosting a game
    subscribeToDiscord().filter(m -> isInState(State.HostingGame)).subscribe(this::onHostingGame);
    // on a running game
    subscribeToDiscord().filter(m -> isInState(State.RunningGame)).subscribe(this::onRunningGame);
  }
  void startGame() {
    if(connection != null) {
      setState(State.HostingGame, true);

      mCurrentGamePlayers.clear();
      addPlayerToGame(mPlayingColor, connection.getSelfId());

      sendMessage(START_GAME + gameColorToString(mPlayingColor));
      info("Starting game in channel " + mSelectedChannel + " as player " + gameColorToString(mPlayingColor));
    }
  }

  private synchronized void onNewJoinableGame(Message message) {

  }

  private Timer waitForJoin;
  private synchronized void onAutoJoinGame(Message message) {
    if( !isInState(State.JoiningGame) &&
        !isInState(State.HostingGame) &&
        !isInState(State.RunningGame)
    ) {
      if(message.getContent().startsWith(START_GAME)){
        Color colorOfHost = getColorFromEnd(message.getContent());
        Optional<User> host = message.getAuthor();
        if(host.isPresent() && colorOfHost != None && colorOfHost != mPlayingColor){
          setState(State.JoiningGame, true);

          mCurrentGamePlayers.clear();
          addPlayerToGame(colorOfHost, host.get().getId());
          addPlayerToGame(mPlayingColor, connection.getSelfId());

          waitForJoin = new Timer();
          waitForJoin.schedule(new TimerTask() {
            @Override
            public void run() {
              info("Could not join game from " + message.getAuthor().map(User::getUsername).orElse("Unknown") + " due timeout");
              setState(State.JoiningGame, false);
            }
          }, TIMEOUT);

          replyToMessage(message, JOIN_GAME + gameColorToString(mPlayingColor));

        } else {
          info("Can not join game from " + message.getAuthor().map(User::getUsername).orElse("Unknown") + " as they are playing " + gameColorToString(colorOfHost));
        }
      }
    }
  }

  private Timer waitForStart;
  private synchronized void onJoiningGame(Message message) {
    if(message.getContent().equals(selfInMessage + " " + JOIN_ACCEPT + Util.gameColorToString(mPlayingColor))){
      Optional.ofNullable(waitForJoin).ifPresent(Timer::cancel);

      waitForStart = new Timer();
      waitForStart.schedule(new TimerTask() {
        @Override
        public void run() {
          info("Could not play game from " + message.getAuthor().map(User::getUsername).orElse("Unknown") + " due start timeout");
          setState(State.JoiningGame, false);
          setState(State.RunningGame, false);
        }
      }, TIMEOUT);

      info("Started game with " + mCurrentGamePlayers.keySet().stream().map(c -> Util.gameColorToString(c) + ": " + mCurrentGamePlayers.get(c).asString()).collect(Collectors.joining(", ")) );

      setState(State.RunningGame, true);
    } else if(message.getContent().startsWith(GAME_START) && message.getContent().contains(selfInMessage)){
      Optional.ofNullable(waitForStart).ifPresent(Timer::cancel);

      setState(State.JoiningGame, false);
      ControlGameProxy.resetGame();

      new Thread( () -> {
        while(!getGame().isFinished()) {
          try {
            Thread.sleep(100);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
        setState(State.RunningGame, false);
      }).start();

    }
  }

  private synchronized void onHostingGame(Message message) {
    if(!isInState(State.RunningGame)) {
      if(message.getContent().startsWith(selfInMessage + " " + JOIN_GAME)){
          Color joiningPlayerColor = getColorFromEnd(message.getContent());

          if( (joiningPlayerColor == Black || joiningPlayerColor == White) && 
              !mCurrentGamePlayers.containsKey(joiningPlayerColor)){

            message.getAuthor().ifPresent( a ->{
              addPlayerToGame(joiningPlayerColor, a.getId());
              info("Added player " + a.getUsername() + "(" +a.getId().asString() + ") as " + Util.gameColorToString(joiningPlayerColor) );

              replyToMessage(message, JOIN_ACCEPT + Util.gameColorToString(joiningPlayerColor));
            });

            beginGame();
          } else {
            replyToMessage(message, JOIN_REJECT + gameColorToString(joiningPlayerColor));
          }
        }
      new Thread( () -> {
        while(!getGame().isFinished()) {
          try {
            Thread.sleep(100);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }

        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        sendMessage("Game finished\n " +
                userInChannel(mCurrentGamePlayers.get(Black)) + " (" + gameColorToString(Black) + ") " + getGame().score().get(Black) +
                " <> " +
                getGame().score().get(White) + " (" + gameColorToString(White) + ") " + userInChannel(mCurrentGamePlayers.get(White)),
            mListener.gameState()
        );
        setState(State.RunningGame, false);
        setState(State.HostingGame, false);
      }).start();
    }
  }

  private void beginGame() {
    if(mCurrentGamePlayers.containsKey(White) && mCurrentGamePlayers.containsKey(Black)){
      setState(State.RunningGame, true);
      sendMessage(GAME_START + new HashSet<>(mCurrentGamePlayers.values()).stream().map(s -> "<@" + s.asString() + ">").collect(Collectors.joining(", ")));

      ControlGameProxy.resetGame();
      info("Started game with " + mCurrentGamePlayers.keySet().stream().map(c -> Util.gameColorToString(c) + ": " + mCurrentGamePlayers.get(c).asString()).collect(Collectors.joining(", ")) );
    }
  }

  private synchronized void onRunningGame(Message message) {
    if(!getGame().isFinished()){
      Optional<User> possiblePlayer = message.getAuthor();
      System.out.println(possiblePlayer);
      System.out.println(message.getContent());
      if( possiblePlayer.isPresent() &&
          mCurrentGamePlayers.get(getGame().getCurrentPlayer()).equals(possiblePlayer.get().getId())
        ) {
        System.out.println("Ha " + message.getContent());
        if(message.getContent().equals(TURN_START + TURN_PASS)){
          info("Player " + gameColorToString(getGame().getCurrentPlayer()) + " passed turn");
          takeTurn(null);
        } else if(message.getContent().startsWith(TURN_START)){
          Placement placement = parseTurn(message.getContent().replace(TURN_START, "").trim());
          String info = "Player " + gameColorToString(getGame().getCurrentPlayer());
          if(takeTurn(placement)){
            info(info + " placed " + placement);
          } else {
            info(info + " tried " + placement + " (" + message.getContent().replace(TURN_START, "").trim() + ")");
          }
        }
      }
    } else {
      setState(State.RunningGame, false);
    }
  }

  private final EnumMap<Color, Snowflake> mCurrentGamePlayers = new EnumMap<>(Color.class);

  private void sendTurn(Placement placement){
    if(connection != null && isInState(State.RunningGame)) {
      Color nextPlayer = getGame().getCurrentPlayer();

      if( nextPlayer == Black && mPlayingColor == White || nextPlayer == White && mPlayingColor == Black){

        Optional.ofNullable(placement)
            .ifPresentOrElse(
                p -> sendMessage(TURN_START + placement.building().getId() + " " + Util.directionToNumber(placement.direction()) + " " + placement.x() + " " + placement.y()),
                () -> sendMessage(TURN_START + TURN_PASS));

        info("Sent turn " + (placement==null?"pass":placement));
      }
    }
  }

  private void replyToMessage(Message message, String reply) {
    synchronized (this) {
      message.getAuthor().ifPresent(a -> {
        connection.getChannelById(channelByGuildAndName.get(mSelectedChannel)).blockOptional()
            .map(TextChannel.class::cast)
            .ifPresent(
                c -> {
                  c.createMessage(userInChannel(a) + reply).block();
                  info("Replied with to channel " + mSelectedChannel + ": " + userInChannel(a) + reply);
                }
            );
      });
      try {
        Thread.sleep(DELAY); // safeguard against spamming
      } catch (Exception e) { /* no */ }
    }
  }

  private synchronized void sendMessage(String message) {
    synchronized (this) {
      connection.getChannelById(channelByGuildAndName.get(mSelectedChannel)).blockOptional()
          .map(TextChannel.class::cast)
          .ifPresent(
              c -> c.createMessage(message).block()
          );
      try {
        Thread.sleep(DELAY); // safeguard against spamming
      } catch (Exception e) { /* no */ }
    }
  }

  public void sendMessage(String message, BufferedImage gameState) {
    synchronized (this) {
      connection.getChannelById(channelByGuildAndName.get(mSelectedChannel)).blockOptional()
          .map(TextChannel.class::cast)
          .ifPresent(
              c -> c.createMessage(
                  MessageCreateSpec.builder()
                      .content(message)
                      .addFile("game.png", asInputStream(gameState))
                      .build()).block()
          );
      try {
        Thread.sleep(DELAY); // safeguard against spamming
      } catch (Exception e) { /* no */ }
    }
  }

  private String userInChannel(User user) {
    return "<@" + user.getId().asString() + "> ";
  }

  private String userInChannel(Snowflake user) {
    return "<@" + user.asString() + "> ";
  }

  private void addPlayerToGame(Color player, Snowflake id) {
    mCurrentGamePlayers.put(player, id);
    if(player == White){
      mCurrentGamePlayers.put(Blue, id);
    }
  }

  private Color getColorFromEnd(String string) {
    if(string.trim().toLowerCase(Locale.ROOT).endsWith(gameColorToString(Black))) {
      return Black;
    }
    if(string.trim().toLowerCase(Locale.ROOT).endsWith(gameColorToString(White))) {
      return White;
    }
    return None;
  }

  @Override
  public void newPlacement(Placement placement) {
    sendTurn(placement);
  }

  @Override
  public void noPlacement() {
    sendTurn(null);
  }

  enum State{
    HostingGame, JoiningGame, AutoJoinGame, RunningGame
  }
  private final Set<State> mCurrentStates = new HashSet<>();
  private void setState(State state, boolean on) {
    if(on) {
      mCurrentStates.add(state);
    } else {
      mCurrentStates.remove(state);
    }
    if(mListener != null){
      mListener.stateChanged();
    }
  }
  private boolean isInState(State state){
    return mCurrentStates.contains(state);
  }

  public List<State> getState() {
    return new ArrayList<>(mCurrentStates);
  }

  public void autoJoin( boolean on){
    setState(State.AutoJoinGame, on);
  }
}
