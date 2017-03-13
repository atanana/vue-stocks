<template>
  <div>
    <MenuEntryItem
      v-for="item in items"
      :item="item"
      :placeholder="newItemPlaceholder"
      class="simple-item"
      @deleteItem="deleteItem(item)"
      ref="items"
    />

    <AddNewButton ref="addNew" :label="newItemLabel" @addNew="addNewItem"/>
  </div>
</template>

<script>
  import Vue from 'vue';
  import moment from 'moment';
  import MenuEntryItem from 'components/menu/MenuEntryItem';
  import AddNewButton from 'components/buttons/AddNewButton';

  export default {
    props: ['items', 'newItemPlaceholder', 'newItemLabel'],
    components: {
      MenuEntryItem,
      AddNewButton
    },
    methods: {
      addNewItem() {
        this.items.push({date: moment().toDate().getTime()});

        this.$nextTick(() => {
          const inputs = this.$el.querySelectorAll('.simple-item > input');
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
