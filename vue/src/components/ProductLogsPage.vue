<template>
  <table class="table is-striped">
    <thead>
    <tr>
      <th width="30%">Имя</th>
      <th width="15%">Место</th>
      <th width="10%">Упаковка</th>
      <th width="10%">Действие</th>
      <th width="20%">Время</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="logEntry in logs">
      <td>{{logEntry.name}}</td>
      <td>{{logEntry.category}}</td>
      <td>{{logEntry.pack}}</td>
      <td>
        <span v-if="logEntry.action === 'add'" class="tag is-success">Добавлен</span>
        <span v-else-if="logEntry.action === 'remove'" class="tag is-danger">Удалён</span>
      </td>
      <td>{{logEntry.time}}</td>
    </tr>
    </tbody>
  </table>
</template>

<script>
  export default {
    computed: {
      logs() {
        return this.$store.getters.productLogs.map(logEntry => ({
          name: logEntry.productType ? logEntry.productType.name : '-',
          category: logEntry.category ? logEntry.category.name : '-',
          pack: logEntry.pack ? logEntry.pack.name : '-',
          action: logEntry.action,
          time: logEntry.time.format('MMMM Do YYYY, H:mm:ss')
        }));
      }
    },
    created() {
      this.$store.dispatch('loadProductLogs');
      this.$store.dispatch('loadCategories');
      this.$store.dispatch('loadPacks');
      this.$store.dispatch('loadProductTypes');
    }
  }
</script>
