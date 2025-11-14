# android-inventario-cliente
# 📱 Android Inventory Client - Retrofit & JWT

![Android](https://img.shields.io/badge/Android-SDK_24%2B-3DDC84?style=for-the-badge&logo=android)
![Java](https://img.shields.io/badge/Java-Language-orange?style=for-the-badge&logo=openjdk)
![Retrofit](https://img.shields.io/badge/Retrofit-2.9.0-bc204b?style=for-the-badge&logo=square)
![Gson](https://img.shields.io/badge/Gson-2.10-blue?style=for-the-badge&logo=google)

Cliente móvil nativo desarrollado en Java para la gestión de inventario. Consume una API REST segura (Spring Boot) utilizando **Retrofit**, implementando autenticación basada en **Tokens JWT** y operaciones CRUD completas.

---

## 🚀 Características

* 🔐 **Login Seguro:** Autenticación de usuarios y almacenamiento de Token JWT.
* 📜 **Listado Dinámico:** Visualización de productos con adaptadores personalizados (`ListView`/`RecyclerView`).
* ✏️ **CRUD Móvil:** Crear, Editar y Eliminar productos directamente desde la App.
* 🌐 **Manejo de Red:** Configuración robusta de Retrofit para consumo de API REST.
* 🎨 **UI Personalizada:** Diseño de tarjetas e indicadores de estado visuales.

---

## 🛠️ Configuración del Proyecto

### 1. Requisitos Previos
* Android Studio Koala o superior.
* JDK 11 o 17.
* Un dispositivo físico o emulador (API 24+).
* **Backend corriendo:** [Link a tu repo del backend aquí]

### 2. Configuración de IP (Importante)
Para que la App se conecte a tu API (Local o AWS), debes configurar la `BASE_URL`.

Ve a `MainActivity.java` (o tu clase de configuración de Retrofit) y edita la URL:

```java
// Opción A: Para emulador de Android Studio
.baseUrl("[http://10.0.2.2:8085/api/v1/demoapirestdam235/](http://10.0.2.2:8085/api/v1/demoapirestdam235/)")

// Opción B: Para dispositivo físico (IP de tu PC)
.baseUrl("[http://192.168.1.43:8085/api/v1/demoapirestdam235/](http://192.168.1.43:8085/api/v1/demoapirestdam235/)")

// Opción C: Para AWS RDS (Si ya desplegaste el backend en la nube)
.baseUrl("[http://tu-api-en-aws.com/api/v1/demoapirestdam235/](http://tu-api-en-aws.com/api/v1/demoapirestdam235/)")

#📦 Dependencias Principales (build.gradle)
--Gradle

dependencies {
    // Retrofit para peticiones HTTP
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    
    // Gson para parsing JSON
    implementation 'com.google.code.gson:gson:2.10.1'
}