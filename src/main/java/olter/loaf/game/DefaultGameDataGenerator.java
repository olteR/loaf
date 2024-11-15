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

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DefaultGameDataGenerator {

    private final AbilityRepository abilityRepository;
    private final DistrictRepository districtRepository;
    private final CharacterRepository characterRepository;
    private final ConfigRepository configRepository;

    // @formatter:off
    private List<AbilityEntity> generateDefaultAbilities() {
        List<AbilityEntity> abilities = List.of(
            new AbilityEntity(1L, "Nemesi arany", "coins", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>Kapsz egy <b><i class=\"fa fa-coins\"></i></b> a városodban lévő minden egyes <span style=\"font-variant: small-caps\">nemesi</span> <i class=\"fa fa-city\"></i> után.</p>"),
            new AbilityEntity(2L, "Nemesi lapok", "sheet-plastic", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>Kapsz egy <b><i class=\"fa fa-sheet-plastic\"></i></b> a városodban lévő minden egyes <span style=\"font-variant: small-caps\">nemesi</span> <i class=\"fa fa-city\"></i> után.</p>"),
            new AbilityEntity(3L, "Vallási arany", "coins", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>Kapsz egy <b><i class=\"fa fa-coins\"></i></b> a városodban lévő minden egyes <span style=\"font-variant: small-caps\">vallási</span> <i class=\"fa fa-city\"></i> után.</p>"),
            new AbilityEntity(4L, "Vallási lapok", "sheet-plastic", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>Kapsz egy <b><i class=\"fa fa-sheet-plastic\"></i></b> a városodban lévő minden egyes <span style=\"font-variant: small-caps\">vallási</span> <i class=\"fa fa-city\"></i> után.</p>"),
            new AbilityEntity(5L, "Vallási arany vagy lapok", "coins", ActivationEnum.MANUAL, AbilityUsageEnum.OR, AbilityTargetEnum.GOLD_OR_CARDS_MULTIPLE, "<p>Kapsz egy <b><i class=\"fa fa-coins\"></i>-t</b> vagy <b><i class=\"fa fa-sheet-plastic\"></i>-t</b> a városodban lévő minden <span style=\"font-variant: small-caps\">egyházi</span> <i class=\"fa fa-city\"></i> után.</p>"),
            new AbilityEntity(6L, "Kereskedelmi arany", "coins", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>Kapsz egy <b><i class=\"fa fa-coins\"></i></b> a városodban lévő minden egyes <span style=\"font-variant: small-caps\">kereskedelmi</span> <i class=\"fa fa-city\"></i> után.</p>"),
            new AbilityEntity(7L, "Katonai arany", "coins", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>Kapsz egy <b><i class=\"fa fa-coins\"></i></b> a városodban lévő minden egyes <span style=\"font-variant: small-caps\">katonai</span> <i class=\"fa fa-city\"></i> után.</p>"),
            new AbilityEntity(8L, "Korona elvétele", "crown", ActivationEnum.MANUAL, AbilityUsageEnum.MUST, AbilityTargetEnum.NONE, "<p>A köröd során el kell venned a <i class=\"fa fa-crown\"></i>-t. A karakter választási fázisban te választasz először karaktert, amíg egy másik játékos nem választja a Királyt.</p><p>Ha meggyilkolnak, akkor ugyanúgy kihagyod a körödet, mint bármely más karakter, a <i class=\"fa fa-crown\"></i> pedig nálad marad, mint trónörökösnél.</p><p>Ha megbabonáznak, attól még megtartod a <i class=\"fa fa-crown\"></i>-t.</p>"),
            new AbilityEntity(9L, "Gyilkolás", "skull", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.CHARACTER, "<p>Válassz egy másik karaktert, akit meg szeretnél gyilkolni! A meggyilkolt karakter kihagyja az egész körét.</p>"),
            new AbilityEntity(10L, "Lopás", "sack-dollar", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.CHARACTER, "<p>Válaszd ki, hogy melyik karaktertől szeretnél lopni! Amikor az a karakter következik, elveszed a kincstartalékában lévő összes <i class=\"fa fa-coins\"></i>-t.</p><p>Nem rabolhatsz ki 1-es rangú karkatert, megölt vagy megbabonázott karaktert.</p>"),
            new AbilityEntity(11L, "Csere játékossal", "right-left", ActivationEnum.MANUAL, AbilityUsageEnum.OR, AbilityTargetEnum.PLAYER, "<p>Kicserélheted a kezedben lévő összes <i class=\"fa fa-sheet-plastic\"></i>-t egy másik játékos kezében lévő <i class=\"fa fa-sheet-plastic\"></i>-okra.</p>"),
            new AbilityEntity(12L, "Csere pakliból", "rotate", ActivationEnum.MANUAL, AbilityUsageEnum.OR, AbilityTargetEnum.OWN_CARDS, "<p>Tetszőleges számú <i class=\"fa fa-sheet-plastic\"></i>-t eldobhatsz a kezedből és húzol helyettük ugyanannyit.</p>"),
            new AbilityEntity(13L, "Védelem", "building-shield", ActivationEnum.START_OF_TURN, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>Ebben a körben a 8-as rangú karakter nem használhatja képességét a <i class=\"fa fa-city\"></i>-eiden.</p><p>Ha megölnek, nyolcas rangú karakter <b>tudja</b> használni a képességét a <i class=\"fa fa-city\"></i>-eiden. Ugyanígy, ha megbabonáznak, nyolcas rangú karakter <b>nem tudja</b> használni a képességeit a Boszorkány <i class=\"fa fa-city\"></i>-ein, de <b>képes</b> használni a képességét a Püspök <i class=\"fa fa-city\"></i>-ein.</p>"),
            new AbilityEntity(14L, "Bevétel", "coins", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>Kapsz plusz egy <i class=\"fa fa-coins\"></i>-t.</p>"),
            new AbilityEntity(15L, "Építőanyagok", "sheet-plastic", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>Kapsz két plusz <i class=\"fa fa-sheet-plastic\"></i>-t.</p>"),
            new AbilityEntity(16L, "Építőmester", "hammer", ActivationEnum.START_OF_TURN, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>Ebben a körben az építkezési korlátod 3 <i class=\"fa fa-city\"></i>.</p>"),
            new AbilityEntity(17L, "Rombolás", "building-circle-xmark", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.BUILT_DISTRICT, "<p>Elpusztíthatsz egy tetszőleges <i class=\"fa fa-city\"></i>-t: ez eggyel kevesebb <i class=\"fa fa-coins\"></i>-ba kerül, mint amennyi az ára.</p><p>Nem pusztíthatsz el befejezett városban <i class=\"fa fa-city\"></i>-t, de saját <i class=\"fa fa-city\"></i>-eid egyikét igen.</p>"),
            new AbilityEntity(18L, "Uralkodói csapatmunka", "chess-queen", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>Ha a sorban egy melletted lévő játékos 4-es rangú karaktert választott, kapsz 3 <i class=\"fa fa-coins\"></i>-t. Ha ezt a karaktert megöli az Orgyilkos, az <i class=\"fa fa-coins\"></i>-t a kör legvégén kapod meg.</p>"),
            new AbilityEntity(19L, "Megbabonázás", "wand-sparkles", ActivationEnum.MANUAL, AbilityUsageEnum.MUST, AbilityTargetEnum.CHARACTER, "<p>Nyersanyag gyűjtés után ki kell választanod melyik karaktert szeretnéd megbabonázni, ekkor a köröd felfüggesztésre kerül! Ekkor nem tudsz építkezni, és a <i class=\"fa fa-city\"></i>-ek közül csak azok hatása érvényesül, amik nyersanyagokat gyűjtenek.</p><p>Amikor a megbabonázott karakter kerül sorra, a játékos nyersanyagokat gyűjt, és azonnal véget kell vetnie a körének. Nem képes <i class=\"fa fa-city\"></i>-t építeni sem, karakterek képességeit használni — még azokat sem, amik <q>plusz</q> nyersanyagokat adnak. A <i class=\"fa fa-city\"></i>-ek közül csak azok hatása érvényesül a megbabonázott karakter esetében, amik nyersanyagokat gyűjtenek.</p><p>Ezután úgy folytatod a körödet, mintha te játszanál a megbabonázott karakterrel: használod a képességeit, beleértve a plusz nyersanyagokat adókat, a passzívakat, és a megkötéseket. A <b>te</b> kezedben lévő <i class=\"fa fa-sheet-plastic\"></i>-okkal játszol, a <b>te</b> kincstartalékodban lévő <i class=\"fa fa-coins\"></i>-al fizetsz, a <b>te</b> városod <i class=\"fa fa-city\"></i>-eiből gyűjtesz nyersanyagokat, a <b>te</b> városodban építesz új kerületeket. Nem tudod használni a megbabonázott játékos bírtokában lévő <span style=\"font-variant: small-caps\">egyedi</span> <i class=\"fa fa-city\"></i>-ek hatásait.</p><p>Ha a Királyt vagy a Patríciust babonázzák meg, varázslattól függetlenül megkapja a <i class=\"fa fa-crown\"></i>-t. Ha a Császárt babonázzák meg, te döntöd el, ki kapja a <i class=\"fa fa-crown\"></i>-t, és attól a játékostól veszed el a nyersanyagot.</p><p>Ha a megbabonázott karakter nincs játékban ebben a körben, a köröd nem folytatódik.</p>"),
            new AbilityEntity(20L, "Kémkedés", "user-secret", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.PLAYER_AND_DISTRICT_TYPE, "<p>Válassz ki egy másik játékost és egy kerülettípust! Ezután megnézheted a másik játékos kezében lévő <i class=\"fa fa-sheet-plastic\"></i>-okat. Minden olyan <i class=\"fa fa-sheet-plastic\"></i>-ért, ami a kezében van, és egyezik a megnevezett kerülettípussal elveszel tőle egy <i class=\"fa fa-coins\"></i>-t és húzol egy <i class=\"fa fa-sheet-plastic\"></i>-t a pakliból.</p><p>Ha több egyező <i class=\"fa fa-sheet-plastic\"></i> van, mint amennyi <i class=\"fa fa-coins\"></i>-a, akkor elveszed minden <i class=\"fa fa-coins\"></i>-át, de ettől még ugyanúgy megkapod a <i class=\"fa fa-sheet-plastic\"></i>-okat.</p>"),
            new AbilityEntity(21L, "Varázslás", "wand-magic-sparkles", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.PLAYER_AND_DISTRICT_IN_HAND, "<p>Megnézheted egy másik játékos kézben tartott <i class=\"fa fa-sheet-plastic\"></i>-jait, majd elvehetsz tőle egyet. A <i class=\"fa fa-sheet-plastic\"></i>-t azonnal beépítheted a városodba, és ez nem számít bele az építkezési korlátba.</p>"),
            new AbilityEntity(22L, "Duplikátumok", "clone", ActivationEnum.START_OF_TURN, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>Ebben a körben olyan <i class=\"fa fa-city\"></i>-eket is építhetsz a városodban, amilyenek már léteznek.</p>"),
            new AbilityEntity(23L, "Korona áttétele", "crown", ActivationEnum.MANUAL, AbilityUsageEnum.MUST, AbilityTargetEnum.PLAYER, "<p>Valamikor a köröd során el kell venned a <i class=\"fa fa-crown\"></i>-t attól a játékostól akinél van és át kell adnod egy másik játékosnak (magadnak nem adhatod). Az a játékos, aki megkapta a <i class=\"fa fa-crown\"></i>-t, ad neked egy <i class=\"fa fa-coins\"></i>-t vagy véletlenszerűen egy <i class=\"fa fa-sheet-plastic\"></i>-t a kezéből. Ha nincs sem <i class=\"fa fa-coins\"></i>-a, sem <i class=\"fa fa-sheet-plastic\"></i>-ja, akkor nem kell adnia semmit.</p><p>Ha meggyilkolnak, a köröd végén teszed át a koronát és nem kapsz érte nyersanyagot.</p>"),
            new AbilityEntity(24L, "Alamizsna", "coins", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.RICHEST_PLAYER, "<p>Ha a köröd során bármikor előfordul, hogy nem te vagy a legtöbb <i class=\"fa fa-coins\"></i>-al rendelkező játékos, a leggazdagabb játékos ad neked egyet. Döntetlen esetén te választasz, hogy ki ad <i class=\"fa fa-coins\"></i>-t.</p>"),
            new AbilityEntity(25L, "Alkímia", "coins", ActivationEnum.END_OF_TURN, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>A köröd végén minden építésre költött <i class=\"fa fa-coins\"></i>-t visszakapsz.</p>"),
            new AbilityEntity(26L, "Szállítmány", "sailboat", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.GOLD_OR_CARDS, "<p>Kapsz 4 <i class=\"fa fa-coins\"></i>-t, vagy 4 <i class=\"fa fa-sheet-plastic\"></i>-t.</p>"),
            new AbilityEntity(27L, "Tengeren", "person-walking-luggage", ActivationEnum.START_OF_TURN, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>Nem építhetsz semmilyen kerületet ebben a körben.</p>"),
            new AbilityEntity(28L, "Kerület csere", "right-left", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.BUILT_DISTRICT, "<p>Kicserélheted az egyik <i class=\"fa fa-city\"></i>-edet egy másik játékos <i class=\"fa fa-city\"></i>-ére. Ha az elvett <i class=\"fa fa-city\"></i> értéke nagyobb, a különbözet árát ki kell fizetned neki!</p><p>Nem cserélheted ki befejezett város <i class=\"fa fa-city\"></i>-ét, kivéve ha a sajátod. Nem adhatsz olyan <i class=\"fa fa-city\"></i>-et, ami már van a másik játékos városában és nem vehetsz el olyat, amilyen már van a tiedben.</p>"),
            new AbilityEntity(29L, "Megszépítés", "paintbrush", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.OWN_BUILT_DISTRICT, "<p>Megszépíthetsz legfejlebb 2 <i class=\"fa fa-city\"></i>-et, fejenként 1 <i class=\"fa fa-coins\"></i>-ért cserébe. A megszépített <i class=\"fa fa-city\"></i>-ek értéke egyel nő a játék végéig. Egy <i class=\"fa fa-city\"></i>-et csak egyszer lehet megszépíteni.</p>"),
            new AbilityEntity(30L, "Parancsolatok kiosztása", "file-signature", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.WARRANTS, ""),
            new AbilityEntity(31L, "Zsarolás", "mask", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.THREAT_MARKERS, ""),
            new AbilityEntity(32L, "Lapkeverés", "hand-sparkles", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.SHUFFLE, "<p>Véletlenszerűen kapsz egy <i class=\"fa fa-sheet-plastic\"></i>-t minden játékos kezéből. Ezután adnod kell minden játékosnak egy lapot a kezedből. Ha egy játékosnak nincs <i class=\"fa fa-sheet-plastic\"></i> a kezében, nem veszel tőle <i class=\"fa fa-sheet-plastic\"></i>-t és nem is adsz neki</p>"),
            new AbilityEntity(33L, "Tapasztalt építő", "hammer", ActivationEnum.START_OF_TURN, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>Ebben a körben az építkezési korlátod 2 <i class=\"fa fa-city\"></i>.</p>"),
            new AbilityEntity(34L, "Egyházi segély", "handshake-angle", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.DISTRICT_AND_PLAYER_HELP, "<p>Megépíthetsz egy <i class=\"fa fa-city\"></i>-et, amire nincs elég <i class=\"fa fa-coins\"></i>-ad. Ehhez ki kell választanod egy másik játékost és elvenni tőle a megépítéshez szükséges <i class=\"fa fa-coins\"></i>-at, cserébe pedig adnod kell ugyannyi <i class=\"fa fa-sheet-plastic\"></i>-t a kezedből."),
            new AbilityEntity(35L, "Fejlődő kereskedelem", "piggy-bank", ActivationEnum.START_OF_TURN, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>Bármennyi <span style=\"font-variant: small-caps\">kereskedelmi</span> kerületet építhetsz.</p>"),
            new AbilityEntity(36L, "Kutatás", "mortar-pestle", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.SELECTOR, "<p>Húzol 7 <i class=\"fa fa-sheet-plastic\"></i>-t és választasz egyet, amit megtarthatsz.</p>"),
            new AbilityEntity(37L, "Kerület foglalás", "building-circle-arrow-right", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.CHEAP_BUILT_DISTRICT, ""),
            new AbilityEntity(38L, "Adószedés", "coins", ActivationEnum.MANUAL, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, ""),
            new AbilityEntity(39L, "Aranybányászat", "coins", ActivationEnum.ON_BUILD, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>Ha aranyat szerzel nyersanyag gyűjtéskor, kapsz 1 <i class=\"fa fa-coins\"></i>-t.</p>"),
            new AbilityEntity(40L, "Állványzat felhasználása", "hammer", ActivationEnum.AFTER_BUILD, AbilityUsageEnum.AND, AbilityTargetEnum.OWN_CARD, "<p>Megépítesz egy kerületet úgy, hogy az Állványzatot semmisíted meg, ahelyett, hogy kifizetnéd a <i class=\"fa fa-city\"></i> árát.</p><p>Magisztrátus nem kobozhat el Állványzat álltal épített <i class=\"fa fa-city\"></i>-t.</p>"),
            new AbilityEntity(41L, "Páratlanok", "place-of-worship", ActivationEnum.END_OF_GAME, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>A játék végén 1 <i class=\"fa fa-star\"></i> jár városodban minden olyan <i class=\"fa fa-city\"></i>-ért, aminek ára páratlan szám.</p>"),
            new AbilityEntity(42L, "Megtakarítás", "piggy-bank", ActivationEnum.END_OF_GAME, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>A játék végén 1 <i class=\"fa fa-star\"></i> jár a kincstartalékodban lévő minden <i class=\"fa fa-coins\"></i> után.</p>"),
            new AbilityEntity(43L, "Tematikus", "landmark-dome", ActivationEnum.END_OF_GAME, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>A játék végén 3 <i class=\"fa fa-star\"></i> jár, ha  van legalább 3 egyforma típusú <i class=\"fa fa-city\"></i> a városodban.</p><p>A Capitolium csak egyszer adhat <i class=\"fa fa-star\"></i>-t.</p>"),
            new AbilityEntity(44L, "Csillag jóslás", "binoculars", ActivationEnum.ON_BUILD, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>Ha <i class=\"fa fa-sheet-plastic\"></i>-t húzol nyersanyag gyűjtéskor, eggyel több közül választhatsz.</p>"),
            new AbilityEntity(45L, "Egy torony mind felett", "chess-rook", ActivationEnum.END_OF_GAME, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>A játék végén 5 <i class=\"fa fa-star\"></i> jár, ha az Elefántcsonttorony az egyetlen <span style=\"font-variant: small-caps\">egyedi</span> <i class=\"fa fa-city\"></i> a városodban.</p>"),
            new AbilityEntity(46L, "Bevehetetlen erőd", "building-shield", ActivationEnum.ON_BUILD, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>8-as rangú karakter nem használhatja a képességét az Erődítményen.</p>"),
            new AbilityEntity(47L, "Fegyvertár felhasználása", "bomb", ActivationEnum.AFTER_BUILD, AbilityUsageEnum.AND, AbilityTargetEnum.BUILT_DISTRICT, "<p>A köröd folyamán elpusztíthatod a Fegyvertárat, hogy elpusztíts egy másik játékos városában lévő <i class=\"fa fa-city\"></i>-t.</p><p>Befejezett városban nem lehet <i class=\"fa fa-city\"></i>-t elpusztítani.</p>"),
            new AbilityEntity(48L, "Tömeggyártás", "industry", ActivationEnum.ON_BUILD, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>Eggyel kevesebbet fizetsz minden más, <span style=\"font-variant: small-caps\">egyedi</span> <i class=\"fa fa-city\"></i> megépítéséért.</p>"),
            new AbilityEntity(49L, "Korlátlan", "horse", ActivationEnum.ON_BUILD, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>Az Istálló megépítése nem számít bele a körre vonatkozó építési korlátba.</p><p>Ha a magisztrátus elkobozza a játékos építhet még egy <i class=\"fa fa-city\"></i>-t ebben a körben.</p>"),
            new AbilityEntity(50L, "Múltbéli szellemek", "ghost", ActivationEnum.END_OF_GAME, AbilityUsageEnum.AND, AbilityTargetEnum.DISTRICT_TYPE, "<p>A játék végén a Kísértetváros az általad választott típusú <i class=\"fa fa-city\"></i>-nek számít.</p><p>Ha a birtokos úgy dönt, hogy a Kísértetváros típusa nem <span style=\"font-variant: small-caps\">egyedi</span>, akkor többé nem számít annak.</p>"),
            new AbilityEntity(51L, "Különleges kívánság", "star", ActivationEnum.END_OF_GAME, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>A játék végén 1 <i class=\"fa fa-star\"></i> jár a városodban lévő minden <span style=\"font-variant: small-caps\">egyedi</span> <i class=\"fa fa-city\"></i> után (beleértve a Kívánságkutat is).</p>"),
            new AbilityEntity(52L, "Kovácsolás", "gavel", ActivationEnum.AFTER_BUILD, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>A köröd folyamán egyszer 2 <i class=\"fa fa-coins\"></i>-ért 3 <i class=\"fa fa-sheet-plastic\"></i>-t húzhatsz.</p>"),
            new AbilityEntity(53L, "Tudás könyvtára", "book", ActivationEnum.ON_BUILD, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>Ha <i class=\"fa fa-sheet-plastic\"></i>-t húzol nyersanyag gyűjtéskor, eggyel több <i class=\"fa fa-sheet-plastic\"></i>-t tarts meg.</p>"),
            new AbilityEntity(54L, "Kőfejtés", "person-digging", ActivationEnum.ON_BUILD, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>Bármennyi egyforma <i class=\"fa fa-city\"></i>-t építhetsz a városodban.</p><p>8-as rangú karakter nem használhatja a képességét egyforma <i class=\"fa fa-city\"></i> megszerzésére.</p>"),
            new AbilityEntity(55L, "Kutatás", "vial", ActivationEnum.AFTER_BUILD, AbilityUsageEnum.AND, AbilityTargetEnum.OWN_CARD, "<p>A köröd folyamán egyszer eldobhatsz egy <i class=\"fa fa-sheet-plastic\"></i>-t a kezedből és kapsz 2 <i class=\"fa fa-coins\"></i>-t.</p>"),
            new AbilityEntity(56L, "A fal védelme", "building-shield", ActivationEnum.ON_BUILD, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>A 8-as rangú karakternek eggyel több <i class=\"fa fa-coins\"></i>-t kell fizetnie, hogy használhassa a képességét városodban lévő bármely más <i class=\"fa fa-city\"></i>-en.</p>"),
            new AbilityEntity(57L, "Friss levegő", "tree", ActivationEnum.ON_BUILD, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>Ha nincs <i class=\"fa fa-sheet-plastic\"></i> a kezedben a köröd végén, húzol két <i class=\"fa fa-sheet-plastic\"></i>-t.</p><p>Ha a birtokos a Boszorkány és nincs megbabonázott köre, akkor a Park képessége nem lép életbe.</p>"),
            new AbilityEntity(58L, "A nagy kapu", "torii-gate", ActivationEnum.END_OF_GAME, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>A játék végén 2 <i class=\"fa fa-star\"></i> jár.</p>"),
            new AbilityEntity(59L, "Adomány", "coins", ActivationEnum.ON_BUILD, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>Ha nincs <i class=\"fa fa-coins\"></i> a kincstartalékodban a köröd végén, kapsz 1 <i class=\"fa fa-coins\"></i>-t.</p><p>Ha a birtokos a Boszorkány és nincs megbabonázott köre, akkor a Szegényház képessége nem lép életbe.</p><p>Az alkímista képessége a Szegényház hatása után érvényesül.</p>"),
            new AbilityEntity(60L, "Királyi szobor", "crown", ActivationEnum.END_OF_GAME, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>A játék végén 5 <i class=\"fa fa-star\"></i> jár, ha nálad van a <i class=\"fa fa-crown\"></i>.</p>"),
            new AbilityEntity(61L, "Katasztrófa", "skull-crossbones", ActivationEnum.BEFORE_BUILD, AbilityUsageEnum.AND, AbilityTargetEnum.OWN_BUILT_DISTRICT, "<p>Megépítheted úgy a Temetőt, hogy elpusztítasz egy <i class=\"fa fa-city\"></i>-t a városodban, ahelyett, hogy kifizetnéd a Temető árát.</p><p>Magisztrátus nem kobozhat el Temetőt ami a képessége által lett építve.</p>"),
            new AbilityEntity(62L, "Térkép a sikerhez", "map", ActivationEnum.END_OF_GAME, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>A játék végén 1 <i class=\"fa fa-star\"></i> jár minden kezedben maradt <i class=\"fa fa-sheet-plastic\"></i> után.</p>"),
            new AbilityEntity(63L, "Tolvajok tanyája", "sack-dollar", ActivationEnum.BEFORE_BUILD, AbilityUsageEnum.AND, AbilityTargetEnum.OWN_CARDS, "<p>Építéskor a Tolvajtanya árát fizetheted részben <i class=\"fa fa-sheet-plastic\"></i>-okból (1 <i class=\"fa fa-sheet-plastic\"></i> = 1 <i class=\"fa fa-coins\"></i>).</p><p>Ha a magisztrátus elkobozza a Tolvajtanyát csak az <i class=\"fa fa-coins\"></i>-t kapja vissza a tulajdonos.</p>"),
            new AbilityEntity(64L, "Mágikus kerület", "hat-wizard", ActivationEnum.ON_BUILD, AbilityUsageEnum.AND, AbilityTargetEnum.NONE, "<p>A kerületekhez nyersanyagokat gyűjtő képességeket tekintve a Varázstanoda az általad választott típusú kerületnek számít.</p>"));
        abilityRepository.saveAll(abilities);
        log.info("Abilities generated successfully!");
        return abilities;
    }

//    private void generateDefaultConditions() {
//        List<ConditionEntity> conditions = List.of(
//            new ConditionEntity(1L, "Megkoronázva", "crown", true, true, ActivationEnum.NONE, ConditionDurationEnum.INDEFINITE, "<p>Nálad van a <i class=\"fa fa-crown\"></i>, te kezded a kíválasztási fázist.</p>"),
//            new ConditionEntity(2L, "Meggyilkolva", "skull", false, true, ActivationEnum.START_OF_TURN, ConditionDurationEnum.END_OF_TURN, "<p>Az orgyilkos megölt, kimaradsz ebből a körből.</p>"),
//            new ConditionEntity(3L, "Kirabolva", "sack-dollar", false, true, ActivationEnum.START_OF_TURN, ConditionDurationEnum.END_OF_TURN, "<p>A tolvaj kirabolt, a köröd elején elveszi az összes <i class=\"fa fa-coins\"></i>-ad.</p>"),
//            new ConditionEntity(4L, "Védelem", "building-shield", true, true, ActivationEnum.START_OF_TURN, ConditionDurationEnum.END_OF_TURN, "<p>A 8-as rangú karakter képességei nem használhatóak a <i class=\"fa fa-city\"></i>-eiden.</p>"),
//            new ConditionEntity(5L, "Megbabonázva", "wand-sparkles", false, true, ActivationEnum.AFTER_GATHERING, ConditionDurationEnum.END_OF_TURN, "<p>A boszorkány megbabonázott. Nyersanyag gyűjtés után vége a körödnek.</p>"),
//            new ConditionEntity(6L, "Duplikátumok", "clone", true, true, ActivationEnum.START_OF_TURN, ConditionDurationEnum.END_OF_TURN, "<p>Ebben a körben olyan <i class=\"fa fa-city\"></i>-eket is építhetsz a városodban, amilyenek már léteznek.</p>"),
//            new ConditionEntity(7L, "Tengeren", "person-walking-luggage", true, true, ActivationEnum.START_OF_TURN, ConditionDurationEnum.END_OF_TURN, "<p>Nem építhetsz semmilyen kerületet ebben a körben.</p>"),
//            new ConditionEntity(8L, "Fejlődő kereskedelem", "piggy-bank", true, true, ActivationEnum.START_OF_TURN, ConditionDurationEnum.END_OF_TURN, "<p>Bármennyi <span style=\"font-variant: small-caps\">kereskedelmi</span> kerületet építhetsz.</p>"),
//            new ConditionEntity(9L, "Aranybányászat", "coins", true, true, ActivationEnum.RESOURCE_GATHERING, ConditionDurationEnum.INDEFINITE, "<p>Ha aranyat szerzel nyersanyag gyűjtéskor, kapsz 1 <i class=\"fa fa-coins\"></i>-t.</p>"),
//            new ConditionEntity(10L, "Csillag jóslás", "binoculars", true, true, ActivationEnum.RESOURCE_GATHERING, ConditionDurationEnum.INDEFINITE, "<p>Ha <i class=\"fa fa-sheet-plastic\"></i>-t húzol nyersanyag gyűjtéskor, eggyel több közül választhatsz.</p>"),
//            new ConditionEntity(11L, "Bevehetetlen erőd", "building-shield", true, true, ActivationEnum.NONE, ConditionDurationEnum.INDEFINITE, "<p>8-as rangú karakter nem használhatja a képességét az Erődítményen.</p>"),
//            new ConditionEntity(12L, "Tömeggyártás", "industry", true, true, ActivationEnum.NONE, ConditionDurationEnum.INDEFINITE, "<p>Eggyel kevesebbet fizetsz minden más, <span style=\"font-variant: small-caps\">egyedi</span> <i class=\"fa fa-city\"></i> megépítéséért.</p>"),
//            new ConditionEntity(13L, "Tudás könyvtára", "book", true, true, ActivationEnum.RESOURCE_GATHERING, ConditionDurationEnum.INDEFINITE, "<p>Ha <i class=\"fa fa-sheet-plastic\"></i>-t húzol nyersanyag gyűjtéskor, eggyel több <i class=\"fa fa-sheet-plastic\"></i>-t tarts meg.</p>"),
//            new ConditionEntity(14L, "Kőfejtés", "person-digging", true, true, ActivationEnum.NONE, ConditionDurationEnum.INDEFINITE, "<p>Bármennyi egyforma <i class=\"fa fa-city\"></i>-t építhetsz a városodban.</p><p>8-as rangú karakter nem használhatja a képességét egyforma <i class=\"fa fa-city\"></i> megszerzésére.</p>"),
//            new ConditionEntity(15L, "A fal védelme", "building-shield", true, true, ActivationEnum.NONE, ConditionDurationEnum.INDEFINITE, "<p>A 8-as rangú karakternek eggyel több <i class=\"fa fa-coins\"></i>-t kell fizetnie, hogy használhassa a képességét városodban lévő bármely más <i class=\"fa fa-city\"></i>-en.</p>"),
//            new ConditionEntity(16L, "Friss levegő", "tree", true, true, ActivationEnum.END_OF_TURN, ConditionDurationEnum.INDEFINITE, "<p>Ha nincs <i class=\"fa fa-sheet-plastic\"></i> a kezedben a köröd végén, húzol két <i class=\"fa fa-sheet-plastic\"></i>-t.</p><p>Ha a birtokos a Boszorkány és nincs megbabonázott köre, akkor a Park képessége nem lép életbe.</p>"),
//            new ConditionEntity(17L, "Adomány", "coins", true, true, ActivationEnum.END_OF_TURN, ConditionDurationEnum.INDEFINITE, "<p>Ha nincs <i class=\"fa fa-coins\"></i> a kincstartalékodban a köröd végén, kapsz 1 <i class=\"fa fa-coins\"></i>-t.</p><p>Ha a birtokos a Boszorkány és nincs megbabonázott köre, akkor a Szegényház képessége nem lép életbe.</p><p>Az alkímista képessége a Szegényház hatása után érvényesül.</p>"),
//            new ConditionEntity(18L, "Mágikus kerület", "hat-wizard", true, true, ActivationEnum.NONE, ConditionDurationEnum.INDEFINITE, "<p>A kerületekhez nyersanyagokat gyűjtő képességeket tekintve a Varázstanoda az általad választott típusú kerületnek számít.</p>")
//        );
//        conditionRepository.saveAll(conditions);
//        log.info("Conditions generated successfully!");
//    }

    private void generateDefaultDistricts(List<AbilityEntity> abilities) {
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
                new DistrictEntity(18L, "Aranybánya", "Aranybánya", 6, DistrictTypeEnum.UNIQUE, List.of(abilities.get(38))),
                new DistrictEntity(19L, "Állványzat", "Állványzat", 3, DistrictTypeEnum.UNIQUE, List.of(abilities.get(39))),
                new DistrictEntity(20L, "Bazilika", "Bazilika", 4, DistrictTypeEnum.UNIQUE, List.of(abilities.get(40))),
                new DistrictEntity(21L, "Birodalmi Kincstár", "Birodalmi Kincstár", 5, DistrictTypeEnum.UNIQUE, List.of(abilities.get(41))),
                new DistrictEntity(22L, "Capitolium", "Capitolium", 5, DistrictTypeEnum.UNIQUE, List.of(abilities.get(42))),
                new DistrictEntity(23L, "Csillagvizsgáló", "Csillagvizsgáló", 4, DistrictTypeEnum.UNIQUE, List.of(abilities.get(43))),
                new DistrictEntity(24L, "Elefántcsonttorony", "Elefántcsont- torony", 5, DistrictTypeEnum.UNIQUE, List.of(abilities.get(44))),
                new DistrictEntity(25L, "Emlékmű", "Emlékmű", 4, DistrictTypeEnum.UNIQUE, List.of()), // "<p>Nem építhetsz Emlékművet, ha 5 vagy annál több <i class=\"fa fa-city\"></i> van a városodban. Az Emlékmű 2 <i class=\"fa fa-city\"></i>-nek számít a befejezett város szempontjából.</p>"
                new DistrictEntity(26L, "Erődítmény", "Erődítmény", 3, DistrictTypeEnum.UNIQUE, List.of(abilities.get(45))),
                new DistrictEntity(27L, "Fegyvertár", "Fegyvertár", 3, DistrictTypeEnum.UNIQUE, List.of(abilities.get(46))),
                new DistrictEntity(28L, "Gyár", "Gyár", 5, DistrictTypeEnum.UNIQUE, List.of(abilities.get(47))),
                new DistrictEntity(29L, "Istálló", "Istálló", 2, DistrictTypeEnum.UNIQUE, List.of(abilities.get(48))),
                new DistrictEntity(30L, "Kísértetváros", "Kísértetváros", 2, DistrictTypeEnum.UNIQUE, List.of(abilities.get(49))),
                new DistrictEntity(31L, "Kívánságkút", "Kívánságkút", 5, DistrictTypeEnum.UNIQUE, List.of(abilities.get(50))),
                new DistrictEntity(32L, "Kovácsműhely", "Kovácsműhely", 5, DistrictTypeEnum.UNIQUE, List.of(abilities.get(51))),
                new DistrictEntity(33L, "Könyvtár", "Könyvtár", 6, DistrictTypeEnum.UNIQUE, List.of(abilities.get(52))),
                new DistrictEntity(34L, "Kőfejtő", "Kőfejtő", 5, DistrictTypeEnum.UNIQUE, List.of(abilities.get(53))),
                new DistrictEntity(35L, "Laboratórium", "Laboratórium", 5, DistrictTypeEnum.UNIQUE, List.of(abilities.get(54))),
                new DistrictEntity(36L, "Múzeum", "Múzeum", 4, DistrictTypeEnum.UNIQUE, List.of()), // "<p>A köröd folyamán egyszer helyezhetsz egy <i class=\"fa fa-sheet-plastic\"></i>-t a kezedből a múzeumba. A játék végén minden <i class=\"fa fa-sheet-plastic\"></i>-ért a múzeumban 1 <i class=\"fa fa-star\"></i> jár.</p><p>Ha a múzeumot kicserélik vagy lefoglalják a benne lévő <i class=\"fa fa-sheet-plastic\"></i>-ok vele mennek. Ha elpusztítják, a benne lévő <i class=\"fa fa-sheet-plastic\"></i>-ok a pakli aljára kerülnek.</p>"
                new DistrictEntity(37L, "Nagy Fal", "Nagy Fal", 6, DistrictTypeEnum.UNIQUE, List.of(abilities.get(55))),
                new DistrictEntity(38L, "Park", "Park", 6, DistrictTypeEnum.UNIQUE, List.of(abilities.get(56))),
                new DistrictEntity(39L, "Sárkánykapu", "Sárkánykapu", 6, DistrictTypeEnum.UNIQUE, List.of(abilities.get(57))),
                new DistrictEntity(40L, "Szegényház", "Szegényház", 4, DistrictTypeEnum.UNIQUE, List.of(abilities.get(58))),
                new DistrictEntity(41L, "Színház", "Színház", 6, DistrictTypeEnum.UNIQUE, List.of()),// "<p>Minden választási fázis végén vakon kicserélheted az általad választott karaktert ellenfeled egyik karakterkártyájával.</p>"
                new DistrictEntity(42L, "Szobor", "Szobor", 3, DistrictTypeEnum.UNIQUE, List.of(abilities.get(59))),
                new DistrictEntity(43L, "Temető", "Temető", 5, DistrictTypeEnum.UNIQUE, List.of(abilities.get(60))),
                new DistrictEntity(44L, "Térképszoba", "Térképszoba", 5, DistrictTypeEnum.UNIQUE, List.of(abilities.get(61))),
                new DistrictEntity(45L, "Titkos Kripta", "Titkos Kripta", 0, DistrictTypeEnum.UNIQUE, List.of()), // "<p>A Titkos kirptát nem lehet építeni. A játék végén 3 <i class=\"fa fa-star\"></i> jár, ha a Titkos kripta a kezedben van.</p>"
                new DistrictEntity(46L, "Tolvajtanya", "Tolvajtanya", 6, DistrictTypeEnum.UNIQUE, List.of(abilities.get(62))),
                new DistrictEntity(47L, "Varázstanoda", "Varázstanoda", 6, DistrictTypeEnum.UNIQUE, List.of(abilities.get(63))));
        districtRepository.saveAll(districts);
        log.info("Districts generated successfully!");
    }

    private void generateDefaultCharacters(List<AbilityEntity> abilities) {
        List<CharacterEntity> characters = List.of(
            new CharacterEntity(1L, 1, "Orgyilkos", null, List.of(abilities.get(8))),
            new CharacterEntity(2L, 2, "Tolvaj", null, List.of(abilities.get(9))),
            new CharacterEntity(3L, 3, "Mágus", null, List.of(abilities.get(10), abilities.get(11))),
            new CharacterEntity(4L, 4, "Király", DistrictTypeEnum.NOBLE, List.of(abilities.get(0), abilities.get(7))),
            new CharacterEntity(5L, 5, "Püspök", DistrictTypeEnum.RELIGIOUS, List.of(abilities.get(2), abilities.get(12))),
            new CharacterEntity(6L, 6, "Kereskedő", DistrictTypeEnum.TRADE, List.of(abilities.get(5), abilities.get(13))),
            new CharacterEntity(7L, 7, "Építész", null, List.of(abilities.get(14), abilities.get(15))),
            new CharacterEntity(8L, 8, "Hadúr", DistrictTypeEnum.MILITARY, List.of(abilities.get(6), abilities.get(16))),
            new CharacterEntity(9L, 9, "Királynő", null, List.of(abilities.get(17))),
            new CharacterEntity(10L, 1, "Boszorkány", null, List.of(abilities.get(18))),
            new CharacterEntity(11L, 2, "Kém", null, List.of(abilities.get(19))),
            new CharacterEntity(12L, 3, "Varázsló", null, List.of(abilities.get(20), abilities.get(21))),
            new CharacterEntity(13L, 4, "Császár", DistrictTypeEnum.NOBLE, List.of(abilities.get(0), abilities.get(22))),
            new CharacterEntity(14L, 5, "Apát", DistrictTypeEnum.RELIGIOUS, List.of(abilities.get(4), abilities.get(23))),
            new CharacterEntity(15L, 6, "Alkimista", null, List.of(abilities.get(24))),
            new CharacterEntity(16L, 7, "Navigátor", null, List.of(abilities.get(25), abilities.get(26))),
            new CharacterEntity(17L, 8, "Diplomata", DistrictTypeEnum.MILITARY, List.of(abilities.get(6), abilities.get(27))),
            new CharacterEntity(18L, 9, "Művész", null, List.of(abilities.get(28))),
            new CharacterEntity(19L, 1, "Magisztrátus", null, List.of(abilities.get(29))),
            new CharacterEntity(20L, 2, "Zsaroló", null, List.of(abilities.get(30))),
            new CharacterEntity(21L, 3, "Látnok", null, List.of(abilities.get(31), abilities.get(32))),
            new CharacterEntity(22L, 4, "Patrícius", DistrictTypeEnum.NOBLE, List.of(abilities.get(1), abilities.get(8))),
            new CharacterEntity(23L, 5, "Bíboros", DistrictTypeEnum.RELIGIOUS, List.of(abilities.get(3), abilities.get(33))),
            new CharacterEntity(24L, 6, "Szatócs", DistrictTypeEnum.TRADE, List.of(abilities.get(6), abilities.get(34))),
            new CharacterEntity(25L, 7, "Tudós", null, List.of(abilities.get(32), abilities.get(35))),
            new CharacterEntity(26L, 8, "Marsall", DistrictTypeEnum.MILITARY, List.of(abilities.get(6), abilities.get(36))),
            new CharacterEntity(27L, 9, "Adószedő", null, List.of(abilities.get(37))));
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

    // @formatter:on
    @EventListener(ApplicationReadyEvent.class)
    public void startUpCheck() {
        List<AbilityEntity> abilities = abilityRepository.findAll();
        if (abilities.isEmpty()) {
            log.info("No Abilities found in database. Generating defaults...");
            abilities = generateDefaultAbilities();
        }
        if (districtRepository.count() == 0) {
            log.info("No Districts found in database. Generating default cards...");
            generateDefaultDistricts(abilities);
        }
        if (characterRepository.count() == 0) {
            log.info("No Characters found in database. Generating default cards...");
            generateDefaultCharacters(abilities);
        }
        if (configRepository.count() == 0) {
            log.info("No Config found in database. Generating default config...");
            generateDefaultConfig();
        }
    }
}
