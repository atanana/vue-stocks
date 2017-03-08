import Vue from "vue";
import HistoryLink from "src/components/buttons/HistoryLink";

describe('HistoryLink.vue', () => {
  function createLink(propsData) {
    const Ctor = Vue.extend(HistoryLink);
    return new Ctor({propsData}).$mount();
  }

  it('should render correct button', () => {
    const link = 'test link';
    const history = createLink({link});

    expect(history.link).to.equals(link);

    const button = history.$el;
    expect(button).to.have.class('button');
    expect(button).to.have.class('is-link');

    const spans = button.querySelectorAll('span');
    const icon = spans[0];
    expect(icon).to.have.class('icon');
    expect(icon).to.have.class('is-small');
    expect(icon.querySelector('i.fa.fa-history')).to.exist;

    expect(spans[1]).to.have.text('История');
  });
});
