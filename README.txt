Se desarrolla una api usando Java 11 y Spring 2, con los servicios requeridos en la descripción de la prueba y los tests
unitarios sobre los distintos componentes.

Puntos de mejora:
 • Utilizar alguna librería que facilite el mantenimiento de los scripts DDL de base de datos.
    Se pensó en emplear MyBatis, pero se descartó por el tiempo requerido para su configuración, actualmente he
    trabajado en proyectos que empleaban MyBatis (la cual además es sencilla de integrar con Spring), pero no he tenido
    ocasión de configurar un proyecto para su uso.

 • Gestión centralizada de excepciones.
    Se agrega una clase para el manejo de excepciones.

 • Test de integración.
    Se implementan test de integración sobre la capa de persistencia y sobre los endpoints.

 • Poder cachear peticiones.
    Como sucede con MyBatis, se pensó en utilizar Kafka, ya que también es de sencilla integración con Spring
    (KafkaTemplate), pero se descartó por el tiempo requerido para la configuración del proyecto y refactorización del
    mismo.

 • Documentación de la API.
    El proyecto incluye Swagger para la documentación y pruebas de la api.
