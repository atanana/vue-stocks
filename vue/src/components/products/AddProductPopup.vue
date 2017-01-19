<template>
  <div>
    <AddNewButton label="Добавить продукт" @addNew="showAddProductPopup = true"/>
    <Popup v-if="showAddProductPopup" @okPressed="addProduct" buttonLabel="Добавить">
      <div slot="header">
        <h2>Добавьте продукт</h2>
      </div>

      <div slot="body">
        <label>
          Категория
          <select v-model="categoryId" class="form-control">
            <option
              v-for="category in availableCategories"
              :value="category.id"
            >{{category.name}}
            </option>
          </select>
        </label>

        <label>
          Тип
          <select v-model="productTypeId" class="form-control">
            <option
              v-for="productType in productTypes"
              :value="productType.id"
            >{{productType.name}}
            </option>
          </select>
        </label>

        <label>
          Упаковка
          <select v-model="packId" class="form-control">
            <option
              v-for="pack in packs"
              :value="pack.id"
            >{{pack.name}}
            </option>
          </select>
        </label>
      </div>
    </Popup>
  </div>
</template>

<script>
  import Popup from 'components/Popup';
  import AddNewButton from 'components/buttons/AddNewButton';

  export default {
    props: ['categories', 'productTypes', 'packs'],
    components: {
      Popup,
      AddNewButton
    },
    data() {
      return {
        showAddProductPopup: false,
        categoryId: 0,
        productTypeId: 0,
        packId: 0
      }
    },
    computed: {
      availableCategories() {
        return [{name: 'Без категории'}].concat(this.categories);
      }
    },
    methods: {
      addProduct() {
        this.showAddProductPopup = false;
        this.$store.dispatch('addProduct', {
          categoryId: this.categoryId,
          productTypeId: this.productTypeId,
          packId: this.packId
        });

        this.categoryId = null;
        this.productTypeId = null;
        this.packId = null;
      }
    }
  }
</script>
