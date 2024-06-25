    document.addEventListener("DOMContentLoaded", function() {
        console.log("JavaScript is loaded.");

        document.querySelectorAll("form[id^='delete-form-']").forEach(form => {
            form.addEventListener("submit", function(event) {
                event.preventDefault();
                const questionId = this.querySelector('input[name="questionId"]').value;
                console.log("Deleting question with ID:", questionId);

                fetch(`/question/delete?questionId=${questionId}`, {
                    method: 'POST'
                })
                .then(response => {
                    console.log("Response status:", response.status);
                    return response.json().then(data => ({
                        status: response.status,
                        body: data
                    }));
                })
                .then(data => {
                    console.log("Response data:", data);
                    if (data.body.redirectUrl) {
                        window.location.href = data.body.redirectUrl;
                    } else if (data.body.message) {
                        alert(data.body.message);
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert("Erreur de suppression de la question.");
                });
            });
        });
    });