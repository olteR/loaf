package olter.loaf.game.cards.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import olter.loaf.game.cards.CardMapper;
import olter.loaf.game.cards.dto.CardsResponse;
import olter.loaf.game.cards.dto.CharacterResponse;
import olter.loaf.game.cards.dto.DistrictResponse;
import olter.loaf.game.cards.model.CharacterRepository;
import olter.loaf.game.cards.model.DistrictRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {
    private final CharacterRepository characterRepository;
    private final DistrictRepository districtRepository;
    private final CardMapper cardMapper;

    public List<CharacterResponse> getAllCharacters() {
        return characterRepository.findAll().stream()
            .map(cardMapper::entityToListResponse)
            .toList();
    }

    public List<DistrictResponse> getAllDistricts() {
        return districtRepository.findAll().stream().map(cardMapper::entityToListResponse).toList();
    }

    public CardsResponse getAllCards() {
        CardsResponse response = new CardsResponse();
        response.setCharacters(characterRepository.findAll().stream()
            .map(cardMapper::entityToListResponse)
            .toList());
        response.setDistricts(districtRepository.findAll().stream().map(cardMapper::entityToListResponse).toList());
        return response;
    }
}
