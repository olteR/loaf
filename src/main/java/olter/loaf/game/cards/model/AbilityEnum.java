package olter.loaf.game.cards.model;

import lombok.Getter;
import olter.loaf.game.games.exception.InvalidTargetException;
import olter.loaf.game.games.model.GameEntity;
import olter.loaf.game.players.model.ConditionEnum;
import olter.loaf.game.players.model.PlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// @formatter:off
@Getter
public enum AbilityEnum {
    NOBLE_GOLD("NOBLE_GOLD", List.of("city", "coins"), "<p>Kapsz egy <b><i class=\"fa fa-coins\"></i></b> a városodban lévő minden egyes <span style=\"font-variant: small-caps\">nemesi</span> <i class=\"fa fa-city\"></i> után.</p>") {
        public void useAbility(GameEntity game, AbilityTargetDto target) {
            getTypeGold(game, DistrictTypeEnum.NOBLE);
        }
    },
    NOBLE_CARDS("NOBLE_CARDS", List.of("city", "sheet-playstic"), "<p>Húzol egy <b><i class=\"fa fa-sheet-plastic\"></i></b> a városodban lévő minden egyes <span style=\"font-variant: small-caps\">nemesi</span> <i class=\"fa fa-city\"></i> után.</p>") {
        public void useAbility(GameEntity game, AbilityTargetDto target) {
            getTypeCards(game, DistrictTypeEnum.NOBLE);
        }
    },
    RELIGIOUS_GOLD("RELIGIOUS_GOLD", List.of("city", "coins"), "<p>Kapsz egy <b><i class=\"fa fa-coins\"></i></b> a városodban lévő minden egyes <span style=\"font-variant: small-caps\">vallási</span> <i class=\"fa fa-city\"></i> után.</p>") {
        public void useAbility(GameEntity game, AbilityTargetDto target) {
            getTypeGold(game, DistrictTypeEnum.RELIGIOUS);
        }
    },
    RELIGIOUS_CARDS("RELIGIOUS_CARDS", List.of("city", "sheet-playstic"), "<p>Húzol egy <b><i class=\"fa fa-sheet-plastic\"></i></b> a városodban lévő minden egyes <span style=\"font-variant: small-caps\">vallási</span> <i class=\"fa fa-city\"></i> után.</p>") {
        public void useAbility(GameEntity game, AbilityTargetDto target) {
            getTypeCards(game, DistrictTypeEnum.RELIGIOUS);
        }
    },
    RELIGIOUS_GOLD_OR_CARDS("RELIGIOUS_GOLD_OR_CARDS", List.of("city", "coins", "sheet-plastic"), AbilityTargetEnum.GOLD_OR_CARDS_MULTIPLE, "<p>Kapsz egy <b><i class=\"fa fa-coins\"></i>-t</b> vagy <b><i class=\"fa fa-sheet-plastic\"></i>-t</b> a városodban lévő minden <span style=\"font-variant: small-caps\">egyházi</span> <i class=\"fa fa-city\"></i> után.</p>") {
        public void useAbility(GameEntity game, AbilityTargetDto target) {
            // TODO
        }
    },
    TRADE_GOLD("TRADE_GOLD", List.of("city", "coins"), "<p>Kapsz egy <b><i class=\"fa fa-coins\"></i></b> a városodban lévő minden egyes <span style=\"font-variant: small-caps\">keresekedelmi</span> <i class=\"fa fa-city\"></i> után.</p>") {
        public void useAbility(GameEntity game, AbilityTargetDto target) {
            getTypeGold(game, DistrictTypeEnum.TRADE);
        }
    },
    MILITARY_GOLD("MILITARY_GOLD", List.of("city", "coins"), "<p>Kapsz egy <b><i class=\"fa fa-coins\"></i></b> a városodban lévő minden egyes <span style=\"font-variant: small-caps\">katonai</span> <i class=\"fa fa-city\"></i> után.</p>") {
        public void useAbility(GameEntity game, AbilityTargetDto target) {
            getTypeGold(game, DistrictTypeEnum.MILITARY);
        }
    },
    TAKE_CROWN("TAKE_CROWN", List.of(), ActivationEnum.START_OF_TURN, "<p>A köröd során elveszed a <i class=\"fa fa-crown\"></i>-t. A karakter választási fázisban te választasz először karaktert, amíg egy másik játékos nem választja a Királyt.</p><p>Ha meggyilkolnak, akkor ugyanúgy kihagyod a körödet, mint bármely más karakter, a <i class=\"fa fa-crown\"></i> pedig nálad marad, mint trónörökösnél.</p><p>Ha megbabonáznak, attól még megtartod a <i class=\"fa fa-crown\"></i>-t.</p>") {
        public void useAbility(GameEntity game, AbilityTargetDto target) {
            game.setCrownedPlayer(game.getCurrentPlayer());
        }
    },
    ASSASSIN("ASSASSIN", List.of("user", "skull"), AbilityTargetEnum.CHARACTER, "<p>Válassz egy másik karaktert, akit meg szeretnél gyilkolni! A meggyilkolt karakter kihagyja az egész körét.</p>") {
        public void useAbility(GameEntity game, AbilityTargetDto target) {
            if (target.getIndex() < 2 || target.getIndex() > game.getCharacters().size()) {
                throw new InvalidTargetException(ASSASSIN);
            }
            game.setKilledCharacter(target.getIndex());
        }
    },
    THIEF("THIEF", List.of("user", "sack-dollar"), AbilityTargetEnum.CHARACTER, "<p>Válaszd ki, hogy melyik karaktertől szeretnél lopni! Amikor az a karakter következik, elveszed a kincstartalékában lévő összes <i class=\"fa fa-coins\"></i>-t.</p><p>Nem rabolhatsz ki 1-es rangú karkatert, megölt vagy megbabonázott karaktert.</p>") {
        public void useAbility(GameEntity game, AbilityTargetDto target) {
            if (target.getIndex() < 3 || target.getIndex() > game.getCharacters().size() &&
                Objects.equals(game.getKilledCharacter(), target.getIndex()) ||
                Objects.equals(game.getBewitchedCharacter(), target.getIndex())) {
                throw new InvalidTargetException(THIEF);
            }
            game.setRobbedCharacter(target.getIndex());
        }
    },
    MAGICIAN_PLAYER("MAGICIAN_PLAYER", List.of("user", "sheet-plastic"), AbilityUsageEnum.OR, AbilityTargetEnum.PLAYER, "<p>Kicserélheted a kezedben lévő összes <i class=\"fa fa-sheet-plastic\"></i>-t egy másik játékos kezében lévő <i class=\"fa fa-sheet-plastic\"></i>-okra.</p>") {
        public void useAbility(GameEntity game, AbilityTargetDto target) {
            // TODO
        }
    },
    MAGICIAN_DECK("MAGICIAN_DECK", List.of("sheet-plastic", "rotate"), AbilityUsageEnum.OR, AbilityTargetEnum.OWN_CARDS, "<p>Tetszőleges számú <i class=\"fa fa-sheet-plastic\"></i>-t eldobhatsz a kezedből és húzol helyettük ugyanannyit.</p>") {
        public void useAbility(GameEntity game, AbilityTargetDto target) {
            // TODO
        }
    },
    BISHOP("BISHOP", ActivationEnum.START_OF_TURN, "<p>Ebben a körben a 8-as rangú karakter nem használhatja képességét a <i class=\"fa fa-city\"></i>-eiden.</p><p>Ha megölnek, nyolcas rangú karakter <b>tudja</b> használni a képességét a <i class=\"fa fa-city\"></i>-eiden. Ugyanígy, ha megbabonáznak, nyolcas rangú karakter <b>nem tudja</b> használni a képességeit a Boszorkány <i class=\"fa fa-city\"></i>-ein, de <b>képes</b> használni a képességét a Püspök <i class=\"fa fa-city\"></i>-ein.</p>") {
        public void useAbility(GameEntity game, AbilityTargetDto target) {
            game.getCurrentPlayer().getConditions().add(ConditionEnum.PROTECTED);
        }
    },
    MERCHANT("MERCHANT", List.of("coins", "plus"), "<p>Kapsz plusz egy <i class=\"fa fa-coins\"></i>-t.</p>") {
        public void useAbility(GameEntity game, AbilityTargetDto target) {
            game.getCurrentPlayer().giveGold(1);
        }
    },
    ARCHITECT("ARCHITECT", List.of("sheet-plastic", "plus"), "<p>Kapsz két plusz <i class=\"fa fa-sheet-plastic\"></i>-t.</p>") {
        public void useAbility(GameEntity game, AbilityTargetDto target) {
            game.getCurrentPlayer().giveCards(game.drawFromDeck(2));
        }
    },
    BUILD_LIMIT_3("BUILD_LIMIT_3", ActivationEnum.START_OF_TURN, "<p>Kapsz két plusz <i class=\"fa fa-sheet-plastic\"></i>-t.</p>") {
        public void useAbility(GameEntity game, AbilityTargetDto target) {
            game.getCurrentPlayer().setBuildLimit(3);
        }
    },
    WARLORD("WARLORD", List.of("city", "x"), AbilityTargetEnum.BUILT_DISTRICT, "<p>Elpusztíthatsz egy tetszőleges <i class=\"fa fa-city\"></i>-t: ez eggyel kevesebb <i class=\"fa fa-coins\"></i>-ba kerül, mint amennyi az ára.</p><p>Nem pusztíthatsz el befejezett városban <i class=\"fa fa-city\"></i>-t, de saját <i class=\"fa fa-city\"></i>-eid egyikét igen.</p>") {
        public void useAbility(GameEntity game, AbilityTargetDto target) {
            // TODO
        }
    },
    QUEEN("QUEEN", List.of("crown", "coins"), "<p>Ha a sorban egy melletted lévő játékos 4-es rangú karaktert választott, kapsz 3 <i class=\"fa fa-coins\"></i>-t. Ha ezt a karaktert megöli az Orgyilkos, az <i class=\"fa fa-coins\"></i>-t a kör legvégén kapod meg.</p>") {
        public void useAbility(GameEntity game, AbilityTargetDto target) {
            // TODO
        }
    },
    WITCH("WITCH", List.of("user", "wand-sparkles"), AbilityUsageEnum.MUST, AbilityTargetEnum.CHARACTER, "<p>Nyersanyag gyűjtés után ki kell választanod melyik karaktert szeretnéd megbabonázni, ekkor a köröd felfüggesztésre kerül! Ekkor nem tudsz építkezni, és a <i class=\"fa fa-city\"></i>-ek közül csak azok hatása érvényesül, amik nyersanyagokat gyűjtenek.</p><p>Amikor a megbabonázott karakter kerül sorra, a játékos nyersanyagokat gyűjt, és azonnal véget kell vetnie a körének. Nem képes <i class=\"fa fa-city\"></i>-t építeni sem, karakterek képességeit használni — még azokat sem, amik <q>plusz</q> nyersanyagokat adnak. A <i class=\"fa fa-city\"></i>-ek közül csak azok hatása érvényesül a megbabonázott karakter esetében, amik nyersanyagokat gyűjtenek.</p><p>Ezután úgy folytatod a körödet, mintha te játszanál a megbabonázott karakterrel: használod a képességeit, beleértve a plusz nyersanyagokat adókat, a passzívakat, és a megkötéseket. A <b>te</b> kezedben lévő <i class=\"fa fa-sheet-plastic\"></i>-okkal játszol, a <b>te</b> kincstartalékodban lévő <i class=\"fa fa-coins\"></i>-al fizetsz, a <b>te</b> városod <i class=\"fa fa-city\"></i>-eiből gyűjtesz nyersanyagokat, a <b>te</b> városodban építesz új kerületeket. Nem tudod használni a megbabonázott játékos bírtokában lévő <span style=\"font-variant: small-caps\">egyedi</span> <i class=\"fa fa-city\"></i>-ek hatásait.</p><p>Ha a Királyt vagy a Patríciust babonázzák meg, varázslattól függetlenül megkapja a <i class=\"fa fa-crown\"></i>-t. Ha a Császárt babonázzák meg, te döntöd el, ki kapja a <i class=\"fa fa-crown\"></i>-t, és attól a játékostól veszed el a nyersanyagot.</p><p>Ha a megbabonázott karakter nincs játékban ebben a körben, a köröd nem folytatódik.</p>") {
        public void useAbility(GameEntity game, AbilityTargetDto target) {
            if (target.getIndex() < 2 || target.getIndex() > game.getCharacters().size()) {
                throw new InvalidTargetException(WITCH);
            }
            game.setBewitchedCharacter(target.getIndex());
        }
    },
    SPY("SPY", List.of("sheet-plastic", "user-secret"), AbilityTargetEnum.PLAYER_AND_DISTRICT_TYPE, "<p>Válassz ki egy másik játékost és egy kerülettípust! Ezután megnézheted a másik játékos kezében lévő <i class=\"fa fa-sheet-plastic\"></i>-okat. Minden olyan <i class=\"fa fa-sheet-plastic\"></i>-ért, ami a kezében van, és egyezik a megnevezett kerülettípussal elveszel tőle egy <i class=\"fa fa-coins\"></i>-t és húzol egy <i class=\"fa fa-sheet-plastic\"></i>-t a pakliból.</p><p>Ha több egyező <i class=\"fa fa-sheet-plastic\"></i> van, mint amennyi <i class=\"fa fa-coins\"></i>-a, akkor elveszed minden <i class=\"fa fa-coins\"></i>-át, de ettől még ugyanúgy megkapod a <i class=\"fa fa-sheet-plastic\"></i>-okat.</p>") {
        public void useAbility(GameEntity game, AbilityTargetDto target) {
            // TODO
        }
    },
    WIZARD("WIZARD", List.of("sheet-plastic", "wand-magic-sparkles"), AbilityTargetEnum.PLAYER_AND_DISTRICT_IN_HAND, "<p>Megnézheted egy másik játékos kézben tartott <i class=\"fa fa-sheet-plastic\"></i>-jait, majd elvehetsz tőle egyet. A <i class=\"fa fa-sheet-plastic\"></i>-t azonnal beépítheted a városodba, és ez nem számít bele az építkezési korlátba.</p>") {
        public void useAbility(GameEntity game, AbilityTargetDto target) {
            // TODO
        }
    },
    DUPLICATES("DUPLICATES", ActivationEnum.START_OF_TURN, "<p>Ebben a körben olyan <i class=\"fa fa-city\"></i>-eket is építhetsz a városodban, amilyenek már léteznek.</p>") {
        public void useAbility(GameEntity game, AbilityTargetDto target) {
            game.getCurrentPlayer().getConditions().add(ConditionEnum.DUPLICATES);
        }
    },
    EMPEROR("EMPEROR", List.of("crown", "user"), AbilityUsageEnum.MUST, AbilityTargetEnum.PLAYER, "<p>Valamikor a köröd során el kell venned a <i class=\"fa fa-crown\"></i>-t attól a játékostól akinél van és át kell adnod egy másik játékosnak (magadnak nem adhatod). Az a játékos, aki megkapta a <i class=\"fa fa-crown\"></i>-t, ad neked egy <i class=\"fa fa-coins\"></i>-t vagy véletlenszerűen egy <i class=\"fa fa-sheet-plastic\"></i>-t a kezéből. Ha nincs sem <i class=\"fa fa-coins\"></i>-a, sem <i class=\"fa fa-sheet-plastic\"></i>-ja, akkor nem kell adnia semmit.</p><p>Ha meggyilkolnak, a köröd végén teszed át a koronát és nem kapsz érte nyersanyagot.</p>") {
        public void useAbility(GameEntity game, AbilityTargetDto target) {
            // TODO
        }
    },
    ABBOT("ABBOT", List.of("user", "coins"), AbilityTargetEnum.RICHEST_PLAYER, "<p>Ha a köröd során bármikor előfordul, hogy nem te vagy a legtöbb <i class=\"fa fa-coins\"></i>-al rendelkező játékos, a leggazdagabb játékos ad neked egyet. Döntetlen esetén te választasz, hogy ki ad <i class=\"fa fa-coins\"></i>-t.</p>") {
        public void useAbility(GameEntity game, AbilityTargetDto target) {
            // TODO
        }
    },
    ALCHEMIST("ALCHEMIST", ActivationEnum.END_OF_TURN, "<p>A köröd végén minden építésre költött <i class=\"fa fa-coins\"></i>-t visszakapsz.</p>"),
    NAVIGATOR("NAVIGATOR", List.of("4", "coins", "sheet-plastic"), AbilityTargetEnum.GOLD_OR_CARDS, "<p>Kapsz 4 <i class=\"fa fa-coins\"></i>-t, vagy 4 <i class=\"fa fa-sheet-plastic\"></i>-t.</p>"),
    CANT_BUILD("CANT_BUILD", ActivationEnum.START_OF_TURN, "<p>Nem építhetsz semmilyen kerületet ebben a körben.</p>"),
    DIPLOMAT("DIPLOMAT", List.of("city", "hand"), AbilityTargetEnum.BUILT_DISTRICT, "<p>Kicserélheted az egyik <i class=\"fa fa-city\"></i>-edet egy másik játékos <i class=\"fa fa-city\"></i>-ére. Ha az elvett <i class=\"fa fa-city\"></i> értéke nagyobb, a különbözet árát ki kell fizetned neki!</p><p>Nem cserélheted ki befejezett város <i class=\"fa fa-city\"></i>-ét, kivéve ha a sajátod. Nem adhatsz olyan <i class=\"fa fa-city\"></i>-et, ami már van a másik játékos városában és nem vehetsz el olyat, amilyen már van a tiedben.</p>"),
    ARTIST("ARTIST", List.of("city", "paintbrush"), AbilityTargetEnum.OWN_BUILT_DISTRICT, "<p>Megszépíthetsz legfejlebb 2 <i class=\"fa fa-city\"></i>-et, fejenként 1 <i class=\"fa fa-coins\"></i>-ért cserébe. A megszépített <i class=\"fa fa-city\"></i>-ek értéke egyel nő a játék végéig. Egy <i class=\"fa fa-city\"></i>-et csak egyszer lehet megszépíteni.</p>"),
    MAGISTRATE("MAGISTRATE", List.of("user", "file-signature"), AbilityTargetEnum.WARRANTS, ""),
    BLACKMAILER("BLACKMAILER", List.of("user", "mask"), AbilityTargetEnum.THREAT_MARKERS, ""),
    SEER("SEER", List.of("users", "hand-sparkles"), AbilityTargetEnum.SHUFFLE, "<p>Véletlenszerűen kapsz egy <i class=\"fa fa-sheet-plastic\"></i>-t minden játékos kezéből. Ezután adnod kell minden játékosnak egy lapot a kezedből. Ha egy játékosnak nincs <i class=\"fa fa-sheet-plastic\"></i> a kezében, nem veszel tőle <i class=\"fa fa-sheet-plastic\"></i>-t és nem is adsz neki</p>"),
    BUILD_LIMIT_2("BUILD_LIMIT_2", ActivationEnum.START_OF_TURN, "<p>Ebben a körben az építkezési korlátod 2 <i class=\"fa fa-city\"></i>.</p>"),
    CARDINAL("CARDINAL", List.of("hammer", "hand-holding-dollar"), AbilityTargetEnum.DISTRICT_AND_PLAYER_HELP, "<p>Megépíthetsz egy <i class=\"fa fa-city\"></i>-et, amire nincs elég <i class=\"fa fa-coins\"></i>-ad. Ehhez ki kell választanod egy másik játékost és elvenni tőle a megépítéshez szükséges <i class=\"fa fa-coins\"></i>-at, cserébe pedig adnod kell ugyannyi <i class=\"fa fa-sheet-plastic\"></i>-t a kezedből."),
    TRADER("TRADER", ActivationEnum.START_OF_TURN, "<p>Bármennyi <span style=\"font-variant: small-caps\">kereskedelmi</span> kerületet építhetsz.</p>"),
    SCHOLAR("SCHOLAR",List.of("7", "sheet-plastic"), AbilityTargetEnum.SELECTOR, "<p>Húzol 7 <i class=\"fa fa-sheet-plastic\"></i>-t és választasz egyet, amit megtarthatsz.</p>"),
    MARSHAL("MARSHAL", List.of("city", "hand"), AbilityTargetEnum.CHEAP_BUILT_DISTRICT, ""),
    TAX_COLLECTOR("TAX_COLLECTOR",List.of("users", "coins"), ""),
    GOLD_MINE("GOLD_MINE", ActivationEnum.ON_BUILD, "<p>Ha aranyat szerzel nyersanyag gyűjtéskor, kapsz 1 <i class=\"fa fa-coins\"></i>-t.</p>"),
    FRAMEWORK("FRAMEWORK", List.of("city", "hammer"), ActivationEnum.AFTER_BUILD, AbilityTargetEnum.OWN_CARD, "<p>Megépítesz egy kerületet úgy, hogy az Állványzatot semmisíted meg, ahelyett, hogy kifizetnéd a <i class=\"fa fa-city\"></i> árát.</p><p>Magisztrátus nem kobozhat el Állványzat álltal épített <i class=\"fa fa-city\"></i>-t.</p>"),
    BASILICA("BASILICA", ActivationEnum.END_OF_GAME, "<p>A játék végén 1 <i class=\"fa fa-star\"></i> jár városodban minden olyan <i class=\"fa fa-city\"></i>-ért, aminek ára páratlan szám.</p>"),
    IMPERIAL_TREASURY("IMPERIAL_TREASURY", ActivationEnum.END_OF_GAME, "<p>A játék végén 1 <i class=\"fa fa-star\"></i> jár a kincstartalékodban lévő minden <i class=\"fa fa-coins\"></i> után.</p>"),
    CAPITOL("CAPITOL", ActivationEnum.END_OF_GAME, "<p>A játék végén 3 <i class=\"fa fa-star\"></i> jár, ha  van legalább 3 egyforma típusú <i class=\"fa fa-city\"></i> a városodban.</p><p>A Capitolium csak egyszer adhat <i class=\"fa fa-star\"></i>-t.</p>"),
    OBSERVATORY("OBSERVATORY", ActivationEnum.ON_BUILD, "<p>Ha <i class=\"fa fa-sheet-plastic\"></i>-t húzol nyersanyag gyűjtéskor, eggyel több közül választhatsz.</p>"),
    IVORY_TOWER("IVORY_TOWER", ActivationEnum.END_OF_GAME, "<p>A játék végén 5 <i class=\"fa fa-star\"></i> jár, ha az Elefántcsonttorony az egyetlen <span style=\"font-variant: small-caps\">egyedi</span> <i class=\"fa fa-city\"></i> a városodban.</p>"),
    KEEP("KEEP", ActivationEnum.ON_BUILD, "<p>8-as rangú karakter nem használhatja a képességét az Erődítményen.</p>"),
    ARMORY("ARMORY", List.of("city", "bomb"), ActivationEnum.AFTER_BUILD, AbilityTargetEnum.BUILT_DISTRICT, "<p>A köröd folyamán elpusztíthatod a Fegyvertárat, hogy elpusztíts egy másik játékos városában lévő <i class=\"fa fa-city\"></i>-t.</p><p>Befejezett városban nem lehet <i class=\"fa fa-city\"></i>-t elpusztítani.</p>"),
    FACTORY("FACTORY", ActivationEnum.ON_BUILD, "<p>Eggyel kevesebbet fizetsz minden más, <span style=\"font-variant: small-caps\">egyedi</span> <i class=\"fa fa-city\"></i> megépítéséért.</p>"),
    STABLES("STABLES", ActivationEnum.ON_BUILD, "<p>Az Istálló megépítése nem számít bele a körre vonatkozó építési korlátba.</p><p>Ha a magisztrátus elkobozza a játékos építhet még egy <i class=\"fa fa-city\"></i>-t ebben a körben.</p>"),
    HAUNTED_QUARTER("HAUNTED_QUARTER", ActivationEnum.END_OF_GAME, "<p>A játék végén a Kísértetváros az általad választott típusú <i class=\"fa fa-city\"></i>-nek számít.</p><p>Ha a birtokos úgy dönt, hogy a Kísértetváros típusa nem <span style=\"font-variant: small-caps\">egyedi</span>, akkor többé nem számít annak.</p>"),
    WISHING_WELL("WISHING_WELL", ActivationEnum.END_OF_GAME, "<p>A játék végén 1 <i class=\"fa fa-star\"></i> jár a városodban lévő minden <span style=\"font-variant: small-caps\">egyedi</span> <i class=\"fa fa-city\"></i> után (beleértve a Kívánságkutat is).</p>"),
    SMITHY("SMITHY", ActivationEnum.AFTER_BUILD, "<p>A köröd folyamán egyszer 2 <i class=\"fa fa-coins\"></i>-ért 3 <i class=\"fa fa-sheet-plastic\"></i>-t húzhatsz.</p>"),
    LIBRARY("LIBRARY", ActivationEnum.ON_BUILD, "<p>Ha <i class=\"fa fa-sheet-plastic\"></i>-t húzol nyersanyag gyűjtéskor, eggyel több <i class=\"fa fa-sheet-plastic\"></i>-t tarts meg.</p>"),
    QUARRY("QUARRY", ActivationEnum.ON_BUILD, "<p>Bármennyi egyforma <i class=\"fa fa-city\"></i>-t építhetsz a városodban.</p><p>8-as rangú karakter nem használhatja a képességét egyforma <i class=\"fa fa-city\"></i> megszerzésére.</p>"),
    LABORATORY("LABORATORY", List.of("sheet-plastic", "left-right", "coins"), ActivationEnum.AFTER_BUILD, AbilityTargetEnum.OWN_CARD, "<p>A köröd folyamán egyszer eldobhatsz egy <i class=\"fa fa-sheet-plastic\"></i>-t a kezedből és kapsz 2 <i class=\"fa fa-coins\"></i>-t.</p>"),
    GREAT_WALL("GREAT_WALL", ActivationEnum.ON_BUILD, "<p>A 8-as rangú karakternek eggyel több <i class=\"fa fa-coins\"></i>-t kell fizetnie, hogy használhassa a képességét városodban lévő bármely más <i class=\"fa fa-city\"></i>-en.</p>"),
    PARK("PARK", ActivationEnum.ON_BUILD, "<p>Ha nincs <i class=\"fa fa-sheet-plastic\"></i> a kezedben a köröd végén, húzol két <i class=\"fa fa-sheet-plastic\"></i>-t.</p><p>Ha a birtokos a Boszorkány és nincs megbabonázott köre, akkor a Park képessége nem lép életbe.</p>"),
    DRAGON_GATE("DRAGON_GATE", ActivationEnum.END_OF_GAME, "<p>A játék végén 2 <i class=\"fa fa-star\"></i> jár.</p>"),
    POOR_HOUSE("POOR_HOUSE", ActivationEnum.ON_BUILD, "<p>Ha nincs <i class=\"fa fa-coins\"></i> a kincstartalékodban a köröd végén, kapsz 1 <i class=\"fa fa-coins\"></i>-t.</p><p>Ha a birtokos a Boszorkány és nincs megbabonázott köre, akkor a Szegényház képessége nem lép életbe.</p><p>Az alkímista képessége a Szegényház hatása után érvényesül.</p>"),
    STATUE("STATUE", ActivationEnum.END_OF_GAME, "<p>A játék végén 5 <i class=\"fa fa-star\"></i> jár, ha nálad van a <i class=\"fa fa-crown\"></i>.</p>"),
    NECROPOLIS("NECROPOLIS", List.of("city", "x", "hammer"), ActivationEnum.BEFORE_BUILD, AbilityTargetEnum.OWN_BUILT_DISTRICT, "<p>Megépítheted úgy a Temetőt, hogy elpusztítasz egy <i class=\"fa fa-city\"></i>-t a városodban, ahelyett, hogy kifizetnéd a Temető árát.</p><p>Magisztrátus nem kobozhat el Temetőt ami a képessége által lett építve.</p>"),
    MAP_ROOM("MAP_ROOM", ActivationEnum.END_OF_GAME, "<p>A játék végén 1 <i class=\"fa fa-star\"></i> jár minden kezedben maradt <i class=\"fa fa-sheet-plastic\"></i> után.</p>"),
    THIEVES_DEN("THIEVES_DEN", List.of("hammer", "sheet-plastic"), ActivationEnum.BEFORE_BUILD, AbilityTargetEnum.OWN_CARDS, "<p>Építéskor a Tolvajtanya árát fizetheted részben <i class=\"fa fa-sheet-plastic\"></i>-okból (1 <i class=\"fa fa-sheet-plastic\"></i> = 1 <i class=\"fa fa-coins\"></i>).</p><p>Ha a magisztrátus elkobozza a Tolvajtanyát csak az <i class=\"fa fa-coins\"></i>-t kapja vissza a tulajdonos.</p>"),
    SCHOOL_OF_MAGIC("SCHOOL_OF_MAGIC", ActivationEnum.ON_BUILD, "<p>A kerületekhez nyersanyagokat gyűjtő képességeket tekintve a Varázstanoda az általad választott típusú kerületnek számít.</p>");

