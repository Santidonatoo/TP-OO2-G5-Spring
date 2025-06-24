# Trabajo Práctico - Spring Framework (Java)

¡Bienvenidos! Este es nuestro trabajo práctico sobre **Spring Framework** de Java, para la materia **Orientación a Objetos II** en la **Universidad Nacional de Lanús**.

En el mismo se realizó un **sistema de turnos en Java** usando el framework **Spring**. Se evaluó el uso de diferentes herramientas de Spring:

- Spring Web  
- Spring Data JPA  
- Thymeleaf  
- Spring Security  
- Java Mail Sender  
- Swagger

---

## Instrucciones para ejecutar el proyecto

En este README vamos a explicar lo necesario para poder utilizar este proyecto en sus computadoras.

---

### Requisitos previos

Lo primero a tener en cuenta son ciertas configuraciones previas al proyecto:

- Versión de Java 21 o superior.  
- Maven 3 o superior.  
- Plugin de Lombok configurado en su IDE.  
- MySQL como base de datos.

---

### Variables de entorno

Luego, hay que asignar las variables de entorno necesarias para que la configuración del archivo `application.yml` esté completa:

- `EMAIL_PASSWORD`: vgcoistvzuipffme  
- `EMAIL_USERNAME`: sistturnosgrupo5@gmail.com  
- `PASSWORD`: Tu usuario local de MySQL  
- `USERNAME`: Tu contraseña local de MySQL  
- `DB_URL`: Acá deberían crear un *schema* en MySQL y poner la URL de la misma. Ejemplo:  
  `"jdbc:mysql://localhost:3306/bd-turnos-tp-oo2-spring"`

---

### Ejecutar la aplicación

Una vez que ya están todas las variables de entorno configuradas, se bootea la aplicación y debería indicar que está abierta en el puerto `8080`.

Abrir el navegador e ir a la siguiente URL:  
[http://localhost:8080](http://localhost:8080)

---

### Usuarios y autenticación

Para registrarse como empleado se puede hacer desde la pantalla de login, pero para crear nuevos servicios, empleados y ubicaciones hay que usar la configuración de administrador. La misma es:

- **Usuario:** admin  
- **Contraseña:** abc1234

---

### Documentación (Swagger)

El Swagger se puede ver en esta dirección una vez que el proyecto esté corriendo de forma local:  
[http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

