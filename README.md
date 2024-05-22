_LOS FAVORITOS PASARAN A SER CAMBIOS FUTUROS_

**<h2>API GLUTEN VOID</h2>** 

Se ha creado una API, posteriormente comunicada con una APP Android, 
la cual consiste en una aplicación destinada a los usuarios vulnerables al gluten 
y facilitarles su bienestar evitando enfermedades como la celiaquía, sensibilidad o alergia a dicha proteina, 
la cual se encuentra principalmente en alimentos tán comunes como el pan, en cereales como el trigo.

Entonces, mediante esta API, se podrá crear una cuenta de usuario, navegar por su perfil, pudiendo localizar estableciemientos o restaurantes
que contengan menús aptos para el usuario, publicar y consultar recetas 
y escanear el código de barras de los productos de un supermercado (cuando se hace la compra, por ejemplo) 
para obtener los ingredientes que contiene un alimento.

Así pues, habrá una pantalla principal de inicio de sesión, con un enlace hacia el formulario de registro.
Una vez se inicie sesión, habrá un menu principal, posiblemente con un listado de recetas o alimentos sin gluten, 
esto se detallará mejor en el proyecto Android.

En esta API tendremos 4 clases + un enum:

<li> <strong>User</strong> : esta clase va a contener los datos necesarios del usuario para configurar su perfil 
e inicio de sesión.</li><br>


<li> <strong>Establishment</strong> : Aquí se aportara toda información relevante para mostrar los restaurantes al usuario,
asi como telefono, dirección, latitud y longitud para marcar puntos en el mapa, entre otras.</li><br>

<li> <strong>Recipe</strong> : Aqui se encuentran las recetas mandadas por los usuarios y consultadas. Pasarán un filtro
para que el administrador si las recetas son validas para ser mostradas.</li><br>

<li> <strong>Product</strong> : Esta clase consta tanto el alimento como los ingredientes que forman este alimento. Aqui
se ha pensado en dar la opción de escanear el codigo de barras de dicho producto para mostrar su información y sobre todo 
si contiene o no gluten.

**<h3>Relación entre clases</h3>**
USER - ESTABLISHMENT - RECIPE - PRODUCT

USER / RECIPE
USER / ESTABLISHMENT
¿RECIPE / PRODUCT?

