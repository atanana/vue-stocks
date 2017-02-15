import Vue from "vue";
import SimplePage from "src/components/SimplePage";

describe('SimplePage.vue', () => {
  const label = 'test label';

  let vm;
  let items;

  beforeEach(() => {
    items = [{}];
    vm = createPage();
  });
  function createPage() {
    const Ctor = Vue.extend(SimplePage);
    return new Ctor({
      propsData: {
        items: items,
        newItemLabel: label
      }
    }).$mount();
  }

  it('should render correct contents', () => {
    const itemsList = vm.$refs.items;
    expect(itemsList).to.exist;
    expect(itemsList.items).to.eql(items);
    expect(itemsList.newItemLabel).to.eql(label);

    const saveButton = vm.$refs.saveButton;
    expect(saveButton).to.exist;
  });

  it('should copy data for list', () => {
    const itemsList = vm.$refs.items;
    items.push({});
    expect(itemsList.items).to.not.eql(items);
  });

  it('should dispatch correct event', () => {
    const spy = sinon.spy();
    vm.$on('saveItems', spy);
    vm.$refs.saveButton.$el.click();
    expect(spy).to.have.been.calledWith(items);
  });
});
