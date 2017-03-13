<template>
  <div>
    <SimpleList
      :items="itemsData"
      newItemLabel="Добавить тип продуктов"
      ref="items"
    >
      <template slot="item" scope="props">
        <ProductTypesItem
          :item="props.item"
          :categories="categories"
          class="simple-item"
          @deleteItem="$refs.items.deleteItem(props.item)"
        />
      </template>
    </SimpleList>
    <SaveButton ref="saveButton" @save="save(itemsData)"/>
  </div>
</template>

<script>
  import SimpleList from 'components/SimpleList';
  import SaveButton from 'components/buttons/SaveButton';
  import {copyData} from 'utility/objectUtils';
  import ProductTypesItem from 'components/product-types/ProductTypesItem';

  export default {
    components: {
      SimpleList,
      SaveButton,
      ProductTypesItem
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
