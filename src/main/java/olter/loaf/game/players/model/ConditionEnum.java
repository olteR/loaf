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
    BLOOMING_TRADE("BLOOMING_TRADE", "Fejlődő kereskedelem", "piggy-bank", DurationEnum.END_OF_TURN, "<p>Bármennyi <span style=\"font-variant: small-caps\">kereskedelmi</span> kerületet építhet.</p>");
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
