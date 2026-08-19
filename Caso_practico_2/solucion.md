# SOLUCION — Caso Práctico 2 BiblioApp

## 1. Gestión de préstamos

Para implementar los préstamos se creó la entidad de prestamo, la cual se relaciona con libro
y usuario mediante las relaciones meany to one vistas en clase.

Dado que un usuario puede pedir varios prestamos mientras tenga una cuenta activa, adicional como un libro puede prestarse a varias personas se va poder ver en varios prestamos, se tomo en cuenta que cada registro de prestamo pertenezca a un solo usuario con su respectivo libro. 


La logica del prestamo es sencilla, primero tiene que validar que exista una copia disponible para entregar, luego se toma la fecha del prestamo la cual siempre la va tomar con la fecha actual y a esa fecha se le suma 14 días para que de la fecha limite para entregar el libro.

Es importante mencionar que la fecha de devolucion inicia como null y ya luego cambia, por otro lado las copias disponibles bajan en menos 1 al prestar el libro y cuando ya lo devuelven se suma 1 copia nuevamente para así que el inventario se mantenga.

## 2. Roles y seguridad

Se implementaron dos roles mediante el enum de rol:

Bibliotecario
Lector


Se toma esta decision para evitar un error humano dado que durante el codigo se recicla mucho por lo que se reduce este factor al hacerlo de esta forma y nos ayuda con centrarlizar lo valores.

También se creó una página personalizada para los errores 403 cuando un usuario
intenta ingresar a una función para la cual no posee permisos.

## 3. Consulta JPQL de préstamos atrasados

La consulta de préstamos atrasados se implementó en en la parte de prestamorepository.

La lógica utilizada fue:
La fecha de devolucion va aparecer null dado que no sabemos durante estos 14 días que tiene el usario lo va a devolver o bien si se va atrasar.

Por eso la fecha limite es de suma importancia dado que es la que limita el día maximo a entregar.

Ambas condiciones se unen con and porque un préstamo solamente se considera atrasado
cuando todavía no fue devuelto y además ya pasó su fecha límite.

## 4. API REST

Se implementaron endpoints solicitados 

200 OK: la solicitud de consulta se realizó correctamente.
201 Created: se creó correctamente un nuevo recurso, en este caso un libro.
400 Bad Request: los datos enviados no cumplen las validaciones.
403 Forbidden: el usuario está autenticado pero su rol no tiene permiso.
404 Not Found: el recurso solicitado no existe.

Refencia https://developer.mozilla.org/es/docs/Web/HTTP/Reference/Status
## 5. Decisiones de implementación


Una decision sencilla fue de calcular automaticamente la fecha de devolucion dado que no conlleva tanta programacion y su logica es simple
Como se explico en el curso la logica se utilza en los servicios dado que los controladores se encargan directamente de ejecutar las reglas
