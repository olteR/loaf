export const validateString = function (field, validations) {
  const stringField = field.toString();
  if (validations.includes("notBlank")) {
    if (!stringField || /^\s*$/.test(stringField)) return false;
  }
  return true;
};
