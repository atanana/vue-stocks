<template>
  <div>
    <slot
      name="item"
      v-for="item in items"
      @deleteItem="deleteItem(item)"
      :item="item"
    />

    <AddNewButton ref="addNew" :label="newItemLabel" @addNew="addNewItem"/>
  </div>
</template>

<script>
  import Vue from 'vue';
  import AddNewButton from 'components/buttons/AddNewButton';

  export default {
    props: ['items', 'newItemLabel'],
    components: {
      AddNewButton
    },
    methods: {
      addNewItem() {
        this.items.push({});

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
