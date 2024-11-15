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
  SELECTION: "SELECTION",
  RESOURCE: "RESOURCE",
  TURN: "TURN",
};

export const CHAR_STATUS = {
  SELECTABLE: "SELECTABLE",
  ON_TURN: "ON_TURN",
  SELECTED: "SELECTED",
  DISCARDED: "DISCARDED",
  UNAVAILABLE: "UNAVAILABLE",
  SKIPPED: "SKIPPED",
};

export const GAME_MODAL = {
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
  CHARACTER_ABILITY: "CHARACTER_ABILITY",
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

export const SUBSCRIPTION_TYPE = {
  LOBBY: "LOBBY",
  GAME: "GAME",
};

export const CONDITION = {
  ON_TURN: "ON_TURN",
  CROWNED: "CROWNED",
  KILLED: "KILLED",
  ROBBED: "ROBBED",
  PROTECTED: "PROTECTED",
  BEWITCHED: "BEWITCHED",
  DUPLICATES: "DUPLICATES",
  AT_SEA: "AT_SEA",
  BLOOMING_TRADE: "BLOOMING_TRADE",
  GOLD_MINING: "GOLD_MINING",
  STAR_GUIDANCE: "STAR_GUIDANCE",
  BASTION: "BASTION",
  INDUSTRY: "INDUSTRY",
  KNOWLEDGE: "KNOWLEDGE",
  STONE_MINING: "STONE_MINING",
  GREAT_WALL: "GREAT_WALL",
  FRESH_AIR: "FRESH_AIR",
  CHARITY: "CHARITY",
  MAGICAL_DISTRICT: "MAGICAL_DISTRICT",
};
