# 📱 RotomPedia (Evaluación DSY1105)

RotomPedia es una aplicación nativa de Android, desarrollada en Kotlin, que sirve como una Pokédex moderna y funcional. Este proyecto fue creado como parte de la Evaluación Parcial 2 de la asignatura "Desarrollo de Aplicaciones Móviles" (DSY1105).

La aplicación implementa un flujo de autenticación (Login y Registro) antes de dar acceso al menú principal. Una vez dentro, el usuario puede explorar la Pokédex Nacional completa, utilizar filtros avanzados, o consultar herramientas de referencia como la tabla de efectividad de tipos.

## ✨ Características Principales

El proyecto está dividido en varias módulos funcionales clave:

### 1. Sistema de Autenticación y Usuarios
* **Registro de Nuevos Usuarios:** Pantalla para la creación de nuevas cuentas (IE 2.2.2).
* **Login de Usuarios:** Formulario de inicio de sesión para usuarios registrados.
* **Gestión de Sesión:** Mantiene al usuario autenticado y maneja el flujo de navegación entre el login y el contenido principal de la app.

### 2. Pokédex Avanzada y Filtros
* **Pokédex Nacional Completa:** Carga y muestra la lista de los 1025 Pokémon utilizando la [PokeAPI](https://pokeapi.co/) (IL 2.5).
* **Barra de Búsqueda Rápida:** Filtra la lista en tiempo real por **nombre** o **ID** del Pokémon.
* **Filtro por Región:** Un menú deslizable (`ModalBottomSheet`) permite filtrar la lista por región (Kanto, Johto, Hoenn, etc.).
* **Gestión de Estado Asíncrono:** La aplicación maneja de forma robusta los estados de **Cargando**, **Éxito** y **Error** en todas las llamadas a la API, usando `ViewModel` y `StateFlow` (IE 2.3.1).
* **Diseño Adaptativo:** La interfaz de usuario soporta completamente el **Modo Claro** y el **Modo Oscuro** del sistema (IE 2.1.1).

### 3. Pantalla de Detalles del Pokémon
Al seleccionar un Pokémon, el usuario accede a una pantalla de detalles con la siguiente información:
* **Descripción del Pokédex:** Obtenida de la API y filtrada para mostrar la versión **en español**.
* **Estadísticas Base:** Muestra los stats (HP, Ataque, Defensa, etc.) con barras de progreso dinámicas que cambian de color (rojo a verde) según el valor.
* **Relaciones de Tipo:** Muestra las **debilidades** y **fortalezas** del Pokémon contra otros tipos.
* **Cadena Evolutiva:** Carga y muestra la línea de evolución completa del Pokémon.

### 4. Herramienta: Tabla de Efectividad de Tipos
* **Pantalla Dedicada:** Se accede desde el menú principal a una "Tabla de Tipos".
* **Scroll 2D:** La pantalla presenta una cuadrícula completa de 18x18 con **scroll horizontal y vertical** para navegar por todas las interacciones de tipo.
* **Rendimiento Instantáneo:** Los datos de la tabla están "hard-codeados" (almacenados localmente) para asegurar una carga instantánea y funcionamiento offline.
* **Diseño Coherente:** Las celdas de la tabla tienen un diseño redondeado, similar a los "chips" de tipo usados en el resto de la aplicación.

## 🛠️ Stack Tecnológico
* **Lenguaje:** Kotlin
* **UI:** Jetpack Compose
* **Arquitectura:** MVVM (Model-View-ViewModel) con una estructura de paquetes limpia (`data`, `ui`, `viewmodel`, `util`).
* **Navegación:** Navigation Compose
* **Asincronía:** Kotlin Coroutines y StateFlow
* **Networking:** Retrofit (para consumir la API)
* **Parsing JSON:** Moshi
* **Carga de Imágenes:** Coil
* **Colaboración:** Git y GitHub (Flujo de Fork y Pull Request) (IE 2.5.1)

## 👨‍💻 Colaboradores

* **[ZorVaK749](https://github.com/ZorVaK749)**: Desarrollo del módulo de Pokédex (Consumo de API, filtros, pantallas de detalles, tabla de tipos y UI/UX general).
* **[ArukuSR](https://github.com/ArukuSR)**: Desarrollo del módulo de Autenticación (Login y Registro).
