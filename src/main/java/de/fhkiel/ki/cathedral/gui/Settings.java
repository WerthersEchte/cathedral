package de.fhkiel.ki.cathedral.gui;

/**
 * Settings to automatically set needed values and actions.
 * It can only be created via a {@link Builder}.
 *
 * @author Eike Petersen {@literal <eike.petersen@fh-kiel.de>}
 * @version 1.1
 * @since 1.1
 */
public class Settings {

  /**
   * Token for connection to a discord bot.
   */
  public final String token;
  /**
   * Default channel to observe for games.
   */
  public final String default_channel;

  private Settings(String token, String default_channel) {
    this.token = token;
    this.default_channel = default_channel;
  }

  /**
   * Get Builder to create {@link Settings}.
   *
   * @return the builder
   */
  public static Builder Builder() {
    return new Builder();
  }

  /**
   * Builder to create {@link Settings}.
   */
  public static class Builder {
    private Builder() {
    }

    private String token = "";
    private String default_channel = "";

    /**
     * Set the discord token.
     *
     * @param token the discord token
     * @return the builder
     */
    public Builder token(String token) {
      this.token = token;
      return this;
    }

    /**
     * Set the default channel.
     *
     * @param channel the default channel
     * @return the builder
     */
    public Builder default_channel(String channel) {
      this.default_channel = channel;
      return this;
    }

    /**
     * Build the settings.
     *
     * @return the settings
     */
    public Settings build() {
      return new Settings(token, default_channel);
    }
  }
}
