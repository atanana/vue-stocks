<template>
  <div>
    <MenuList :items="itemsData" ref="items"/>
    <SaveButton ref="saveButton" @save="save(itemsData)"/>
  </div>
</template>

<script>
  import SaveButton from 'components/buttons/SaveButton';
  import {copyData} from 'utility/objectUtils';
  import MenuList from 'components/menu/MenuList';

  export default {
    components: {
      MenuList,
      SaveButton
    },
    computed: {
      menuItems() {
        return copyData(this.$store.state.menuItems);
      }
    },
    data() {
      return {
        itemsData: copyData(this.$store.state.menuItems)
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
