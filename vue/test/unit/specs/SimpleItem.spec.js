import Vue from "vue";
import SimpleItem from "src/components/SimpleItem";

describe('SimpleItem.vue', () => {
  const itemName = 'test item';
  const placeholder = 'test placeholder';

  let vm;

  beforeEach(() => {
    vm = createItem({
      name: itemName
    });
  });

  function createItem(item) {
    const Ctor = Vue.extend(SimpleItem);
    return new Ctor({
      propsData: {
        item: item,
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

  it('should render empty item', () => {
    vm = createItem({});
    const input = vm.$el.querySelector('input');
    expect(input.value).to.empty;
  });

  it('should dispatch correct delete event', () => {
    const spy = sinon.spy();
    vm.$on('deleteItem', spy);
    vm.$refs.deleteButton.$el.click();
    expect(spy).to.have.been.calledOnce;
  });
});
