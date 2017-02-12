import Vue from "vue";
import SaveButton from "src/components/buttons/SaveButton";

describe('SaveButton.vue', () => {
  let vm;

  before(() => {
    vm = createButton();
  });

  function createButton() {
    const Ctor = Vue.extend(SaveButton);
    return new Ctor().$mount();
  }

  it('should render correct contents', () => {
    const button = vm.$el;
    expect(button).to.exist;

    const buttonClasses = button.className.split(' ');
    expect(buttonClasses).to.contain('button');
    expect(buttonClasses).to.contain('is-primary');

    const icon = button.querySelector('.icon .fa.fa-floppy-o');
    expect(icon).to.exist;

    const span = button.querySelector('span:last-child');
    expect(span.textContent).to.equal('Сохранить');
  });

  it('should dispatch correct event', () => {
    const spy = sinon.spy();
    vm.$on('save', spy);
    vm.$el.click();
    expect(spy).to.have.been.calledOnce;
  });
});
