package com.example.billarapp.presentation.view

fun verifyRegistrationData(
    fullName: String,
    username: String,
    email: String,
    confirmEmail: String,
    phone: String,
    password: String,
    confirmPassword: String
): Map<String, String?> {
    val errors = mutableMapOf<String, String?>()

    // Verificar nombre completo
    if (fullName.isBlank()) {
        errors["fullName"] = "Ingrese su nombre"
    }

    // Verificar nombre de usuario
    val usernameRegex = Regex("^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%^&*(),.?\":{}|<>_]).{8,}")
    if (!username.matches(usernameRegex)) {
        errors["username"] = "El nombre de usuario debe ser de al menos 8 caracteres, contener al menos una mayúscula, un numero y un carácter especial"
    }

    // Verificar correo
    if (!email.contains("@") || !email.endsWith(".com")) {
        errors["email"] = "Correo no valido"
    }

    // Verificar confirmación de correo
    if (email != confirmEmail) {
        errors["confirmEmail"] = "El correo ingresado no coincide"
    }

    // Verificar número de teléfono
    if (phone.length != 10 || phone.any { !it.isDigit() }) {
        errors["phone"] = "El número debe contener 10 dígitos"
    }

    // Verificar contraseña
    if (password.isBlank()) {
        errors["password"] = "Cree una contraseña"
    }

    // Verificar confirmación de contraseña
    if (password != confirmPassword) {
        errors["confirmPassword"] = "La contraseña no coincide"
    }

    return errors
}