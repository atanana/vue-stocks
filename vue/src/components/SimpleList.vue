<template>
  <div>
    <SimpleItem
      v-for="item in items"
      :item="item"
      :placeholder="newItemPlaceholder"
      class="simple-item"
      @deleteItem="deleteItem(item)"
    />

    <AddNewButton :label="newItemLabel" @addNew="addNewItem"/>
  </div>
</template>

<script>
  import SimpleItem from 'components/SimpleItem';
  import AddNewButton from 'components/buttons/AddNewButton';

  export default {
    props: ['items', 'newItemPlaceholder', 'newItemLabel'],
    components: {
      SimpleItem,
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
