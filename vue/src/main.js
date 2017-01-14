// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from "vue";
import VueRouter from "vue-router";
import ProductsPage from "components/products/ProductsPage";
import SimplePage from "components/SimplePage";
import ProductTypesPage from "components/product-types/ProductTypesPage";
import store from "store/store";

Vue.use(VueRouter);

const Products = {
  template: '<ProductsPage/>',
  components: {ProductsPage}
};

const Categories = {
  template: `<SimplePage
                :items="categories"
                @saveItems="save"
                newItemPlaceholder="Название категории"
                newItemLabel="Добавить категорию"/>`,
  components: {SimplePage},
  computed: {
    categories() {
      return this.$store.state.categories;
    }
  },
  created() {
    this.$store.dispatch('loadCategories');
  },
  methods: {
    save() {
      this.$store.dispatch('updateCategories', this.categories);
    }
  }
};
const Packs = {
  template: `<SimplePage
                :items="packs"
                @saveItems="save"
                newItemPlaceholder="Название упаковки"
                newItemLabel="Добавить упаковку"/>`,
  components: {SimplePage},
  computed: {
    packs() {
      return this.$store.state.packs;
    }
  },
  created() {
    this.$store.dispatch('loadPacks');
  },
  methods: {
    save() {
      this.$store.dispatch('updatePacks', this.packs);
    }
  }
};
const ProductTypes = {
  template: `<ProductTypesPage
                :items="productTypes"
                :categories="categories"
                @saveItems="save"
                newItemPlaceholder="Название типа продуктов"
                newItemLabel="Добавить тип продуктов"/>`,
  components: {ProductTypesPage},
  computed: {
    productTypes() {
      return this.$store.state.productTypes;
    },
    categories() {
      return this.$store.state.categories;
    }
  },
  created() {
    this.$store.dispatch('loadProductTypes');
    this.$store.dispatch('loadCategories');
  },
  methods: {
    save() {
      this.$store.dispatch('updateProductTypes', this.productTypes);
    }
  }
};

const routes = [
  {path: '/products', component: Products},
  {path: '/categories', component: Categories},
  {path: '/packs', component: Packs},
  {path: '/product-types', component: ProductTypes},
];

const router = new VueRouter({
  routes, // short for routes: routes,
  mode: 'history'
});

const app = new Vue({
  router,
  store
}).$mount('#app');

if (location.pathname === '/') {
  router.replace(routes[0].path);
}
