<template>
  <div class="simple-item">
    <!--suppress HtmlFormInputWithoutLabel -->
    <input type="text" class="input" placeholder="Название блюда" v-model="item.name">
    <datepicker
      :value="date"
      input-class="input"
      :monday-first="true"
      language="ru"
      @selected="updateDate"
      ref="datePicker"
    />
    <DeleteButton ref="deleteButton" class="delete-button" @delete="$emit('deleteItem')"/>
  </div>
</template>

<script>
  import DeleteButton from 'components/buttons/DeleteButton';
  import Datepicker from 'vuejs-datepicker';
  import moment from 'moment';

  export default {
    props: ['item'],
    components: {
      DeleteButton,
      Datepicker
    },
    methods: {
      updateDate(newDate) {
        this.item.date = moment(newDate).format('DD-MM-YYYY');
      }
    },
    computed: {
      date() {
        return moment(this.item.date, 'DD-MM-YYYY').toDate();
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

  .datepicker {
    margin-left: 1em;
  }
</style>
