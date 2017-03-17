import Vue from "vue";
import MenuList from "src/components/menu/MenuList";

describe('MenuList.vue', () => {
  function createList(propsData) {
    const Ctor = Vue.extend(MenuList);
    return new Ctor({
      propsData
    }).$mount();
  }

  it('should render correct items', () => {
    const items = [
      {name: 'test 1'},
      {name: 'test 2'},
      {name: 'test 3'}
    ];
    const vm = createList({
      items: items
    });

    const itemsList = vm.$refs.items;

    for (let i = 0; i < items.length; i++) {
      const item = itemsList[i];
      expect(item.item).to.eql(items[i]);
      expect(item.$el).to.have.class('simple-item');
    }
  });

  it('should render correct new item button', () => {
    const vm = createList({
      items: []
    });
    const newItemButton = vm.$refs.addNew;
    expect(newItemButton.label).to.equal('Добавить блюдо');
  });

  it('should add new item', () => {
    const vm = createList({
      items: []
    });

    vm.$refs.addNew.$emit('addNew');
    expect(vm.items).to.have.lengthOf(1);
  });

  it('should delete items', () => {
    const items = [
      {name: 'test 1'},
      {name: 'test 2'},
      {name: 'test 3'}
    ];
    const vm = createList({
      items: items.slice()
    });

    vm.$refs.items[1].$emit('deleteItem');
    expect(vm.items).to.eql([items[0], items[2]]);
  });
});
