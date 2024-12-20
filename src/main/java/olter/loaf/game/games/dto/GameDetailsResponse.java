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
    private Integer killedCharacter;
    private Integer robbedCharacter;
    private Integer bewitchedCharacter;
    private Integer warrantedCharacter;
    private Integer threatenedCharacter;
    private GamePhaseEnum phase;
    private List<Integer> discardedCharacters;
    private List<Integer> warrantedCharacters;
    private List<Integer> threatenedCharacters;
    private List<CharacterResponse> characters;
    private List<PublicPlayerResponse> players;

    // Player information
    private Integer character;
    private Long abilityTarget;
    private AbilityEnum usingAbility;
    private List<DistrictResponse> hand;
    private List<DistrictResponse> drawnCards;
    private List<Integer> unavailableCharacters;
    private List<Integer> skippedCharacters;
    private List<AbilityEnum> usedAbilities;
}
