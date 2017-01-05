<template>
  <div>
    <CategoryItem
      v-for="category in categories"
      :category="category"
      class="category-item"
    />

    <button type="button" class="btn btn-default new-category-button" @click="addNewCategory">
      <span class="glyphicon glyphicon-plus"></span>
      Добавить категорию
    </button>
  </div>
</template>

<script>
  import CategoryItem from 'components/categories/CategoryItem'

  export default {
    props: ['categories'],
    components: {
      CategoryItem
    },
    methods: {
      addNewCategory() {
        this.categories.push({});
      }
    }
  }
</script>

<style>
  .category-item {
    margin-bottom: 1em;
  }

  .new-category-button {
    margin-bottom: 1em;
  }
</style>
