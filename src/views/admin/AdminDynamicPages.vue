<template>
  <div class="admin-page-editor">
    <div class="header">Administrador de páginas informativas</div>

    <div class="section">
    <select v-model="selectedPage" @change="loadContent">
      <option disabled value="">Selecciona una página</option>
      <option v-for="page in pages" :key="page" :value="page">
        {{ page }}
      </option>
    </select>

    <div v-for="content in contents" :key="content.idContent" class="content-editor">
      <input v-model="content.sectionName" placeholder="Nombre de sección" />
      <input v-model="content.contentTitle" placeholder="Título del contenido" />
      <textarea v-model="content.contentBody" placeholder="Contenido HTML"></textarea>

      <!-- ✅ Campo para subir la imagen (sin vista previa) -->
      <input type="file" @change="handleFileUpload($event, content)" accept="image/*" />

      <select v-model="content.status">
        <option value="PROCESO">Borrador</option>
        <option value="PUBLICADO">Publicado</option>
      </select>

      <button class="edit-button" @click="saveContent(content)">Guardar Cambios</button>
    </div>

    <button class="edit-button" @click="addContent">Añadir nueva sección</button>
  </div>
</div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import {
  getPublishedContent,
  getDraftContent,
  updateContent,
  createContent
} from '../../services/pageContentService';

const selectedPage = ref('');
const pages = ref(['about', 'contact']);
const contents = ref([]);

// 🔹 Cargar contenido desde backend
const loadContent = async () => {
  if (!selectedPage.value) return;
  try {
    const published = await getPublishedContent(selectedPage.value);
    const drafts = await getDraftContent();

    contents.value = [...published, ...drafts.filter(c => c.pageName === selectedPage.value)];
  } catch (error) {
    console.error("Error cargando contenido:", error);
  }
};

// 🔹 Manejar la subida de imágenes (convertir a Base64, sin vista previa)
const handleFileUpload = (event, content) => {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      content.image = e.target.result.split(',')[1]; // Guardar la imagen en Base64
    };
    reader.readAsDataURL(file);
  }
};

// 🔹 Guardar contenido (nueva sección o actualización)
const saveContent = async (content) => {
  try {
    if (content.idContent) {
      await updateContent(content.idContent, content);
    } else {
      content.pageName = selectedPage.value;
      content.modifiedBy = Number(localStorage.getItem('userId'));
      await createContent(content);
    }
    await loadContent();
    alert("Contenido guardado correctamente");
  } catch (error) {
    console.error("Error al guardar contenido:", error);
  }
};

// 🔹 Añadir una nueva sección
const addContent = () => {
  contents.value.push({
    pageName: selectedPage.value,
    sectionName: '',
    contentTitle: '',
    contentBody: '',
    image: null,
    status: 'PROCESO',
    modifiedBy: Number(localStorage.getItem('userId'))
  });
};

onMounted(loadContent);
</script>

<style scoped>
/* Contenedor principal oscuro (similar a tu "Historia") */
.admin-page-editor {
  background: #f9f9f9;
  color: #e0e1dd;          /* Texto verde/menta */
  min-height: 100vh;
  padding: 2rem;
  border-radius: 10px;
  max-width: 900px;
  margin: 0 auto;
}


.section {
  border: 1px solid #45C4B0;
  padding: 15px;
  margin: 10px 0;
  background: #13678a;
  border-radius: 8px;
}


/* Fila de botones en el formulario */
.button-row {
  display: flex;
  gap: 1rem;
  margin-top: 1rem;
}

/* Botones del form */
.btn-save,
.btn-reset {
  padding: 0.6rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.header {
  text-align: center;
  font-size: 22px;
  font-weight: bold;
  background: #45C4B0;
  color: white;
  padding: 12px;
  border-radius: 5px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.4);
}

.btn-save {
  background-color: #DAFDBA;
  color: #012030;
  margin-right: 1rem;
}
.btn-save:hover {
  background-color: #DAFDBA;
}

.btn-reset {
  background-color: #ff8a7d;
  color: #fff;
}
.btn-reset:hover {
  background-color: #ff8a7d;
}

/* Botón Editar */
.btn-edit {
  background-color: #f0ad4e; /* celeste */
}
.btn-delete {
  background-color: #ff8a7d; /* rojo */
}
</style>




