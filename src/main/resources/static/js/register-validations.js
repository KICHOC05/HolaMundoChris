/**
 * register-validations.js
 * Manejo de validaciones en tiempo real y botones de mostrar/ocultar contraseña
 */

document.addEventListener('DOMContentLoaded', function() {
    // Inicializar funcionalidades
    initPasswordToggle();
    initRealTimeValidations();
    initFormValidation();
});

/**
 * Inicializa los botones de mostrar/ocultar contraseña
 */
function initPasswordToggle() {
    const toggleButtons = document.querySelectorAll('.toggle-password');
    
    toggleButtons.forEach(button => {
        button.addEventListener('click', function() {
            const targetId = this.getAttribute('data-target');
            const passwordInput = document.getElementById(targetId);
            const icon = this.querySelector('i');
            
            if (passwordInput.type === 'password') {
                passwordInput.type = 'text';
                icon.className = 'bi bi-eye-slash';
                this.setAttribute('title', 'Ocultar contraseña');
            } else {
                passwordInput.type = 'password';
                icon.className = 'bi bi-eye';
                this.setAttribute('title', 'Mostrar contraseña');
            }
        });
    });
}

/**
 * Inicializa las validaciones en tiempo real
 */
function initRealTimeValidations() {
    const form = document.getElementById('registerForm');
    if (!form) return;
    
    // Validación de nombre (solo letras y espacios)
    const nombreInput = document.getElementById('nombre');
    if (nombreInput) {
        nombreInput.addEventListener('input', function() {
            validateNombre(this);
        });
        nombreInput.addEventListener('blur', function() {
            validateNombre(this);
        });
    }
    
    // Validación de apellido (solo letras y espacios)
    const apellidoInput = document.getElementById('apellido');
    if (apellidoInput) {
        apellidoInput.addEventListener('input', function() {
            validateApellido(this);
        });
        apellidoInput.addEventListener('blur', function() {
            validateApellido(this);
        });
    }
    
    // Validación de email
    const emailInput = document.getElementById('email');
    if (emailInput) {
        emailInput.addEventListener('input', function() {
            validateEmail(this);
        });
        emailInput.addEventListener('blur', function() {
            validateEmail(this);
        });
    }
    
    // Validación de teléfono (solo números, opcional)
    const telefonoInput = document.getElementById('telefono');
    if (telefonoInput) {
        telefonoInput.addEventListener('input', function() {
            validateTelefono(this);
        });
        telefonoInput.addEventListener('blur', function() {
            validateTelefono(this);
        });
    }
    
    // Validación de contraseña en tiempo real
    const passwordInput = document.getElementById('contraseña');
    if (passwordInput) {
        passwordInput.addEventListener('input', function() {
            validatePassword(this);
            validatePasswordMatch();
        });
        passwordInput.addEventListener('blur', function() {
            validatePassword(this);
            validatePasswordMatch();
        });
    }
    
    // Validación de confirmación de contraseña
    const confirmPasswordInput = document.getElementById('confirmarContraseña');
    if (confirmPasswordInput) {
        confirmPasswordInput.addEventListener('input', validatePasswordMatch);
        confirmPasswordInput.addEventListener('blur', validatePasswordMatch);
    }
    
    // Validación de fecha de nacimiento
    const fechaInput = document.getElementById('fechaNacimiento');
    if (fechaInput) {
        fechaInput.addEventListener('change', function() {
            validateFechaNacimiento(this);
        });
        fechaInput.addEventListener('blur', function() {
            validateFechaNacimiento(this);
        });
    }
}

/**
 * Valida el campo nombre
 */
function validateNombre(input) {
    const value = input.value.trim();
    const regex = /^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$/;
    
    if (value === '') {
        showError(input, 'El nombre es obligatorio');
        return false;
    } else if (value.length > 50) {
        showError(input, 'Máximo 50 caracteres');
        return false;
    } else if (!regex.test(value)) {
        showError(input, 'No puede contener números ni caracteres especiales');
        return false;
    } else {
        clearError(input);
        return true;
    }
}

/**
 * Valida el campo apellido
 */
function validateApellido(input) {
    const value = input.value.trim();
    const regex = /^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$/;
    
    if (value === '') {
        showError(input, 'El apellido es obligatorio');
        return false;
    } else if (value.length > 50) {
        showError(input, 'Máximo 50 caracteres');
        return false;
    } else if (!regex.test(value)) {
        showError(input, 'No puede contener números ni caracteres especiales');
        return false;
    } else {
        clearError(input);
        return true;
    }
}

/**
 * Valida el campo email
 */
function validateEmail(input) {
    const value = input.value.trim();
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    
    if (value === '') {
        showError(input, 'El email es obligatorio');
        return false;
    } else if (value.length > 100) {
        showError(input, 'Máximo 100 caracteres');
        return false;
    } else if (!emailRegex.test(value)) {
        showError(input, 'Debe ser un email válido');
        return false;
    } else {
        clearError(input);
        return true;
    }
}

