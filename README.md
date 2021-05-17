# Twitter

Este proyecto se trata de una aplicación de microblogging, con basntantes bases en la red social de Twitter.

Esta aplicacion esta desarrollada con el patrón modelo-vista-controlador.
De esta forma, el código esta dividido en tres carpetas con código que realiza distintas funciones.

-/src/Twitter: En esta carpeta se han definido las dos clases que representan tanto los mensajes como los usuarios de la aplicación. En ellos se definen los atributos que usa la aplicacón, sus funciones para obtener los atributos y para establecerlos. A suvez, tambien se define el DBManager, en el que se han definido todas las funciones que interactuan con la base de datos.

-/src/controllers: En esta carpeta, se almacenan los controladores que van a hacer que la propia aplicación funcione. Cada uno de ellos se encarga de controlar una funcion básica de la aplicacion para asi simplificar el proyecto y su complejidad. En ellos se almacenan en la sesion atributos que se consideren importantes, tambien parámetros que se vayan a usar posteriormente para el desarrollo y las comprobaciones que sean necesarias.

-/jsp/: En esta carpeta se almacenan los ficheros .jsp. Estos ficheros son elementos html en los que se usa java para hacer que sean dinámicos, haciendo que dependiendo de los datos que han preparado los controladores puedan camvbiar la vista de estos para una mejor esperiencia del usuario.

Funcionamiento de la aplicación:

-Login: En el inicio de sesión o login, el usuario introduce sus datos, el controlador los recoge y por medio de una funcion, comprueba si en la BD hay un elemento en su tabala Usuarios, que tenga el mismo nombre y contraseña, en caso afirmativo, devuelve el usuario con todos sus datos, para asi almacenarlo en sesion y poder usarlo,y nos envia a la página principal de la aplicacion, a la que hemos definido como Home. Si no hay ningun usuarios con las característica especificadas, nos devuelve a la página de inicio de sesion.

-Registro: A esta pagina se puede acceder por medio de un enlace en el inicio de sesión. En ella se pide que el usuario rellene con los datos necesarios para crear el usuario. El propio html, por los parametros que e le han pedido, comprueba que datos como la contraseña esten bien introducidos, y que la contraseña no se vean los carácteres para mayor seguridad. Una vez introducidos los datos, el controlador los recoge, por medio de una funcion, comprueba si en la BD  existe algun usuario con alguno de los datos introducidos, y en caso de que si los tenga, se le reenvia al registro. Sino, se añade al a base de datos la informacion del usuario, para futuros accesos y se envia al usuario al inicio de sesión para que se registre.

-Home: Esta página es unicamente accesible para los usuario sque han iniciado sesión. En ella, se nos muestra una lista de los ultimos mensajes que han escrito los usuarios a loq eusigue el usuario. Esos mensajes pueden ser originales de ellos, respuestas a otros mensajes o mensajes que han retransmitido(retweet). Dentro del mensaje, se nos muestra el usuarios que han escrito el mensaje(siendo este nombre un enlace al perfil del usuario), la hora en la quelo realizaron, el propio mensaje que escribieron y tambiern tres botones distintos. El primero, lo que hace es retransmitir el mensaje para que los usuarios que te sigan, puedan llegar a ver el mensaje de otra persona, un boton que te lleva a la conversación y por ultimo un botón con un campo de texto en el que podras escribir una respuesta a ese mensaje.
Dentro de la página tambien podemos encontrarnos con otro cuadro de texto en el que escribir nuuestros mensajes y un saludo de la página que al clickarlo, nos redirige a la página del perfil del propio usuario.

-Profile: En esta página se nos muestra la información del usuario(no se muestran datos importantes como la contyraseña o el correo, por seguridad). Se nos muestra la descripcion que tiene el usuario, si es el perfil del propio usuario, nos dará la opcion de cambiarla; en el caso en el que el perfil no sea del usuario, se hara una comprobacion de si el usuario sigue al usuario de la página del perfil , para mostrar el botón del Follow y Unfollow dependiiendo de si lo sigue o no. 
y al final, tambien se mostrará una lista de mensajes que ha escrito el usuario o mensajes que ha retransmitido

-Conversation:Nos muestra el mensaje principal de una conversación destacado, un cuadro de texto, en el que podemos escribir una respuesdta a ese mensaje y justo debajos las respuestas que ha recibido el mensaje.

En todas las páginas indicadas en la parte superior, menos el registro, Tenemos un cuadr5o de texto con un boton pequeños que nos permite buscar a los usuarios que queramos. Para asi facilitar la búsqueda de usuarios nuevos.
