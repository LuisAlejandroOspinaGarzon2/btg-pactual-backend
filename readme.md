# BTG Pactual - Gestión de Fondos de Inversión

## Descripción

Aplicación que permite a los clientes de BTG Pactual suscribirse y cancelar suscripciones a fondos de inversión, ver su historial de transacciones y manejar su saldo disponible.

## Tecnologías Utilizadas y Justificación

### Backend

- **Java 17 y Spring Boot 3.3.4**

  - **Justificación:** Spring Boot es un framework robusto que facilita la creación de aplicaciones Java empresariales. Proporciona un conjunto de herramientas para desarrollar servicios web RESTful de manera rápida y eficiente.

- **MongoDB**

  - **Justificación:** MongoDB es una base de datos NoSQL orientada a documentos, ideal para manejar datos flexibles y sin esquemas rígidos. Permite almacenar los datos de clientes, fondos y transacciones de forma eficiente.

- **Lombok**

  - **Justificación:** Lombok ayuda a reducir el código boilerplate mediante anotaciones que generan automáticamente getters, setters y otros métodos comunes.

### Testing

- **JUnit 5 y Mockito**

  - **Justificación:** JUnit 5 es el estándar para pruebas unitarias en Java. Mockito permite crear mocks para aislar componentes durante las pruebas, asegurando que los tests sean independientes y confiables.

### Herramientas

- **Maven**

  - **Justificación:** Maven es una herramienta de gestión de proyectos y dependencias que simplifica la construcción y mantenimiento del proyecto.

## Diseño del Modelo de Datos NoSQL

### Colecciones y Estructuras

- **Cliente**
  ```json
  {
    "_id": "string",
    "nombre": "string",
    "email": "string",
    "telefono": "string",
    "saldo": "double"
  }

- **Fondo**
  ```json
  {
    "_id": "string",
    "nombre": "string",
    "monto": "double"
    }


- **Transaccion**
  ```json
    {
  "_id": "string",
  "fondoId": "string",
  "clienteId": "string",
  "tipo": "string",
  "monto": "double",
  "fecha": "datetime"
    }


## Relaciones
### Cliente y Transaccion: Un cliente puede tener múltiples transacciones.
### Fondo y Transaccion: Un fondo puede estar asociado a múltiples transacciones.

## Justificación del Modelo:

### La elección de MongoDB permite almacenar los datos de manera flexible, y las referencias entre documentos (a través de clienteId y fondoId) facilitan las consultas y operaciones necesarias para la aplicación.

## Instrucciones para Ejecutar la Aplicación
### Prerrequisitos:
Java 17 instalado.
Maven instalado.
MongoDB ejecutándose en localhost en el puerto 27017.
Pasos para Ejecutar:

# Clonar el repositorio
git clone (https://github.com/LuisAlejandroOspinaGarzon2/btg-pactual-backend)

# Navegar al directorio del proyecto
cd pactual

# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run

### Endpoints Disponibles:

#### Clientes:

- `GET /api/clientes`: Obtener todos los clientes.
- `POST /api/clientes`: Crear un nuevo cliente.
- `GET /api/clientes/{id}`: Obtener un cliente por ID.
- `PUT /api/clientes/{id}`: Actualizar un cliente.
- `DELETE /api/clientes/{id}`: Eliminar un cliente.

#### Fondos:

- `GET /api/fondos`: Obtener todos los fondos.
- `POST /api/fondos`: Crear un nuevo fondo.
- `GET /api/fondos/{id}`: Obtener un fondo por ID.
- `PUT /api/fondos/{id}`: Actualizar un fondo.
- `DELETE /api/fondos/{id}`: Eliminar un fondo.
- `POST /api/fondos/{id}/suscribir?clienteId={clienteId}`: Suscribir a un cliente a un fondo.
- `POST /api/fondos/{id}/cancelar?clienteId={clienteId}`: Cancelar suscripción de un cliente a un fondo.

#### Transacciones:

- `GET /api/fondos/cliente/{clienteId}/transacciones`: Obtener las transacciones de un cliente.

## Despliegue con AWS CloudFormation

En el directorio raíz del proyecto se incluye el archivo `template.yaml`, que es una plantilla de **AWS CloudFormation** para desplegar la aplicación en AWS de manera automatizada.

### Pasos para Desplegar:

1. **Prerrequisitos:**

   - **Cuenta en AWS** con permisos para crear recursos de CloudFormation, EC2, VPC, etc.
   - **Par de claves (Key Pair)** existente en la región donde vas a desplegar, para acceder a la instancia EC2 vía SSH.
   - **Consola de AWS CLI** instalada y configurada (opcional, si deseas usar la línea de comandos).

2. **Desplegar la Plantilla usando la Consola de AWS:**

   a. Inicia sesión en la **Consola de AWS** y navega a **CloudFormation**.

   b. Haz clic en **"Create stack"** y selecciona **"With new resources (standard)"**.

   c. En **"Specify template"**, elige **"Upload a template file"** y carga el archivo `template.yaml` que se encuentra en el directorio raíz del proyecto.

   d. Haz clic en **"Next"**.

   e. En **"Stack name"**, ingresa un nombre para tu stack, por ejemplo, `btg-pactual-stack`.

   f. En **"Parameters"**, ingresa el nombre de tu Key Pair en el campo `KeyName` (por ejemplo, `key-0c69cf15b275ffc68`).

   g. Haz clic en **"Next"** y configura las opciones adicionales si lo deseas, o deja los valores por defecto.

   h. Revisa los detalles y haz clic en **"Create stack"**.


# Solucion Parte 2 - 20 % 

SELECT DISTINCT c.nombre
FROM cliente c
JOIN inscripcion i ON c.id = i.idCliente
JOIN producto p ON i.idProducto = p.id
JOIN disponibilidad d ON p.id = d.idProducto
JOIN visitan v ON d.idSucursal = v.idSucursal AND c.id = v.idCliente
GROUP BY c.id, c.nombre
HAVING COUNT(DISTINCT d.idSucursal) = COUNT(DISTINCT v.idSucursal);