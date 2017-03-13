import Vue from "vue";
import ProductTypesPage from "src/components/product-types/ProductTypesPage";

describe('ProductTypesPage.vue', () => {
  function createPage(store) {
    const Ctor = Vue.extend(ProductTypesPage);
    return new Ctor({
      store
    }).$mount();
  }

  function createStore(store) {
    return Object.assign({
      dispatch: function () {
      },
      state: {
        productTypes: [],
        categories: []
      }
    }, store);
  }

  it('should load data on create', () => {
    const store = createStore({});
    const storeMock = sinon.mock(store);
    storeMock.expects('dispatch').withExactArgs('loadProductTypes');
    storeMock.expects('dispatch').withExactArgs('loadCategories');

    createPage(store);

    storeMock.verify();
  });

  it('should render correct contents', () => {
    const productTypes = [
      {name: 'productType 1'},
      {name: 'productType 2'},
      {name: 'productType 3'}
    ];
    const vm = createPage(createStore({
      state: {
        productTypes,
        categories: []
      }
    }));

    const itemsList = vm.$refs.items;
    expect(itemsList).to.exist;
    expect(itemsList.items).to.eql(productTypes);
    expect(itemsList.newItemLabel).to.eql('Добавить тип продуктов');

    const saveButton = vm.$refs.saveButton;
    expect(saveButton).to.exist;
  });

  it('should render correct items', () => {
    const productTypes = [
      {name: 'productType 1'},
      {name: 'productType 2'},
      {name: 'productType 3'}
    ];
    const categories = [
      {name: 'category 1'},
      {name: 'category 2'},
      {name: 'category 3'}
    ];
    const vm = createPage(createStore({
      state: {
        productTypes,
        categories
      }
    }));

    const realItems = vm.$refs.items.$children;
    for (let i = 0; i < productTypes.length; i++) {
      checkItem(realItems[i], productTypes[i]);
    }

    function checkItem(realItem, item) {
      expect(realItem.item).to.eql(item);
      expect(realItem.categories).to.eql(categories);
      expect(realItem.$el).to.class('simple-item');
    }
  });

  it('should call list to delete item', () => {
    const productType = {name: 'productType 1'};
    const vm = createPage(createStore({
      state: {
        productTypes: [productType],
        categories: []
      }
    }));

    const itemsList = vm.$refs.items;
    sinon.spy(itemsList, 'deleteItem');

    const realItem = itemsList.$children[0];
    realItem.$refs.deleteButton.$emit('delete');

    expect(itemsList.deleteItem).to.calledWith(productType);
  });

  it('should copy data for list', () => {
    const productTypes = [
      {name: 'productType 1'},
      {name: 'productType 2'},
      {name: 'productType 3'}
    ];
    const vm = createPage(createStore({
      state: {
        productTypes,
        categories: []
      }
    }));

    const itemsList = vm.$refs.items;
    productTypes.push({});
    expect(itemsList.items).to.not.eql(productTypes);
  });

  it('should dispatch correct event', () => {
    const productTypes = [
      {name: 'productType 1'},
      {name: 'productType 2'},
      {name: 'productType 3'}
    ];
    const store = createStore({
      state: {
        productTypes,
        categories: []
      }
    });
    const vm = createPage(store);
    const storeMock = sinon.mock(store);
    storeMock.expects('dispatch').withExactArgs('updateProductTypes', productTypes);

    vm.$refs.saveButton.$emit('save');

    storeMock.verify();
  });
});
