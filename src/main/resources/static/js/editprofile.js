document.addEventListener('DOMContentLoaded', () => {
    const avatarInput = document.getElementById('avatarInput');
    const avatarPreview = document.getElementById('avatarPreview');

    if (avatarInput && avatarPreview) {
        // When the hidden file input changes (i.e., a file is selected)
        avatarInput.addEventListener('change', (event) => {
            const file = event.target.files[0];

            if (file) {
                // Use FileReader to read the file and generate a temporary URL
                const reader = new FileReader();
                reader.onload = (e) => {
                    // Set the preview image's source to the new temporary URL
                    avatarPreview.src = e.target.result;
                };
                reader.readAsDataURL(file);
            }
        });
    }
});