package olter.loaf.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import olter.loaf.game.cards.model.*;
import olter.loaf.game.config.model.ConfigEntity;
import olter.loaf.game.config.model.ConfigRepository;
import olter.loaf.game.config.model.ConfigTypeEnum;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DefaultGameDataGenerator {

    private final DistrictRepository districtRepository;
    private final CharacterRepository characterRepository;
    private final ConfigRepository configRepository;

    // @formatter:off
    private void generateDefaultDistricts() {
        List<DistrictEntity> districts =
            List.of(
                new DistrictEntity(1L, "Uradalom", "Uradalom", 3, DistrictTypeEnum.NOBLE, null),
                new DistrictEntity(2L, "Kastély", "Kastély", 4, DistrictTypeEnum.NOBLE, null),
                new DistrictEntity(3L, "Palota", "Palota", 5, DistrictTypeEnum.NOBLE, null),
                new DistrictEntity(4L, "Templom", "Templom", 1, DistrictTypeEnum.RELIGIOUS, null),
                new DistrictEntity(5L, "Kápolna", "Kápolna", 2, DistrictTypeEnum.RELIGIOUS, null),
                new DistrictEntity(6L, "Kolostor", "Kolostor", 3, DistrictTypeEnum.RELIGIOUS, null),
                new DistrictEntity(7L, "Katedrális", "Katedrális", 5, DistrictTypeEnum.RELIGIOUS, null),
                new DistrictEntity(8L, "Fogadó", "Fogadó", 1, DistrictTypeEnum.TRADE, null),
                new DistrictEntity(9L, "Kereskedőház", "Kereskedőház", 2, DistrictTypeEnum.TRADE, null),
                new DistrictEntity(10L, "Piac", "Piac", 2, DistrictTypeEnum.TRADE,  null),
                new DistrictEntity(11L, "Dokk", "Dokk", 3, DistrictTypeEnum.TRADE, null),
                new DistrictEntity(12L, "Kikötő", "Kikötő", 4, DistrictTypeEnum.TRADE, null),
                new DistrictEntity(13L, "Városháza", "Városháza", 5, DistrictTypeEnum.TRADE, null),
                new DistrictEntity(14L, "Őrtorony", "Őrtorony", 1, DistrictTypeEnum.MILITARY, null),
                new DistrictEntity(15L, "Börtön", "Börtön", 2, DistrictTypeEnum.MILITARY, null),
                new DistrictEntity(16L, "Barakk", "Barakk", 3, DistrictTypeEnum.MILITARY, null),
                new DistrictEntity(17L, "Erőd", "Erőd", 5, DistrictTypeEnum.MILITARY, null),
                new DistrictEntity(18L, "Aranybánya", "Aranybánya", 6, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.GOLD_MINE)),
                new DistrictEntity(19L, "Állványzat", "Állványzat", 3, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.FRAMEWORK)),
                new DistrictEntity(20L, "Bazilika", "Bazilika", 4, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.BASILICA)),
                new DistrictEntity(21L, "Birodalmi Kincstár", "Birodalmi Kincstár", 5, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.IMPERIAL_TREASURY)),
                new DistrictEntity(22L, "Capitolium", "Capitolium", 5, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.CAPITOL)),
                new DistrictEntity(23L, "Csillagvizsgáló", "Csillagvizsgáló", 4, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.OBSERVATORY)),
                new DistrictEntity(24L, "Elefántcsonttorony", "Elefántcsont- torony", 5, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.IVORY_TOWER)),
                new DistrictEntity(25L, "Emlékmű", "Emlékmű", 4, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.MONUMENT)),
                new DistrictEntity(26L, "Erődítmény", "Erődítmény", 3, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.KEEP)),
                new DistrictEntity(27L, "Fegyvertár", "Fegyvertár", 3, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.ARMORY)),
                new DistrictEntity(28L, "Gyár", "Gyár", 5, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.FACTORY)),
                new DistrictEntity(29L, "Istálló", "Istálló", 2, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.STABLES)),
                new DistrictEntity(30L, "Kísértetváros", "Kísértetváros", 2, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.HAUNTED_QUARTER)),
                new DistrictEntity(31L, "Kívánságkút", "Kívánságkút", 5, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.WISHING_WELL)),
                new DistrictEntity(32L, "Kovácsműhely", "Kovácsműhely", 5, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.SMITHY)),
                new DistrictEntity(33L, "Könyvtár", "Könyvtár", 6, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.LIBRARY)),
                new DistrictEntity(34L, "Kőfejtő", "Kőfejtő", 5, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.QUARRY)),
                new DistrictEntity(35L, "Laboratórium", "Laboratórium", 5, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.LABORATORY)),
                new DistrictEntity(36L, "Múzeum", "Múzeum", 4, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.PUT_IN_MUSEUM, AbilityEnum.MUSEUM)),
                new DistrictEntity(37L, "Nagy Fal", "Nagy Fal", 6, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.GREAT_WALL)),
                new DistrictEntity(38L, "Park", "Park", 6, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.PARK)),
                new DistrictEntity(39L, "Sárkánykapu", "Sárkánykapu", 6, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.DRAGON_GATE)),
                new DistrictEntity(40L, "Szegényház", "Szegényház", 4, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.POOR_HOUSE)),
                new DistrictEntity(41L, "Színház", "Színház", 6, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.THEATER)),
                new DistrictEntity(42L, "Szobor", "Szobor", 3, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.STATUE)),
                new DistrictEntity(43L, "Temető", "Temető", 5, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.NECROPOLIS)),
                new DistrictEntity(44L, "Térképszoba", "Térképszoba", 5, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.MAP_ROOM)),
                new DistrictEntity(45L, "Titkos Kripta", "Titkos Kripta", 0, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.SECRET_VAULT)),
                new DistrictEntity(46L, "Tolvajtanya", "Tolvajtanya", 6, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.THIEVES_DEN)),
                new DistrictEntity(47L, "Varázstanoda", "Varázstanoda", 6, DistrictTypeEnum.UNIQUE, List.of(AbilityEnum.SCHOOL_OF_MAGIC)));
        districtRepository.saveAll(districts);
        log.info("Districts generated successfully!");
    }

    private void generateDefaultCharacters() {
        List<CharacterEntity> characters = List.of(
            new CharacterEntity(1L, 1, "Orgyilkos", null, List.of(AbilityEnum.ASSASSIN)),
            new CharacterEntity(2L, 2, "Tolvaj", null, List.of(AbilityEnum.THIEF)),
            new CharacterEntity(3L, 3, "Mágus", null, List.of(AbilityEnum.MAGICIAN_DECK, AbilityEnum.MAGICIAN_PLAYER)),
            new CharacterEntity(4L, 4, "Király", DistrictTypeEnum.NOBLE, List.of(AbilityEnum.NOBLE_GOLD, AbilityEnum.TAKE_CROWN)),
            new CharacterEntity(5L, 5, "Püspök", DistrictTypeEnum.RELIGIOUS, List.of(AbilityEnum.RELIGIOUS_GOLD, AbilityEnum.BISHOP)),
            new CharacterEntity(6L, 6, "Kereskedő", DistrictTypeEnum.TRADE, List.of(AbilityEnum.TRADE_GOLD, AbilityEnum.MERCHANT)),
            new CharacterEntity(7L, 7, "Építész", null, List.of(AbilityEnum.ARCHITECT, AbilityEnum.BUILD_LIMIT_3)),
            new CharacterEntity(8L, 8, "Hadúr", DistrictTypeEnum.MILITARY, List.of(AbilityEnum.MILITARY_GOLD, AbilityEnum.WARLORD)),
            new CharacterEntity(9L, 9, "Királynő", null, List.of(AbilityEnum.QUEEN)),
            new CharacterEntity(10L, 1, "Boszorkány", null, List.of(AbilityEnum.WITCH)),
            new CharacterEntity(11L, 2, "Kém", null, List.of(AbilityEnum.SPY)),
            new CharacterEntity(12L, 3, "Varázsló", null, List.of(AbilityEnum.WIZARD, AbilityEnum.DUPLICATES)),
            new CharacterEntity(13L, 4, "Császár", DistrictTypeEnum.NOBLE, List.of(AbilityEnum.NOBLE_GOLD, AbilityEnum.EMPEROR)),
            new CharacterEntity(14L, 5, "Apát", DistrictTypeEnum.RELIGIOUS, List.of(AbilityEnum.RELIGIOUS_GOLD_OR_CARDS, AbilityEnum.ABBOT)),
            new CharacterEntity(15L, 6, "Alkimista", null, List.of(AbilityEnum.ALCHEMIST)),
            new CharacterEntity(16L, 7, "Navigátor", null, List.of(AbilityEnum.NAVIGATOR, AbilityEnum.CANT_BUILD)),
            new CharacterEntity(17L, 8, "Diplomata", DistrictTypeEnum.MILITARY, List.of(AbilityEnum.MILITARY_GOLD, AbilityEnum.DIPLOMAT)),
            new CharacterEntity(18L, 9, "Művész", null, List.of(AbilityEnum.ARTIST)),
            new CharacterEntity(19L, 1, "Magisztrátus", null, List.of(AbilityEnum.MAGISTRATE)),
            new CharacterEntity(20L, 2, "Zsaroló", null, List.of(AbilityEnum.BLACKMAILER)),
            new CharacterEntity(21L, 3, "Látnok", null, List.of(AbilityEnum.SEER, AbilityEnum.BUILD_LIMIT_2)),
            new CharacterEntity(22L, 4, "Patrícius", DistrictTypeEnum.NOBLE, List.of(AbilityEnum.NOBLE_CARDS, AbilityEnum.TAKE_CROWN)),
            new CharacterEntity(23L, 5, "Bíboros", DistrictTypeEnum.RELIGIOUS, List.of(AbilityEnum.RELIGIOUS_CARDS, AbilityEnum.CARDINAL)),
            new CharacterEntity(24L, 6, "Szatócs", DistrictTypeEnum.TRADE, List.of(AbilityEnum.TRADE_GOLD, AbilityEnum.TRADER)),
            new CharacterEntity(25L, 7, "Tudós", null, List.of(AbilityEnum.SCHOLAR, AbilityEnum.BUILD_LIMIT_2)),
            new CharacterEntity(26L, 8, "Marsall", DistrictTypeEnum.MILITARY, List.of(AbilityEnum.MILITARY_GOLD, AbilityEnum.MARSHAL)),
            new CharacterEntity(27L, 9, "Adószedő", null, List.of(AbilityEnum.TAX_COLLECTOR)));
        characterRepository.saveAll(characters);
        log.info("Characters generated successfully!");
    }

    private void generateDefaultConfig() {
        List<ConfigEntity> defaultConfig = List.of(
            // Base Cards
            new ConfigEntity(1L, 1L, 5, ConfigTypeEnum.BASE_CARD), // Uradalom - 5
            new ConfigEntity(2L, 2L, 4, ConfigTypeEnum.BASE_CARD), // Kastély - 4
            new ConfigEntity(3L, 3L, 3, ConfigTypeEnum.BASE_CARD), // Palota - 3
            new ConfigEntity(4L, 4L, 3, ConfigTypeEnum.BASE_CARD), // Templom - 3
            new ConfigEntity(5L, 5L, 3, ConfigTypeEnum.BASE_CARD), // Kápolna - 3
            new ConfigEntity(6L, 6L, 3, ConfigTypeEnum.BASE_CARD), // Kolostor - 3
            new ConfigEntity(7L, 7L, 2, ConfigTypeEnum.BASE_CARD), // Katedrális - 2
            new ConfigEntity(8L, 8L, 5, ConfigTypeEnum.BASE_CARD), // Fogadó - 5
            new ConfigEntity(9L, 9L, 3, ConfigTypeEnum.BASE_CARD), // Kereskedőház - 3
            new ConfigEntity(10L, 10L, 4, ConfigTypeEnum.BASE_CARD), // Piac - 4
            new ConfigEntity(11L, 11L, 3, ConfigTypeEnum.BASE_CARD), // Dokk - 3
            new ConfigEntity(12L, 12L, 3, ConfigTypeEnum.BASE_CARD), // Kikötő - 3
            new ConfigEntity(13L, 13L, 2, ConfigTypeEnum.BASE_CARD), // Városháza 2
            new ConfigEntity(14L, 14L, 3, ConfigTypeEnum.BASE_CARD), // Őrtorony - 3
            new ConfigEntity(15L, 15L, 3, ConfigTypeEnum.BASE_CARD), // Börtön - 3
            new ConfigEntity(16L, 16L, 3, ConfigTypeEnum.BASE_CARD), // Barakk - 3
            new ConfigEntity(17L, 17L, 2, ConfigTypeEnum.BASE_CARD), // Erőd - 2
            new ConfigEntity(18L, 39L, 1, ConfigTypeEnum.DEFAULT_UNIQUE_DISTRICT), // Sárkánykapu
            new ConfigEntity(19L, 28L, 2, ConfigTypeEnum.DEFAULT_UNIQUE_DISTRICT), // Gyár
            new ConfigEntity(20L, 30L, 3, ConfigTypeEnum.DEFAULT_UNIQUE_DISTRICT), // Kísértetváros
            new ConfigEntity(21L, 21L, 4, ConfigTypeEnum.DEFAULT_UNIQUE_DISTRICT), // Birodalmi Kincstár
            new ConfigEntity(22L, 26L, 5, ConfigTypeEnum.DEFAULT_UNIQUE_DISTRICT), // Erődítmény
            new ConfigEntity(23L, 35L, 6, ConfigTypeEnum.DEFAULT_UNIQUE_DISTRICT), // Laboratórium
            new ConfigEntity(24L, 33L, 7, ConfigTypeEnum.DEFAULT_UNIQUE_DISTRICT), // Könyvtár
            new ConfigEntity(25L, 44L, 8, ConfigTypeEnum.DEFAULT_UNIQUE_DISTRICT), // Térképszoba
            new ConfigEntity(26L, 34L, 9, ConfigTypeEnum.DEFAULT_UNIQUE_DISTRICT), // Kőfejtő
            new ConfigEntity(27L, 47L, 10, ConfigTypeEnum.DEFAULT_UNIQUE_DISTRICT), // Varázstanoda
            new ConfigEntity(28L, 32L, 11, ConfigTypeEnum.DEFAULT_UNIQUE_DISTRICT), // Kovácsműhely
            new ConfigEntity(29L, 42L, 12, ConfigTypeEnum.DEFAULT_UNIQUE_DISTRICT), // Szobor
            new ConfigEntity(30L, 46L, 13, ConfigTypeEnum.DEFAULT_UNIQUE_DISTRICT), // Tolvajtanya
            new ConfigEntity(31L, 31L, 14, ConfigTypeEnum.DEFAULT_UNIQUE_DISTRICT), // Kívánságkút
            new ConfigEntity(32L, 1L, 1, ConfigTypeEnum.DEFAULT_CHARACTER), // Orgyilkos
            new ConfigEntity(33L, 2L, 2, ConfigTypeEnum.DEFAULT_CHARACTER), // Tolvaj
            new ConfigEntity(34L, 3L, 3, ConfigTypeEnum.DEFAULT_CHARACTER), // Mágus
            new ConfigEntity(35L, 4L, 4, ConfigTypeEnum.DEFAULT_CHARACTER), // Király
            new ConfigEntity(36L, 5L, 5, ConfigTypeEnum.DEFAULT_CHARACTER), // Püspök
            new ConfigEntity(37L, 6L, 6, ConfigTypeEnum.DEFAULT_CHARACTER), // Kereskedő
            new ConfigEntity(38L, 7L, 7, ConfigTypeEnum.DEFAULT_CHARACTER), // Építész
            new ConfigEntity(39L, 8L, 8, ConfigTypeEnum.DEFAULT_CHARACTER), // Hadúr
            new ConfigEntity(40L, 18L, 9, ConfigTypeEnum.DEFAULT_CHARACTER)); // Művész
        configRepository.saveAll(defaultConfig);
        log.info("Default config generated successfully!");
    }

    // @formatter:on
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
        if (configRepository.count() == 0) {
            log.info("No Config found in database. Generating default config...");
            generateDefaultConfig();
        }
    }
}
