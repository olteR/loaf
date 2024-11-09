package olter.loaf.game.cards.model;

import lombok.Getter;
import olter.loaf.game.games.exception.InvalidTargetException;
import olter.loaf.game.games.model.GameEntity;
import olter.loaf.game.players.model.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum AbilityEnum {
    NOBLE_GOLD("NOBLE_GOLD") {
        @Override
        public void useAbility(GameEntity game, Long target) {
            getTypeGold(game, DistrictTypeEnum.NOBLE);
        }
    }, NOBLE_CARDS("NOBLE_CARDS") {
        @Override
        public void useAbility(GameEntity game, Long target) {
            getTypeCards(game, DistrictTypeEnum.NOBLE);
        }
    }, RELIGIOUS_GOLD("RELIGIOUS_GOLD") {
        @Override
        public void useAbility(GameEntity game, Long target) {
            getTypeGold(game, DistrictTypeEnum.RELIGIOUS);
        }
    }, RELIGIOUS_CARDS("RELIGIOUS_CARDS") {
        @Override
        public void useAbility(GameEntity game, Long target) {
            getTypeCards(game, DistrictTypeEnum.RELIGIOUS);
        }
    }, TRADE_GOLD("TRADE_GOLD") {
        @Override
        public void useAbility(GameEntity game, Long target) {
            getTypeGold(game, DistrictTypeEnum.TRADE);
        }
    }, MILITARY_GOLD("MILITARY_GOLD") {
        @Override
        public void useAbility(GameEntity game, Long target) {
            getTypeGold(game, DistrictTypeEnum.MILITARY);
        }
    }, TAKE_CROWN("TAKE_CROWN") {
        @Override
        public void useAbility(GameEntity game, Long target) {
            game.setCrownedPlayer(game.getCurrentPlayer());
        }
    }, ASSASSIN("ASSASSIN", AbilityTargetEnum.CHARACTER) {
        @Override
        public void useAbility(GameEntity game, Long target) {
            if (target < 2 || target > game.getCharacters().size()) {
                throw new InvalidTargetException(target, ASSASSIN);
            }
            game.setKilledCharacter(target.intValue());
        }
    }, THIEF("THIEF", AbilityTargetEnum.CHARACTER) {
        @Override
        public void useAbility(GameEntity game, Long target) {
            if (target < 3 || target > game.getCharacters().size() && game.getKilledCharacter() == target.intValue() ||
                game.getBewitchedCharacter() == target.intValue()) {
                throw new InvalidTargetException(target, THIEF);
            }
            game.setRobbedCharacter(target.intValue());
        }
    }, MAGICIAN_PLAYER("MAGICIAN_PLAYER", AbilityTargetEnum.PLAYER) {
        @Override
        public void useAbility(GameEntity game, Long target) {
            if (target.equals(game.getCurrentPlayer().getId())) {
                throw new InvalidTargetException(target, MAGICIAN_PLAYER);
            }
            List<DistrictEntity> magicianHand = new ArrayList<>(game.getCurrentPlayer().getHand());
            PlayerEntity targetPlayer = findPlayer(game, target, MAGICIAN_PLAYER);
            game.getCurrentPlayer().setHand(targetPlayer.getHand());
            targetPlayer.setHand(magicianHand);
        }
    }, MAGICIAN_DECK("MAGICIAN_DECK", AbilityTargetEnum.OWN_CARDS) {
        @Override
        public void useAbility(GameEntity game, Long target) {
            discardCard(game, target);
            game.getCurrentPlayer().giveCards(drawFromDeck(game, 1));
        }

        @Override
        public void useAbility(GameEntity game, List<Long> targets) {
            targets.forEach(target -> discardCard(game, target));
            game.getCurrentPlayer().giveCards(drawFromDeck(game, targets.size()));
        }
    }, MERCHANT("MERCHANT") {
        @Override
        public void useAbility(GameEntity game, Long target) {
            game.getCurrentPlayer().giveGold(1);
        }
    }, ARCHITECT("ARCHITECT") {
        @Override
        public void useAbility(GameEntity game, Long target) {
            game.getCurrentPlayer().giveCards(drawFromDeck(game, 2));
        }
    }, WARLORD("WARLORD", AbilityTargetEnum.BUILT_DISTRICT) {
        @Override
        public void useAbility(GameEntity game, Long target) {
            // TODO
        }
    }, QUEEN("QUEEN") {
        @Override
        public void useAbility(GameEntity game, Long target) {
            // TODO
        }
    }, WITCH("WITCH", AbilityTargetEnum.CHARACTER) {
        @Override
        public void useAbility(GameEntity game, Long target) {
            if (target < 2 || target > game.getCharacters().size()) {
                throw new InvalidTargetException(target, WITCH);
            }
            game.setKilledCharacter(target.intValue());
        }
    }, SPY("SPY") {
        @Override
        public void useAbility(GameEntity game, Long target) {
            // TODO
        }
    }, WIZARD("WIZARD") {
        @Override
        public void useAbility(GameEntity game, Long target) {
            // TODO
        }
    }, EMPEROR("EMPEROR") {
        @Override
        public void useAbility(GameEntity game, Long target) {
            if (target.equals(game.getCurrentPlayer().getId())) {
                throw new InvalidTargetException(target, EMPEROR);
            }
            PlayerEntity targetPlayer = findPlayer(game, target, EMPEROR);
            game.getCurrentPlayer().giveGold(targetPlayer.takeGold(1));
        }

        @Override
        public void useAbility(GameEntity game, List<Long> targets) {
            if (targets.get(0).equals(game.getCurrentPlayer().getId())) {
                throw new InvalidTargetException(targets.get(0), EMPEROR);
            }
            PlayerEntity targetPlayer = findPlayer(game, targets.get(0), EMPEROR);
            game.getCurrentPlayer().giveGold(targetPlayer.takeGold(1));
        }
    };

    private final String value;
    private final AbilityTargetEnum target;

    AbilityEnum(String value) {
        this.value = value;
        this.target = AbilityTargetEnum.NONE;
    }

    AbilityEnum(String value, AbilityTargetEnum target) {
        this.value = value;
        this.target = target;
    }

    public abstract void useAbility(GameEntity game, Long targetId);

    public void useAbility(GameEntity game, List<Long> targets) {
        useAbility(game, targets.get(0));
    }

    protected PlayerEntity findPlayer(GameEntity game, Long targetId, AbilityEnum ability) {
        return game.getPlayers().stream().filter(player -> player.getId().equals(targetId)).findFirst()
            .orElseThrow(() -> new InvalidTargetException(targetId, ability));
    }

    protected void discardCard(GameEntity game, Long targetId) {
        game.getCurrentPlayer().getHand().remove(
            game.getCurrentPlayer().getHand().stream().filter(district -> district.getId().equals(targetId)).findFirst()
                .orElseThrow(() -> new InvalidTargetException(targetId, MAGICIAN_DECK)));
    }

    protected List<DistrictEntity> drawFromDeck(GameEntity game, int cardCount) {
        List<DistrictEntity> drawnCards = new ArrayList<>();
        for (int i = 0; i < cardCount; i++) {
            drawnCards.add(game.getDeck().remove(0));
        }
        return drawnCards;
    }

    protected void getTypeGold(GameEntity game, DistrictTypeEnum type) {
        game.getCurrentPlayer().giveGold(
            game.getCurrentPlayer().getDistricts().stream().filter(district -> district.getType() == type).toList()
                .size());
    }

    protected void getTypeCards(GameEntity game, DistrictTypeEnum type) {
        game.getCurrentPlayer().giveCards(drawFromDeck(game,
            game.getCurrentPlayer().getDistricts().stream().filter(district -> district.getType() == type).toList()
                .size()));
    }
}
