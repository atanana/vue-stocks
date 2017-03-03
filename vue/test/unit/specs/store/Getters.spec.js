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
