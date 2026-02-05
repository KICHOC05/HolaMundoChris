// register-validations.js

// Definir límites de caracteres
const LIMITES_CARACTERES = {
    nombre: 50,
    apellido: 50,
    email: 100,
    telefono: 20,
    direccion: 200,
    contraseña: 50
};

document.addEventListener('DOMContentLoaded', function() {
    initValidations();
});

function initValidations() {
    // Configurar fecha máxima (hoy) y mínima (113 años atrás)
    const fechaInput = document.getElementById('fechaNacimiento');
    if (fechaInput) {
        const hoy = new Date();
        const maxDate = new Date(hoy.getFullYear() - 13, hoy.getMonth(), hoy.getDate());
        const minDate = new Date(hoy.getFullYear() - 113, hoy.getMonth(), hoy.getDate());
        
        fechaInput.max = maxDate.toISOString().split('T')[0];
        fechaInput.min = minDate.toISOString().split('T')[0];
        
        fechaInput.addEventListener('change', validarFechaNacimiento);
    }
    
    // Configurar eventos para todos los campos
    setupFieldValidation('nombre', validarNombre);
    setupFieldValidation('apellido', validarApellido);
    setupFieldValidation('email', validarEmail);
    setupFieldValidation('telefono', validarTelefono);
    setupFieldValidation('direccion', validarDireccion);
    setupFieldValidation('contraseña', validarContraseña);
    
    // Agregar contadores de caracteres en tiempo real
    addCharacterCounters();
    
    // Validación especial para confirmación de contraseña
    const confirmPasswordInput = document.getElementById('confirmarContraseña');
    if (confirmPasswordInput) {
        confirmPasswordInput.addEventListener('input', validarCoincidenciaContraseña);
        confirmPasswordInput.addEventListener('blur', validarCoincidenciaContraseña);
    }
    
    // Configurar validación del formulario completo
    const form = document.getElementById('registerForm');
    if (form) {
        form.addEventListener('submit', validarFormularioCompleto);
    }
}

function setupFieldValidation(fieldId, validationFunction) {
    const input = document.getElementById(fieldId);
    if (input) {
        input.addEventListener('input', validationFunction);
        input.addEventListener('blur', validationFunction);
        
        // Prevenir que se escriban más caracteres que el límite
        input.addEventListener('keydown', function(e) {
            if (this.value.length >= LIMITES_CARACTERES[fieldId] && 
                !['Backspace', 'Delete', 'ArrowLeft', 'ArrowRight', 'Tab'].includes(e.key)) {
                e.preventDefault();
                
                // Mostrar notificación visual
                this.style.borderColor = 'red';
                setTimeout(() => {
                    if (this.value.length <= LIMITES_CARACTERES[fieldId]) {
                        this.style.borderColor = '';
                    }
                }, 500);
            }
        });
    }
}

// Agregar contadores de caracteres debajo de cada campo
function addCharacterCounters() {
    Object.keys(LIMITES_CARACTERES).forEach(fieldId => {
        const input = document.getElementById(fieldId);
        if (input) {
            const counterDiv = document.createElement('div');
            counterDiv.id = `${fieldId}-counter`;
            counterDiv.style.fontSize = '12px';
            counterDiv.style.color = '#666';
            counterDiv.style.marginTop = '2px';
            counterDiv.style.textAlign = 'right';
            
            updateCharacterCounter(fieldId);
            
            input.parentNode.appendChild(counterDiv);
            
            input.addEventListener('input', () => updateCharacterCounter(fieldId));
        }
    });
}

// Actualizar contador de caracteres
function updateCharacterCounter(fieldId) {
    const input = document.getElementById(fieldId);
    const counterDiv = document.getElementById(`${fieldId}-counter`);
    
    if (input && counterDiv) {
        const currentLength = input.value.length;
        const maxLength = LIMITES_CARACTERES[fieldId];
        
        counterDiv.textContent = `${currentLength} / ${maxLength} caracteres`;
        
        // Cambiar color según el límite
        if (currentLength > maxLength) {
            counterDiv.style.color = 'red';
            counterDiv.style.fontWeight = 'bold';
        } else if (currentLength > maxLength * 0.8) {
            counterDiv.style.color = 'orange';
        } else {
            counterDiv.style.color = '#666';
        }
    }
}

