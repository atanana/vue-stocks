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

export const groupedProducts = (store, getters) => categoryId => {
  let products = store.products
    .map(product => ({
      productType: getters.productTypesMap[product.productTypeId],
      category: getters.categoriesMap[product.categoryId],
      packs: product.packs.map(pack => ({
        pack: getters.packsMap[pack.packId],
        quantity: pack.quantity
      }))
    }));

  if (categoryId) {
    products = products.filter(product => product.category.id === categoryId);
  }

  return products.sort((left, right) => {
    let result = 0;
    if (left.productType && right.productType && left.category && right.category) {
      const leftProduct = left.productType.name;
      const rightProduct = right.productType.name;

      if (leftProduct !== rightProduct) {
        result = leftProduct < rightProduct ? -1 : 1;
      } else {
        const leftCategory = left.category.name;
        const rightCategory = right.category.name;

        if (leftCategory !== rightCategory) {
          result = leftCategory < rightCategory ? -1 : 1;
        }
      }
    }

    return result;
  });
};
