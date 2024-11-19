import { ABILITY_USE_RULE, COLORS } from "@/utils/const";

export function composeCharacterDescription(character) {
  return character.abilities
    .map((ability) => ability.description)
    .join(
      character.abilities[0].useRule === ABILITY_USE_RULE.OR
        ? "<p>VAGY</p>"
        : ""
    );
}

export function composeDistrictDescription(district, withName = false) {
  return (
    (withName ? `<p>${district.name}</p>` : "") +
    `${district.abilities.map((ability) => ability.description).join("")}`
  );
}

export function primaryColor(type) {
  return COLORS[type]?.PRIMARY ?? COLORS.DEFAULT.PRIMARY;
}

export function secondaryColor(type) {
  return COLORS[type]?.SECONDARY ?? COLORS.DEFAULT.SECONDARY;
}

export function primaryColorOrDisabled(type, condition) {
  return condition ? primaryColor(type) : COLORS.DISABLED.PRIMARY;
}

export function secondaryColorOrDisabled(type, condition) {
  return condition ? secondaryColor(type) : COLORS.DISABLED.SECONDARY;
}
