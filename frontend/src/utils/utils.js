import { COLORS } from "@/utils/const";

export function composeCharacterDescription(character) {
  return character.abilities
    .map((ability) => ability.description)
    .join(character.id === 3 ? "<p><b>VAGY</b></p>" : "");
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

export function hasCondition(player, condition) {
  return player.conditions
    .map((condition) => condition.value)
    .includes(condition);
}

export function hasDistrict(player, district) {
  return player.districts
    .map((district) => district.abilities)
    .flat(Infinity)
    .includes(district);
}

export function sortCharacters(a, b) {
  if (a.number < b.number) {
    return -1;
  }
  if (b.number < a.number) {
    return 1;
  }
  if (a.id < b.id) {
    return -1;
  }
  if (b.id < a.id) {
    return 1;
  }
  return 0;
}
