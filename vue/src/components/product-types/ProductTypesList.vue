<template>
  <div>
    <ProductTypesItem
      v-for="item in items"
      :item="item"
      :categories="categories"
      :placeholder="newItemPlaceholder"
      class="simple-item"
      @deleteItem="deleteItem(item)"
    />

    <button type="button" class="btn btn-default new-item-button" @click="addNewItem">
      <span class="glyphicon glyphicon-plus"></span>
      {{newItemLabel}}
    </button>
  </div>
</template>

<script>
  import ProductTypesItem from 'components/product-types/ProductTypesItem'

  export default {
    props: ['items', 'categories', 'newItemPlaceholder', 'newItemLabel'],
    components: {
      ProductTypesItem
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

  .new-item-button {
    margin-bottom: 1em;
  }
</style>
