import Vue from "vue";
import ProductTypesList from "src/components/product-types/ProductTypesList.vue";

describe('ProductTypesList.vue', () => {
  function createList(propsData) {
    const Ctor = Vue.extend(ProductTypesList);
    return new Ctor({
      propsData
    }).$mount();
  }

  it('should render correct items', () => {
    const placeholder = 'test placeholder';
    const categories = [
      {name: 'category 1'},
      {name: 'category 2'},
      {name: 'category 3'}
    ];
    const originalItems = [
      {name: 'item 1'},
      {name: 'item 2'},
      {name: 'item 3'}
    ];
    const vm = createList({
      items: originalItems,
      categories: categories,
      newItemPlaceholder: placeholder
    });

    const items = vm.$refs.items;
    expect(items).to.lengthOf(3);
    for (let i = 0; i < items.length; i++) {
      checkItem(items[i], originalItems[i]);
    }

    function checkItem(item, originalItem) {
      expect(item.item).to.eql(originalItem);
      expect(item.categories).to.eql(categories);
      expect(item.placeholder).to.eql(placeholder);
    }
  });

  it('should render correct new item button', () => {
    const label = 'test label';
    const vm = createList({
      newItemLabel: label
    });

    const addNewButton = vm.$refs.addNew;
    expect(addNewButton).to.exist;
    expect(addNewButton.label).to.equal(label);
  });

  it('should add new item', () => {
    const vm = createList({
      items: []
    });

    vm.$refs.addNew.$el.click();
    expect(vm.items).to.have.lengthOf(1);
  });

  it('should delete items', () => {
    const originalItems = [
      {name: 'item 1'},
      {name: 'item 2'},
      {name: 'item 3'}
    ];
    const vm = createList({
      items: originalItems.slice(),
      categories: []
    });

    vm.$refs.items[1].$refs.deleteButton.$el.click();
    expect(vm.items).to.eql([originalItems[0], originalItems[2]]);
  });
});
