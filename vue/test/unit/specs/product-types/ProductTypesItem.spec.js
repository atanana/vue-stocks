import Vue from "vue";
import ProductTypesItem from "src/components/product-types/ProductTypesItem";

describe('ProductTypesItem.vue', () => {
  function createItem(propsData) {
    const Ctor = Vue.extend(ProductTypesItem);
    return new Ctor({
      propsData
    }).$mount();
  }

  it('should render correct contents', () => {
    const itemName = 'test name';
    const vm = createItem({
      item: {
        name: itemName,
        categoryId: 2
      },
      categories: [
        {id: 1, name: 'test category 1'},
        {id: 2, name: 'test category 2'},
        {id: 3, name: 'test category 3'}
      ]
    });

    const item = vm.$el;
    expect(item).to.have.class('simple-item');

    const input = item.querySelector('input.input');
    expect(input).to.exist;
    expect(input.value).to.equal(itemName);
    expect(input.getAttribute('placeholder')).to.equal('Название типа продуктов');

    const selectContainer = item.querySelector('.select');
    expect(selectContainer).to.exist;

    const select = selectContainer.querySelector('select');
    expect(select).to.exist;
    expect(select).to.have.class('category-select');
    expect(select.selectedIndex).to.equal(2);

    const options = select.querySelectorAll('option');
    expect(options).to.lengthOf(4);
    checkOption(options[0], '', 'Без категории');
    checkOption(options[1], '1', 'test category 1');
    checkOption(options[2], '2', 'test category 2');
    checkOption(options[3], '3', 'test category 3');

    function checkOption(option, value, text) {
      expect(option.value).to.equal(value);
      expect(option.textContent.trim()).to.equal(text);
    }

    const deleteButton = vm.$refs.deleteButton.$el;
    expect(deleteButton).to.exist;
    expect(deleteButton).to.have.class('delete-button');
  });

  it('should render empty item', () => {
    const vm = createItem({
      item: {},
      categories: []
    });
    const input = vm.$el.querySelector('input');
    expect(input.value).to.empty;
  });

  it('should dispatch correct delete event', () => {
    const vm = createItem({
      item: {},
      categories: []
    });
    const spy = sinon.spy();
    vm.$on('deleteItem', spy);
    vm.$refs.deleteButton.$el.click();
    expect(spy).to.have.been.calledOnce;
  });
});
