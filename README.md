# E-commerce App (Test-IT-Rock)

`E-commerce App` es una aplicación Android nativa desarrollada en Kotlin, diseñada para simular una experiencia de compra de productos. La aplicación permite a los usuarios autenticarse, explorar un catálogo de productos y realizar compras simuladas.

El proyecto está construido siguiendo las mejores prácticas de desarrollo de software, con un enfoque en la arquitectura limpia, escalabilidad y una base sólida de testing.

## ✨ Features (Características)

*   **Autenticación de Usuarios:**
    *   Sistema completo de registro y login usando **Email/Contraseña**.
    *   Inicio de sesión simplificado a través de cuentas de **Google (Google Sign-In)**.
    *   La sesión del usuario persiste entre reinicios de la aplicación para una experiencia fluida.
*   **Catálogo de Productos:**
    *   Visualización de un listado de productos disponibles para la compra.
    *   Vista de detalle para cada producto con descripción, precio e imagen.
*   **Simulación de Compra:**
    *   Funcionalidad para que un usuario autenticado pueda "comprar" productos.

## 🛠️ Tecnologías y Librerías Utilizadas

*   **Lenguaje:** [Kotlin](https://kotlinlang.org/)
*   **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) para construir la interfaz de usuario de forma declarativa y moderna.
*   **Arquitectura:**
    *   **Clean Architecture** (dividida en módulos `:app`, `:auth`, `:products`, `:core`).
    *   Patrón **MVVM** (Model-View-ViewModel) en la capa de presentación.
*   **Asincronía:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) y [Flow](https://kotlinlang.org/docs/flow.html) para manejar operaciones en segundo plano y flujos de datos reactivos.
*   **Inyección de Dependencias:** [Hilt](https://dagger.dev/hilt/) para gestionar las dependencias de forma centralizada y facilitar el testing.
*   **Navegación:** [Navigation for Compose](https://developer.android.com/jetpack/compose/navigation) para gestionar los flujos de navegación dentro de la app.
*   **Networking:**
    *   [Retrofit](https://square.github.io/retrofit/) para realizar las llamadas a la API REST (para los productos).
    *   [OkHttp](https://square.github.io/okhttp/) (con `logging-interceptor`) para inspeccionar el tráfico de red.
*   **Autenticación:**
    *   [Firebase Authentication](https://firebase.google.com/docs/auth) (proveedores de Email/Password y Google).
*   **Carga de Imágenes:** [Coil](https://coil-kt.github.io/coil/) para cargar y cachear imágenes de productos de manera eficiente.
*   **Testing:**
    *   **JUnit 4** & **Truth** para aserciones.
    *   **MockK** para la creación de mocks en tests unitarios.
    *   **Turbine** para testear `Flows` de forma robusta.

## 🏛️ Arquitectura y Conceptos Implementados

La aplicación se fundamenta en una arquitectura moderna y modular, siguiendo los principios de **Clean Architecture**.

### 1. Modularización por Capa y Funcionalidad
El proyecto está dividido en módulos de Gradle que separan responsabilidades de forma clara:
*   **`:app` (Capa de Presentación):** Contiene la `MainActivity`, la configuración de la navegación principal y los ViewModels que orquestan las pantallas. Su única responsabilidad es mostrar la UI y delegar las acciones del usuario.
*   **`:auth` (Módulo de Funcionalidad):** Contiene toda la lógica de autenticación (UI de login/registro, Casos de Uso, Repositorio de autenticación). Es un módulo autocontenido.
*   **`:products` (Módulo de Funcionalidad):** Encapsula todo lo relacionado con los productos (UI de listado/detalle, Casos de Uso, Repositorio de productos).
*   **`:core` (Módulo Común):** Contiene código compartido por toda la aplicación.

### 2. Flujo de Datos Unidireccional (UDF)
En la capa de presentación, se sigue un patrón de UDF. El estado (`State`) fluye desde el `ViewModel` hacia la `UI`, y los eventos (`Event`) fluyen desde la `UI` hacia el `ViewModel`. Esto hace que el estado sea predecible y fácil de depurar.

### 3. Navegación Condicional Asíncrona
La `MainActivity` implementa una lógica de arranque inteligente. Muestra una pantalla de carga (`CircularProgressIndicator`) mientras el `NavViewModel` determina si el usuario ya ha iniciado sesión. Basado en este estado, la navegación se inicia en la pantalla principal (`home`) o en el flujo de autenticación (`auth`), garantizando que el usuario siempre vea la pantalla correcta.

## 🚀 Instrucciones para Ejecutar la Aplicación

Para compilar y ejecutar el proyecto, sigue estos pasos:

### Prerrequisitos
*   Android Studio Iguana | 2023.2.1 o superior.
*   JDK 17 o superior.

### 1. Clonar el Repositorio
git clone https://github.com/urielgarrido/itrocktest.git

### 2. Ejecutar la Aplicación
1.  Abre el proyecto en Android Studio.
2.  Espera a que Gradle sincronice todas las dependencias.
3.  Selecciona un dispositivo (emulador o físico).
4.  Haz clic en el botón "Run" (▶️). ¡Y listo!

### 3. Datos para Login
* Email: uriel@uriel.com
* Password: urieluriel

### 4. Datos para Tarjeta
* Número: 1111222233334444
* Titular: Uriel Garrido
* Fecha de expiración: 0930
* CVV: 123 

## 🔮 Puntos de Mejora a Futuro

*   **Caché Local de Productos:** Integrar **Room** para cachear el catálogo de productos. Esto permitiría a la aplicación mostrar productos incluso sin conexión a internet y mejoraría significativamente la velocidad de carga.
*   **Carrito de Compras:** Implementar una funcionalidad completa de carrito de compras, permitiendo a los usuarios añadir, modificar y eliminar productos antes de finalizar la compra.
*   **Tests de UI (Instrumentados):** Añadir tests utilizando el **framework de testing de Compose** para verificar los flujos de usuario completos (ej. "login, añadir producto al carrito, checkout") en un dispositivo real.


