import Vue from 'vue';
import MenuEntryItem from 'src/components/menu/MenuEntryItem';

describe('MenuEntryItem.vue', () => {
  function createItem(propsData) {
    const Ctor = Vue.extend(MenuEntryItem);
    return new Ctor({
      propsData
    }).$mount();
  }

  it('should render correct contents', () => {
    const itemName = 'test name';
    const vm = createItem({
      item: {
        name: itemName,
        date: 1489523481376
      }
    });

    const item = vm.$el;
    expect(item).to.have.class('simple-item');

    const input = item.querySelector('input.input');
    expect(input).to.exist;
    expect(input.value).to.equal(itemName);
    expect(input.getAttribute('placeholder')).to.equal('Название блюда');

    const datePicker = vm.$refs.datePicker;
    expect(datePicker).to.exist;

    const deleteButton = vm.$refs.deleteButton.$el;
    expect(deleteButton).to.exist;
    expect(deleteButton).to.have.class('delete-button');
  });

  it('should render empty item', () => {
    const vm = createItem({
      item: {}
    });
    const input = vm.$el.querySelector('input');
    expect(input.value).to.empty;
  });

  it('should dispatch correct delete event', () => {
    const vm = createItem({
      item: {}
    });
    const spy = sinon.spy();
    vm.$on('deleteItem', spy);
    vm.$refs.deleteButton.$el.click();
    expect(spy).to.have.been.calledOnce;
  });
});
