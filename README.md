# Student API REST

API REST desarrollada con **Spring Boot**, **Kotlin**, **Gradle**, **Spring Data JPA** y **H2 Database** para la gestión básica de estudiantes.

El proyecto permite realizar las siguientes funcionalidades:

- Crear estudiantes.
- Listar todos los estudiantes registrados.

---

## Tecnologías utilizadas

- Kotlin
- Spring Boot
- Gradle
- Spring Data JPA
- H2 Database
- Postman

---

## Estructura del proyecto

```text
src/main/kotlin/com/pucetec/demo
├── controllers
│   └── StudentController.kt
├── dto
│   └── StudentDto.kt
├── entities
│   └── Student.kt
├── exceptions
│   ├── EmailAlreadyExistsException.kt
│   └── GlobalExceptionHandler.kt
├── mappers
│   └── StudentMapper.kt
├── repositories
│   └── StudentRepository.kt
├── services
│   └── StudentService.kt
└── StudentsApplication.kt
```

---

## Entidad Student

La entidad `Student` contiene únicamente los siguientes campos:

```kotlin
id: Long
name: String
email: String
```

La tabla se almacena en la base de datos H2 con el nombre:

```text
students
```

---

## Configuración de la base de datos H2

El proyecto utiliza una base de datos en memoria H2.

Archivo de configuración:

```text
src/main/resources/application.yaml
```

Configuración utilizada:

```yaml
spring:
  application:
    name: Student

  datasource:
    url: jdbc:h2:mem:Student
    driver-class-name: org.h2.Driver
    username: matezepam
    password:

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

  h2:
    console:
      enabled: true
      path: /h2-console

server:
  port: 8989
```

---

## Cómo ejecutar el proyecto

Desde la terminal, dentro de la carpeta del proyecto, ejecutar:

```bash
./gradlew bootRun
```

En Windows:

```bash
gradlew bootRun
```

También se puede ejecutar directamente desde IntelliJ IDEA usando la clase:

```text
StudentsApplication.kt
```

---

## URL base

La aplicación se ejecuta en el puerto `8989`.

```text
http://localhost:8989
```

---

## Endpoints disponibles

### Crear estudiante

```http
POST /api/students
```

URL completa:

```text
http://localhost:8989/api/students
```

Body en formato JSON:

```json
{
  "name": "Mateo Salazar",
  "email": "pmsalazare@puce.edu.ec"
}
```

Respuesta esperada:

```json
{
  "id": 1,
  "name": "Mateo Salazar",
  "email": "pmsalazare@puce.edu.ec"
}
```

---

### Listar estudiantes

```http
GET /api/students
```

URL completa:

```text
http://localhost:8989/api/students
```

Respuesta esperada:

```json
[
  {
    "id": 1,
    "name": "Mateo Salazar",
    "email": "pmsalazare@puce.edu.ec"
  }
]
```

---

## Validación de correo repetido

El proyecto incluye una validación para evitar registrar estudiantes con el mismo correo electrónico.

Si se intenta registrar un correo ya existente, la API devuelve una respuesta de error.

Ejemplo:

```json
{
  "message": "Email already exists: El correo pmsalazare@puce.edu.ec ya está registrado.",
  "timestamp": "2026-06-01T22:30:00"
}
```

---

## Consola H2

Para acceder a la consola de H2, abrir en el navegador:

```text
http://localhost:8989/h2-console/
```

Datos de conexión:

```text
JDBC URL: jdbc:h2:mem:Student
User Name: matezepam
Password:
```

La contraseña debe dejarse vacía.

Consulta para verificar los estudiantes registrados:

```sql
SELECT * FROM STUDENTS;
```

---

## Pruebas con Postman

El proyecto incluye una colección de Postman para probar los endpoints de la API.

Ubicación:

```text
postman/clase_6_postman.json
```

La colección contiene las siguientes peticiones:

- `Create Student`
- `Get All Students`

---

## Ejemplo de prueba en Postman

### Create Student

Método:

```text
POST
```

URL:

```text
http://localhost:8989/api/students
```

Body:

```json
{
  "name": "Mateo Salazar",
  "email": "pmsalazare@puce.edu.ec"
}
```

### Get All Students

Método:

```text
GET
```

URL:

```text
http://localhost:8989/api/students
```

---

## Evidencia de funcionamiento

### Ejecución del proyecto

<img width="1919" height="1031" alt="image" src="https://github.com/user-attachments/assets/f42aeba6-b729-4054-afa8-8c93f2147887" />

### Prueba POST en Postman

<img width="1919" height="584" alt="image" src="https://github.com/user-attachments/assets/3b14e541-683b-406c-8f24-a031a1c8f82c" />

### Prueba GET en Postman

<img width="593" height="112" alt="image" src="https://github.com/user-attachments/assets/647e02e2-70d9-4fc2-b256-0195a8890ece" />
<img width="1571" height="753" alt="image" src="https://github.com/user-attachments/assets/5c87bbe5-58fc-41e7-962f-cf9bc6d05469" />

### Consola H2

<img width="805" height="491" alt="image" src="https://github.com/user-attachments/assets/ee42fb62-3136-4881-902f-d79dcc6fc02b" />
<img width="978" height="450" alt="image" src="https://github.com/user-attachments/assets/8643961b-7035-46b4-b757-676507ce091f" />
<img width="901" height="415" alt="image" src="https://github.com/user-attachments/assets/a9bccfa0-ea52-47a5-9c22-0c6af2d467c2" />



---

## Autor

Paulo Salazar

