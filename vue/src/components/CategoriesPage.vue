<template>
  <SimplePage
    :items="categories"
    @saveItems="save"
    newItemPlaceholder="Название категории"
    newItemLabel="Добавить категорию"
    ref="page"/>
</template>

<script>
  import SimplePage from "components/SimplePage";

  export default {
    components: {SimplePage},
    computed: {
      categories() {
        return this.$store.state.categories;
      }
    },
    created() {
      this.$store.dispatch('loadCategories');
    },
    methods: {
      save(newCategories) {
        this.$store.dispatch('updateCategories', newCategories);
      }
    }
  }
</script>
