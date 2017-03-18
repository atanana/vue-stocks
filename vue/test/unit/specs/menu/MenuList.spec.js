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

  it('should render correct sorting controls', () => {
    const vm = createList({
      items: []
    });

    const sortingContainer = vm.$el.querySelector('.field.menu-sorting > .control');
    expect(sortingContainer).to.exist;

    const labels = sortingContainer.querySelectorAll('label');
    checkRadioLabel(labels[0], 'По имени', 'by-name');
    checkRadioLabel(labels[1], 'По дате', 'by-date');

    function checkRadioLabel(label, text, value) {
      expect(label).to.have.class('radio');
      expect(label).to.contains.text(text);

      const input = label.querySelector('input');
      expect(input).to.have.value(value);
      expect(input).to.have.attribute('type', 'radio');
    }
  });

  it('should sort by name by default', () => {
    const items = [
      {name: 'test 3'},
      {name: 'test 2'},
      {name: 'test 1'}
    ];
    const vm = createList({
      items: items
    });

    const itemsList = vm.$refs.items;
    expect(itemsList[0].item).to.eql(items[2]);
    expect(itemsList[1].item).to.eql(items[1]);
    expect(itemsList[2].item).to.eql(items[0]);
  });

  it('should sort by date', done => {
    const items = [
      {name: 'test 1', date: '01-01-2017'},
      {name: 'test 2', date: '01-02-2017'},
      {name: 'test 3', date: '01-03-2017'}
    ];
    const vm = createList({
      items: items
    });

    vm.$refs.sortByDate.click();

    vm.$nextTick(() => {
      const itemsList = vm.$refs.items;
      expect(itemsList[0].item).to.eql(items[2]);
      expect(itemsList[1].item).to.eql(items[1]);
      expect(itemsList[2].item).to.eql(items[0]);

      done();
    });
  });

  it('should sort by name', done => {
    const items = [
      {name: 'test 1', date: '01-01-2017'},
      {name: 'test 2', date: '01-02-2017'},
      {name: 'test 3', date: '01-03-2017'}
    ];
    const vm = createList({
      items: items
    });

    vm.$refs.sortByDate.click();

    vm.$nextTick(() => {
      vm.$refs.sortByName.click();

      vm.$nextTick(() => {
        const itemsList = vm.$refs.items;
        expect(itemsList[0].item).to.eql(items[0]);
        expect(itemsList[1].item).to.eql(items[1]);
        expect(itemsList[2].item).to.eql(items[2]);

        done();
      });
    });
  });
});
