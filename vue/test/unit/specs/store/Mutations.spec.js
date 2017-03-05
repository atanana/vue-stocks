import * as mutations from "src/store/mutations";

describe('mutations.js', () => {
  it('should set products', () => {
    checkSet('products', mutations.setProducts);
  });

  it('should set categories', () => {
    checkSet('categories', mutations.setCategories);
  });

  it('should set packs', () => {
    checkSet('packs', mutations.setPacks);
  });

  it('should set product types', () => {
    checkSet('productTypes', mutations.setProductTypes);
  });

  it('should set product logs', () => {
    checkSet('productLogs', mutations.setProductLogs);
  });

  function checkSet(property, method) {
    const state = {};
    const items = [
      {item: 'item 1'},
      {item: 'item 2'},
      {item: 'item 3'}
    ];

    method(state, items);

    expect(state).to.eql({
      [property]: items
    });
  }
});
