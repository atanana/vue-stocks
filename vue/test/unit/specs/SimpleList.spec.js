import Vue from 'vue';
import SimpleList from 'src/components/SimpleList';

describe('SimpleList.vue', () => {
  function createList(propsData) {
    const Ctor = Vue.extend(SimpleList);
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

    for (let item of items) {
      expect(vm.$el).to.contain.text(item.name)
    }
  });

  it('should render correct new item button', () => {
    const newItemLabel = 'test label';
    const vm = createList({
      items: [],
      newItemLabel: newItemLabel
    });
    const newItemButton = vm.$refs.addNew;
    expect(newItemButton.label).to.equal(newItemLabel);
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

    vm.deleteItem(items[1]);
    expect(vm.items).to.eql([items[0], items[2]]);
  });
});
