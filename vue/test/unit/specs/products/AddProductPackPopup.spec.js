import Vue from "vue";
import AddProductPackPopup from "src/components/products/AddProductPackPopup";

describe('AddProductPackPopup.vue', () => {
  function createPopup(propsData, store) {
    const Ctor = Vue.extend(AddProductPackPopup);
    return new Ctor({
      propsData,
      store
    }).$mount();
  }

  it('should render correct contents', done => {
    const categoryId = 123;
    const productTypeId = 321;
    const packs = [
      {name: 'pack 1', id: '1'},
      {name: 'pack 2', id: '2'},
      {name: 'pack 3', id: '3'},
    ];
    const vm = createPopup({
      categoryId,
      productTypeId,
      packs
    });

    const popupElement = vm.$el;
    expect(popupElement).to.have.class('add-pack-container');

    const addButton = popupElement.querySelector('.tag.is-dark.add-pack');
    expect(addButton).to.exist;
    expect(addButton.textContent.trim()).to.equal('Добавить упаковку');

    vm.showAddPackPopup = true;

    vm.$nextTick(() => {
      const popup = vm.$refs.popup;
      expect(popup.buttonLabel).to.equal('Добавить');

      const header = popup.$slots.header[0];
      expect(header.elm).to.have.html('<h2>Добавьте продукт</h2>');

      const body = popup.$slots.body[0];
      expect(body.elm).to.have.class('control');

      const label = body.elm.querySelector('label.label');
      expect(label).to.exist;
      expect(label).to.have.text('Упаковка');

      const selectContainer = body.elm.querySelector('.select');
      expect(selectContainer).to.exist;

      const select = selectContainer.querySelector('select.form-control');
      expect(select).to.exist;

      const options = select.querySelectorAll('option');
      expect(options).to.have.length(packs.length);

      for (let i = 0; i < packs.length; i++) {
        expect(options[i]).to.have.value(packs[i].id);
        expect(options[i].textContent.trim()).to.equal(packs[i].name);
      }

      done();
    });
  });

  it('should show popup on press button', done => {
    const vm = createPopup();

    expect(vm.$refs.popup).to.not.exist;

    vm.$refs.addButton.click();

    vm.$nextTick(() => {
      expect(vm.$refs.popup).to.exist;
      done();
    });
  });

  it('should close popup', done => {
    const vm = createPopup();
    vm.$refs.addButton.click();

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
      categoryId,
      productTypeId,
      packs: []
    }, store);
    storeMock.expects('dispatch').once().withExactArgs('addProduct', {
      productTypeId,
      categoryId,
      packId,
    });

    vm.packId = packId;
    vm.showAddPackPopup = true;

    vm.$nextTick(() => {
      vm.$refs.popup.$emit('okPressed');

      expect(vm.showAddPackPopup).to.be.false;
      storeMock.verify();

      done();
    });
  });
});
