import Vue from "vue";
import moment from "moment";
import ProductLogsPage from "src/components/ProductLogsPage";

describe('ProductLogsPage.vue', () => {
  function createPage(store) {
    const Ctor = Vue.extend(ProductLogsPage);
    return new Ctor({
      store
    }).$mount();
  }

  function createStore(logs) {
    return {
      dispatch: function () {
      },
      getters: {
        productLogs: logs
      }
    };
  }

  it('should load data on create', () => {
    const store = createStore([]);
    const storeMock = sinon.mock(store);
    storeMock.expects('dispatch').withExactArgs('loadProductLogs');
    storeMock.expects('dispatch').withExactArgs('loadCategories');
    storeMock.expects('dispatch').withExactArgs('loadPacks');
    storeMock.expects('dispatch').withExactArgs('loadProductTypes');

    createPage(store);

    storeMock.verify();
  });

  it('should render correct data wrapper', () => {
    const table = createPage(createStore([])).$el;

    expect(table).to.have.class('table');
    expect(table).to.have.class('is-striped');

    const headers = Array.from(table.querySelectorAll('thead th'));
    expect(headers.map(head => head.textContent)).to.eql(['Имя', 'Место', 'Упаковка', 'Действие', 'Время']);
  });

  it('should render empty log entry', () => {
    const time = moment();
    const table = createPage(createStore([{time: time}])).$el;

    const data = table.querySelectorAll('tbody td');
    expect(data[0].textContent).to.equals('-');
    expect(data[1].textContent).to.equals('-');
    expect(data[2].textContent).to.equals('-');
    expect(data[3].html).to.empty;
    expect(data[4].textContent).to.equals(time.format('MMMM Do YYYY, H:mm:ss'));
  });

  it('should render log entry', () => {
    const time = moment();
    const table = createPage(createStore([{
      productType: {name: 'test product type'},
      category: {name: 'test category type'},
      pack: {name: 'test pack type'},
      time: time
    }])).$el;

    const data = table.querySelectorAll('tbody td');
    expect(data[0].textContent).to.equals('test product type');
    expect(data[1].textContent).to.equals('test category type');
    expect(data[2].textContent).to.equals('test pack type');
    expect(data[3].html).to.empty;
    expect(data[4].textContent).to.equals(time.format('MMMM Do YYYY, H:mm:ss'));
  });

  it('should render added action', () => {
    const table = createPage(createStore([{
      action: 'add',
      time: moment()
    }])).$el;

    const action = table.querySelectorAll('tbody td')[3];
    expect(action).to.have.html('<span class="tag is-success">Добавлен</span>');
  });

  it('should render removed action', () => {
    const table = createPage(createStore([{
      action: 'remove',
      time: moment()
    }])).$el;

    const action = table.querySelectorAll('tbody td')[3];
    expect(action).to.have.html('<span class="tag is-danger">Удалён</span>');
  });
});
