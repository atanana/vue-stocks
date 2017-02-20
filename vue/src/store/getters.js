import {toMap} from "utility/objectUtils";

export const categoriesMap = store => {
  return toMap(store.categories);
};

export const productTypesMap = store => {
  return toMap(store.productTypes);
};

export const packsMap = store => {
  return toMap(store.packs);
};
