
    document.addEventListener("DOMContentLoaded", function() {
        document.getElementById("update-form").addEventListener("submit", function(event) {
            event.preventDefault();
            const formData = new FormData(this);
            fetch(this.action, {
                method: 'POST',
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                if (data.redirectUrl) {
                    window.location.href = data.redirectUrl;
                } else if (data.message) {
                    alert(data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert("Erreur de mise à jour de la question.");
            });
        });
    });