<template>
  <div class="simple-item">
    <!--suppress HtmlFormInputWithoutLabel -->
    <input type="text" class="input" :placeholder="placeholder" v-model="item.name">
    <span class="select">
      <!--suppress HtmlFormInputWithoutLabel -->
      <select v-model="item.categoryId" class="category-select">
        <option
          v-for="category in availableCategories"
          :value="category.id"
        >{{category.name}}
        </option>
      </select>
    </span>
    <DeleteButton ref="deleteButton" class="delete-button" @delete="$emit('deleteItem')"/>
  </div>
</template>

<script>
  import DeleteButton from 'components/buttons/DeleteButton'

  export default {
    props: ['item', 'categories', 'placeholder'],
    components: {
      DeleteButton
    },
    computed: {
      availableCategories() {
        return [{name: 'Без категории'}].concat(this.categories);
      }
    }
  }
</script>

<style>
  .delete-button {
    margin-left: 1em;
  }

  .simple-item {
    display: flex;
  }

  .category-select {
    margin-left: 1em;
  }
</style>
