# CashTrack

> An android app to track your movements with cash in a quick way.

- [CashTrack](#cashtrack)
- [Project](#project)
  - [Architecture](#architecture)
  - [Libraries](#libraries)
  - [Features](#features)
- [To Do](#to-do)
- [Info](#info)
  - [Known Bugs](#known-bugs)
  - [About](#about)
    - [Developers](#developers)

# Project

## Architecture

// TODO

## Libraries

* Livedata
* Paging
* Room
* Jetpack Navigation
* Timber
* Material Design

## Features
@
// TODO

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
- [ ] Agregar logos posibles para las wallets
- [ ] Completar las tecnologías usadas
- [ ] Implementar algunos test
- [ ] Linkear las librerías usadas en el README.md
- [ ] Implementar que cuando se agregue un nuevo movimiento se scrolle automáticamente hacia el lugar de la timeline donde se encuentra

Opcionales

- [ ] Implementar Koin o algun otra librería para inyección de dependencias
- [ ] Implementar muchos test



# Info

## Known Bugs

 * The first time that the app is started, the default wallet is created but doesn't show up until you recreate the fragment

## About

### Developers

 * Agustín Domínguez
 * Valentina Solange Vispo
