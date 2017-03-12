<template>
  <div class="simple-item">
    <!--suppress HtmlFormInputWithoutLabel -->
    <input type="text" class="input stocks-menu-label" :placeholder="placeholder" v-model="item.name">
    <datepicker v-model="item.date" input-class="input" :monday-first="true" language="ru"/>
    <DeleteButton ref="deleteButton" class="delete-button" @delete="$emit('deleteItem')"/>
  </div>
</template>

<script>
  import DeleteButton from 'components/buttons/DeleteButton';
  import Datepicker from 'vuejs-datepicker';

  export default {
    props: ['item', 'placeholder'],
    components: {
      DeleteButton,
      Datepicker
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
</style>
