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

    private void generateDefaultDistricts() {
        List<DistrictEntity> districts =
            List.of(new DistrictEntity(1L, "Uradalom", "Uradalom", null, DistrictTypeEnum.NOBLE, 3),
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
                new DistrictEntity(18L, "Aranybánya", "Aranybánya",
                    "<p>Ha aranyat szerzel nyersanyag gyűjtéskor, kapsz 1 <i class=\"fa" + " fa-coins\"></i>-t.</p>",
                    DistrictTypeEnum.UNIQUE, 6), new DistrictEntity(19L, "Állványzat", "Állványzat",
                    "<p>Építhetsz úgy kerületet, hogy az Állványzatot semmisíted meg, ahelyett, hogy kifizetnéd a <i " +
                        "class=\"fa fa-city\"></i> árát.</p><p>Magisztrátus nem kobozhat el Állványzat" +
                        " álltal épített <i class=\"fa fa-city\"></i>-t.</p>", DistrictTypeEnum.UNIQUE, 3),
                new DistrictEntity(20L, "Bazilika", "Bazilika",
                    "<p>A játék végén 1 <i class=\"fa fa-star\"></i> jár városodban minden olyan <i class=\"fa " +
                        "fa-city\"></i>-ért, aminek ára páratlan szám.</p>", DistrictTypeEnum.UNIQUE, 4),
                new DistrictEntity(21L, "Birodalmi Kincstár", "Birodalmi Kincstár",
                    "<p>A játék végén 1 <i class=\"fa fa-star\"></i> jár a kincstartalékodban lévő minden <i " +
                        "class=\"fa fa-coins\"></i> után.</p>", DistrictTypeEnum.UNIQUE, 5),
                new DistrictEntity(22L, "Capitolium", "Capitolium",
                    "<p>A játék végén 3 <i class=\"fa fa-star\"></i> jár, ha  van legalább 3 egyforma típusú <i " +
                        "class=\"fa fa-city\"></i> a városodban.</p><p>A Capitolium csak egyszer adhat <i class=\"fa " +
                        "fa-star\"></i>-t.</p>", DistrictTypeEnum.UNIQUE, 5),
                new DistrictEntity(23L, "Csillagvizsgáló", "Csillag- vizsgáló",
                    "<p>Ha <i class=\"fa fa-sheet-plastic\"></i>-t húzol nyersanyag gyűjtéskor, eggyel több közül " +
                        "választhatsz.", DistrictTypeEnum.UNIQUE, 4),
                new DistrictEntity(24L, "Elefántcsonttorony", "Elefántcsont- torony",
                    "<p>A játék végén 5 <i class=\"fa fa-star\"></i> jár, ha az Elefántcsonttorony az egyetlen <span " +
                        "style=\"font-variant: small-caps\">egyedi</span> <i class=\"fa fa-city\"></i> a" +
                        " városodban.</p>", DistrictTypeEnum.UNIQUE, 5), new DistrictEntity(25L, "Emlékmű", "Emlékmű",
                    "<p>Nem építhetsz Emlékművet, ha 5 vagy annál több <i class=\"fa fa-city\"></i> van a városodban." +
                        " Az Emlékmű 2 <i class=\"fa fa-city\"></i>-nek számít a befejezett város szempontjából.</p>",
                    DistrictTypeEnum.UNIQUE, 4), new DistrictEntity(26L, "Erődítmény", "Erődítmény",
                    "<p>8-as rangú karakter nem használhatja a képességét az Erődítményen.</p>",
                    DistrictTypeEnum.UNIQUE, 3), new DistrictEntity(27L, "Fegyvertár", "Fegyvertár",
                    "<p>A köröd folyamán elpusztíthatod a Fegyvertárat, hogy elpusztíts egy másik játékos városában " +
                        "lévő <i class=\"fa fa-city\"></i>-t.</p><p>Befejezett városban nem lehet <i class=\"fa " +
                        "fa-city\"></i>-t" + " elpusztítani.</p>", DistrictTypeEnum.UNIQUE, 3),
                new DistrictEntity(28L, "Gyár", "Gyár",
                    "<p>Eggyel kevesebbet fizetsz minden más, <span style=\"font-variant: small-caps\">egyedi</span> " +
                        "<i class=\"fa fa-city\"></i> megépítéséért.</p>", DistrictTypeEnum.UNIQUE, 5),
                new DistrictEntity(29L, "Istálló", "Istálló",
                    "<p>Az Istálló megépítése nem számít bele a körre vonatkozó építési korlátba.</p><p>Ha a " +
                        "magisztrátus elkobozza a játékos építhet még egy <i class=\"fa fa-city\"></i>-t ebben a " +
                        "körben.</p>", DistrictTypeEnum.UNIQUE, 2),
                new DistrictEntity(30L, "Kísértetváros", "Kísértetváros",
                    "<p>A játék végén a Kísértetváros az általad választott típusú <i class=\"fa fa-city\"></i>-nek " +
                        "számít.</p><p>Ha a birtokos úgy dönt, hogy a Kísértetváros típusa nem <span " +
                        "style=\"font-variant: small-caps\">egyedi</span>, akkor többé nem számít annak.</p>",
                    DistrictTypeEnum.UNIQUE, 2), new DistrictEntity(31L, "Kívánságkút", "Kívánságkút",
                    "<p>A játék végén 1 <i class=\"fa fa-star\"></i> jár a városodban lévő minden <span " +
                        "style=\"font-variant: small-caps\">egyedi</span> <i class=\"fa fa-city\"></i> után " +
                        "(beleértve a" + " Kívánságkutat is).</p>", DistrictTypeEnum.UNIQUE, 5),
                new DistrictEntity(32L, "Kovácsműhely", "Kovácsműhely",
                    "<p>A köröd folyamán egyszer 2 <i class=\"fa fa-coins\"></i>-ért 3 <i class=\"fa " +
                        "fa-sheet-plastic\"></i>-t" + " húzhatsz.</p>", DistrictTypeEnum.UNIQUE, 5),
                new DistrictEntity(33L, "Könyvtár", "Könyvtár",
                    "<p>Ha <i class=\"fa fa-sheet-plastic\"></i>-t húzol nyersanyag gyűjtéskor, eggyel több <i " +
                        "class=\"fa " + "fa-sheet-plastic\"></i>-t tarts meg.</p>", DistrictTypeEnum.UNIQUE, 6),
                new DistrictEntity(34L, "Kőfejtő", "Kőfejtő",
                    "<p>Bármennyi egyforma <i class=\"fa fa-city\"></i>-t építhetsz a városodban.</p><p>8-as rangú " +
                        "karakter nem használhatja a képességét egyforma <i class=\"fa fa-city\"></i> megszerzésére" +
                        ".</p>", DistrictTypeEnum.UNIQUE, 5), new DistrictEntity(35L, "Laboratórium", "Laboratórium",
                    "<p>A köröd folyamán egyszer eldobhatsz egy <i class=\"fa fa-sheet-plastic\"></i>-t a kezedből és" +
                        " kapsz 2" + " <i class=\"fa fa-coins\"></i>-t.</p>", DistrictTypeEnum.UNIQUE, 5),
                new DistrictEntity(36L, "Múzeum", "Múzeum",
                    "<p>A köröd folyamán egyszer helyezhetsz egy <i class=\"fa fa-sheet-plastic\"></i>-t a kezedből a" +
                        " múzeumba. A játék végén minden <i class=\"fa fa-sheet-plastic\"></i>-ért a múzeumban 1 <i " +
                        "class=\"fa fa-star\"></i> jár.</p><p>Ha a múzeumot kicserélik vagy lefoglalják a benne lévő " +
                        "<i class=\"fa fa-sheet-plastic\"></i>-ok vele mennek. Ha elpusztítják, a benne lévő <i " +
                        "class=\"fa fa-sheet-plastic\"></i>-ok a pakli aljára kerülnek.</p>", DistrictTypeEnum.UNIQUE,
                    4), new DistrictEntity(37L, "Nagy Fal", "Nagy Fal",
                    "<p>A 8-as rangú karakternek eggyel több <i class=\"fa fa-coins\"></i>-t kell fizetnie, hogy " +
                        "használhassa a képességét városodban lévő bármely más <i class=\"fa fa-city\"></i>-en.</p>",
                    DistrictTypeEnum.UNIQUE, 6), new DistrictEntity(38L, "Park", "Park",
                    "<p>Ha nincs <i class=\"fa fa-sheet-plastic\"></i> a kezedben a köröd végén, húzol két <i " +
                        "class=\"fa fa-sheet-plastic\"></i>-t.</p><p>Ha a birtokos a Boszorkány és nincs " +
                        "megbabonázott köre, akkor a Park képessége nem lép" + " életbe.</p>", DistrictTypeEnum.UNIQUE,
                    6), new DistrictEntity(39L, "Sárkánykapu", "Sárkánykapu",
                    "<p>A játék végén 2 <i class=\"fa fa-star\"></i> jár.</p>", DistrictTypeEnum.UNIQUE, 6),
                new DistrictEntity(40L, "Szegényház", "Szegényház",
                    "<p>Ha nincs <i class=\"fa fa-coins\"></i> a kincstartalékodban a köröd végén, kapsz 1 <i " +
                        "class=\"fa" +
                        " fa-coins\"></i>-t.</p><p>Ha a birtokos a Boszorkány és nincs megbabonázott köre, akkor a " +
                        "Szegényház képessége nem lép életbe.</p><p>Az alkímista képessége a Szegényház hatása után " +
                        "érvényesül.</p>", DistrictTypeEnum.UNIQUE, 4), new DistrictEntity(41L, "Színház", "Színház",
                    "<p>Minden választási fázis végén vakon kicserélheted az általad választott karaktert ellenfeled " +
                        "egyik karakterkártyájával.</p>", DistrictTypeEnum.UNIQUE, 6),
                new DistrictEntity(42L, "Szobor", "Szobor",
                    "<p>A játék végén 5 <i class=\"fa fa-star\"></i> jár, ha nálad van a <i class=\"fa " +
                        "fa-crown\"></i>.</p>", DistrictTypeEnum.UNIQUE, 3), new DistrictEntity(43L, "Temető", "Temető",
                    "<p>Megépítheted úgy a Temetőt, hogy elpusztítasz egy <i class=\"fa fa-city\"></i>-t a " +
                        "városodban, ahelyett, hogy kifizetnéd a Temető árát.</p><p>Magisztrátus nem kobozhat el " +
                        "Temetőt ami a képessége által lett építve.</p>", DistrictTypeEnum.UNIQUE, 5),
                new DistrictEntity(44L, "Térképszoba", "Térképszoba",
                    "<p>A játék végén 1 <i class=\"fa fa-star\"></i> jár minden kezedben maradt <i class=\"fa " +
                        "fa-sheet-plastic\"></i>" + " után.</p>", DistrictTypeEnum.UNIQUE, 5),
                new DistrictEntity(45L, "Titkos Kripta", "Titkos Kripta",
                    "<p>A Titkos kirptát nem lehet építeni. A játék végén 3 <i class=\"fa fa-star\"></i> jár, ha a " +
                        "Titkos kripta a kezedben van.</p>", DistrictTypeEnum.UNIQUE, 0),
                new DistrictEntity(46L, "Tolvajtanya", "Tolvajtanya",
                    "<p>Építéskor a Tolvajtanya árát fizetheted részben <i class=\"fa fa-sheet-plastic\"></i>-okból " +
                        "(1 <i class=\"fa fa-sheet-plastic\"></i> = 1 <i class=\"fa fa-coins\"></i>).</p><p>Ha a " +
                        "magisztrátus elkobozza a Tolvajtanyát csak az <i class=\"fa fa-coins\"></i>-t kapja vissza a" +
                        " tulajdonos.</p>", DistrictTypeEnum.UNIQUE, 6),
                new DistrictEntity(47L, "Varázstanoda", "Varázstanoda",
                    "<p>A kerületekhez nyersanyagokat gyűjtő képességeket tekintve a Varázstanoda az általad " +
                        "választott típusú kerületnek számít.</p>", DistrictTypeEnum.UNIQUE, 6));
        districtRepository.saveAll(districts);
        log.info("Districts generated successfully!");
    }

    private void generateDefaultCharacters() {
        List<CharacterEntity> characters = List.of(new CharacterEntity(1L, 1, "Orgyilkos",
                "<p>Válassz egy másik karaktert, akit meg szeretnél gyilkolni! A meggyilkolt" +
                    " karakter kihagyja az egész körét.</p>", null, List.of(AbilityEnum.ASSASSIN)),
            new CharacterEntity(2L, 2, "Tolvaj",
                "<p>Válaszd ki, hogy melyik karaktertől szeretnél lopni! Amikor az a karakter" +
                    " következik, elveszed a kincstartalékában lévő összes <i class=\"fa fa-coins\"></i>-t.</p><p>Nem" +
                    " rabolhatsz ki 1-es rangú" + " karkatert, megölt vagy megbabonázott karaktert.</p>", null,
                List.of(AbilityEnum.THIEF)), new CharacterEntity(3L, 3, "Mágus",
                "<p>A következő lehetőségek közül választhatsz:</p><p><ul><li>Kicserélheted a kezedben lévő összes <i" +
                    " class=\"fa fa-sheet-plastic\"></i>-t egy másik játékos kezében lévő <i class=\"fa " +
                    "fa-sheet-plastic\"></i>-okra. Ha egyetlen <i class=\"fa fa-sheet-plastic\"></i> sincs a " +
                    "kezedben, egszerűen elveszed a másik játékos kezében lévő <i class=\"fa " +
                    "fa-sheet-plastic\"></i>-okat.</li><li>Tetszőleteg számú <i class=\"fa fa-sheet-plastic\"></i>-t " +
                    "eldobsz a kezedből, és húzol helyettük ugyanannyit.</li></ul></p>", null,
                List.of(AbilityEnum.MAGICIAN_PLAYER, AbilityEnum.MAGICIAN_DECK)), new CharacterEntity(4L, 4, "Király",
                "<p>Kapsz egy <b><i class=\"fa fa-coins\"></i></b> a városodban lévő minden egyes <span " +
                    "style=\"font-variant: small-caps\">nemesi</span> <i class=\"fa fa-city\"></i> után.</p><p>A " +
                    "köröd során megkapod a <i class=\"fa fa-crown\"></i>-t. A karakter választási fázisban te " +
                    "választasz először karaktert, amíg egy másik játékos nem választja a Királyt.</p><p>Ha " +
                    "meggyilkolnak, akkor ugyanúgy kihagyod a körödet, mint bármely más karakter, a <i class=\"fa " +
                    "fa-crown\"></i> pedig nálad marad, mint trónörökösnél.</p><p>Ha megbabonáznak, attól még " +
                    "megtartod a <i class=\"fa fa-crown\"></i>-t.</p>", DistrictTypeEnum.NOBLE,
                List.of(AbilityEnum.TAKE_CROWN, AbilityEnum.NOBLE_GOLD)), new CharacterEntity(5L, 5, "Püspök",
                "<p>Kapsz egy <b><i class=\"fa fa-coins\"></i></b>-t a városodban lévő minden egyes <span " +
                    "style=\"font-variant: small-caps\">egyházi</span> <i class=\"fa fa-city\"></i> után.</p><p>Ebben" +
                    " a körben a 8-as rangú karakter nem használhatja képességét a <i class=\"fa fa-city\"></i>-eiden" +
                    ".</p><p>Ha megölnek, nyolcas rangú karakter <b>tudja</b> használni a képességét a <i class=\"fa " +
                    "fa-city\"></i>-eiden. Ugyanígy, ha megbabonáznak, nyolcas rangú karakter <b>nem tudja</b> " +
                    "használni a képességeit a Boszorkány <i class=\"fa fa-city\"></i>-ein, de <b>képes</b> használni" +
                    " a képességét a Püspök <i class=\"fa fa-city\"></i>-ein.", DistrictTypeEnum.RELIGIOUS,
                List.of(AbilityEnum.RELIGIOUS_GOLD)), new CharacterEntity(6L, 6, "Kereskedő",
                "<p>Kapsz egy <b><i class=\"fa fa-coins\"></i></b> a városodban lévő minden egyes <span " +
                    "style=\"font-variant: small-caps\">kereskedelmi</span> <i class=\"fa fa-city\"></i> után" +
                    ".</p><p>Kapsz plusz egy <i class=\"fa fa-coins\"></i>-t.</p>", DistrictTypeEnum.TRADE,
                List.of(AbilityEnum.MERCHANT, AbilityEnum.TRADE_GOLD)), new CharacterEntity(7L, 7, "Építész",
                "<p>Kapsz két plusz <i class=\"fa fa-sheet-plastic\"></i>-t.</p><p>Ebben a körben az építkezési " +
                    "korlátod 3" + " <i class=\"fa fa-city\"></i>.</p>", null, List.of(AbilityEnum.ARCHITECT)),
            new CharacterEntity(8L, 8, "Hadúr",
                "<p>Kapsz egy <b><i class=\"fa fa-coins\"></i>-t</b> a városodban lévő minden egyes <span " +
                    "style=\"font-variant: small-caps\">katonai</span> <i class=\"fa fa-city\"></i> után" +
                    ".</p><p>Elpusztíthatsz egy tetszőleges <i class=\"fa fa-city\"></i>-t: ez eggyel kevesebb <i " +
                    "class=\"fa fa-coins\"></i>-ba kerül, mint amennyi az ára.</p><p>Nem pusztíthatsz el befejezett " +
                    "városban <i class=\"fa fa-city\"></i>-t, de saját <i class=\"fa fa-city\"></i>-eid egyikét igen" +
                    ".</p>", DistrictTypeEnum.MILITARY, List.of()), new CharacterEntity(9L, 9, "Királynő",
                "<p>Ha olyan játékos mellett ülsz, aki 4-es rangú karaktert választott, kapsz 3 <i" +
                    " class=\"fa fa-coins\"></i>-t. Ha ezt a karaktert megöli az Orgyilkos, és" +
                    " melletted ült, az <i class=\"fa fa-coins\"></i>-t a kör legvégén kapod meg.</p>", null,
                List.of()), new CharacterEntity(10L, 1, "Boszorkány",
                "<p>Nyersanyag gyűjtés után ki kell választanod melyik karaktert szeretnéd megbabonázni, ekkor a " +
                    "köröd felfüggesztésre kerül! Ekkor nem tudsz építkezni, és a <i class=\"fa fa-city\"></i>-ek " +
                    "közül csak azok hatása érvényesül, amik nyersanyagokat gyűjtenek.</p><p>Amikor a megbabonázott " +
                    "karakter kerül sorra, a játékos nyersanyagokat gyűjt, és azonnal véget kell vetnie a körének. " +
                    "Nem képes <i class=\"fa fa-city\"></i>-t építeni sem, karakterek képességeit használni — még " +
                    "azokat sem, amik <q>plusz</q> nyersanyagokat adnak. A <i class=\"fa fa-city\"></i>-ek közül csak" +
                    " azok hatása érvényesül a megbabonázott karakter esetében, amik nyersanyagokat gyűjtenek" +
                    ".</p><p>Ezután úgy folytatod a körödet, mintha te játszanál a megbabonázott karakterrel: " +
                    "használod a képességeit, beleértve a plusz nyersanyagokat adókat, a passzívakat, és a " +
                    "megkötéseket. A <b>te</b> kezedben lévő <i class=\"fa fa-sheet-plastic\"></i>-okkal játszol, a " +
                    "<b>te</b> kincstartalékodban lévő <i class=\"fa fa-coins\"></i>-al fizetsz, a <b>te</b> városod " +
                    "<i class=\"fa fa-city\"></i>-eiből gyűjtesz nyersanyagokat, a <b>te</b> városodban építesz új " +
                    "kerületeket. Nem tudod használni a megbabonázott játékos bírtokában lévő <span " +
                    "style=\"font-variant: small-caps\">egyedi</span> <i class=\"fa fa-city\"></i>-ek hatásait" +
                    ".</p><p>Ha a Királyt vagy a Patríciust babonázzák meg, varázslattól függetlenül megkapja a <i " +
                    "class=\"fa fa-crown\"></i>-t. Ha a Császárt babonázzák meg, te döntöd el, ki kapja a <i " +
                    "class=\"fa fa-crown\"></i>-t, és attól a játékostól veszed el a nyersanyagot.</p><p>Ha a " +
                    "megbabonázott karakter nincs játékban ebben a körben, a köröd nem folytatódik.</p>", null,
                List.of(AbilityEnum.WITCH)), new CharacterEntity(11L, 2, "Kém",
                "<p>Válassz ki egy másik játékost és egy kerülettípust! Ezután megnézheted a másik játékos kezében " +
                    "lévő <i class=\"fa fa-sheet-plastic\"></i>-okat. Minden olyan <i class=\"fa " +
                    "fa-sheet-plastic\"></i>-ért, ami a kezében van, és egyezik a megnevezett kerülettípussal " +
                    "elveszel tőle egy <i class=\"fa fa-coins\"></i>-t és húzol egy <i class=\"fa " +
                    "fa-sheet-plastic\"></i>-t a pakliból.</p><p>Ha több egyező <i class=\"fa fa-sheet-plastic\"></i>" +
                    " van, mint amennyi <i class=\"fa fa-coins\"></i>-a, akkor elveszed minden <i class=\"fa " +
                    "fa-coins\"></i>-át, de ettől még ugyanúgy megkapod a <i class=\"fa fa-sheet-plastic\"></i>-okat.",
                null, List.of()), new CharacterEntity(12L, 3, "Varázsló",
                "<p>Megnézheted egy másik játékos kézben tartott <i class=\"fa fa-sheet-plastic\"></i>-jait, majd " +
                    "elvehetsz tőle egyet. A <i class=\"fa fa-sheet-plastic\"></i>-t azonnal beépítheted a városodba," +
                    " és ez nem számít bele az építkezési korlátba.</p><p>Ebben a körben olyan <i class=\"fa " +
                    "fa-city\"></i>-eket is építhetsz a városodban, amilyenek már léteznek.</p>", null, List.of()),
            new CharacterEntity(13L, 4, "Császár",
                "<p>Kapsz egy <b><i class=\"fa fa-coins\"></i>-t</b> a városodban lévő minden egyes <span " +
                    "style=\"font-variant: small-caps\">nemesi</span> <i class=\"fa fa-city\"></i> után" +
                    ".</p><p>Valamikor a köröd során el kell venned a <i class=\"fa fa-crown\"></i>-t attól a " +
                    "játékostól akinél van és át kell adnod egy másik játékosnak (magadnak nem adhatod). Az a " +
                    "játékos, aki megkapta a <i class=\"fa fa-crown\"></i>-t, ad neked egy <i class=\"fa " +
                    "fa-coins\"></i>-t vagy véletlenszerűen egy <i class=\"fa fa-sheet-plastic\"></i>-t a kezéből. Ha" +
                    " nincs sem <i class=\"fa fa-coins\"></i>-a, sem <i class=\"fa fa-sheet-plastic\"></i>-ja, akkor " +
                    "nem kell adnia semmit.</p><p>Ha meggyilkolnak, a köröd végén teszed át a koronát és nem kapsz " +
                    "érte nyersanyagot.</p>", DistrictTypeEnum.NOBLE, List.of(AbilityEnum.NOBLE_GOLD)),
            new CharacterEntity(14L, 5, "Apát",
                "<p>Kapsz egy <b><i class=\"fa fa-coins\"></i>-t</b> vagy <b><i class=\"fa " +
                    "fa-sheet-plastic\"></i>-t</b> a városodban lévő minden <span style=\"font-variant: " +
                    "small-caps\">egyházi</span> <i class=\"fa fa-city\"></i> után.</p><p>Ha a köröd során bármikor " +
                    "előfordul, hogy nem te vagy a legtöbb <i class=\"fa fa-coins\"></i>-al rendelkező játékos, a " +
                    "leggazdagabb játékos ad neked egyet. Döntetlen esetén te választasz, hogy ki ad <i class=\"fa " +
                    "fa-coins\"></i>-t.</p>", DistrictTypeEnum.RELIGIOUS,
                List.of(AbilityEnum.RELIGIOUS_GOLD, AbilityEnum.RELIGIOUS_CARDS)),
            new CharacterEntity(15L, 6, "Alkimista",
                "<p>A köröd végén minden építésre költött <i class=\"fa fa-coins\"></i>-t visszakapsz.</p>", null,
                List.of()), new CharacterEntity(16L, 7, "Navigátor",
                "<p>Kapsz 4 <i class=\"fa fa-coins\"></i>-t, vagy 4 <i class=\"fa fa-sheet-plastic\"></i>-t" +
                    ".</p><p>Nem építhetsz semmilyen kerületet ebben a körben.</p>", null, List.of()),
            new CharacterEntity(17L, 8, "Diplomata",
                "<p>Kapsz egy <b><i class=\"fa fa-coins\"></i>-t</b> a városodban lévő minden egyes <span " +
                    "style=\"font-variant: small-caps\">katonai</span> <i class=\"fa fa-city\"></i> után.</p>",
                DistrictTypeEnum.MILITARY, List.of(AbilityEnum.MILITARY_GOLD)),
            new CharacterEntity(18L, 9, "Művész", "<p></p>", null, List.of()),
            new CharacterEntity(19L, 1, "Magisztrátus", "<p></p>", null, List.of()),
            new CharacterEntity(20L, 2, "Zsaroló", "<p></p>", null, List.of()),
            new CharacterEntity(21L, 3, "Látnok", "<p></p>", null, List.of()), new CharacterEntity(22L, 4, "Patrícius",
                "<p>Kapsz egy <b><i class=\"fa fa-sheet-plastic\"></i>-t</b> a városodban lévő minden egyes <span " +
                    "style=\"font-variant: small-caps\">nemesi</span> <i class=\"fa fa-city\"></i> után.</p>",
                DistrictTypeEnum.NOBLE, List.of(AbilityEnum.TAKE_CROWN, AbilityEnum.NOBLE_CARDS)),
            new CharacterEntity(23L, 5, "Bíboros",
                "<p>Kapsz egy <b><i class=\"fa fa-sheet-plastic\"></i>-t</b> a városodban lévő minden egyes <span " +
                    "style=\"font-variant: small-caps\">egyházi</span> <i class=\"fa fa-city\"></i> után.</p>",
                DistrictTypeEnum.RELIGIOUS, List.of(AbilityEnum.RELIGIOUS_CARDS)),
            new CharacterEntity(24L, 6, "Szatócs",
                "<p>Kapsz egy <b><i class=\"fa fa-coins\"></i></b> a városodban lévő minden egyes <span " +
                    "style=\"font-variant: small-caps\">kereskedelmi</span> <i class=\"fa fa-city\"></i> után.</p>",
                DistrictTypeEnum.TRADE, List.of(AbilityEnum.TRADE_GOLD)),
            new CharacterEntity(25L, 7, "Tudós", "<p></p>", null, List.of()), new CharacterEntity(26L, 8, "Marsall",
                "<p>Kapsz egy <b><i class=\"fa fa-coins\"></i></b> a városodban lévő minden egyes <span " +
                    "style=\"font-variant: small-caps\">katonai</span> <i class=\"fa fa-city\"></i>" + " után.</p>",
                DistrictTypeEnum.MILITARY, List.of()),
            new CharacterEntity(27L, 9, "Adószedő", "<p></p>", null, List.of()));
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
            new ConfigEntity(40L, 18L, 9, ConfigTypeEnum.DEFAULT_CHARACTER), // Művész
            new ConfigEntity(41L, 4L, 2, ConfigTypeEnum.UPWARDS_CARDS_8C),
            new ConfigEntity(42L, 5L, 1, ConfigTypeEnum.UPWARDS_CARDS_8C),
            new ConfigEntity(43L, 6L, 0, ConfigTypeEnum.UPWARDS_CARDS_8C),
            new ConfigEntity(44L, 7L, 0, ConfigTypeEnum.UPWARDS_CARDS_8C),
            new ConfigEntity(45L, 4L, 3, ConfigTypeEnum.UPWARDS_CARDS_9C),
            new ConfigEntity(46L, 5L, 2, ConfigTypeEnum.UPWARDS_CARDS_9C),
            new ConfigEntity(47L, 6L, 1, ConfigTypeEnum.UPWARDS_CARDS_9C),
            new ConfigEntity(48L, 7L, 0, ConfigTypeEnum.UPWARDS_CARDS_9C),
            new ConfigEntity(49L, 8L, 0, ConfigTypeEnum.UPWARDS_CARDS_9C));
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
