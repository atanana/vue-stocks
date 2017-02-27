import Vue from "vue";
import PacksPage from "src/components/PacksPage";

describe('PacksPage.vue', () => {
  function createPage(store) {
    const Ctor = Vue.extend(PacksPage);
    return new Ctor({
      store
    }).$mount();
  }

  function createStore(store) {
    return Object.assign({
      dispatch: function () {
      },
      state: {
        packs: [],
      }
    }, store);
  }

  it('should data on create', () => {
    const store = createStore({});
    const storeMock = sinon.mock(store);
    storeMock.expects('dispatch').withExactArgs('loadPacks');

    createPage(store);

    storeMock.verify();
  });

  it('should render correct contents', () => {
    const packs = [
      {name: 'pack 1'},
      {name: 'pack 2'},
      {name: 'pack 3'}
    ];
    const vm = createPage(createStore({
      state: {
        packs
      }
    }));

    const page = vm.$refs.page;
    expect(page).to.exist;
    expect(page.items).to.eql(packs);
    expect(page.newItemLabel).to.equal('Добавить упаковку');
    expect(page.newItemPlaceholder).to.equal('Название упаковки');
  });

  it('should dispatch correct event', () => {
    const packs = [
      {name: 'pack 1'},
      {name: 'pack 2'},
      {name: 'pack 3'}
    ];
    const store = createStore({
      state: {
        packs
      }
    });
    const vm = createPage(store);
    const storeMock = sinon.mock(store);
    storeMock.expects('dispatch').withExactArgs('updatePacks', packs);

    vm.$refs.page.$emit('saveItems', packs);

    storeMock.verify();
  });
});
