import Vue from "vue";
import ProductsPage from "src/components/products/ProductsPage";

describe('ProductsPage.vue', () => {
  function createStore(store) {
    return Object.assign({
      dispatch: function () {
      },
      state: {
        categories: [],
        productTypes: [],
        packs: [],
        products: []
      },
      getters: {
        groupedProducts: function () {
          return [];
        }
      }
    }, store);
  }

  function createPage(store) {
    const Ctor = Vue.extend(ProductsPage);
    return new Ctor({
      store
    }).$mount();
  }

  it('should load data on create', () => {
    const store = createStore({});
    const storeMock = sinon.mock(store);
    storeMock.expects('dispatch').withExactArgs('loadProducts');
    storeMock.expects('dispatch').withExactArgs('loadCategories');
    storeMock.expects('dispatch').withExactArgs('loadPacks');
    storeMock.expects('dispatch').withExactArgs('loadProductTypes');
    createPage(store);

    storeMock.verify();
  });

  it('should render correct add product popup', () => {
    const categories = [
      {name: 'category 1'},
      {name: 'category 2'}
    ];
    const productTypes = [
      {name: 'product type 1'},
      {name: 'product type 2'}
    ];
    const packs = [
      {name: 'pack 1'},
      {name: 'pack 2'}
    ];
    const vm = createPage(createStore({
      state: {
        categories: categories,
        productTypes: productTypes,
        packs: packs,
        products: []
      }
    }));

    const addProductPopup = vm.$refs.addProductPopup;
    expect(addProductPopup.categories).to.eql(categories);
    expect(addProductPopup.productTypes).to.eql(productTypes);
    expect(addProductPopup.packs).to.eql(packs);
  });

  it('should render correct product list', () => {
    const packs = [
      {name: 'pack 1'},
      {name: 'pack 2'}
    ];
    const products = [
      {name: 'product 1', packs: []},
      {name: 'product 2', packs: []}
    ];
    const categories = [
      {name: 'category 1'},
      {name: 'category 2'},
      {name: 'category 3'}
    ];
    const vm = createPage(createStore({
      state: {
        categories: categories,
        productTypes: [],
        packs: packs
      },
      getters: {
        groupedProducts: function () {
          return products;
        }
      }
    }));

    const tabsContainer = vm.$el.querySelector('.tabs.is-boxed.is-fullwidth');
    expect(tabsContainer).to.exist;

    const tabs = tabsContainer.querySelectorAll('li');
    for (let i = 0; i < tabs.length; i++) {
      const tabName = tabs[i].textContent;
      if (i === 0) {
        expect(tabName).to.equal('Всё');
      } else {
        expect(tabName).to.equal(categories[i - 1].name);
      }
    }

    const productsList = vm.$refs.products;
    expect(productsList.products).to.eql(products);
    expect(productsList.packs).to.eql(packs);
  });

  it('should highlight default category', () => {
    const vm = createPage(createStore({
      state: {
        categories: [{}, {}, {}]
      }
    }));

    const tabs = vm.$el.querySelectorAll('.tabs.is-boxed.is-fullwidth li');
    expect(tabs[0]).to.have.class('is-active');
    expect(tabs[1]).to.not.have.class('is-active');
    expect(tabs[2]).to.not.have.class('is-active');
    expect(tabs[3]).to.not.have.class('is-active');
  });

  it('should highlight selected category', (done) => {
    const vm = createPage(createStore({
      state: {
        productTypes: [],
        packs: [],
        products: [],
        categories: [
          {id: 1},
          {id: 2},
          {id: 3}
        ]
      }
    }));

    const tabs = vm.$el.querySelectorAll('.tabs.is-boxed.is-fullwidth li');
    tabs[2].click();
    expect(vm.currentCategory).to.equal(2);

    Vue.nextTick(() => {
      const tabs = vm.$el.querySelectorAll('.tabs.is-boxed.is-fullwidth li');
      expect(tabs[0]).to.not.have.class('is-active');
      expect(tabs[1]).to.not.have.class('is-active');
      expect(tabs[2]).to.have.class('is-active');
      expect(tabs[3]).to.not.have.class('is-active');

      done();
    });
  });
});
