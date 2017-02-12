import Vue from "vue";
import FloatingButton from "src/components/buttons/FloatingButton";

describe('FloatingButton.vue', () => {
  let vm;

  before(() => {
    vm = createButton();
  });

  function createButton() {
    const Ctor = Vue.extend(FloatingButton);
    return new Ctor().$mount();
  }

  it('should render correct contents', () => {
    const button = vm.$el;
    expect(button).to.exist;

    const buttonClasses = button.className.split(' ');
    expect(buttonClasses).to.contain('floating-button');

    const span = button.querySelector('p.plus');
    expect(span.textContent).to.equal('+');
  });

  it('should dispatch correct event', () => {
    const spy = sinon.spy();
    vm.$on('addNew', spy);
    vm.$el.click();
    expect(spy).to.have.been.calledOnce;
  });
});
