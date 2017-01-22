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
        }))
          .sort((left, right) => {
            const leftProduct = left.productType.name;
            const rightProduct = right.productType.name;
            let result = 0;

            if (leftProduct !== rightProduct) {
              result = leftProduct < rightProduct ? -1 : 1;
            } else {
              const leftCategory = left.category.name;
              const rightCategory = right.category.name;

              if (leftCategory !== rightCategory) {
                result = leftCategory < rightCategory ? -1 : 1;
              }
            }

            return result;
          });
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
