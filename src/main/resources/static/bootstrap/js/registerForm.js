
    document.addEventListener("DOMContentLoaded", function() {
        document.getElementById("username").addEventListener("input", function() {
            document.getElementById("usernameError").textContent = "";
        });
        document.getElementById("email").addEventListener("input", function() {
            document.getElementById("emailError").textContent = "";
        });
        document.getElementById("password").addEventListener("input", function() {
            document.getElementById("passwordError").textContent = "";
        });
    });

    function registerUser(event) {
        event.preventDefault();
        var formData = new FormData(document.getElementById("registerForm"));
        fetch('/users/register', {
            method: 'POST',
            body: formData
        })
        .then(response => response.json().then(data => ({
            status: response.status,
            body: data
        })))
        .then(data => {
            // Réinitialiser le captcha après chaque tentative
            grecaptcha.reset();

            if (data.status === 200) {
                window.location.href = "/users/login";
            } else {

             // Nettoyage du champs "mot de passe" et des erreurs de validation après une tentative infructueuse
                document.getElementById("password").value = "";
                document.getElementById("errorMessage").textContent = "";
                document.getElementById("usernameError").textContent = "";
                document.getElementById("emailError").textContent = "";
                document.getElementById("passwordError").textContent = "";

                if (data.status === 400) {
                    var errors = data.body;
                    if (Array.isArray(errors)) {
                        errors.forEach(error => {
                            if (error.field === "username") {
                                document.getElementById("usernameError").textContent = error.defaultMessage;
                            } else if (error.field === "email") {
                                document.getElementById("emailError").textContent = error.defaultMessage;
                            } else if (error.field === "password") {
                                document.getElementById("passwordError").textContent = error.defaultMessage;
                            }
                        });
                    } else {
                        errorMessage.textContent = data.body.message;
                    }
                } else {
                    errorMessage.textContent = data.body.message;
                }
            }
        })
        .catch(error => {
            grecaptcha.reset(); // Réinitialiser le captcha en cas d'erreur
            var errorMessage = document.getElementById("errorMessage");
            errorMessage.textContent = "An error occurred: " + error.message;
        });
    }