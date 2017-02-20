<template>
  <div>
    <AddProductPopup
      :categories="categories"
      :productTypes="productTypes"
      :packs="packs"
      ref="addProductPopup"
    />
    <div class="tabs is-boxed is-fullwidth">
      <ul>
        <li v-for="category in availableCategories"
            :class="{'is-active':category.id == currentCategory}"
            @click="currentCategory = category.id">
          <a>{{category.name}}</a>
        </li>
      </ul>
    </div>
    <ProductsList
      :products="products"
      :packs="packs"
      ref="products"
    />
  </div>
</template>

<script>
  import ProductsList from 'components/products/ProductsList';
  import AddProductPopup from 'components/products/AddProductPopup';
  import {toMap} from 'utility/objectUtils';

  export default {
    components: {
      ProductsList,
      AddProductPopup
    },
    computed: {
      products() {
        return this.$store.getters.groupedProducts(this.currentCategory);
      },
      categories() {
        return this.$store.state.categories;
      },
      productTypes() {
        return this.$store.state.productTypes;
      },
      packs() {
        return this.$store.state.packs;
      },
      availableCategories() {
        return [{name: 'Всё', id: 0}].concat(this.categories);
      }
    },
    data() {
      return {
        currentCategory: 0
      }
    },
    created() {
      this.$store.dispatch('loadProducts');
      this.$store.dispatch('loadCategories');
      this.$store.dispatch('loadPacks');
      this.$store.dispatch('loadProductTypes');
    }
  }
</script>
