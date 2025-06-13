Bienvenidos! Esta la primera entrega del grupo 5 sobre Spring framework de java, para la asignatura Orientacion a Objetos 2.
En este README vamos a explicar lo necesario para poder utilizar este proyecto en sus computadoras.

Lo primero a tener en cuenta es ciertas configuraciones de anteriores al proyecto:

- Version de Java 21 o superior.

- Maven 3 o superior.

- Plugin de lombok configurado en su IDE.

- MySQL como Base de Datos.

luego hay que asignar las variables de entorno necesarias para que la configuracion del yml este completa.

- EMAIL_PASSWORD: vgcoistvzuipffme

- EMAIL_USERNAME: sistturnosgrupo5@gmail.com

- PASSWORD: Tu usuario local de mySql

- USERNAME: Tu contraseña local de mySql

- DB_URL: aca deberian crear un schema en mySql y poner la url de la misma, como ejemplo: "jdbc:mysql://localhost:3306/bd-turnos-tp-oo2-spring"

Una vez que ya estan todas las variables de entorno se bootea la aplicacion y deberia decir que esta abierta en el puerto 8080.

Abrir el navegador e ir a la siguiente url: http://localhost:8080

Para registrarse como empleado se puede hacer desde la pantalla de login, pero para crear nuevos servicios, empleados y ubicaciones hay que usar la configuracion de admin. La misma es la siguiente 

- Usuario: admin
  
- Contraseña: abc1234

