<template>
  <div>
    <AddProductPopup v-if="showAddProductPopup" @close="showAddProductPopup = false"/>
    <ProductsList
      :products="products"
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
        //noinspection JSUnresolvedVariable
        return this.$store.state.products.map(product => ({
          productType: this.productTypesMap[product.productTypeId],
          category: this.categoriesMap[product.categoryId],
          packs: product.packs.map(pack => ({
            pack: this.packsMap[pack.packId],
            quantity: pack.quantity
          }))
        }));
      },
      categoriesMap() {
        return toMap(this.$store.state.categories);
      },
      packsMap() {
        return toMap(this.$store.state.packs);
      },
      productTypesMap() {
        return toMap(this.$store.state.productTypes);
      },
    },
    created() {
      this.$store.dispatch('loadProducts');
      this.$store.dispatch('loadCategories');
      this.$store.dispatch('loadPacks');
      this.$store.dispatch('loadProductTypes');
    },
    data() {
      return {
        showAddProductPopup: false
      }
    }
  }
</script>
