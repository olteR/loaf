package olter.loaf.game.players.model;

import lombok.Getter;

@Getter
public enum ConditionEnum {
    // @formatter:off
    CROWNED("CROWNED", "Megkoronázva", "crown", "<p>Ennél a játékosnál van a korona, ő kezdi a választási fázist.</p>"),
    KILLED("KILLED", "Meggyilkolva", "skull", DurationEnum.END_OF_TURN, false),
    ROBBED("ROBBED", "Kirabolva", "sack-dollar", DurationEnum.END_OF_TURN, false),
    PROTECTED("PROTECTED", "Védelem", "user-shield", DurationEnum.END_OF_TURN, "<p>A 8-as rangú karakter képességei nem használhatóak a játékos <i class=\"fa fa-city\"></i>-ein.</p>"),
    MASTER_BUILDER("MASTER_BUILDER", "Építőmester", "gavel", DurationEnum.END_OF_TURN, "<p>Ebben a körben az építkezési korlátod 3 <i class=\"fa fa-city\"></i>.</p>"),
    BEWITCHED("BEWITCHED", "Megbabonázva", "wand-sparkles", DurationEnum.END_OF_TURN, false),
    DUPLICATES("DUPLICATES", "Duplikátumok", "clone", DurationEnum.END_OF_TURN, "<p>Ebben a körben olyan <i class=\"fa fa-city\"></i>-eket is építhetőek a városban, amilyenek már léteznek.</p>"),
    AT_SEA("AT_SEA", "Tengeren", "person-walking-luggage", DurationEnum.END_OF_TURN, "<p>Nem építhet semmilyen kerületet ebben a körben.</p>"),
    WARRANTED("WARRANTED", "Parancsot kapott", "scroll", DurationEnum.END_OF_TURN, false),
    THREATENED("THREATENED", "Megfenyegetve", "envelope", DurationEnum.END_OF_TURN, false),
    GREAT_BUILDER("GREAT_BUILDER", "Fejlett építő", "hammer", DurationEnum.END_OF_TURN, "<p>Ebben a körben az építkezési korlátod 2 <i class=\"fa fa-city\"></i>.</p>"),
    BLOOMING_TRADE("BLOOMING_TRADE", "Fejlődő kereskedelem", "piggy-bank", DurationEnum.END_OF_TURN, "<p>Bármennyi <span style=\"font-variant: small-caps\">kereskedelmi</span> kerületet építhet.</p>"),
    GOLD_MINING("GOLD_MINING", "Aranybányászat", "coins", "<p>Ha aranyat szerez nyersanyag gyűjtéskor, kap 1 <i class=\"fa fa-coins\"></i>-t.</p>"),
    STAR_GUIDANCE("STAR_GUIDANCE", "Csillag jóslás", "binoculars", "<p>Ha <i class=\"fa fa-sheet-plastic\"></i>-t húz nyersanyag gyűjtéskor, eggyel több közül választhat.</p>"),
    BASTION("STAR_GUIDANCE", "Bevehetetlen erőd", "building-shield", "<p>8-as rangú karakter nem használhatja a képességét az Erődítményen.</p>"),
    INDUSTRY("INDUSTRY", "Tömeggyártás", "industry", "<p>Eggyel kevesebbet fizet minden más, <span style=\"font-variant: small-caps\">egyedi</span> <i class=\"fa fa-city\"></i> megépítéséért.</p>"),
    KNOWLEDGE("KNOWLEDGE", "Tudás könyvtára", "book","<p>Ha <i class=\"fa fa-sheet-plastic\"></i>-t húz nyersanyag gyűjtéskor, eggyel több <i class=\"fa fa-sheet-plastic\"></i>-t tart meg.</p>"),
    STONE_MINING("STONE_MINING", "Kőfejtés", "person-digging", "<p>Bármennyi egyforma <i class=\"fa fa-city\"></i>-t építhet a városában.</p><p>8-as rangú karakter nem használhatja a képességét egyforma <i class=\"fa fa-city\"></i> megszerzésére.</p>"),
    GREAT_WALL("GREAT_WALL", "A fal védelme", "shield-halved", "<p>A 8-as rangú karakternek eggyel több <i class=\"fa fa-coins\"></i>-t kell fizetnie, hogy használhassa a képességét városban lévő bármely más <i class=\"fa fa-city\"></i>-en.</p>"),
    FRESH_AIR("FRESH_AIR", "Friss levegő", "tree", "<p>Ha nincs <i class=\"fa fa-sheet-plastic\"></i> a játékos kezében a köre végén, húz két <i class=\"fa fa-sheet-plastic\"></i>-t.</p><p>Ha a birtokos a Boszorkány és nincs megbabonázott köre, akkor a Park képessége nem lép életbe.</p>"),
    CHARITY("CHARITY", "Adomány", "hand-holding-dollar", "<p>Ha nincs <i class=\"fa fa-coins\"></i> a játékos kincstartalékában a köre végén, kap 1 <i class=\"fa fa-coins\"></i>-t.</p><p>Ha a birtokos a Boszorkány és nincs megbabonázott köre, akkor a Szegényház képessége nem lép életbe.</p><p>Az alkímista képessége a Szegényház hatása után érvényesül.</p>"),
    MAGICAL_DISTRICT("MAGICAL_DISTRICT", "Mágikus kerület", "hat-wizard", "<p>A kerületekhez nyersanyagokat gyűjtő képességeket tekintve a Varázstanoda a karaktered típusának számít.</p>");
    // @formatter:on

    private final String value;
    private final String name;
    private final String icon;
    private final DurationEnum duration;
    private final Boolean visible;
    private String description;

    ConditionEnum(String value, String name, String icon, String description) {
        this.value = value;
        this.name = name;
        this.icon = icon;
        this.duration = DurationEnum.INDEFINITE;
        this.visible = true;
        this.description = description;
    }

    ConditionEnum(String value, String name, String icon, DurationEnum duration, String description) {
        this.value = value;
        this.name = name;
        this.icon = icon;
        this.duration = duration;
        this.visible = true;
        this.description = description;
    }

    ConditionEnum(String value, String name, String icon, DurationEnum duration,
        Boolean visible
    ) {
        this.value = value;
        this.name = name;
        this.icon = icon;
        this.duration = duration;
        this.visible = visible;
    }
}
