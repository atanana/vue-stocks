<template>
  <div>
    <AddProductPopup
      :categories="categories"
      :productTypes="productTypes"
      :packs="packs"
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
        let products = this.$store.state.products
          .map(product => ({
            productType: this.$store.getters.productTypesMap[product.productTypeId],
            category: this.$store.getters.categoriesMap[product.categoryId],
            packs: product.packs.map(pack => ({
              pack: this.$store.getters.packsMap[pack.packId],
              quantity: pack.quantity
            }))
          }));

        if (this.currentCategory) {
          products = products.filter(product => product.category.id === this.currentCategory);
        }

        return products.sort((left, right) => {
          let result = 0;
          if (left.productType && right.productType && left.category && right.category) {
            const leftProduct = left.productType.name;
            const rightProduct = right.productType.name;

            if (leftProduct !== rightProduct) {
              result = leftProduct < rightProduct ? -1 : 1;
            } else {
              const leftCategory = left.category.name;
              const rightCategory = right.category.name;

              if (leftCategory !== rightCategory) {
                result = leftCategory < rightCategory ? -1 : 1;
              }
            }
          }

          return result;
        });
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
