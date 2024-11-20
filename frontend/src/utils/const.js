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
  17: "27%",
  18: "22%",
  19: "19%",
  20: "23%",
  21: "29%",
  22: "18%",
  23: "30%",
  24: "13%",
  25: "58%",
  26: "24%",
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
};

export const CHAR_STATUS = {
  SELECTED: "SELECTED",
  DISCARDED: "DISCARDED",
  UNAVAILABLE: "UNAVAILABLE",
  SKIPPED: "SKIPPED",
  KILLED: "KILLED",
  ROBBED: "ROBBED",
  BEWITCHED: "BEWITCHED",
};

export const GAME_MODAL = {
  CHARACTER: "CHARACTER",
  RESOURCE: "RESOURCE",
  CARDS: "CARDS",
};

export const DISTRICT_TYPE = {
  NOBLE: "NOBLE",
  RELIGIOUS: "RELIGIOUS",
  TRADE: "TRADE",
  MILITARY: "MILITARY",
  UNIQUE: "UNIQUE",
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
};

export const LOBBY_STATUS = {
  CREATED: "CREATED",
  ONGOING: "ONGOING",
  ENDED: "ENDED",
};

export const ABILITY_USE_RULE = {
  AND: "AND",
  OR: "OR",
  MUST: "",
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

export const ABILITY_TARGET = {
  NONE: "NONE",
  GOLD_OR_CARDS: "GOLD_OR_CARDS",
  GOLD_OR_CARDS_MULTIPLE: "GOLD_OR_CARDS_MULTIPLE",
  CHARACTER: "CHARACTER",
  PLAYER: "PLAYER",
  RICHEST_PLAYER: "RICHEST_PLAYER",
  BUILT_DISTRICT: "BUILT_DISTRICT",
  OWN_BUILT_DISTRICT: "OWN_BUILT_DISTRICT",
  CHEAP_BUILT_DISTRICT: "CHEAP_BUILT_DISTRICT",
  OWN_CARD: "OWN_CARD",
  OWN_CARDS: "OWN_CARDS",
  DISTRICT_TYPE: "DISTRICT_TYPE",
  PLAYER_AND_DISTRICT_TYPE: "PLAYER_AND_DISTRICT_TYPE",
  PLAYER_AND_DISTRICT_IN_HAND: "PLAYER_AND_DISTRICT_IN_HAND",
  DISTRICT_AND_PLAYER_HELP: "DISTRICT_AND_PLAYER_HELP",
  WARRANTS: "WARRANTS",
  THREAT_MARKERS: "THREAT_MARKERS",
  SHUFFLE: "SHUFFLE",
  SELECTOR: "SELECTOR",
};

export const CONDITIONS = {
  CROWNED: "CROWNED",
  KNOWLEDGE: "KNOWLEDGE",
};
