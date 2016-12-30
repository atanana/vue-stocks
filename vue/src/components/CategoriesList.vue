<template>
  <table class="table">
    <thead>
    <tr>
      <th>Имя</th>
    </tr>
    </thead>
    <tbody>
    <CategoryItem
      v-for="category in categories"
      :category="category"
    />
    </tbody>
  </table>
</template>

<script>
  import CategoryItem from 'components/CategoryItem'

  export default {
    props: ['categories'],
    components: {
      CategoryItem
    }
  }
</script>
