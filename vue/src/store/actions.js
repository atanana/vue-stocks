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

export default {
  loadProducts: loadOnce((commit) => {
    axios.get('/api/products/all')
      .then(response => {
        commit('setProducts', response.data);
      });
  }),
  loadCategories: loadOnce((commit) => {
    axios.get('/api/categories/all')
      .then(response => {
        commit('setCategories', response.data);
      });
  }),
  loadPacks: loadOnce((commit) => {
    axios.get('/api/packs/all')
      .then(response => {
        commit('setPacks', response.data);
      });
  }),
  loadProductTypes: loadOnce((commit) => {
    axios.get('/api/product-types/all')
      .then(response => {
        commit('setProductTypes', response.data);
      });
  }),
  updateCategories({commit}, categories) {
    axios.put('/api/categories/update', categories)
      .then(response => {
        commit('setCategories', response.data);
      });
  },
  updatePacks({commit}, packs){
    axios.put('/api/packs/update', packs)
      .then(response => {
        commit('setPacks', response.data);
      });
  },
  updateProductTypes({commit}, productTypes) {
    axios.put('/api/product-types/update', productTypes)
      .then(response => {
        commit('setProductTypes', response.data);
      });
  },
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
