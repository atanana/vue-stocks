<template>
  <div>
    <ProductTypesList
      :items="itemsData"
      :categories="categories"
      newItemPlaceholder="Название типа продуктов"
      newItemLabel="Добавить тип продуктов"
      ref="items"
    />
    <SaveButton ref="saveButton" @save="save(itemsData)"/>
  </div>
</template>

<script>
  import ProductTypesList from 'components/product-types/ProductTypesList';
  import SaveButton from 'components/buttons/SaveButton';
  import {copyData} from 'utility/objectUtils';

  export default {
    components: {
      ProductTypesList,
      SaveButton
    },
    computed: {
      productTypes() {
        return copyData(this.$store.state.productTypes);
      },
      categories() {
        return this.$store.state.categories;
      }
    },
    data() {
      return {
        itemsData: copyData(this.$store.state.productTypes)
      }
    },
    watch: {
      productTypes(newItems) {
        this.itemsData = copyData(newItems)
      }
    },
    created() {
      this.$store.dispatch('loadProductTypes');
      this.$store.dispatch('loadCategories');
    },
    methods: {
      save(newProductTypes) {
        this.$store.dispatch('updateProductTypes', newProductTypes);
      }
    },
  }
</script>
