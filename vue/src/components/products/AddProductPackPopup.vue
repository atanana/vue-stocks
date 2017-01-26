<template>
  <div class="add-pack-container">
    <span class="tag is-dark add-pack" @click="showAddPackPopup = true">
      Добавить упаковку
    </span>
    <Popup v-if="showAddPackPopup" @okPressed="addProduct" @close="showAddPackPopup = false"
           buttonLabel="Добавить">
      <div slot="header">
        <h2>Добавьте продукт</h2>
      </div>

      <div slot="body" class="control">
        <label class="label">Упаковка</label>
        <span class="select">
          <!--suppress HtmlFormInputWithoutLabel -->
          <select v-model="packId" class="form-control">
            <option
              v-for="pack in packs"
              :value="pack.id"
            >{{pack.name}}
            </option>
          </select>
        </span>
      </div>
    </Popup>
  </div>
</template>

<script>
  import Popup from 'components/Popup';
  import AddNewButton from 'components/buttons/AddNewButton';

  export default {
    props: ['categoryId', 'productTypeId', 'packs'],
    components: {
      Popup,
      AddNewButton
    },
    data() {
      return {
        showAddPackPopup: false,
        packId: 0
      }
    },
    methods: {
      addProduct() {
        this.showAddPackPopup = false;
        this.$store.dispatch('addProduct', {
          categoryId: this.categoryId,
          productTypeId: this.productTypeId,
          packId: this.packId
        });

        this.packId = null;
      }
    }
  }
</script>

<style>
  .add-pack {
    cursor: pointer;
  }

  .add-pack-container {
    display: inline-block;
  }
</style>
