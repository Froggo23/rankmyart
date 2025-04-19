const dropzone = document.getElementById('dropzone');
const fileInput = document.getElementById('fileInput');
const preview = document.getElementById('preview');

const stepUpload = document.querySelector('.step-upload');
const stepForm = document.querySelector('.step-form');
const toFormBtn = document.getElementById('toForm');
const backBtn = document.getElementById('backToUpload');

const form = document.querySelector('.upload-form');
const submitBtn = form.querySelector('.submit-btn');
const titleField = form.querySelector('input[name="title"]');
const descField = form.querySelector('textarea[name="description"]');
const tagsField = form.querySelector('input[name="tags"]');

// Drag and drop behavior
dropzone.addEventListener('click', () => fileInput.click());

fileInput.addEventListener('change', () => {
  const file = fileInput.files[0];
  if (file && file.type.startsWith('image/')) {
    const reader = new FileReader();
    reader.onload = e => {
      preview.src = e.target.result;
      preview.style.display = 'block';
    };
    reader.readAsDataURL(file);
  }
});

dropzone.addEventListener('dragover', (e) => {
  e.preventDefault();
  dropzone.classList.add('hover');
});

dropzone.addEventListener('dragleave', () => {
  dropzone.classList.remove('hover');
});

dropzone.addEventListener('drop', (e) => {
  e.preventDefault();
  dropzone.classList.remove('hover');
  const file = e.dataTransfer.files[0];
  if (file && file.type.startsWith('image/')) {
    fileInput.files = e.dataTransfer.files;
    const reader = new FileReader();
    reader.onload = e => {
      preview.src = e.target.result;
      preview.style.display = 'block';
    };
    reader.readAsDataURL(file);
  }
});

// Navigation logic
toFormBtn.addEventListener('click', () => {
  stepUpload.classList.remove('active');
  stepForm.classList.add('active');
});

backBtn.addEventListener('click', () => {
  stepForm.classList.remove('active');
  stepUpload.classList.add('active');
});

// Enable submit only when all fields are filled
form.addEventListener('input', () => {
  const allFilled = titleField.value.trim() && descField.value.trim() && tagsField.value.trim();
  if (allFilled) {
    submitBtn.classList.add('active');
    submitBtn.disabled = false;
  } else {
    submitBtn.classList.remove('active');
    submitBtn.disabled = true;
  }
});
