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

  it('should update categories', done => {
    checkUpdate('/api/categories/update', actions.updateCategories, 'setCategories', done);
  });

  it('should update packs', done => {
    checkUpdate('/api/packs/update', actions.updatePacks, 'setPacks', done);
  });

  it('should update product types', done => {
    checkUpdate('/api/product-types/update', actions.updateProductTypes, 'setProductTypes', done);
  });

  it('should add product', done => {
    checkPost('/api/products/new', actions.addProduct, 'setProducts', done);
  });

  it('should delete product', done => {
    checkPost('/api/products/delete', actions.deleteProduct, 'setProducts', done);
  });

  function checkUpdate(path, method, action, done) {
    checkMethod(path, method, action, done, server.onPut);
  }

  function checkPost(path, method, action, done) {
    checkMethod(path, method, action, done, server.onPost);
  }

  function checkMethod(path, method, action, done, serverMethod) {
    const data = {
      items: [
        {name: 'item 1'},
        {name: 'item 2'},
        {name: 'item 3'}
      ]
    };
    const newData = {
      items: [
        {name: 'item 1'}
      ]
    };
    serverMethod.call(server, path, newData).reply(200, data);
    const spy = sinon.spy();

    method({commit: spy}, newData);

    setTimeout(() => {
      expect(spy).to.calledWith(action, data);
      done();
    }, 0);
  }

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
    method({commit: spy});

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
