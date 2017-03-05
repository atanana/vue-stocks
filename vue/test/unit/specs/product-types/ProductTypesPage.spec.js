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

    const itemsList = vm.$refs.items;
    expect(itemsList).to.exist;
    expect(itemsList.items).to.eql(productTypes);
    expect(itemsList.categories).to.eql(categories);
    expect(itemsList.newItemLabel).to.eql('Добавить тип продуктов');
    expect(itemsList.newItemPlaceholder).to.eql('Название типа продуктов');

    const saveButton = vm.$refs.saveButton;
    expect(saveButton).to.exist;
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
