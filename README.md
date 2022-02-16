# CashTrack

> Una aplicación de Android para rastrear el historial de gasto de efectivo junto con división de tipos de gastos con sus respectivos límites.

- [CashTrack](#cashtrack)
- [Projecto](#projecto)
  - [Architectura](#architectura)
  - [Librerías](#librerías)
  - [Features](#features)
    - [Paging Data](#paging-data)
    - [SharedViewModel con eventos](#sharedviewmodel-con-eventos)
    - [WalletRepositoryProvider y WalletCache](#walletrepositoryprovider-y-walletcache)
- [To Do](#to-do)
- [Info](#info)
  - [Bugs Conocidos](#bugs-conocidos)
  - [About](#about)
    - [Desarrolladores](#desarrolladores)

# Projecto

## Architectura

Se siguió la arquitectura recomendada por [Android Developers](https://developer.android.com/) con separación de tareas con una capa de UI y una capa de Data. Se implementó [MVVM](https://www.geeksforgeeks.org/mvvm-model-view-viewmodel-architecture-pattern-in-android/) con uso de Livedata.

La aplicación consiste de una única [Activity](app/src/main/java/org/amdoige/cashtrack/mainscreen/MainActivity.kt) que levanta un [Bottom Navigation](https://material.io/components/bottom-navigation), un [SharedViewModel](app/src/main/java/org/amdoige/cashtrack/mainscreen/SharedViewModel.kt), un botón *Add* para agregar elementos y un Fragment Container que intercambia fragmentos basados en el BottomNav.

![home screen](/meta_assets/home_screen_v.0.6.0.png)

El [fragmento de Home](app/src/main/java/org/amdoige/cashtrack/history/ui/movements/HistoryFragment.kt) muestra el balance general guardado, y dos recyclers que muestran los *Movimientos actuales* y las *Billeteras* (de una manera resumida). 

El [fragmento de Billeteras](app/src/main/java/org/amdoige/cashtrack/billfolds/ui/BillfoldsFragment.kt) muestra las billeteras con más detalles y posiblidad de editarlas.

El manejo de datos de hace con una capa de Repositorios que persiste datos en una base de datos de [Room](https://developer.android.com/jetpack/androidx/releases/room)

## Librerías

* Livedata
* Paging
* Room
* Jetpack Navigation
* Timber
* Material Design
* DiffUtils

## Features

### Paging Data

Luego de un considerable tiempo de uso existe la fuerte posibilidad de que la cantidad de moviemientos comience a afectar la eficiencia de la aplicación, no al nivel de UI ya que para eso se usa RecyclerView sino al nivel de Data ya que se tienen que fetchear posiblemente cientos de movimientos cuando el usuario probablemente vea los último 50 a lo mucho) por lo que se implementó la librería de Paginación entre el [MovementsAdapter](/app/src/main/java/org/amdoige/cashtrack/history/ui/movements/adapters/HistoryMovementsAdapter.kt) y el [History Repository](/app/src/main/java/org/amdoige/cashtrack/history/data/HistoryRepository.kt). Esta implementación se basa en la [gúia de paging](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) de android developers.

### [SharedViewModel](/app/src/main/java/org/amdoige/cashtrack/mainscreen/SharedViewModel.kt) con eventos
Es un ViewModel que se encarga de la comunicación entre fragmentos y el MainActivity mediante el uso de las clases [LivedataEvent](/app/src/main/java/org/amdoige/cashtrack/core/classes/LivedataEvent.kt) y [PulseEvent](/app/src/main/java/org/amdoige/cashtrack/core/classes/PulseEvent.kt)

Tanto LivedataEvent como PulseEvent son wrappers de livedata que permiten el push y manejo (o *ack*, ya que sigue en términos gruesos un patrón publish-subscriber) de eventos de data genéricos con todos los beneficios de la implementación de livedata. PulseEvent es simplemente un LivedataEvent de tipo booleano.

### WalletRepositoryProvider y WalletCache
Ya que Room no permite el acceso por referencias de objeto en runtime, y las dos clases centrales del proyectos están relacionadas entre sí, existe una necesidad particular de tener un Repositorio de la clase de Wallets muy rápido y accesible desde distintos módulos, por lo que se implementaron tanto un Provider que guarda una instancia global del repositorio como un Cache para ese repositorio que mantiene actualizados los datos sin tener que hacer tantos fetcheos a la base de datos.

# To Do

- [X] Implementar fetcheo y seteo de la Default Wallet con SharedPreferences
- [X] Implementar una visualización del item Movement en el recycler de Movements
- [X] Implementar una visualización del item Wallet en el recycler de Movements
- [ ] Implementar una visualización del item Wallet en el recycler de Billfolds
- [ ] **Implementar el fragmento de agregar una movimiento**
  - [X] Implementar fragmento básico
  - [ ] Implementar opciones customizables
- [X] Implementar DiffUtils en el RecyclerView de Wallets
- [X] Hacer que el repositorio de Wallets tenga un cache
- [X] Agregar logos posibles para las wallets
- [X] Completar las tecnologías usadas
- [ ] Implementar algunos test
- [X] Linkear las librerías usadas en el README.md
- [ ] Implementar que cuando se agregue un nuevo movimiento se scrolle automáticamente hacia el lugar de la timeline donde se encuentra

Opcionales

- [ ] Implementar Koin o algun otra librería para inyección de dependencias
- [ ] Implementar muchos test
- [ ] Implementar variación de idiomas:
  - [ ] Inglés
  - [ ] Español

Posibles a futuro
- [ ] Mostrar resúmenes de gastos de cada *Wallet*
- [ ] Cambio de temas


# Info

- Versión de desarrollo: **v.0.6.0**
- Última versión estable: **-**

## Bugs Conocidos

 * The first time that the app is started, the default wallet is created but doesn't show up until you recreate the fragment

## About

### Desarrolladores

 * Agustín Domínguez
 * Valentina Solange Vispo