// Funciones de validación individuales con límites de caracteres
function validarNombre() {
    const input = document.getElementById('nombre');
    const value = input.value.trim();
    const regex = /^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$/;
    const maxLength = LIMITES_CARACTERES.nombre;
    
    if (value === '') {
        mostrarError(input, 'El nombre es obligatorio');
        return false;
    } else if (value.length > maxLength) {
        mostrarError(input, `Máximo ${maxLength} caracteres (tienes ${value.length})`);
        return false;
    } else if (!regex.test(value)) {
        mostrarError(input, 'Solo letras y espacios permitidos');
        return false;
    } else {
        limpiarError(input);
        return true;
    }
}

function validarApellido() {
    const input = document.getElementById('apellido');
    const value = input.value.trim();
    const regex = /^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$/;
    const maxLength = LIMITES_CARACTERES.apellido;
    
    if (value === '') {
        mostrarError(input, 'El apellido es obligatorio');
        return false;
    } else if (value.length > maxLength) {
        mostrarError(input, `Máximo ${maxLength} caracteres (tienes ${value.length})`);
        return false;
    } else if (!regex.test(value)) {
        mostrarError(input, 'Solo letras y espacios permitidos');
        return false;
    } else {
        limpiarError(input);
        return true;
    }
}

function validarEmail() {
    const input = document.getElementById('email');
    const value = input.value.trim();
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const maxLength = LIMITES_CARACTERES.email;
    
    if (value === '') {
        mostrarError(input, 'El email es obligatorio');
        return false;
    } else if (value.length > maxLength) {
        mostrarError(input, `Máximo ${maxLength} caracteres (tienes ${value.length})`);
        return false;
    } else if (!emailRegex.test(value)) {
        mostrarError(input, 'Formato de email inválido');
        return false;
    } else {
        limpiarError(input);
        return true;
    }
}

function validarTelefono() {
    const input = document.getElementById('telefono');
    const value = input.value.trim();
    const regex = /^$|^[0-9]{7,20}$/;
    const maxLength = LIMITES_CARACTERES.telefono;
    
    if (value !== '' && value.length > maxLength) {
        mostrarError(input, `Máximo ${maxLength} caracteres (tienes ${value.length})`);
        return false;
    } else if (value !== '' && !regex.test(value)) {
        mostrarError(input, 'Solo números (7-20 dígitos) o vacío');
        return false;
    } else {
        limpiarError(input);
        return true;
    }
}

function validarDireccion() {
    const input = document.getElementById('direccion');
    const value = input.value.trim();
    const maxLength = LIMITES_CARACTERES.direccion;
    
    // Dirección es opcional, solo validar si tiene contenido
    if (value !== '' && value.length > maxLength) {
        mostrarError(input, `Máximo ${maxLength} caracteres (tienes ${value.length})`);
        return false;
    } else {
        limpiarError(input);
        return true;
    }
}

function validarFechaNacimiento() {
    const input = document.getElementById('fechaNacimiento');
    const value = input.value;
    
    if (value === '') {
        mostrarError(input, 'La fecha de nacimiento es obligatoria');
        return false;
    }
    
    const fechaNacimiento = new Date(value);
    const hoy = new Date();
    const edadMinima = new Date(hoy.getFullYear() - 13, hoy.getMonth(), hoy.getDate());
    const edadMaxima = new Date(hoy.getFullYear() - 113, hoy.getMonth(), hoy.getDate());
    
    if (fechaNacimiento > hoy) {
        mostrarError(input, 'La fecha no puede ser en el futuro');
        return false;
    } else if (fechaNacimiento > edadMinima) {
        mostrarError(input, 'Debes tener al menos 13 años');
        return false;
    } else if (fechaNacimiento < edadMaxima) {
        mostrarError(input, 'Edad no válida (máximo 113 años)');
        return false;
    } else {
        limpiarError(input);
        return true;
    }
}

