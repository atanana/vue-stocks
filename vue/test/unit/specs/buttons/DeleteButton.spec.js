import Vue from "vue";
import DeleteButton from "src/components/buttons/DeleteButton";

describe('DeleteButton.vue', () => {
  let vm;

  beforeEach(() => {
    vm = createButton();
  });

  function createButton() {
    const Ctor = Vue.extend(DeleteButton);
    return new Ctor().$mount();
  }

  it('should render correct contents', () => {
    const button = vm.$el;
    expect(button).to.exist;

    expect(button).to.have.class('button');
    expect(button).to.have.class('is-danger');

    const icon = button.querySelector('.icon .fa.fa-times');
    expect(icon).to.exist;

    const span = button.querySelector('span:last-child');
    expect(span.textContent).to.equal('Удалить');
  });

  it('should dispatch correct event', () => {
    const spy = sinon.spy();
    vm.$on('delete', spy);
    vm.$el.click();
    expect(spy).to.have.been.calledOnce;
  });
});
