package olter.loaf.game.cards.model;

import olter.loaf.game.games.exception.InvalidTargetException;
import olter.loaf.game.games.model.GameEntity;
import olter.loaf.game.players.model.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

public enum AbilityEnum {
    ASSASSIN("ASSASSIN") {
        @Override
        public void useAbility(GameEntity game, String target) {
            PlayerEntity targetPlayer = findPlayer(game, target);
            targetPlayer.setIsKilled(true);
        }
    },
    THIEF("THIEF") {
        @Override
        public void useAbility(GameEntity game, String target) {
            PlayerEntity targetPlayer = findPlayer(game, target);
            if (targetPlayer.getIsKilled()) {
                throw new InvalidTargetException(targetPlayer.getId());
            }
            targetPlayer.setIsRobbed(true);
        }
    },
    MAGICIAN_PLAYER("MAGICIAN_PLAYER") {
        @Override
        public void useAbility(GameEntity game, String target) {
            List<DistrictEntity> magicianHand = new ArrayList<>(game.getCurrentPlayer().getHand());
            PlayerEntity targetPlayer = findPlayer(game, target);
            game.getCurrentPlayer().setHand(targetPlayer.getHand());
            targetPlayer.setHand(magicianHand);
        }
    },
    MAGICIAN_DECK("MAGICIAN_DECK") {
        @Override
        public void useAbility(GameEntity game, String target) {
        }
    };

    private final String value;

    AbilityEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public abstract void useAbility(GameEntity game, String target);

    protected PlayerEntity findPlayer(GameEntity game, String target) {
        Long targetId = Long.valueOf(target);
        return game.getPlayers().stream()
            .filter(player -> player.getId().equals(targetId)).findFirst()
            .orElseThrow(() -> new InvalidTargetException(targetId));
    }
}
