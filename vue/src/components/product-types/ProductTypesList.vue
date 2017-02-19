<template>
  <div>
    <ProductTypesItem
      v-for="item in items"
      :item="item"
      :categories="categories"
      :placeholder="newItemPlaceholder"
      class="simple-item"
      @deleteItem="deleteItem(item)"
      ref="items"
    />

    <AddNewButton ref="addNew" :label="newItemLabel" @addNew="addNewItem"/>
  </div>
</template>

<script>
  import ProductTypesItem from 'components/product-types/ProductTypesItem'
  import AddNewButton from 'components/buttons/AddNewButton'

  export default {
    props: ['items', 'categories', 'newItemPlaceholder', 'newItemLabel'],
    components: {
      ProductTypesItem,
      AddNewButton
    },
    methods: {
      addNewItem() {
        this.items.push({});

        this.$nextTick(() => {
          const inputs = document.querySelectorAll('.simple-item input');
          inputs[inputs.length - 1].focus();
        });
      },
      deleteItem(item) {
        this.items.splice(this.items.indexOf(item), 1);
      }
    }
  }
</script>

<style>
  .simple-item {
    margin-bottom: 1em;
  }
</style>
