<template>
  <div>
    <SimpleList
      :items="items"
      :newItemPlaceholder="newItemPlaceholder"
      :newItemLabel="newItemLabel"
    />
    <SaveButton @save="$emit('saveItems')"/>
  </div>
</template>

<script>
  import SimpleList from 'components/SimpleList'
  import SaveButton from 'components/buttons/SaveButton'

  export default {
    props: ['items', 'newItemPlaceholder', 'newItemLabel'],
    components: {
      SimpleList,
      SaveButton
    }
  }
</script>
