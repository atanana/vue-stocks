import Vue from "vue";
import Popup from "src/components/Popup";

describe('Popup.vue', () => {
  function createPopup(propsData) {
    const Ctor = Vue.extend(Popup);
    return new Ctor({
      propsData
    }).$mount();
  }

  function createPopupContainer(content) {
    return new Vue({
      render: h => h('div', [h('Popup', content(h))]),
      components: {
        Popup
      }
    }).$mount();
  }

  it('should render correct contents', () => {
    const vm = createPopupContainer(h => []);

    const modal = vm.$el.querySelector('.modal.is-active');
    expect(modal).to.exist;

    const modalBackground = modal.querySelector('.modal-background');
    expect(modalBackground).to.exist;

    const modalBody = modal.querySelector('.modal-card');
    expect(modalBody).to.exist;

    const header = modalBody.querySelector('header.modal-card-head');
    expect(header).to.exist;

    const title = header.querySelector('.modal-card-title');
    expect(title).to.exist;

    const closeButton = header.querySelector('button.delete');
    expect(closeButton).to.exist;

    const content = modalBody.querySelector('.modal-card-body');
    expect(content).to.exist;

    const footer = modalBody.querySelector('footer.modal-card-foot');
    expect(footer).to.exist;

    const okButton = footer.querySelector('a.button.is-primary');
    expect(okButton).to.exist;
    expect(okButton.textContent.trim()).to.equal('Ok');
  });

  it('should render correct header', () => {
    const testContent = 'test header';
    const vm = createPopupContainer(h => [h('div', {slot: 'header'}, [testContent])]);

    const title = vm.$el.querySelector('.modal-card-title');
    expect(title.childNodes[0].textContent).to.equal(testContent);
  });

  it('should render correct body', () => {
    const testContent = 'test body';
    const vm = createPopupContainer(h => [h('div', {slot: 'body'}, [testContent])]);

    const title = vm.$el.querySelector('.modal-card-body');
    expect(title.childNodes[0].textContent).to.equal(testContent);
  });

  it('should render correct button label', () => {
    const label = 'test label';
    const vm = createPopup({
      buttonLabel: label
    });

    const button = vm.$el.querySelector('.button');
    expect(button.textContent.trim()).to.equal(label);
  });

  it('should dispatch correct close event', () => {
    const vm = createPopup();
    const spy = sinon.spy();
    vm.$on('close', spy);

    vm.$el.querySelector('.delete').click();

    expect(spy).to.have.been.calledOnce;
  });

  it('should dispatch correct ok event', () => {
    const vm = createPopup();
    const spy = sinon.spy();
    vm.$on('okPressed', spy);

    vm.$el.querySelector('.button').click();

    expect(spy).to.have.been.calledOnce;
  });
});
