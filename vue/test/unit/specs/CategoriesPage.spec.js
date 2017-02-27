import Vue from "vue";
import CategoriesPage from "src/components/CategoriesPage";

describe('CategoriesPage.vue', () => {
  function createPage(store) {
    const Ctor = Vue.extend(CategoriesPage);
    return new Ctor({
      store
    }).$mount();
  }

  function createStore(store) {
    return Object.assign({
      dispatch: function () {
      },
      state: {
        categories: [],
      }
    }, store);
  }

  it('should data on create', () => {
    const store = createStore({});
    const storeMock = sinon.mock(store);
    storeMock.expects('dispatch').withExactArgs('loadCategories');

    createPage(store);

    storeMock.verify();
  });

  it('should render correct contents', () => {
    const categories = [
      {name: 'category 1'},
      {name: 'category 2'},
      {name: 'category 3'}
    ];
    const vm = createPage(createStore({
      state: {
        categories
      }
    }));

    const page = vm.$refs.page;
    expect(page).to.exist;
    expect(page.items).to.eql(categories);
    expect(page.newItemLabel).to.equal('Добавить категорию');
    expect(page.newItemPlaceholder).to.equal('Название категории');
  });

  it('should dispatch correct event', () => {
    const categories = [
      {name: 'category 1'},
      {name: 'category 2'},
      {name: 'category 3'}
    ];
    const store = createStore({
      state: {
        categories
      }
    });
    const vm = createPage(store);
    const storeMock = sinon.mock(store);
    storeMock.expects('dispatch').withExactArgs('updateCategories', categories);

    vm.$refs.page.$emit('saveItems', categories);

    storeMock.verify();
  });
});