function validarContraseña() {
    const input = document.getElementById('contraseña');
    const value = input.value;
    const maxLength = LIMITES_CARACTERES.contraseña;
    
    if (value === '') {
        mostrarError(input, 'La contraseña es obligatoria');
        return false;
    } else if (value.length > maxLength) {
        mostrarError(input, `Máximo ${maxLength} caracteres (tienes ${value.length})`);
        return false;
    } else if (value.length < 8) {
        mostrarError(input, 'Mínimo 8 caracteres');
        return false;
    }
    
    // Validar complejidad
    const tieneMinuscula = /[a-z]/.test(value);
    const tieneMayuscula = /[A-Z]/.test(value);
    const tieneNumero = /\d/.test(value);
    const tieneEspecial = /[@$!%*?&.#_-]/.test(value);
    
    if (!tieneMinuscula || !tieneMayuscula || !tieneNumero || !tieneEspecial) {
        mostrarError(input, 'Debe incluir mayúscula, minúscula, número y carácter especial (@$!%*?&.#_-)');
        return false;
    } else {
        limpiarError(input);
        return true;
    }
}

function validarCoincidenciaContraseña() {
    const password = document.getElementById('contraseña').value;
    const confirmInput = document.getElementById('confirmarContraseña');
    const confirmValue = confirmInput.value;
    const maxLength = LIMITES_CARACTERES.contraseña;
    
    if (confirmValue === '') {
        mostrarError(confirmInput, 'Confirma tu contraseña');
        return false;
    } else if (confirmValue.length > maxLength) {
        mostrarError(confirmInput, `Máximo ${maxLength} caracteres (tienes ${confirmValue.length})`);
        return false;
    } else if (password !== confirmValue) {
        mostrarError(confirmInput, 'Las contraseñas no coinciden');
        return false;
    } else {
        limpiarError(confirmInput);
        return true;
    }
}

function validarFormularioCompleto(event) {
    // Validar todos los campos
    const validaciones = [
        validarNombre(),
        validarApellido(),
        validarEmail(),
        validarTelefono(),
        validarDireccion(),
        validarFechaNacimiento(),
        validarContraseña(),
        validarCoincidenciaContraseña()
    ];
    
    // Verificar si todas las validaciones pasaron
    const esFormularioValido = validaciones.every(validacion => validacion === true);
    
    // Validar reCAPTCHA
    const recaptchaResponse = grecaptcha.getResponse();
    if (recaptchaResponse.length === 0) {
        alert("Por favor, complete el reCAPTCHA");
        event.preventDefault();
        return false;
    }
    
    // Si algún campo no es válido, prevenir envío
    if (!esFormularioValido) {
        event.preventDefault();
        
        // Desplazar a primer error
        const primerError = document.querySelector('.js-error');
        if (primerError) {
            primerError.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
        
        alert("Por favor, corrige los errores en el formulario antes de enviar.");
        return false;
    }
    
    return true;
}

// Funciones auxiliares
function mostrarError(input, mensaje) {
    limpiarError(input);
    
    const errorDiv = document.createElement('div');
    errorDiv.className = 'js-error';
    errorDiv.style.color = 'red';
    errorDiv.style.fontSize = '12px';
    errorDiv.style.marginTop = '5px';
    errorDiv.textContent = mensaje;
    
    input.parentNode.appendChild(errorDiv);
    input.style.borderColor = 'red';
}

function limpiarError(input) {
    const errorDiv = input.parentNode.querySelector('.js-error');
    if (errorDiv) {
        errorDiv.remove();
    }
    
    const serverError = input.parentNode.querySelector('[th\\:if]');
    if (!serverError || serverError.children.length === 0) {
        input.style.borderColor = '';
    }
}

function calcularEdad(fechaNacimiento) {
    const hoy = new Date();
    const nacimiento = new Date(fechaNacimiento);
    let edad = hoy.getFullYear() - nacimiento.getFullYear();
    const mes = hoy.getMonth() - nacimiento.getMonth();
    
    if (mes < 0 || (mes === 0 && hoy.getDate() < nacimiento.getDate())) {
        edad--;
    }
    
    return edad;
}

// Función adicional para truncar texto automáticamente si excede el límite
function truncateTextIfNeeded(fieldId) {
    const input = document.getElementById(fieldId);
    if (input) {
        const maxLength = LIMITES_CARACTERES[fieldId];
        if (input.value.length > maxLength) {
            input.value = input.value.substring(0, maxLength);
            
            // Mostrar notificación
            const notification = document.createElement('div');
            notification.textContent = `Texto truncado a ${maxLength} caracteres`;
            notification.style.position = 'fixed';
            notification.style.top = '20px';
            notification.style.right = '20px';
            notification.style.backgroundColor = '#ff9800';
            notification.style.color = 'white';
            notification.style.padding = '10px';
            notification.style.borderRadius = '5px';
            notification.style.zIndex = '1000';
            
            document.body.appendChild(notification);
            
            // Remover notificación después de 3 segundos
            setTimeout(() => {
                notification.remove();
            }, 3000);
            
            // Actualizar contador
            updateCharacterCounter(fieldId);
        }
    }
}

// Opcional: Puedes llamar a truncateTextIfNeeded en eventos blur
document.addEventListener('DOMContentLoaded', function() {
    Object.keys(LIMITES_CARACTERES).forEach(fieldId => {
        const input = document.getElementById(fieldId);
        if (input) {
            input.addEventListener('blur', () => truncateTextIfNeeded(fieldId));
        }
    });
});