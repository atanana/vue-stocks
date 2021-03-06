import Vue from "vue";
import AddNewButton from "src/components/buttons/AddNewButton";

describe('AddNewButton.vue', () => {
  const label = 'test label';

  let vm;

  beforeEach(() => {
    vm = createButton();
  });

  function createButton() {
    const Ctor = Vue.extend(AddNewButton);
    return new Ctor({
      propsData: {
        label: label
      }
    }).$mount();
  }

  it('should render correct contents', () => {
    const button = vm.$el;
    expect(button).to.exist;

    expect(button).to.have.class('button');
    expect(button).to.have.class('new-item-button');

    const icon = button.querySelector('.icon .fa.fa-plus');
    expect(icon).to.exist;

    const span = button.querySelector('span:last-child');
    expect(span.textContent).to.equal(label);
  });

  it('should dispatch correct event', () => {
    const spy = sinon.spy();
    vm.$on('addNew', spy);
    vm.$el.click();
    expect(spy).to.have.been.calledOnce;
  });
});
