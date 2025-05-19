# Weekend Planner - Proyecto Final
Este proyecto ha sido desarrollado como parte de la asignatura *Desarrollo de Aplicaciones para Ciencia de Datos*, del Grado en Ingeniería en Ciencia de Datos de la Universidad de Las Palmas de Gran Canaria.

## Propuesta de valor
Weekend Planner permite a los usuarios consultar eventos y vuelos de forma integrada, explotando datos reales y almacenados mediante una interfaz sencilla. Su valor reside en combinar dos fuentes externas (Ticketmaster y Amadeus) para crear un plan de fin de semana personalizado, y almacenarlo de forma estructurada para su posterior análisis o explotación.

## Integrantes del equipo
- Claudia Hernández Miranda
- Jaime Rivero Santana

## Repositorio del proyecto
[Enlace al repositorio de GitHub](https://github.com/DACD-Claudia-Jaime/weekendplan.git)

## Tecnologías utilizadas
- Java 21
- Apache ActiveMQ
- SQLite
- Swing (interfaz gráfica)
- Gson

## Arquitectura general del sistema
El sistema está dividido en módulos según los sprints definidos:

### Sprint 1
- event-feeder: consume datos de la API de Ticketmaster y los almacena en la base de datos SQLite.
- flight-feeder: consume datos de la API de Amadeus (requiere token) y los almacena en SQLite.

### Sprint 2
- event-store-builder: se suscribe a los topics de ActiveMQ (TicketmasterEvents, AmadeusFlights) y almacena los mensajes en ficheros .events.

### Sprint 3
- business-unit: explota los datos almacenados, tanto en tiempo real (ActiveMQ) como en diferido (event store y datamart), mediante una interfaz gráfica.

## Justificación de las APIs y del datamart
- *Ticketmaster*: aporta eventos en ciudades clave para planificar actividades.
- *Amadeus*: proporciona vuelos reales que complementan la planificación.
Se usa SQLite como datamart local, permitiendo integración sencilla, persistencia y acceso eficiente.

## Instrucciones de ejecución

### Requisitos previos
- Java 21 instalado y variable JAVA_HOME configurada
- IntelliJ o cualquier IDE con soporte para Maven
- Apache ActiveMQ instalado y funcionando en tcp://localhost:61616
- Acceso a internet

### 1. Clonar el repositorio
bash
git clone https://github.com/usuario/weekend-planner.git


### 2. Arrancar ActiveMQ
bash
cd ruta/a/apache-activemq-6.1.6/bin
activemq.bat start  (Windows)
./activemq start     (Linux/macOS)


### 3. Ejecutar los módulos

#### a) event-feeder
- Ejecutar Main.java del módulo
- Requiere los siguientes argumentos:
  1. API key de Ticketmaster
  2. Secret (si fuera necesario)
  3. Lista de ciudades separadas por comas

*Ejemplo*:
bash
APIKEY_TICKETMASTER SECRETTICKETMASTER Madrid,Barcelona,Amsterdam,Paris


#### b) flight-feeder
- Obtener token de Amadeus:
bash
curl -X POST https://test.api.amadeus.com/v1/security/oauth2/token   -H "Content-Type: application/x-www-form-urlencoded"   -d "grant_type=client_credentials"   -d "client_id=TU_API_KEY"   -d "client_secret=TU_API_SECRET"

- Ejecutar Main.java con dos argumentos:
  1. Token de acceso
  2. Lista de rutas separadas por comas (ej: MAD-BCN,MAD-LHR)

#### c) event-store-builder
- Ejecutar Main.java
- El módulo se suscribe y guarda eventos en eventstore/<topic>/<fecha>.events

#### d) business-unit
- Ejecutar BusinessUnitGUI.java
- Permite consultar eventos por ciudad y vuelos por origen/destino

## Ejemplo de uso
- Buscar eventos en "Barcelona" desde la GUI
- Consultar vuelos entre "MAD" y "LHR"

## Diagramas de clase

### Módulo Event Feeder
![Event Feeder](https://github.com/DACD-Claudia-Jaime/weekendplan/blob/main/diagrams/event-feeder.png?raw=true)

### Módulo Flight Feeder
![Flight Feeder](https://github.com/DACD-Claudia-Jaime/weekendplan/blob/main/diagrams/flight-feeder.png?raw=true)

### Módulo Event Store Builder
![Event Store Builder](https://github.com/DACD-Claudia-Jaime/weekendplan/blob/main/diagrams/event-store-builder.png?raw=true)

### Módulo Business Unit
![Business Unit](https://github.com/DACD-Claudia-Jaime/weekendplan/blob/main/diagrams/business-unit.png?raw=true)


## Principios y patrones de diseño aplicados
- Separación por responsabilidades (SRP)
- Interfaces para desacoplar feeders y stores
- Patrón Publisher/Subscriber con ActiveMQ
- Patrón Adapter para compatibilidad con formatos externos

## Archivos incluidos en el repositorio
- pom.xml para cada módulo
- Archivos .events de ejemplo en la carpeta eventstore/
- Base de datos datamart.db con datos almacenados
- README.md con toda la documentación
