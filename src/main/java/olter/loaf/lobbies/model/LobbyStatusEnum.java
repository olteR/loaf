package olter.loaf.lobbies.model;

public enum LobbyStatusEnum {
  CREATED("CREATED"),
  ONGOING("ONGOING"),
  ENDED("ENDED");

  private final String value;

  LobbyStatusEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
