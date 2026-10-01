# SpeedFast - Sistema de Gestión de Entregas

## Descripción

SpeedFast es una aplicación de escritorio desarrollada en Java para gestionar un sistema de entregas.

El sistema permite registrar y administrar pedidos, repartidores y entregas, utilizando una base de datos MySQL para almacenar la información.

La aplicación fue desarrollada utilizando programación orientada a objetos, JDBC y una interfaz gráfica construida con Java Swing.

---

## Objetivo del proyecto

El objetivo del proyecto es implementar un sistema funcional que permita gestionar el ciclo completo de una entrega:

1. Registrar un pedido.
2. Consultar los pedidos registrados.
3. Asignar un repartidor.
4. Iniciar una entrega.
5. Actualizar el estado del pedido.
6. Registrar la entrega realizada.
7. Consultar y administrar las entregas.

---

## Funcionalidades

### Gestión de pedidos

El sistema permite:

- Registrar nuevos pedidos.
- Seleccionar el tipo de pedido:
  - Comida
  - Encomienda
  - Express
- Asignar un estado al pedido.
- Editar pedidos existentes.
- Eliminar pedidos.
- Visualizar los pedidos registrados.
- Consultar el repartidor asociado a una entrega.

### Gestión de repartidores

El sistema permite:

- Registrar repartidores.
- Visualizar los repartidores registrados.
- Editar información de los repartidores.
- Eliminar repartidores.
- Seleccionar repartidores para realizar entregas.

### Gestión de entregas

El sistema permite:

- Registrar entregas asociadas a un pedido y un repartidor.
- Registrar fecha y hora de la entrega.
- Visualizar las entregas registradas.
- Filtrar entregas por pedido.
- Filtrar entregas por repartidor.
- Editar entregas.
- Eliminar entregas.

### Proceso de entrega

El sistema implementa un flujo de entrega utilizando estados:

```text
PENDIENTE
    ↓
EN_REPARTO
    ↓
ENTREGADO

### Arquitectura del proyecto

src/
├── dao/
│   ├── ConexionDB.java
│   ├── PedidoDAO.java
│   ├── RepartidorDAO.java
│   └── EntregaDAO.java
│
├── model/
│   ├── Pedido.java
│   ├── PedidoComida.java
│   ├── PedidoEncomienda.java
│   ├── PedidoExpress.java
│   ├── EstadoPedido.java
│   ├── Repartidor.java
│   ├── Entrega.java
│   ├── ZonaDeCarga.java
│   └── GestorPedidos.java
│
├── vista/
│   ├── VentanaPrincipal.java
│   ├── VentanaRegistroPedido.java
│   ├── VentanaListaPedidos.java
│   ├── VentanaAsignarRepartidor.java
│   ├── VentanaGestionRepartidores.java
│   └── VentanaGestionEntregas.java
│
└── main/
    └── Main.java

### Flujo de uso.

Registrar Pedido
       ↓
Pedido queda PENDIENTE
       ↓
Asignar Repartidor
       ↓
Pedido pasa a EN_REPARTO
       ↓
Se procesa la entrega
       ↓
Se registra la Entrega
       ↓
Pedido pasa a ENTREGADO
