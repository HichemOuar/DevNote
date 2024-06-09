    function scaleCaptcha() {
        var reCaptchaWidth = 340; // Largeur fixe du widget reCAPTCHA
        var formWidth = document.querySelector('.form-inscription').offsetWidth; // form-inscription est la classe du formulaire

            var captchaScale = formWidth / reCaptchaWidth; // Calculer du facteur de mise à échelle=> plus le formulaire devient petit, plus le catcha devient petit lui aussi
                                                                                                    // car on le scale par un nombre de plus en plus petit
            var captchaElement = document.querySelector('.g-recaptcha');
            if (captchaElement) {
                captchaElement.style.transform = 'scale(' + captchaScale + ')';
                captchaElement.style.transformOrigin = '0 0';
            }

    }

    window.addEventListener('load', scaleCaptcha); // Mise à l'échelle lors du chargement de la page
    window.addEventListener('resize', scaleCaptcha); // Mise à l'échelle lors du redimensionnement de la fenêtre