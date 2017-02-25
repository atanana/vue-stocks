import Vue from "vue";
import SaveButton from "src/components/buttons/SaveButton";

describe('SaveButton.vue', () => {
  let vm;

  beforeEach(() => {
    vm = createButton();
  });

  function createButton() {
    const Ctor = Vue.extend(SaveButton);
    return new Ctor().$mount();
  }

  it('should render correct contents', () => {
    const button = vm.$el;
    expect(button).to.exist;

    expect(button).to.have.class('button');
    expect(button).to.have.class('is-primary');

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
