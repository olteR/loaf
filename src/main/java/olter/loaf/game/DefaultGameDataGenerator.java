package olter.loaf.game;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import olter.loaf.game.characters.model.CharacterEntity;
import olter.loaf.game.characters.model.CharacterRepository;
import olter.loaf.game.config.model.ConfigEntity;
import olter.loaf.game.config.model.ConfigRepository;
import olter.loaf.game.config.model.ConfigTypeEnum;
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
  private final ConfigRepository configRepository;

  private void generateDefaultDistricts() {
    List<DistrictEntity> districts =
        List.of(
            new DistrictEntity(1L, "Uradalom", "Uradalom", null, DistrictTypeEnum.NOBLE, 3),
            new DistrictEntity(2L, "Kastély", "Kastély", null, DistrictTypeEnum.NOBLE, 4),
            new DistrictEntity(3L, "Palota", "Palota", null, DistrictTypeEnum.NOBLE, 5),
            new DistrictEntity(4L, "Templom", "Templom", null, DistrictTypeEnum.RELIGIOUS, 1),
            new DistrictEntity(5L, "Kápolna", "Kápolna", null, DistrictTypeEnum.RELIGIOUS, 2),
            new DistrictEntity(6L, "Kolostor", "Kolostor", null, DistrictTypeEnum.RELIGIOUS, 3),
            new DistrictEntity(7L, "Katedrális", "Katedrális", null, DistrictTypeEnum.RELIGIOUS, 5),
            new DistrictEntity(8L, "Fogadó", "Fogadó", null, DistrictTypeEnum.TRADE, 1),
            new DistrictEntity(9L, "Kereskedőház", "Kereskedőház", null, DistrictTypeEnum.TRADE, 2),
            new DistrictEntity(10L, "Piac", "Piac", null, DistrictTypeEnum.TRADE, 2),
            new DistrictEntity(11L, "Dokk", "Dokk", null, DistrictTypeEnum.TRADE, 3),
            new DistrictEntity(12L, "Kikötő", "Kikötő", null, DistrictTypeEnum.TRADE, 4),
            new DistrictEntity(13L, "Városháza", "Városháza", null, DistrictTypeEnum.TRADE, 5),
            new DistrictEntity(14L, "Őrtorony", "Őrtorony", null, DistrictTypeEnum.MILITARY, 1),
            new DistrictEntity(15L, "Börtön", "Börtön", null, DistrictTypeEnum.MILITARY, 2),
            new DistrictEntity(16L, "Barakk", "Barakk", null, DistrictTypeEnum.MILITARY, 3),
            new DistrictEntity(17L, "Erőd", "Erőd", null, DistrictTypeEnum.MILITARY, 3),
            new DistrictEntity(
                18L,
                "Aranybánya",
                "Aranybánya",
                "<p>Ha aranyat szerzel nyersanyag gyűjtéskor, kapsz 1 <i class=\"fa"
                    + " fa-coins\"/>.</p>",
                DistrictTypeEnum.UNIQUE,
                6),
            new DistrictEntity(
                19L,
                "Állványzat",
                "Állványzat",
                "<p>Építhetsz úgy kerületet, hogy az Állványzatot semmisíted meg, ahelyett, hogy"
                    + " kifizetnéd a kerület árát.</p><p>Magisztrátus nem kobozhat el Állványzat"
                    + " álltal épített kerületet.</p>",
                DistrictTypeEnum.UNIQUE,
                3),
            new DistrictEntity(
                20L,
                "Bazilika",
                "Bazilika",
                "<p>A játék végén 1 <i class=\"fa fa-star\"/> jár városod minden olyan kerületéért,"
                    + " aminek ára páratlan szám.</p>",
                DistrictTypeEnum.UNIQUE,
                4),
            new DistrictEntity(
                21L,
                "Birodalmi Kincstár",
                "Birodalmi Kincstár",
                "<p>A játék végén 1 <i class=\"fa fa-star\"/> jár a kincstartalékodban lévő minden"
                    + " arany után.</p>",
                DistrictTypeEnum.UNIQUE,
                5),
            new DistrictEntity(
                22L,
                "Capitolium",
                "Capitolium",
                "<p>A játék végén 3 <i class=\"fa fa-star\"/> jár, ha  van legalább 3 egyforma"
                    + " típusú kerületed.</p><p>A Capitolium csak egyszer adhat plusz"
                    + " pontokat.</p>",
                DistrictTypeEnum.UNIQUE,
                5),
            new DistrictEntity(
                23L,
                "Csillagvizsgáló",
                "Csillag- vizsgáló",
                "<p>Ha lapot húzol nyersanyag gyűjtéskor, eggyel több közül választhatsz.",
                DistrictTypeEnum.UNIQUE,
                4),
            new DistrictEntity(
                24L,
                "Elefántcsonttorony",
                "Elefántcsont- torony",
                "<p>A játék végén 5 <i class=\"fa fa-star\"/> jár, ha az Elefántcsonttorony az"
                    + " egyetlen <span style=\"font-variant: small-caps\">egyedi</span> kerület a"
                    + " városodban.</p>",
                DistrictTypeEnum.UNIQUE,
                5),
            new DistrictEntity(
                25L,
                "Emlékmű",
                "Emlékmű",
                "<p>Nem építhetsz Emlékművet, ha 5 vagy annál több kerületed van a városodban."
                    + " Kezeld az Emlékművet 2 kerületként a befejezett város szempontjából.</p>",
                DistrictTypeEnum.UNIQUE,
                4),
            new DistrictEntity(
                26L,
                "Erődítmény",
                "Erődítmény",
                "<p>8-as rangú karakter nem használhatja a képességét az Erődítményen.</p>",
                DistrictTypeEnum.UNIQUE,
                3),
            new DistrictEntity(
                27L,
                "Fegyvertár",
                "Fegyvertár",
                "<p>A köröd folyamán elpusztíthatod a Fegyvertárat, hogy elpusztítsd egy másik"
                    + " játékos kerületét.</p><p>Befejezett városban nem lehet kerületet"
                    + " elpusztítani.</p>",
                DistrictTypeEnum.UNIQUE,
                3),
            new DistrictEntity(
                28L,
                "Gyár",
                "Gyár",
                "<p>Eggyel kevesebbet fizetsz minden más, <span style=\"font-variant:"
                    + " small-caps\">egyedi</span> kerület megépítéséért.</p>",
                DistrictTypeEnum.UNIQUE,
                5),
            new DistrictEntity(
                29L,
                "Istálló",
                "Istálló",
                "<p>Az Istálló megépítése nem számít bele a körre vonatkozó építési"
                    + " korlátba.</p><p>Ha a magisztrátus elkobozza a játékos építhet még egy"
                    + " kerületet ebben a körben.</p>",
                DistrictTypeEnum.UNIQUE,
                2),
            new DistrictEntity(
                30L,
                "Kísértetváros",
                "Kísértetváros",
                "<p>A játék végén a Kísértetváros az általad választott típusú kerületnek"
                    + " számít.</p><p>Ha a birtokos úgy dönt, hogy a Kísértetváros kerülettípusa"
                    + " nem egyedi, akkor többé nem számít egyedinek.</p>",
                DistrictTypeEnum.UNIQUE,
                2),
            new DistrictEntity(
                31L,
                "Kívánságkút",
                "Kívánságkút",
                "<p>A játék végén 1 <i class=\"fa fa-star\"/> jár a városod minden <span"
                    + " style=\"font-variant: small-caps\">egyedi</span> kerülete után (beleértve a"
                    + " Kívánságkutat is)./p>",
                DistrictTypeEnum.UNIQUE,
                5),
            new DistrictEntity(
                32L,
                "Kovácsműhely",
                "Kovácsműhely",
                "<p>A köröd folyamán egyszer 2 <i class=\"fa fa-coins\"/>-ért 3 lapot"
                    + " kaphatsz.</p>",
                DistrictTypeEnum.UNIQUE,
                5),
            new DistrictEntity(
                33L,
                "Könyvtár",
                "Könyvtár",
                "<p>Ha lapot húzol nyersanyag gyűjtéskor, eggyel több lapot tarts meg.</p>",
                DistrictTypeEnum.UNIQUE,
                6),
            new DistrictEntity(
                34L,
                "Kőfejtő",
                "Kőfejtő",
                "<p>Bármennyi egyforma kerületet építhetsz a városodban.</p><p>8-as rangú karakter"
                    + " nem használhatja a képességét egyforma kerületek megszerzésére.</p>",
                DistrictTypeEnum.UNIQUE,
                5),
            new DistrictEntity(
                35L,
                "Laboratórium",
                "Laboratórium",
                "<p>A köröd folyamán egyszer a pakli aljára tehetsz egy lapot a kezedből és kapsz 2"
                    + " <i class=\"fa fa-coins\"/>.</p>",
                DistrictTypeEnum.UNIQUE,
                5),
            new DistrictEntity(
                36L,
                "Múzeum",
                "Múzeum",
                "<p>A köröd folyamán egyszer helyezhetsz egy kártyát a kezedből a múzeum alá képpel"
                    + " lefelé. A játék végén minden kártyáért a múzeum alatt 1 <i class=\"fa"
                    + " fa-star\"/> jár.</p><p>Ha a múzeumot kicserélik vagy lefoglalják az odatett"
                    + " lapok vele mennek. Ha elpusztítják, a lapok a pakli aljára kerülnek.</p>",
                DistrictTypeEnum.UNIQUE,
                4),
            new DistrictEntity(
                37L,
                "Nagy Fal",
                "Nagy Fal",
                "<p>A 8-as rangú karakternek eggyel több aranyat kell fizetnie, hogy használhassa a"
                    + " képességét városod bármely más területén.</p>",
                DistrictTypeEnum.UNIQUE,
                6),
            new DistrictEntity(
                38L,
                "Park",
                "Park",
                "<p>Ha nincs lap a kezedben a köröd végén, húzol két kártyát.</p><p>Ha a birtokos a"
                    + " Boszorkány és nincs megbabonázott köre, akkor a Park képessége nem lép"
                    + " életbe.</p>",
                DistrictTypeEnum.UNIQUE,
                6),
            new DistrictEntity(
                39L,
                "Sárkánykapu",
                "Sárkánykapu",
                "<p>A játék végén 2 <i class=\"fa fa-star\"/> jár.</p>",
                DistrictTypeEnum.UNIQUE,
                6),
            new DistrictEntity(
                40L,
                "Szegényház",
                "Szegényház",
                "<p>Ha nincs arany a kincstartalékodban a köröd végén, kapsz 1 <i class=\"fa"
                    + " fa-coins\"/>.</p><p>Ha a birtokos a Boszorkány és nincs megbabonázott köre,"
                    + " akkor a Szegényház képessége nem lép életbe.</p><p>Az alkímista képessége a"
                    + " Szegényház hatása után érvényesül.</p>",
                DistrictTypeEnum.UNIQUE,
                4),
            new DistrictEntity(
                41L,
                "Színház",
                "Színház",
                "<p>Minden választási fázis végén vakon kicserélheted az általad választott"
                    + " karaktert ellenfeled egyik karakterkártyájával.</p>",
                DistrictTypeEnum.UNIQUE,
                6),
            new DistrictEntity(
                42L,
                "Szobor",
                "Szobor",
                "<p>A játék végén 5 <i class=\"fa fa-star\"/> jár, ha nálad van a korona</p>",
                DistrictTypeEnum.UNIQUE,
                3),
            new DistrictEntity(
                43L,
                "Temető",
                "Temető",
                "<p>Megépítheted úgy a Temetőt, hogy elpusztítasz egy kerületet a városodban,"
                    + " ahelyett, hogy kifizetnéd a Temető árát.</p><p>Magisztrátus nem kobozhat el"
                    + " Temetőt ami a képessége által lett építve.</p>",
                DistrictTypeEnum.UNIQUE,
                5),
            new DistrictEntity(
                44L,
                "Térképszoba",
                "Térképszoba",
                "<p>A játék végén 1 <i class=\"fa fa-star\"/> jár minden kezedben marad lap"
                    + " után.</p>",
                DistrictTypeEnum.UNIQUE,
                5),
            new DistrictEntity(
                45L,
                "Titkos Kripta",
                "Titkos Kripta",
                "<p>A Titkos kirptát nem lehet építeni. A játék végén 3 <i class=\"fa fa-star\"/>"
                    + " jár, ha a Titkos kripta a kezedben van.</p>",
                DistrictTypeEnum.UNIQUE,
                0),
            new DistrictEntity(
                46L,
                "Tolvajtanya",
                "Tolvajtanya",
                "<p>Építéskor a Tolvajtanya árát fizetheted részben lapokból (1 lap = 1 <i"
                    + " class=\"fa fa-coins\"/>).</p><p>Ha a magisztrátus elkobozza a Tolvajtanyát"
                    + " csak az aranyat kapja vissza a tulajdonos.</p>",
                DistrictTypeEnum.UNIQUE,
                6),
            new DistrictEntity(
                47L,
                "Varázstanoda",
                "Varázstanoda",
                "<p>A kerületekhez nyersanyagokat gyűjtő képességeket tekintve a Varázstanoda az"
                    + " általad választott típusú kerületnek számít.</p>",
                DistrictTypeEnum.UNIQUE,
                6));
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

  private void generateDefaultConfig() {
    List<ConfigEntity> defaultConfig =
        List.of(
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
            new ConfigEntity(
                21L, 21L, 4, ConfigTypeEnum.DEFAULT_UNIQUE_DISTRICT), // Birodalmi Kincstár
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
            new ConfigEntity(40L, 9L, 9, ConfigTypeEnum.DEFAULT_CHARACTER)); // Királynő
    configRepository.saveAll(defaultConfig);
    log.info("Default config generated successfully!");
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
    if (configRepository.count() == 0) {
      log.info("No Config found in database. Generating default config...");
      generateDefaultConfig();
    }
  }
}
