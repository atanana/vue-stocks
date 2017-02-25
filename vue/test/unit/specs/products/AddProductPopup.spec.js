import Vue from "vue";
import AddProductPopup from "src/components/products/AddProductPopup";

describe('AddProductPopup.vue', () => {
  function createPopup(propsData, store) {
    const Ctor = Vue.extend(AddProductPopup);
    return new Ctor({
      propsData,
      store
    }).$mount();
  }

  it('should render correct contents', done => {
    const categories = [
      {name: 'category 1', id: '1'},
      {name: 'category 2', id: '2'},
      {name: 'category 3', id: '3'}
    ];
    const productTypes = [
      {name: 'product type 1', id: '1'},
      {name: 'product type 2', id: '2'},
      {name: 'product type 3', id: '3'}
    ];
    const packs = [
      {name: 'pack 1', id: '1'},
      {name: 'pack 2', id: '2'},
      {name: 'pack 3', id: '3'},
    ];
    const vm = createPopup({
      categories,
      productTypes,
      packs
    });

    expect(vm.$refs.addButton).to.exist;

    vm.showAddProductPopup = true;

    vm.$nextTick(() => {
      const popup = vm.$refs.popup;
      expect(popup.buttonLabel).to.equal('Добавить');

      const header = popup.$slots.header[0];
      expect(header.elm).to.have.html('<h2>Добавьте продукт</h2>');

      const body = popup.$slots.body[0];
      expect(body.elm).to.have.class('control');

      const labels = body.elm.querySelectorAll('label.label');
      checkLabel(labels[0], 'Категория');
      checkLabel(labels[1], 'Тип');
      checkLabel(labels[2], 'Упаковка');

      const selectContainers = body.elm.querySelectorAll('.select');
      checkSelect(selectContainers[0], [{name: 'Без категории', id: ''}].concat(categories));
      checkSelect(selectContainers[1], productTypes);
      checkSelect(selectContainers[2], packs);

      function checkLabel(label, title) {
        expect(label).to.exist;
        expect(label).to.have.text(title);
      }

      function checkSelect(selectContainer, items) {
        expect(selectContainer).to.exist;

        const select = selectContainer.querySelector('select.form-control');
        expect(select).to.exist;

        const options = select.querySelectorAll('option');
        expect(options).to.have.length(items.length);

        for (let i = 0; i < items.length; i++) {
          expect(options[i]).to.have.value(items[i].id);
          expect(options[i].textContent.trim()).to.equal(items[i].name);
        }
      }

      done();
    });
  });

  it('should show popup on press button', done => {
    const vm = createPopup({
      categories: [],
      productTypes: [],
      packs: []
    });

    expect(vm.$refs.popup).to.not.exist;

    vm.$refs.addButton.$emit('addNew');

    vm.$nextTick(() => {
      expect(vm.$refs.popup).to.exist;
      done();
    });
  });

  it('should close popup', done => {
    const vm = createPopup({
      categories: [],
      productTypes: [],
      packs: []
    });
    vm.$refs.addButton.$emit('addNew');

    vm.$nextTick(() => {
      vm.$refs.popup.$emit('close');
      vm.$nextTick(() => {
        expect(vm.$refs.popup).to.not.exist;
        done();
      });
    });
  });

  it('should add product', done => {
    const store = {
      dispatch: function () {
      }
    };
    const storeMock = sinon.mock(store);
    const categoryId = 123;
    const productTypeId = 321;
    const packId = 1234;
    const vm = createPopup({
      categories: [],
      productTypes: [],
      packs: []
    }, store);
    storeMock.expects('dispatch').once().withExactArgs('addProduct', {
      productTypeId,
      categoryId,
      packId,
    });

    vm.packId = packId;
    vm.categoryId = categoryId;
    vm.productTypeId = productTypeId;
    vm.showAddProductPopup = true;

    vm.$nextTick(() => {
      vm.$refs.popup.$emit('okPressed');

      expect(vm.showAddProductPopup).to.be.false;
      storeMock.verify();

      done();
    });
  });

  it('should filter product types when category selected', done => {
    const categoryId = 1;
    const productTypes = [
      {name: 'product type 1', id: '1', categoryId: 2},
      {name: 'product type 2', id: '2'},
      {name: 'product type 3', id: '3', categoryId: categoryId}
    ];
    const vm = createPopup({
      categories: [],
      productTypes,
      packs: []
    });

    vm.categoryId = categoryId;

    vm.$nextTick(() => {
      expect(vm.availableProductTypes).to.eql([productTypes[1], productTypes[2]]);
      done();
    });
  });

  it('should select appropriate category when product type selected', done => {
    const vm = createPopup({
      categories: [
        {name: 'category 1', id: 1},
        {name: 'category 2', id: 2},
        {name: 'category 3', id: 3}
      ],
      productTypes: [
        {id: 2, categoryId: 2}
      ],
      packs: []
    });

    vm.productTypeId = 2;
    vm.$nextTick(() => {
      expect(vm.categoryId).to.equal(2);
      done();
    });
  })
});
