import axios from "axios";

const loadOnce = (func) => {
  let loaded = false;
  return ({commit}) => {
    if (!loaded) {
      func(commit);
      loaded = true;
    }
  }
};

const defaultLoad = (url, action) =>
  loadOnce((commit) => {
    axios.get(url)
      .then(response => {
        commit(action, response.data);
      });
  });

const defaultUpdate = (url, action) =>
  ({commit}, items) => {
    axios.put(url, items)
      .then(response => {
        commit(action, response.data);
      });
  };

export default {
  loadProducts: defaultLoad('/api/products/all', 'setProducts'),
  loadCategories: defaultLoad('/api/categories/all', 'setCategories'),
  loadPacks: defaultLoad('/api/packs/all', 'setPacks'),
  loadProductTypes: defaultLoad('/api/product-types/all', 'setProductTypes'),
  loadMenuItems: defaultLoad('/api/menu/all', 'setMenuItems'),
  loadProductLogs({commit}) {
    axios.get('/api/product-logs/all')
      .then(response => {
        commit('setProductLogs', response.data);
      });
  },
  updateCategories: defaultUpdate('/api/categories/update', 'setCategories'),
  updatePacks: defaultUpdate('/api/packs/update', 'setPacks'),
  updateProductTypes: defaultUpdate('/api/product-types/update', 'setProductTypes'),
  updateMenuItems: defaultUpdate('/api/menu/update', 'setMenuItems'),
  addProduct({commit}, newProduct) {
    axios.post('/api/products/new', newProduct)
      .then(response => {
        commit('setProducts', response.data);
      })
  },
  deleteProduct({commit}, product) {
    axios.post('/api/products/delete', product)
      .then(response => {
        commit('setProducts', response.data);
      })
  }
}
