
# Gluten Void API

## Descripción del Proyecto
La API de Gluten Void es un componente crítico en la arquitectura de la aplicación Gluten Void, diseñada para facilitar la gestión de información relacionada con alimentos, recetas y establecimientos libres de gluten. Esta API backend está desarrollada usando Spring Boot y se aloja en Heroku, proporcionando una solución robusta y escalable para la gestión de datos en tiempo real.

## Funcionalidades de la API

### Endpoints
1. **Usuarios**
   - Autenticación y gestión de sesiones.
   - Registro y manejo de perfiles de usuario.
   - Administración de derechos de usuario.

2. **Recetas**
   - Creación, actualización y eliminación de recetas.
   - Validación de contenido para asegurar la ausencia de gluten.

3. **Establecimientos**
   - Registro y actualización de datos de establecimientos.
   - Geolocalización para identificar establecimientos cercanos sin gluten.

4. **Productos**
   - Integración con la API externa OpenFoodFacts para obtener detalles de productos.
   - Funcionalidades de búsqueda y filtrado de productos sin gluten.

## Tecnologías Utilizadas
- **Spring Boot**: Framework para la creación de aplicaciones web y microservicios.
- **Java**: Lenguaje de programación utilizado para el desarrollo del backend.
- **Maven**: Herramienta de gestión y construcción de proyectos.
- **PostgreSQL**: Sistema de gestión de bases de datos relacional.
- **Heroku**: Plataforma de hospedaje en la nube para despliegue y escalado de aplicaciones.

## Seguridad
- **JWT (JSON Web Tokens)**: Utilizado para la autenticación y mantenimiento de sesiones seguras.
- **Spring Security**: Configuraciones de seguridad para proteger endpoints y datos sensibles
