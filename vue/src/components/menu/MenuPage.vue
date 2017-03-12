<template>
  <div>
    <MenuItemsList
      :items="itemsData"
      newItemPlaceholder="Название блюда"
      newItemLabel="Добавить блюдо"
      ref="items"
    />
    <SaveButton ref="saveButton" @save="save(itemsData)"/>
  </div>
</template>

<script>
  import MenuItemsList from 'components/menu/MenuItemsList';
  import SaveButton from 'components/buttons/SaveButton';
  import {copyData} from 'utility/objectUtils';

  export default {
    components: {
      MenuItemsList,
      SaveButton
    },
    computed: {
      menuItems() {
        return copyData(this.$store.state.menuItems);
      }
    },
    data() {
      return {
        itemsData: []
      }
    },
    watch: {
      menuItems(newItems) {
        this.itemsData = copyData(newItems)
      }
    },
    created() {
      this.$store.dispatch('loadMenuItems');
    },
    methods: {
      save(newMenuItems) {
        this.$store.dispatch('updateMenuItems', newMenuItems);
      }
    },
  }
</script>
