import * as getters from "src/store/getters";
import {toMap} from "src/utility/objectUtils";

describe('getters.js', () => {
  it('should get correct categories data', () => {
    checkSimpleGetter('categories', getters.categoriesMap);
  });

  it('should get correct product types data', () => {
    checkSimpleGetter('productTypes', getters.productTypesMap);
  });

  it('should get correct packs data', () => {
    checkSimpleGetter('packs', getters.packsMap);
  });

  it('should map products', () => {
    const store = {
      products: [{
        productTypeId: 1,
        categoryId: 2,
        packs: [
          {packId: 1, quantity: 2},
          {packId: 2, quantity: 3},
        ]
      }]
    };
    const mockGetters = {
      productTypesMap: {
        1: {name: 'product type 1'}
      },
      categoriesMap: {
        2: {name: 'category 2'}
      },
      packsMap: {
        1: {name: 'pack 1'},
        2: {name: 'pack 2'}
      }
    };

    expect(getters.groupedProducts(store, mockGetters)(0)).to.eql([{
      productType: {name: 'product type 1'},
      category: {name: 'category 2'},
      packs: [
        {pack: {name: 'pack 1'}, quantity: 2},
        {pack: {name: 'pack 2'}, quantity: 3},
      ]
    }]);
  });

  it('should filter products by category', () => {
    const store = {
      products: [
        {productTypeId: 1, categoryId: 1, packs: []},
        {productTypeId: 1, categoryId: 2, packs: []},
        {productTypeId: 1, categoryId: 3, packs: []},
      ]
    };
    const mockGetters = {
      productTypesMap: {
        1: {name: 'product type 1'}
      },
      categoriesMap: {
        1: {id: 1, name: 'category 1'},
        2: {id: 2, name: 'category 2'},
        3: {id: 3, name: 'category 3'}
      },
      packsMap: {}
    };

    expect(getters.groupedProducts(store, mockGetters)(2)).to.eql([{
      productType: {name: 'product type 1'},
      category: {id: 2, name: 'category 2'},
      packs: []
    }]);
  });

  it('should sort products', () => {
    const store = {
      products: [
        {productTypeId: 2, categoryId: 2, packs: []},
        {productTypeId: 2, categoryId: 1, packs: []},
        {productTypeId: 1, categoryId: 2, packs: []},
      ]
    };
    const mockGetters = {
      productTypesMap: {
        1: {name: 'product type 1'},
        2: {name: 'product type 2'}
      },
      categoriesMap: {
        1: {id: 1, name: 'category 1'},
        2: {id: 2, name: 'category 2'}
      },
      packsMap: {}
    };

    expect(getters.groupedProducts(store, mockGetters)(0)).to.eql([
      {
        productType: {name: 'product type 1'},
        category: {id: 2, name: 'category 2'},
        packs: []
      },
      {
        productType: {name: 'product type 2'},
        category: {id: 1, name: 'category 1'},
        packs: []
      },
      {
        productType: {name: 'product type 2'},
        category: {id: 2, name: 'category 2'},
        packs: []
      }
    ]);
  });

  function checkSimpleGetter(property, method) {
    const items = [
      {id: 1, name: 'item 1'},
      {id: 2, name: 'item 2'},
      {id: 3, name: 'item 3'}
    ];
    const store = {
      [property]: items
    };

    expect(method(store)).to.eql(toMap(items));
  }
});
