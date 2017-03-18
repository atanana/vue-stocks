<template>
  <div>
    <div class="field menu-sorting">
      <p class="control">
        <label class="radio">
          <input type="radio" value="by-name" v-model="sorting" ref="sortByName">
          По имени
        </label>
        <label class="radio">
          <input type="radio" value="by-date" v-model="sorting" ref="sortByDate">
          По дате
        </label>
      </p>
    </div>

    <MenuEntryItem
      v-for="item in sortedItems"
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
  import _ from 'lodash';

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
    },
    data() {
      return {
        sorting: 'by-name'
      }
    },
    computed: {
      sortedItems() {
        return _.sortBy(this.items, item => {
          if (this.sorting === 'by-name') {
            return item.name;
          } else {
            return -moment(item.date, 'DD-MM-YYYY').unix()
          }
        });
      }
    }
  }
</script>

<style>
  .menu-sorting {
    margin-bottom: 1em;
  }
</style>
