<template>
  <div>
    <MenuEntryItem
      v-for="item in items"
      :item="item"
      class="simple-item"
      @deleteItem="deleteItem(item)"
      ref="items"
    />

    <AddNewButton ref="addNew" label="Добавить блюдо" @addNew="addNewItem"/>
  </div>
</template>

<script>
  import Vue from 'vue';
  import AddNewButton from 'components/buttons/AddNewButton';
  import MenuEntryItem from 'components/menu/MenuEntryItem';
  import moment from 'moment';

  export default {
    props: ['items'],
    components: {
      AddNewButton,
      MenuEntryItem
    },
    methods: {
      addNewItem() {
        this.items.push({date: moment().format('DD-MM-YYYY')});

        this.$nextTick(() => {
          const inputs = this.$el.querySelectorAll('.simple-item > input');
          if (inputs.length) {
            inputs[inputs.length - 1].focus();
          }
        });
      },
      deleteItem(item) {
        this.items.splice(this.items.indexOf(item), 1);
      }
    }
  }
</script>
