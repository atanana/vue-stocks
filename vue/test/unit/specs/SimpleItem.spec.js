import Vue from "vue";
import SimpleItem from "src/components/SimpleItem";

describe('SimpleItem.vue', () => {
  const itemName = 'test item';
  const placeholder = 'test placeholder';

  let vm;

  before(() => {
    vm = createItem();
  });
  function createItem() {
    const Ctor = Vue.extend(SimpleItem);
    return new Ctor({
      propsData: {
        item: {
          name: itemName
        },
        placeholder: placeholder
      }
    }).$mount();
  }

  it('should render correct contents', () => {
    const item = vm.$el;

    expect(item.className).to.equal('simple-item');

    const input = item.querySelector('input');
    expect(input.className).to.equal('input');
    expect(input.getAttribute('placeholder')).to.equal(placeholder);
    expect(input.value).to.equal(itemName);

    const deleteButton = vm.$refs.deleteButton.$el;
    expect(deleteButton.className.split(' ')).to.contain('delete-button');
  });

  it('should dispatch correct event', () => {
    const spy = sinon.spy();
    vm.$on('deleteItem', spy);
    vm.$refs.deleteButton.$el.click();
    expect(spy).to.have.been.calledOnce;
  });
});
