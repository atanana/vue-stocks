<template>
  <div>
    <CategoriesList
      :categories="categories"
    />
    <SaveButton @save="save"/>
  </div>
</template>

<script>
  import axios from 'axios'
  import CategoriesList from 'components/categories/CategoriesList'
  import SaveButton from 'components/buttons/SaveButton'

  export default {
    name: 'app',
    components: {
      CategoriesList,
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
