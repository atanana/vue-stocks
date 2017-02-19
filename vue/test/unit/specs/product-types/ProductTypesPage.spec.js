import Vue from "vue";
import ProductTypesPage from "src/components/product-types/ProductTypesPage";

describe('ProductTypesPage.vue', () => {
  function createPage(propsData) {
    const Ctor = Vue.extend(ProductTypesPage);
    return new Ctor({
      propsData
    }).$mount();
  }

  it('should render correct contents', () => {
    const items = [
      {name: 'item 1'},
      {name: 'item 2'}
    ];
    const placeholder = 'test placeholder';
    const label = 'test label';
    const vm = createPage({
      items: items,
      categories: [],
      newItemPlaceholder: placeholder,
      newItemLabel: label
    });

    const itemsList = vm.$refs.items;
    expect(itemsList).to.exist;
    expect(itemsList.items).to.eql(items);
    expect(itemsList.newItemLabel).to.eql(label);
    expect(itemsList.newItemPlaceholder).to.eql(placeholder);

    const saveButton = vm.$refs.saveButton;
    expect(saveButton).to.exist;
  });

  it('should copy data for list', () => {
    const items = [
      {name: 'item 1'},
      {name: 'item 2'}
    ];
    const vm = createPage({
      items: items,
      categories: []
    });

    const itemsList = vm.$refs.items;
    items.push({});
    expect(itemsList.items).to.not.eql(items);
  });

  it('should dispatch correct event', () => {
    const items = [
      {name: 'item 1'},
      {name: 'item 2'}
    ];
    const vm = createPage({
      items: items,
      categories: []
    });

    const spy = sinon.spy();
    vm.$on('saveItems', spy);
    vm.$refs.saveButton.$el.click();
    expect(spy).to.have.been.calledWith(items);
  });
});
