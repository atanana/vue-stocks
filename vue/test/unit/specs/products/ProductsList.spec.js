import Vue from "vue";
import ProductsList from "src/components/products/ProductsList";

describe('ProductsList.vue', () => {
  function createList(propsData) {
    const Ctor = Vue.extend(ProductsList);
    return new Ctor({
      propsData
    }).$mount();
  }

  it('should render correct contents', () => {
    const packs = [
      {name: 'pack 1'},
      {name: 'pack 2'},
      {name: 'pack 3'}
    ];
    const products = [
      {name: 'product 1', packs: []},
      {name: 'product 2', packs: []},
      {name: 'product 3', packs: []},
    ];
    const vm = createList({
      products: products,
      packs: packs
    });

    const table = vm.$el;

    expect(table).to.have.class('table');
    expect(table).to.have.class('is-striped');

    const headers = table.querySelectorAll('thead th');
    checkHeader(headers[0], '30%', 'Имя');
    checkHeader(headers[1], '15%', 'Место');
    checkHeader(headers[2], '*', 'Количество');

    function checkHeader(header, width, title) {
      expect(header.getAttribute('width')).to.equals(width);
      expect(header.textContent).to.equals(title);
    }

    const items = vm.$refs.items;
    for (let i = 0; i < items.length; i++) {
      checkItem(items[i], products[i]);
    }

    function checkItem(item, originalItem) {
      expect(item.product).to.eql(originalItem);
      expect(item.packTypes).to.eql(packs);
    }
  });
});
