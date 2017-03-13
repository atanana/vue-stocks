import Vue from "vue";
import SimplePage from "src/components/SimplePage";

describe('SimplePage.vue', () => {
  function createPage(propsData) {
    const Ctor = Vue.extend(SimplePage);
    return new Ctor({
      propsData
    }).$mount();
  }

  it('should render correct contents', () => {
    const label = 'test label';
    const items = [{}];
    const vm = createPage({
      items: items,
      newItemLabel: label,
      newItemPlaceholder: 'test placeholder'
    });

    const itemsList = vm.$refs.items;
    expect(itemsList).to.exist;
    expect(itemsList.items).to.eql(items);
    expect(itemsList.newItemLabel).to.eql(label);

    const saveButton = vm.$refs.saveButton;
    expect(saveButton).to.exist;
  });

  it('should render correct items', () => {
    const items = [
      {name: 'test 1'},
      {name: 'test 2'},
      {name: 'test 3'},
    ];
    const newItemPlaceholder = 'test placeholder';
    const vm = createPage({
      items: items,
      newItemLabel: 'test label',
      newItemPlaceholder: newItemPlaceholder
    });

    const realItems = vm.$refs.items.$children;
    for (let i = 0; i < items.length; i++) {
      checkItem(realItems[i], items[i])
    }

    function checkItem(realItem, item) {
      expect(realItem.item).to.eql(item);
      expect(realItem.placeholder).to.eql(newItemPlaceholder);
      expect(realItem.$el).to.class('simple-item');
    }
  });

  it('should call list to delete item', () => {
    const item = {name: 'test 1'};
    const vm = createPage({
      items: [item],
      newItemLabel: 'test label',
      newItemPlaceholder: 'test placeholder'
    });

    const itemsList = vm.$refs.items;
    sinon.spy(itemsList, 'deleteItem');

    const realItem = itemsList.$children[0];
    realItem.$refs.deleteButton.$emit('delete');

    expect(itemsList.deleteItem).to.calledWith(item);
  });

  it('should copy data for list', () => {
    const items = [{}];
    const vm = createPage({
      items: items
    });

    const itemsList = vm.$refs.items;
    items.push({});
    expect(itemsList.items).to.not.eql(items);
  });

  it('should dispatch correct event', () => {
    const items = [{}];
    const vm = createPage({
      items: items
    });

    const spy = sinon.spy();
    vm.$on('saveItems', spy);
    vm.$refs.saveButton.$el.click();
    expect(spy).to.have.been.calledWith(items);
  });
});
