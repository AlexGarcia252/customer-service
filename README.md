# Customer Service (Microservicio de Clientes)

Microservicio encargado de la administración y validación de los clientes del banco dentro del sistema de transferencias bancarias. Construido con Quarkus y PostgreSQL bajo una arquitectura síncrona.

## 1. Descripción de la Arquitectura

* **Framework:** Quarkus (Java 21)

* **Capa Web:** RESTEasy Reactive con serialización JSON (Jackson) y Bean Validation.

* **Persistencia:** Hibernate ORM con Panache (patrón Active Record).

* **Base de Datos:** PostgreSQL independiente (`customer_db`).

* **Seguridad:** Autenticación y Autorización basada en JWT (SmallRye JWT) con validación de firmas criptográficas asimétricas (RSA).

* **Observabilidad:** SmallRye Health (Liveness/Readiness), Micrometer Prometheus (`/q/metrics`), y Logs estructurados en JSON con Correlation IDs.

* **Comunicación:** Síncrona vía HTTP/REST.

El servicio almacena el estado y datos identificatorios de cada usuario y expone consultas directas que los demás microservicios (como `account-service`) consumen de forma síncrona.

## 2. Instrucciones de Ejecución

### Prerrequisitos

* Java Development Kit (JDK) 17 o superior.

* PostgreSQL ejecutándose localmente en el puerto `5432` con una base de datos llamada `customer_db`.

### Pasos para iniciar

1. Clonar el repositorio y entrar a la carpeta del proyecto:

   ```
   cd customer-service
   
   ```

2. Configurar credenciales de base de datos en `src/main/resources/application.properties` si difieren de las por defecto (`postgres`/`postgres`).

3. Ejecutar en modo desarrollo:

   ```
   ./mvnw quarkus:dev
   
   ```

   *En Windows (CMD/PowerShell):*

   ```
   mvnw.cmd quarkus:dev
   
   ```

El servicio iniciará en: `http://localhost:8081`

## 3. Endpoints Disponibles

| Método | Endpoint | Descripción | Requiere Auth | 
| ----- | ----- | ----- | ----- | 
| **GET** | `/customers/{id}` | Consulta y validación de cliente por ID (RF1) | No (Validación interna) | 
| **GET** | `/customers` | Listado general de clientes registrados | No | 
| **POST** | `/customers` | Creación de un nuevo cliente | No | 
| **GET** | `/q/health` | Health checks de Liveness y Readiness | No | 
| **GET** | `/q/metrics` | Métricas del servicio en formato Prometheus | No | 

## 4. Observabilidad y Monitoreo

Este microservicio cumple con los estándares de observabilidad cloud-native:

* **Health Checks (`/q/health`):** Incluye validación automática de la conexión a PostgreSQL y un validador personalizado (`CustomerReadinessCheck`) para confirmar la sincronización del núcleo.

* **Métricas (`/q/metrics`):** Expone datos de la JVM, uso de memoria, CPU y tiempos de respuesta HTTP en formato estándar de Prometheus.

* **Logs Estructurados:** Las trazas de la aplicación se emiten en formato JSON (habilitado vía `quarkus.log.console.json=true`), ideales para ser procesadas por sistemas como ELK/Datadog.

## 5. Seguridad (JWT)

Este microservicio está integrado en la cadena de confianza del ecosistema bancario mediante **SmallRye JWT**. Comparte la misma llave pública (`mp.jwt.verify.publickey`) configurada en el orquestador para validar la autenticidad de los tokens en tránsito.

> **🔒 Nota de Seguridad sobre JWT y Credenciales**
> Las llaves criptográficas para la firma de tokens JWT y las credenciales de base de datos incluidas en este repositorio se proveen "hardcodeadas" de manera intencional en el archivo `application.properties`, exclusivamente para facilitar la ejecución, evaluación y pruebas de este reto técnico en un entorno local.
>
> En un entorno de producción real, estos valores jamás se versionan en Git. La arquitectura correcta implica inyectar estos valores en tiempo de despliegue mediante variables de entorno o utilizar un gestor centralizado de secretos (como AWS Secrets Manager o HashiCorp Vault).

### Generación de Token de Prueba

Para probar los endpoints protegidos en el ecosistema (ej. el orquestador de transacciones), puedes generar un token válido utilizando la herramienta integrada de Quarkus Dev UI.
Con cualquier microservicio corriendo, abre en tu navegador:
`http://localhost:8081/q/jwt/generate?roles=ROLE_USER`

## 6. Ejemplos de Uso (cURL)

### Crear un Cliente

```
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

```
{
  "id": 1,
  "name": "Juan Perez",
  "idDocument": "1098765432",
  "email": "juan.perez@test.com",
  "status": "ACTIVE"
}

```

### Consultar Cliente por ID

```
curl -X GET http://localhost:8081/customers/1

```

**Respuesta (200 OK):**

```
{
  "id": 1,
  "name": "Juan Perez",
  "idDocument": "1098765432",
  "email": "juan.perez@test.com",
  "status": "ACTIVE"
}

```

### Consultar Cliente Inexistente (Manejo de Errores)

```
curl -X GET http://localhost:8081/customers/999

```

**Respuesta (404 Not Found):**

```
{
  "status": 404,
  "error": "Cliente con ID 999 no encontrado."
}

```

## 7. Credenciales y Datos de Prueba

### Conexión a Base de Datos Local

* **Host:** `localhost:5432`

* **Base de Datos:** `customer_db`

* **Usuario:** `postgres`

* **Contraseña:** `postgres`

### Clientes Precargados / Datos de Prueba

* **Cliente 1:** ID `1` | Doc: `1098765432` | Juan Perez | `ACTIVE`

* **Cliente 2:** ID `2` | Doc: `1012345678` | Maria Gomez | `ACTIVE`