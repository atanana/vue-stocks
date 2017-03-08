<template>
  <div>
    <AddProductPopup
      :categories="categories"
      :productTypes="productTypes"
      :packs="packs"
      ref="addProductPopup"
    />
    <HistoryLink class="is-pulled-right" link="/products-logs" ref="history"/>
    <div class="is-clearfix"></div>
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
  import HistoryLink from 'components/buttons/HistoryLink';

  export default {
    components: {
      ProductsList,
      AddProductPopup,
      HistoryLink
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
