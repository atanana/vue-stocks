<template>
  <div>
    <AddProductPopup
      :categories="categories"
      :productTypes="productTypes"
      :packs="packs"
    />
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
        return toMap(this.categories);
      },
      packsMap() {
        return toMap(this.packs);
      },
      productTypesMap() {
        return toMap(this.productTypes);
      },
      categories() {
        return this.$store.state.categories;
      },
      productTypes() {
        return this.$store.state.productTypes;
      },
      packs() {
        return this.$store.state.packs;
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
