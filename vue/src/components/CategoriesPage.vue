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
    data() {
      return {
        categories: []
      }
    },
    created () {
      axios.get('/api/categories/all')
        .then((response) => {
          console.log(response.data);
          this.categories = response.data;
        });
    }
  }
</script>