    // @formatter:on
    private final String value;
    private final List<String> icons;
    private final ActivationEnum type;
    private final AbilityUsageEnum useRule;
    private final AbilityTargetEnum target;
    private final String description;

    AbilityEnum(String value) {
        this.value = value;
        this.icons = List.of();
        this.type = ActivationEnum.MANUAL;
        this.useRule = AbilityUsageEnum.AND;
        this.target = AbilityTargetEnum.NONE;
        this.description = "";
    }

    AbilityEnum(String value, List<String> icons, String description) {
        this.value = value;
        this.icons = icons;
        this.type = ActivationEnum.MANUAL;
        this.useRule = AbilityUsageEnum.AND;
        this.target = AbilityTargetEnum.NONE;
        this.description = description;
    }

    AbilityEnum(String value, List<String> icons, ActivationEnum type, String description) {
        this.value = value;
        this.icons = icons;
        this.type = type;
        this.useRule = AbilityUsageEnum.AND;
        this.target = AbilityTargetEnum.NONE;
        this.description = description;
    }

    AbilityEnum(String value, List<String> icons, AbilityTargetEnum target, String description) {
        this.value = value;
        this.icons = icons;
        this.type = ActivationEnum.MANUAL;
        this.useRule = AbilityUsageEnum.AND;
        this.target = target;
        this.description = description;
    }

