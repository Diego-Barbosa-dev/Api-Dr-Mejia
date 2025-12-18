
# API REST

## Api Dr Mejía

Este proyecto está compuesto por una Api tipo REST desarrollada principalmente en Spring Boot, Spring Web,
 Spring Data JPA y Spring Security, la app está diseñada principalmente compuesta para poder realizar llamados
a la base de datos (principalmente pensado para Mariadb/MySQL), intentando seguir los lineamientos de una Api REST
 e intentado una estructura simple y concisa para que el manejo de la misma sea bastante eficiente.


## Instalación
___
#### Pre-requisitos:
- Base de datos MariaDB/MySQL instalada:
```shell
sudo apt install mariadb-server
```
- Tener una base de datos ya definida:

```mariadb
CREATE DATABASE IF NOT EXISTS "<db-name>";
```
- Establecer variables de entorno:
```shell
export DB_PLATAFORM="<hibernate-dialect>"
export DB_URL="<your-url>"
export DB_USER="<your-user>"
export DB_PASSWORD="<your-password>"
export DB_DRIVER="<your-db-driver>"
export JWT_SECRET="<your-jwt-secret>"
export EXP_TIME="<your-time>"
```
- Correr la app:
```shell
mvn clean package
```
***
# Árbol del Proyecto
```terminaloutput
src
├── main
│   ├── java
│   │   └── com
│   │       └── drmejia
│   │           └── core
│   │               ├── config
│   │               ├── controllers
│   │               ├── enums
│   │               ├── exceptions
│   │               ├── models
│   │               ├── persistence
│   │               │   ├── entities
│   │               │   └── repository
│   │               ├── security
│   │               │   ├── models
│   │               │   └── services
│   │               └── services
│   │                   ├── implementation
│   │                   └── interfaces
│   └── resources
│       └── static
```
***
## Ópticas Dr Mejia
Como parte de un evento de colaboración con los estudiantes y empresas del sector empresarial Colombiano,
se desarrollaron distintos retos para la creación de aplicaciones para cada empresa en base a sus necesidades
empresariales. Los estudiantes, quienes se inscribían, debían hacerlo en grupos de tres personas para llevar a 
cabo la aplicación, en este caso dicha empresa es Inversiones Mejías S.A.S. la cual colaboró con distintas
universidades para la creación de una aplicación web de Gestión de ordenes de trabajo.

Con esto en mente y los requerimientos dados por la misma empresa y el seguimiento de la Gobernación del Meta
se consiguió crear distintos espacios de trabajo para que los estudiantes pudieran desarrollar la aplicación.

***
# Dedicatoria Especial
***
Muchísimas gracias al profesor *Gilberto Florez Gualacón*, por su tiempo y su enseñanza en la universidad
Santo Tomás, este proyecto se le dedicará a él como forma de respeto hacia él y su memoria. Muchísimas gracias
a uno de los profesores que me apoyó en este proyecto y su memoria quedará en nuestros corazones. 🥀

Q.E.P.D. 

*Si morimos con él, viviremos con él*
---
*2 Timoteo 2:11*

