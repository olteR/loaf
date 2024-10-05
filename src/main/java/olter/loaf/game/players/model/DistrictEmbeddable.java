package olter.loaf.game.players.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.cards.model.DistrictTypeEnum;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Embeddable
public class DistrictEmbeddable {
    private Long districtId;
    private Boolean isBeautified;

    @Enumerated(EnumType.STRING)
    private DistrictTypeEnum type;
}
