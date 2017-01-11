<template>
  <div>
    <SimpleList
      :items="categories"
      newItemPlaceholder="Название категории"
      newItemLabel="Добавить категорию"
    />
    <SaveButton @save="save"/>
  </div>
</template>

<script>
  import SimpleList from 'components/SimpleList'
  import SaveButton from 'components/buttons/SaveButton'

  export default {
    name: 'app',
    components: {
      SimpleList,
      SaveButton
    },
    computed: {
      categories() {
        return this.$store.state.categories;
      }
    },
    created() {
      this.$store.dispatch('loadCategories');
    },
    methods: {
      save() {
        this.$store.dispatch('updateCategories', this.categories);
      }
    }
  }
</script>