/**
 * Valida el campo teléfono
 */
function validateTelefono(input) {
    const value = input.value.trim();
    const regex = /^$|^[0-9]{7,20}$/;
    
    if (value !== '' && !regex.test(value)) {
        showError(input, 'Debe contener solo números (7 a 20 dígitos) o estar vacío');
        return false;
    } else {
        clearError(input);
        return true;
    }
}

/**
 * Valida la contraseña
 */
function validatePassword(input) {
    const value = input.value;
    
    if (value === '') {
        showError(input, 'La contraseña es obligatoria');
        return false;
    } else if (value.length < 8) {
        showError(input, 'Debe tener al menos 8 caracteres');
        return false;
    } else {
        // Validar complejidad de contraseña
        const hasLowercase = /[a-z]/.test(value);
        const hasUppercase = /[A-Z]/.test(value);
        const hasNumber = /\d/.test(value);
        const hasSpecial = /[@$!%*?&.#_-]/.test(value);
        
        if (!hasLowercase || !hasUppercase || !hasNumber || !hasSpecial) {
            showError(input, 'Debe contener mayúscula, minúscula, número y carácter especial (@$!%*?&.#_-)');
            return false;
        } else {
            clearError(input);
            return true;
        }
    }
}

/**
 * Valida que las contraseñas coincidan
 */
function validatePasswordMatch() {
    const passwordInput = document.getElementById('contraseña');
    const confirmInput = document.getElementById('confirmarContraseña');
    
    if (!passwordInput || !confirmInput) return;
    
    const password = passwordInput.value;
    const confirm = confirmInput.value;
    
    if (confirm === '') {
        showError(confirmInput, 'Debe confirmar la contraseña');
        return false;
    } else if (password !== confirm) {
        showError(confirmInput, 'Las contraseñas no coinciden');
        return false;
    } else {
        clearError(confirmInput);
        return true;
    }
}

/**
 * Valida la fecha de nacimiento
 */
function validateFechaNacimiento(input) {
    const value = input.value;
    
    if (value === '') {
        showError(input, 'La fecha de nacimiento es obligatoria');
        return false;
    }
    
    const fechaNacimiento = new Date(value);
    const hoy = new Date();
    
    if (fechaNacimiento > hoy) {
        showError(input, 'La fecha debe ser en el pasado');
        return false;
    } else {
        clearError(input);
        return true;
    }
}

/**
 * Muestra un error para un campo de entrada
 */
function showError(input, message) {
    clearError(input);
    
    const errorDiv = document.createElement('div');
    errorDiv.className = 'field-error';
    errorDiv.style.color = 'red';
    errorDiv.style.fontSize = '12px';
    errorDiv.style.marginTop = '5px';
    errorDiv.textContent = message;
    
    input.parentNode.appendChild(errorDiv);
    input.style.borderColor = 'red';
}

/**
 * Limpia el error de un campo de entrada
 */
function clearError(input) {
    // Eliminar mensajes de error existentes
    const errorDiv = input.parentNode.querySelector('.field-error');
    if (errorDiv) {
        errorDiv.remove();
    }
    
    // Restaurar borde
    input.style.borderColor = '';
}

/**
 * Inicializa la validación del formulario antes de enviar
 */
function initFormValidation() {
    const form = document.getElementById('registerForm');
    if (!form) return;
    
    form.addEventListener('submit', function(event) {
        // Validar todos los campos
        const isNombreValid = validateNombre(document.getElementById('nombre'));
        const isApellidoValid = validateApellido(document.getElementById('apellido'));
        const isEmailValid = validateEmail(document.getElementById('email'));
        const isTelefonoValid = validateTelefono(document.getElementById('telefono'));
        const isPasswordValid = validatePassword(document.getElementById('contraseña'));
        const isPasswordMatchValid = validatePasswordMatch();
        const isFechaValid = validateFechaNacimiento(document.getElementById('fechaNacimiento'));
        
        // Validar reCAPTCHA
        const recaptchaResponse = grecaptcha.getResponse();
        if (recaptchaResponse.length === 0) {
            alert("Por favor, complete el reCAPTCHA");
            event.preventDefault();
            return false;
        }
        
        // Si algún campo no es válido, prevenir envío
        if (!isNombreValid || !isApellidoValid || !isEmailValid || 
            !isTelefonoValid || !isPasswordValid || !isPasswordMatchValid || 
            !isFechaValid) {
            event.preventDefault();
            return false;
        }
        
        return true;
    });
}

/**
 * Función para validar la fortaleza de la contraseña (opcional)
 */
function checkPasswordStrength(password) {
    let strength = 0;
    
    // Longitud
    if (password.length >= 8) strength++;
    if (password.length >= 12) strength++;
    
    // Complejidad
    if (/[a-z]/.test(password)) strength++;
    if (/[A-Z]/.test(password)) strength++;
    if (/\d/.test(password)) strength++;
    if (/[@$!%*?&.#_-]/.test(password)) strength++;
    
    return strength;
}