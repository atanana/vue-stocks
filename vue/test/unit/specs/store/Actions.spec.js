import MockServer from "axios-mock-adapter";
import axios from "axios";
import actions from "src/store/actions";

describe('actions.js', () => {
  let server;

  beforeEach(() => {
    server = new MockServer(axios);
  });

  it('should load products only once', done => {
    checkLoadedOnce('/api/products/all', actions.loadProducts, 'setProducts', done);
  });

  it('should load categories only once', done => {
    checkLoadedOnce('/api/categories/all', actions.loadCategories, 'setCategories', done);
  });

  it('should load product packs only once', done => {
    checkLoadedOnce('/api/packs/all', actions.loadPacks, 'setPacks', done);
  });

  it('should load products types once', done => {
    checkLoadedOnce('/api/product-types/all', actions.loadProductTypes, 'setProductTypes', done);
  });

  function checkLoadedOnce(path, method, action, done) {
    const data = {
      items: [
        {name: 'item 1'},
        {name: 'item 2'},
        {name: 'item 3'}
      ]
    };
    server.onGet(path).reply(200, data);
    const spy = sinon.spy();
    method.call(actions, {commit: spy});

    setTimeout(() => {
      expect(spy).to.calledWith(action, data);

      actions.loadProducts({commit: spy});

      setTimeout(() => {
        expect(spy).to.calledOnce;
        done();
      });
    }, 0);
  }
});
