export const BREADCRUMB = {
  MY_GAMES: {
    name: "my-games",
    label: "Játékaim",
    icon: "fa fa-dice",
  },
  LOBBIES: {
    name: "lobbies",
    label: "Lobbik",
    icon: "fa fa-people-group",
  },
  GAME_RESULTS: {
    name: "game-results",
    label: "Játék eredménye",
    icon: "fa fa-trophy",
  },
  STATISTICS: {
    name: "statistics",
    label: "Statisztikák",
    icon: "fa fa-chart-line",
  },
  RULES: {
    name: "rules",
    label: "Szabályok",
    icon: "fa fa-circle-info",
  },
};

export const MAX_UNIQUE_DISTRICTS = 14;
export const MIN_LOBBY_PLAYERS = 4;
export const MAX_LOBBY_PLAYERS = 7;
export const RESOURCE_GOLD = 2;
export const RESOURCE_CARDS = 2;

export const COLORS = {
  NOBLE: {
    PRIMARY: "#F6E012",
    SECONDARY: "#44381F",
  },
  RELIGIOUS: {
    PRIMARY: "#40AFD5",
    SECONDARY: "#020B2B",
  },
  TRADE: {
    PRIMARY: "#92BF48",
    SECONDARY: "#002F0D",
  },
  MILITARY: {
    PRIMARY: "#E24549",
    SECONDARY: "#23040B",
  },
  UNIQUE: {
    PRIMARY: "#C05EA1",
    SECONDARY: "#0D0236",
  },
  DEFAULT: {
    PRIMARY: "#94989B",
    SECONDARY: "#06100D",
  },
  DISABLED: {
    PRIMARY: "#06100D",
    SECONDARY: "#94989B",
  },
};

export const CARD_POS = {
  1: "29%",
  2: "32%",
  3: "35%",
  4: "16%",
  5: "41%",
  6: "29%",
  7: "49%",
  8: "32%",
  9: "40%",
  10: "34%",
  11: "28%",
  12: "29%",
  13: "28%",
  14: "10%",
  15: "34%",
  16: "33%",
  17: "22%",
  18: "22%",
  19: "33%",
  20: "40%",
  21: "50%",
  22: "31%",
  23: "37%",
  24: "25%",
  25: "60%",
  26: "42%",
  27: "20%",
};

export const RESOURCE = {
  GOLD: "GOLD",
  CARDS: "CARDS",
};

export const GAME_PHASE = {
  NOT_STARTED: "NOT_STARTED",
  SELECTION: "SELECTION",
  RESOURCE: "RESOURCE",
  TURN: "TURN",
  ENDED: "ENDED",
};

export const CHAR_STATUS = {
  SELECTED: "SELECTED",
  DISCARDED: "DISCARDED",
  UNAVAILABLE: "UNAVAILABLE",
  SKIPPED: "SKIPPED",
  KILLED: "KILLED",
  ROBBED: "ROBBED",
  BEWITCHED: "BEWITCHED",
  WARRANTED: "WARRANTED",
  WARRANTED_SIGNED: "WARRANTED_SIGNED",
  THREATENED: "THREATENED",
  THREATENED_REAL: "THREATENED_REAL",
};

export const GAME_MODAL = {
  CHARACTER: "CHARACTER",
  CARDS: "CARDS",
  PLAYER: "PLAYER",
  DISTRICT: "DISTRICT",
  CHOICE: "CHOICE",
  SLIDER: "SLIDER",
};

export const LOBBY_UPDATE = {
  JOIN: "JOIN",
  LEAVE: "LEAVE",
  EDIT: "EDIT",
  SECURITY: "SECURITY",
  OWNER: "OWNER",
  KICK: "KICK",
  CHARACTERS: "CHARACTERS",
  DISTRICTS: "DISTRICTS",
  CROWN: "CROWN",
  START: "START",
  DELETE: "DELETE",
};

export const GAME_UPDATE = {
  NEXT_PLAYER: "NEXT_PLAYER",
  PLAYER_TURN: "PLAYER_TURN",
  CHARACTER_REVEAL: "CHARACTER_REVEAL",
  RESOURCE_COLLECTION: "RESOURCE_COLLECTION",
  BUILD: "BUILD",
  USE_ABILITY: "USE_ABILITY",
  NEW_TURN: "NEW_TURN",
  END_GAME: "END_GAME",
};

export const LOBBY_STATUS = {
  CREATED: "CREATED",
  ONGOING: "ONGOING",
  ENDED: "ENDED",
};

export const ABILITY = {
  ASSASSIN: "ASSASSIN",
  THIEF: "THIEF",
  MAGICIAN_PLAYER: "MAGICIAN_PLAYER",
  MAGICIAN_DECK: "MAGICIAN_DECK",
  WARLORD: "WARLORD",
  WITCH: "WITCH",
  SPY: "SPY",
  WIZARD: "WIZARD",
  EMPEROR: "EMPEROR",
  ABBOT: "ABBOT",
  RELIGIOUS_GOLD_OR_CARDS: "RELIGIOUS_GOLD_OR_CARDS",
  NAVIGATOR: "NAVIGATOR",
  DIPLOMAT: "DIPLOMAT",
  MAGISTRATE: "MAGISTRATE",
  BLACKMAILER: "BLACKMAILER",
  PAY_OFF: "PAY_OFF",
  SEER: "SEER",
  CARDINAL: "CARDINAL",
  SCHOLAR: "SCHOLAR",
  MARSHAL: "MARSHAL",
};

export const ABILITY_TYPE = {
  MANUAL: "MANUAL",
  START_OF_TURN: "START_OF_TURN",
  END_OF_TURN: "END_OF_TURN",
  RESOURCE_GATHERING: "RESOURCE_GATHERING",
  AFTER_GATHERING: "AFTER_GATHERING",
  BEFORE_BUILD: "BEFORE_BUILD",
  ON_BUILD: "ON_BUILD",
  AFTER_BUILD: "AFTER_BUILD",
  END_OF_GAME: "END_OF_GAME",
  NONE: "NONE",
};

export const SUBSCRIPTION_TYPE = {
  LOBBY: "LOBBY",
  GAME: "GAME",
};

export const DISABLED_CHARACTERS = [9, 18, 27];

export const CONDITIONS = {
  CROWNED: "CROWNED",
  PROTECTED: "PROTECTED",
};

export const DISTRICTS = {
  GOLD_MINE: "GOLD_MINE",
  FRAMEWORK: "FRAMEWORK",
  OBSERVATORY: "OBSERVATORY",
  KEEP: "KEEP",
  ARMORY: "ARMORY",
  FACTORY: "FACTORY",
  LIBRARY: "LIBRARY",
  LABORATORY: "LABORATORY",
  GREAT_WALL: "GREAT_WALL",
  NECROPOLIS: "NECROPOLIS",
  THIEVES_DEN: "THIEVES_DEN",
  SCHOOL_OF_MAGIC: "SCHOOL_OF_MAGIC",
};

export const DISTRICT_TYPE = {
  NOBLE: "NOBLE",
  RELIGIOUS: "RELIGIOUS",
  TRADE: "TRADE",
  MILITARY: "MILITARY",
  UNIQUE: "UNIQUE",
};
