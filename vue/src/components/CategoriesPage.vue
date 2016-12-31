<template>
  <CategoriesList
    :categories="categories"
  />
</template>

<script>
  import axios from 'axios'
  import CategoriesList from 'components/CategoriesList'

  export default {
    name: 'app',
    components: {
      CategoriesList
    },
    computed: {
      categories() {
        return this.$store.state.categories;
      }
    },
    created () {
      this.$store.commit('loadCategories');
    }
  }
</script>
