package olter.loaf.game.games.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.cards.dto.CharacterResponse;
import olter.loaf.game.cards.dto.DistrictResponse;
import olter.loaf.game.cards.model.AbilityEnum;
import olter.loaf.game.games.model.GamePhaseEnum;
import olter.loaf.game.players.dto.PublicPlayerResponse;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class GameDetailsResponse {
    // Lobby information
    private String code;
    private Integer turn;
    private Long currentPlayer;
    private Long killedCharacter;
    private Long robbedCharacter;
    private Long bewitchedCharacter;
    private GamePhaseEnum phase;
    private List<Integer> discardedCharacters;
    private List<CharacterResponse> characters;
    private List<PublicPlayerResponse> players;

    // Player information
    private Integer gold;
    private Integer character;
    private List<DistrictResponse> hand;
    private List<DistrictResponse> drawnCards;
    private List<Integer> unavailableCharacters;
    private List<Integer> skippedCharacters;
    private List<AbilityEnum> usedAbilities;
}
