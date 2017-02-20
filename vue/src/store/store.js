import Vuex from "vuex";
import Vue from "vue";
import actions from "store/actions";
import * as mutations from "store/mutations";
import * as getters from "store/getters";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    products: [],
    categories: [],
    packs: [],
    productTypes: []
  },
  mutations,
  actions,
  getters
});
