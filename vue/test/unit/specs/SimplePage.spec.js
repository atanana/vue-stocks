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
    const placeholder = 'test placeholder';
    const items = [{}];
    const vm = createPage({
      items: items,
      newItemLabel: label,
      newItemPlaceholder: placeholder
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
