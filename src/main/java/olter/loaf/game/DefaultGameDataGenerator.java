package olter.loaf.game;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import olter.loaf.game.characters.model.CharacterEntity;
import olter.loaf.game.characters.model.CharacterRepository;
import olter.loaf.game.districts.model.DistrictEntity;
import olter.loaf.game.districts.model.DistrictRepository;
import olter.loaf.game.districts.model.DistrictTypeEnum;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DefaultGameDataGenerator {

  private final DistrictRepository districtRepository;
  private final CharacterRepository characterRepository;

  /*
  Uradalom - 5
  Kastély - 4
  Palota - 3
  Templom - 3
  Kápolna - 3
  Kolostor - 3
  Katedrális - 2
  Fogadó - 5
  Kereskedőház - 3
  Piac - 4
  Dokk - 3
  Kikötő - 3
  Városháza 2
  Őrtorony - 3
  Börtön - 3
  Barakk - 3
  Erőd - 2
  */

  private void generateDefaultDistricts() {
    List<DistrictEntity> districts =
        List.of(
            new DistrictEntity(1L, "Uradalom", DistrictTypeEnum.NOBLE, 3),
            new DistrictEntity(2L, "Kastely", DistrictTypeEnum.NOBLE, 4),
            new DistrictEntity(3L, "Palota", DistrictTypeEnum.NOBLE, 5),
            new DistrictEntity(4L, "Templom", DistrictTypeEnum.RELIGIOUS, 1),
            new DistrictEntity(5L, "Kapolna", DistrictTypeEnum.RELIGIOUS, 2),
            new DistrictEntity(6L, "Kolostor", DistrictTypeEnum.RELIGIOUS, 3),
            new DistrictEntity(7L, "Katedralis", DistrictTypeEnum.RELIGIOUS, 5),
            new DistrictEntity(8L, "Fogadó", DistrictTypeEnum.TRADE, 1),
            new DistrictEntity(9L, "Kereskedőház", DistrictTypeEnum.TRADE, 2),
            new DistrictEntity(10L, "Piac", DistrictTypeEnum.TRADE, 2),
            new DistrictEntity(11L, "Dokk", DistrictTypeEnum.TRADE, 3),
            new DistrictEntity(12L, "Kikötő", DistrictTypeEnum.TRADE, 4),
            new DistrictEntity(13L, "Városháza", DistrictTypeEnum.TRADE, 5),
            new DistrictEntity(14L, "Őrtorony", DistrictTypeEnum.MILITARY, 1),
            new DistrictEntity(15L, "Börtön", DistrictTypeEnum.MILITARY, 2),
            new DistrictEntity(16L, "Barakk", DistrictTypeEnum.MILITARY, 3),
            new DistrictEntity(17L, "Erőd", DistrictTypeEnum.MILITARY, 3),
            new DistrictEntity(18L, "Aranybánya", DistrictTypeEnum.UNIQUE, 6),
            new DistrictEntity(19L, "Állványzat", DistrictTypeEnum.UNIQUE, 3),
            new DistrictEntity(20L, "Bazilika", DistrictTypeEnum.UNIQUE, 4),
            new DistrictEntity(21L, "Birodalmi Kincstár", DistrictTypeEnum.UNIQUE, 5),
            new DistrictEntity(22L, "Capitolium", DistrictTypeEnum.UNIQUE, 5),
            new DistrictEntity(23L, "Csillagvizsgáló", DistrictTypeEnum.UNIQUE, 4),
            new DistrictEntity(24L, "Elefántcsonttorony", DistrictTypeEnum.UNIQUE, 5),
            new DistrictEntity(25L, "Emlékmű", DistrictTypeEnum.UNIQUE, 4),
            new DistrictEntity(26L, "Erődítmény", DistrictTypeEnum.UNIQUE, 3),
            new DistrictEntity(27L, "Fegyvertár", DistrictTypeEnum.UNIQUE, 3),
            new DistrictEntity(28L, "Gyár", DistrictTypeEnum.UNIQUE, 5),
            new DistrictEntity(29L, "Istálló", DistrictTypeEnum.UNIQUE, 2),
            new DistrictEntity(30L, "Kísértetváros", DistrictTypeEnum.UNIQUE, 2),
            new DistrictEntity(31L, "Kívánságkút", DistrictTypeEnum.UNIQUE, 5),
            new DistrictEntity(32L, "Kovácsműhely", DistrictTypeEnum.UNIQUE, 5),
            new DistrictEntity(33L, "Könyvtár", DistrictTypeEnum.UNIQUE, 6),
            new DistrictEntity(34L, "Kőfejtő", DistrictTypeEnum.UNIQUE, 5),
            new DistrictEntity(35L, "Laboratórium", DistrictTypeEnum.UNIQUE, 5),
            new DistrictEntity(36L, "Múzeum", DistrictTypeEnum.UNIQUE, 4),
            new DistrictEntity(37L, "Nagy Fal", DistrictTypeEnum.UNIQUE, 6),
            new DistrictEntity(38L, "Park", DistrictTypeEnum.UNIQUE, 6),
            new DistrictEntity(39L, "Sárkánykapu", DistrictTypeEnum.UNIQUE, 6),
            new DistrictEntity(40L, "Szegényház", DistrictTypeEnum.UNIQUE, 4),
            new DistrictEntity(41L, "Színház", DistrictTypeEnum.UNIQUE, 6),
            new DistrictEntity(42L, "Szobor", DistrictTypeEnum.UNIQUE, 3),
            new DistrictEntity(43L, "Temető", DistrictTypeEnum.UNIQUE, 5),
            new DistrictEntity(44L, "Térképszoba", DistrictTypeEnum.UNIQUE, 5),
            new DistrictEntity(45L, "Titkos Kripta", DistrictTypeEnum.UNIQUE, 0),
            new DistrictEntity(46L, "Tolvajtanya", DistrictTypeEnum.UNIQUE, 6),
            new DistrictEntity(47L, "Varázstanoda", DistrictTypeEnum.UNIQUE, 6));
    districtRepository.saveAll(districts);
    log.info("Districts generated successfully!");
  }

  private void generateDefaultCharacters() {
    List<CharacterEntity> characters =
        List.of(
            new CharacterEntity(1L, 1, "Orgyilkos", null),
            new CharacterEntity(2L, 2, "Tolvaj", null),
            new CharacterEntity(3L, 3, "Mágus", null),
            new CharacterEntity(4L, 4, "Király", DistrictTypeEnum.NOBLE),
            new CharacterEntity(5L, 5, "Püspök", DistrictTypeEnum.RELIGIOUS),
            new CharacterEntity(6L, 6, "Kereskedő", DistrictTypeEnum.TRADE),
            new CharacterEntity(7L, 7, "Építész", null),
            new CharacterEntity(8L, 8, "Hadúr", DistrictTypeEnum.MILITARY),
            new CharacterEntity(9L, 9, "Királynő", null),
            new CharacterEntity(10L, 1, "Boszorkány", null),
            new CharacterEntity(11L, 2, "Kém", null),
            new CharacterEntity(12L, 3, "Varázsló", null),
            new CharacterEntity(13L, 4, "Császár", DistrictTypeEnum.NOBLE),
            new CharacterEntity(14L, 5, "Apát", DistrictTypeEnum.RELIGIOUS),
            new CharacterEntity(15L, 6, "Alkimista", null),
            new CharacterEntity(16L, 7, "Navigátor", null),
            new CharacterEntity(17L, 8, "Diplomata", DistrictTypeEnum.MILITARY),
            new CharacterEntity(18L, 9, "Művész", null),
            new CharacterEntity(19L, 1, "Magisztrátus", null),
            new CharacterEntity(20L, 2, "Zsaroló", null),
            new CharacterEntity(21L, 3, "Látnok", null),
            new CharacterEntity(22L, 4, "Patrícius", DistrictTypeEnum.NOBLE),
            new CharacterEntity(23L, 5, "Bíboros", DistrictTypeEnum.RELIGIOUS),
            new CharacterEntity(24L, 6, "Szatócs", DistrictTypeEnum.TRADE),
            new CharacterEntity(25L, 7, "Tudós", null),
            new CharacterEntity(26L, 8, "Marsall", DistrictTypeEnum.MILITARY),
            new CharacterEntity(27L, 9, "Adószedő", null));
    characterRepository.saveAll(characters);
    log.info("Characters generated successfully!");
  }

  @EventListener(ApplicationReadyEvent.class)
  public void startUpCheck() {
    if (districtRepository.count() == 0) {
      log.info("No Districts found in database. Generating default cards...");
      generateDefaultDistricts();
    }
    if (characterRepository.count() == 0) {
      log.info("No Characters found in database. Generating default cards...");
      generateDefaultCharacters();
    }
  }
}
