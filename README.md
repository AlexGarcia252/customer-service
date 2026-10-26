# Customer Service (Microservicio de Clientes)

Microservicio encargado de la administración y validación de los clientes del banco dentro del sistema de transferencias bancarias. Construido con Quarkus y PostgreSQL bajo una arquitectura síncrona.

---

## 1. Descripción de la Arquitectura

* **Framework:** Quarkus (Java 21)
* **Capa Web:** RESTEasy Reactive con serialización JSON (Jackson) y Bean Validation
* **Persistencia:** Hibernate ORM con Panache (patrón Active Record)
* **Base de Datos:** PostgreSQL independiente (`customer_db`)
* **Observabilidad:** SmallRye Health (`/q/health`) y Micrometer Prometheus (`/q/metrics`)
* **Comunicación:** Síncrona vía HTTP/REST

El servicio almacena el estado y datos identificatorios de cada usuario (documento, email, nombre) y expone consultas directas que los demás microservicios (como `account-service`) consumen de forma síncrona para validar la existencia y estado de los clientes.

---

## 2. Instrucciones de Ejecución

### Prerrequisitos
* Java Development Kit (JDK) 17 o superior
* PostgreSQL ejecutándose localmente en el puerto `5432` con una base de datos llamada `customer_db`

### Pasos para iniciar

1. Clonar el repositorio y entrar a la carpeta del proyecto:
   ```bash
   cd customer-service
   ```

2. Configurar credenciales de base de datos en `src/main/resources/application.properties` si difieren de las por defecto (`postgres`/`postgres`).

3. Ejecutar en modo desarrollo:
   ```bash
   ./mvnw quarkus:dev
   ```
   *En Windows (CMD/PowerShell):*
   ```cmd
   mvnw.cmd quarkus:dev
   ```

El servicio iniciará en: `http://localhost:8081`

---

## 3. Endpoints Disponibles

| Método | Endpoint | Descripción | Requiere Auth |
| :--- | :--- | :--- | :--- |
| **GET** | `/customers/{id}` | Consulta y validación de cliente por ID (RF1) | No (Básica/Pública) |
| **GET** | `/customers` | Listado general de clientes registrados | No |
| **POST** | `/customers` | Creación de un nuevo cliente | No |
| **GET** | `/q/health` | Health checks de Liveness y Readiness | No |
| **GET** | `/q/metrics` | Métricas del servicio en formato Prometheus | No |

---

## 4. Ejemplos de Uso (cURL)

### Crear un Cliente
```bash
curl -X POST http://localhost:8081/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan Perez",
    "idDocument": "1098765432",
    "email": "juan.perez@test.com",
    "status": "ACTIVE"
  }'
```

**Respuesta (201 Created):**
```json
{
  "id": 1,
  "name": "Juan Perez",
  "idDocument": "1098765432",
  "email": "juan.perez@test.com",
  "status": "ACTIVE"
}
```

### Consultar Cliente por ID
```bash
curl -X GET http://localhost:8081/customers/1
```

**Respuesta (200 OK):**
```json
{
  "id": 1,
  "name": "Juan Perez",
  "idDocument": "1098765432",
  "email": "juan.perez@test.com",
  "status": "ACTIVE"
}
```

### Consultar Cliente Inexistente (Manejo de Errores)
```bash
curl -X GET http://localhost:8081/customers/999
```

**Respuesta (404 Not Found):**
```json
{
  "error": "NOT_FOUND",
  "message": "Cliente con ID 999 no encontrado."
}
```

---

## 5. Credenciales y Datos de Prueba

### Conexión a Base de Datos Local
* **Host:** `localhost:5432`
* **Base de Datos:** `customer_db`
* **Usuario:** `postgres`
* **Contraseña:** `postgres`

### Clientes Precargados / Datos de Prueba
* **Cliente 1:** ID `1` | Doc: `1098765432` | Juan Perez | `ACTIVE`
* **Cliente 2:** ID `2` | Doc: `1012345678` | Maria Gomez | `ACTIVE`