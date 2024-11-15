import { ABILITY_USE_RULE } from "@/utils/const";

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
  return withName
    ? `<p>${district.name}</p>`
    : "" +
        `${district.abilities.map((ability) => ability.description).join("")}`;
}