    AbilityEnum(String value, List<String> icons, AbilityUsageEnum usage, AbilityTargetEnum target,
        String description
    ) {
        this.value = value;
        this.icons = icons;
        this.type = ActivationEnum.MANUAL;
        this.useRule = usage;
        this.target = target;
        this.description = description;
    }

    AbilityEnum(String value, List<String> icons, ActivationEnum activation, AbilityTargetEnum target,
        String description
    ) {
        this.value = value;
        this.icons = icons;
        this.type = activation;
        this.useRule = AbilityUsageEnum.AND;
        this.target = target;
        this.description = description;
    }

    AbilityEnum(String value, ActivationEnum activation, String description) {
        this.value = value;
        this.icons = new ArrayList<>();
        this.type = activation;
        this.useRule = AbilityUsageEnum.AND;
        this.target = AbilityTargetEnum.NONE;
        this.description = description;
    }

    public void useAbility(GameEntity game, AbilityTargetDto target) {
        throw new InvalidTargetException(this);
    }

    protected PlayerEntity findPlayer(GameEntity game, Long targetId, AbilityEnum ability) {
        return game.getPlayers().stream().filter(player -> player.getId().equals(targetId)).findFirst()
            .orElseThrow(() -> new InvalidTargetException(ability));
    }

    protected void discardCard(GameEntity game, Long targetId) {
        game.getCurrentPlayer().getHand().remove(
            game.getCurrentPlayer().getHand().stream().filter(district -> district.getId().equals(targetId)).findFirst()
                .orElseThrow(() -> new InvalidTargetException(MAGICIAN_DECK)));
    }

    protected void getTypeGold(GameEntity game, DistrictTypeEnum type) {
        game.getCurrentPlayer().giveGold(
            (int) game.getCurrentPlayer().getDistricts().stream().filter(district -> district.getType() == type)
                .count());
    }

    protected void getTypeCards(GameEntity game, DistrictTypeEnum type) {
        game.getCurrentPlayer().giveCards(game.drawFromDeck(
            (int) game.getCurrentPlayer().getDistricts().stream().filter(district -> district.getType() == type)
                .count()));
    }
}
