package de.fhkiel.ki.cathedral.gui;

public class Settings {
  public final String token;
  public final String default_channel;

  private Settings(String token, String default_channel) {
    this.token = token;
    this.default_channel = default_channel;
  }

  public static Builder Builder(){
    return new Builder();
  }

  public static class Builder {
    private Builder(){}

    private String token = "";
    private String default_channel = "";

    public Builder token(String token){
      this.token = token;
      return this;
    }
    public Builder default_channel(String channel){
      this.default_channel = channel;
      return this;
    }

    public Settings build(){
      return new Settings(token, default_channel);
    }
  }
}
