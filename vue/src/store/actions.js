import axios from "axios";

export const loadProducts = ({commit}) => {
  axios.get('/api/products/all')
    .then(response => {
      commit('setProducts', response.data);
    });
};

export const loadCategories = ({commit}) => {
  axios.get('/api/categories/all')
    .then(response => {
      commit('setCategories', response.data);
    });
};

export const loadPacks = ({commit}) => {
  axios.get('/api/packs/all')
    .then(response => {
      commit('setPacks', response.data);
    });
};

export const loadProductTypes = ({commit}) => {
  axios.get('/api/product-types/all')
    .then(response => {
      commit('setProductTypes', response.data);
    });
};

export const updateCategories = ({commit}, categories) => {
  axios.put('/api/categories/update', categories)
    .then(response => {
      commit('setCategories', response.data);
    });
};

export const updatePacks = ({commit}, packs) => {
  axios.put('/api/packs/update', packs)
    .then(response => {
      commit('setPacks', response.data);
    });
};

export const updateProductTypes = ({commit}, productTypes) => {
  axios.put('/api/product-types/update', productTypes)
    .then(response => {
      commit('setProductTypes', response.data);
    });
};
