package olter.loaf.game.characters.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import olter.loaf.game.characters.CharacterMapper;
import olter.loaf.game.characters.dto.CharacterListResponse;
import olter.loaf.game.characters.model.CharacterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final CharacterMapper characterMapper;

    public List<CharacterListResponse> getAllCharacters() {
        return characterRepository.findAll().stream()
            .map(characterMapper::entityToListResponse)
            .toList();
    }
}
