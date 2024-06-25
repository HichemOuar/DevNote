
    document.getElementById("answerForm").addEventListener("submit", function(event) {
        event.preventDefault();

        const formData = new FormData(this);
        fetch("/question/answer/submit", {
            method: 'POST',
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            if (data.message) {
                alert(data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert("Erreur lors de la soumission de la réponse.");
        });
    });