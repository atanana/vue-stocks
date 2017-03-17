import Vue from "vue";
import MenuPage from "src/components/menu/MenuPage";

describe('MenuPage.vue', () => {
  function createPage(store) {
    const Ctor = Vue.extend(MenuPage);
    return new Ctor({
      store
    }).$mount();
  }

  function createStore(store) {
    return Object.assign({
      dispatch: function () {
      },
      state: {
        menuItems: []
      }
    }, store);
  }

  it('should load data on create', () => {
    const store = createStore({});
    const storeMock = sinon.mock(store);
    storeMock.expects('dispatch').withExactArgs('loadMenuItems');

    createPage(store);

    storeMock.verify();
  });

  it('should render correct contents', () => {
    const menuItems = [
      {name: 'item 1'},
      {name: 'item 2'},
      {name: 'item 3'}
    ];
    const vm = createPage(createStore({
      state: {
        menuItems
      }
    }));

    const itemsList = vm.$refs.items;
    expect(itemsList).to.exist;
    expect(itemsList.items).to.eql(menuItems);

    const saveButton = vm.$refs.saveButton;
    expect(saveButton).to.exist;
  });

  it('should render correct items', () => {
    const menuItems = [
      {name: 'item 1'},
      {name: 'item 2'},
      {name: 'item 3'}
    ];
    const vm = createPage(createStore({
      state: {
        menuItems
      }
    }));

    const realItems = vm.$refs.items.$children;
    for (let i = 0; i < menuItems.length; i++) {
      checkItem(realItems[i], menuItems[i]);
    }

    function checkItem(realItem, item) {
      expect(realItem.item).to.eql(item);
      expect(realItem.$el).to.class('simple-item');
    }
  });

  it('should call list to delete item', () => {
    const item = {name: 'item 1'};
    const vm = createPage(createStore({
      state: {
        menuItems: [item],
      }
    }));

    const itemsList = vm.$refs.items;
    sinon.spy(itemsList, 'deleteItem');

    const realItem = itemsList.$children[0];
    realItem.$refs.deleteButton.$emit('delete');

    expect(itemsList.deleteItem).to.calledWith(item);
  });

  it('should copy data for list', () => {
    const menuItems = [
      {name: 'item 1'},
      {name: 'item 2'},
      {name: 'item 3'}
    ];
    const vm = createPage(createStore({
      state: {
        menuItems
      }
    }));

    const itemsList = vm.$refs.items;
    menuItems.push({});
    expect(itemsList.items).to.not.eql(menuItems);
  });

  it('should dispatch correct event', () => {
    const menuItems = [
      {name: 'item 1'},
      {name: 'item 2'},
      {name: 'item 3'}
    ];
    const store = createStore({
      state: {
        menuItems
      }
    });
    const vm = createPage(store);
    const storeMock = sinon.mock(store);
    storeMock.expects('dispatch').withExactArgs('updateMenuItems', menuItems);

    vm.$refs.saveButton.$emit('save');

    storeMock.verify();
  });
});
