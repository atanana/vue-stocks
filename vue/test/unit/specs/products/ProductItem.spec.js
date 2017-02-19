import Vue from "vue";
import ProductItem from "src/components/products/ProductItem";

describe('ProductItem.vue', () => {
  function createItem(propsData, store) {
    const Ctor = Vue.extend(ProductItem);
    return new Ctor({
      propsData,
      store
    }).$mount();
  }

  it('should render correct contents', () => {
    const productType = 'test product type';
    const category = 'category';
    const vm = createItem({
      product: {
        productType: {name: productType},
        category: {name: category},
        packs: [
          {
            pack: {name: 'pack 1'},
            quantity: 1
          },
          {
            pack: {name: 'pack 2'},
            quantity: 2
          }
        ]
      },
      packTypes: []
    });

    const tds = vm.$el.querySelectorAll('td');
    expect(tds[0].textContent).to.equal(productType);
    expect(tds[1].textContent).to.equal(category);

    const packs = tds[2].querySelectorAll('span.tag.is-info');
    expect(packs).to.lengthOf(2);

    expect(packs[0].textContent.trim()).to.equal('pack 1 x 1');
    expect(packs[0].querySelector('button.delete.is-small')).to.exist;
    expect(packs[1].textContent.trim()).to.equal('pack 2 x 2');
    expect(packs[1].querySelector('button.delete.is-small')).to.exist;
  });

  it('should render correct contents for empty item', () => {
    const vm = createItem({
      product: {
        packs: []
      }
    });

    const tds = vm.$el.querySelectorAll('td');
    expect(tds[0].textContent).to.equal('-');
    expect(tds[1].textContent).to.equal('-');

    const packs = tds[2].querySelectorAll('span.tag.is-info');
    expect(packs).to.lengthOf(0);
  });

  it('should render correct add product pack popup', () => {
    const categoryId = 123;
    const productTypeId = 321;
    const packTypes = [
      {name: 'pack type 1'},
      {name: 'pack type 2'},
      {name: 'pack type 3'}
    ];
    const vm = createItem({
      product: {
        productType: {id: productTypeId},
        category: {id: categoryId},
        packs: [
          {
            pack: {name: 'pack 1'},
            quantity: 1
          },
          {
            pack: {name: 'pack 2'},
            quantity: 2
          }
        ]
      },
      packTypes: packTypes
    });

    const addProductPack = vm.$refs.addProductPack;
    expect(addProductPack).to.exist;
    expect(addProductPack.categoryId).to.equal(categoryId);
    expect(addProductPack.productTypeId).to.equal(productTypeId);
    expect(addProductPack.packs).to.equal(packTypes);
  });

  it('should delete item', () => {
    const store = {
      dispatch: function () {
      }
    };
    const storeMock = sinon.mock(store);
    const productTypeId = 123;
    const categoryId = 321;
    const packId = 1234;
    const vm = createItem({
      product: {
        productType: {id: productTypeId},
        category: {id: categoryId},
        packs: [
          {
            pack: {
              name: 'pack 1',
              id: packId
            },
            quantity: 1
          }
        ]
      }
    }, store);

    window.confirm = sinon.stub().returns(true);
    storeMock.expects("dispatch").once().withExactArgs('deleteProduct', {
      productTypeId: productTypeId,
      categoryId: categoryId,
      packId: packId,
    });

    vm.$el.querySelector('button.delete.is-small').click();

    storeMock.verify();
  });

  it('should not delete item', () => {
    const store = {
      dispatch: function () {
      }
    };
    const storeMock = sinon.mock(store);
    const vm = createItem({
      product: {
        productType: {id: 123},
        category: {id: 321},
        packs: [
          {
            pack: {
              name: 'pack 1',
              id: 1234
            },
            quantity: 1
          }
        ]
      }
    }, store);

    window.confirm = sinon.stub().returns(false);
    storeMock.expects("dispatch").never();

    vm.$el.querySelector('button.delete.is-small').click();

    storeMock.verify();
  })
});
