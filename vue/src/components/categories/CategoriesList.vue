<template>
  <div>
    <SimpleItem
      v-for="category in categories"
      :item="category"
      placeholder="Название категории"
      class="category-item"
      @deleteItem="deleteCategory(category)"
    />

    <button type="button" class="btn btn-default new-category-button" @click="addNewCategory">
      <span class="glyphicon glyphicon-plus"></span>
      Добавить категорию
    </button>
  </div>
</template>

<script>
  import SimpleItem from 'components/SimpleItem'

  export default {
    props: ['categories'],
    components: {
      SimpleItem
    },
    methods: {
      addNewCategory() {
        this.categories.push({});

        this.$nextTick(() => {
          const inputs = document.querySelectorAll('.category-item input');
          inputs[inputs.length - 1].focus();
        });
      },
      deleteCategory(category) {
        this.categories.splice(this.categories.indexOf(category), 1);
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
