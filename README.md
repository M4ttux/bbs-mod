# BBS mod

BBS mod is a Minecraft mod for Fabric 1.20.4 and 1.20.1 (works on Forge as well) for creating animations within Minecraft. It has more features than that, but overall its main task is to facilitate making animated content within Minecraft. For more information, see BBS mod's [Modrinth](https://modrinth.com/mod/bbs-mod/) or [CurseForge](https://www.curseforge.com/minecraft/mc-mods/bbs-mod) pages.

This repository is the source code of BBS mod. The `1.20.4` code is in the `master` branch, and `1.20.1` in `1.20.1` branch, which is usually just a merge of the `master` branch.

If you'd like to contribute to BBS mod code-wise, I'm not looking for contributions. **Please fork the repository, and make your own version**.

See `LICENSE.md` for information about the license.

## Fork Roadmap

- ✅ **Implement a "/kill" button for the Actor in the "Damage" keyframe/tracker.**

- Implement a new keyframe/tracker called "Shadow".

- Explore the possibility of adding item equipment functionality to the Model Block.

## 🆕 Implemented Features

### Kill Animation System (Damage = -1)

A complete death animation system has been implemented that exactly simulates the behavior of Minecraft's `/kill` command when using the value **-1** in the damage keyframe.

#### ✨ Features:

- **🔴 Red Flash**: Visual damage effect for 10 ticks
- **🔄 Death Animation**: Gradual entity rotation to 90 degrees over 20 ticks
- **💨 Death Particles**: 15 "poof" particles when animation completes
- **👻 Complete Concealment**: Both entity and shadow disappear at the end
- **✨ No Loops**: Animation executes only once until completion

#### 🎮 Usage:

1. Create a **"Damage"** keyframe on your actor
2. Set the value to **-1** to activate kill
3. The death sequence will execute automatically
4. To reset, change the value to 0 or any positive number

#### ⚙️ Technical Implementation:

- **IEntity Interface**: Methods `getDeathTime()`, `setDeathTime()`, `hasDeathParticlesSpawned()`, `getHealth()`, `setHealth()`
- **StubEntity & MCEntity**: Complete support for preview and gameplay entities
- **BaseFilmController**: Rendering system with death rotation and particles
- **ReplayKeyframes**: Smart logic that prevents infinite loops

#### 🔧 Customization:

Animation duration can be modified by changing the value `20` in:
- `update()` methods of StubEntity and MCEntity (deathTime limit)
- Rotation calculation in BaseFilmController (`/20F` in angle formula)

---

## 🇪🇸 Español

## Hoja de Ruta del Fork

- ✅ **Implementar en el keyframe/tracker de "Damage" un boton para hacer "/kill" al Actor.**

- Implementar un nuevo keyframe/tracker llamado "Shadow".

- Ver si es posible agregar al Model Block la posibilidad de equipar items al modelo.

## 🆕 Funcionalidades Implementadas

### Kill Animation System (Damage = -1)

Se ha implementado un sistema completo de animación de muerte que simula exactamente el comportamiento del comando `/kill` de Minecraft cuando se usa el valor **-1** en el keyframe de damage.

#### ✨ Características:

- **🔴 Flash Rojo**: Efecto visual de daño durante 10 ticks
- **🔄 Animación de Muerte**: Rotación gradual de la entidad a 90 grados durante 20 ticks
- **💨 Partículas de Muerte**: 15 partículas tipo "poof" al completar la animación
- **👻 Ocultación Completa**: La entidad y su sombra desaparecen al finalizar
- **✨ Sin Bucles**: La animación se ejecuta una sola vez hasta completarse

#### 🎮 Uso:

1. Crea un keyframe de **"Damage"** en tu actor
2. Establece el valor en **-1** para activar el kill
3. La secuencia de muerte se ejecutará automáticamente
4. Para resetear, cambia el valor a 0 o cualquier número positivo

#### ⚙️ Implementación Técnica:

- **IEntity Interface**: Métodos `getDeathTime()`, `setDeathTime()`, `hasDeathParticlesSpawned()`, `getHealth()`, `setHealth()`
- **StubEntity & MCEntity**: Soporte completo para entidades de preview y gameplay
- **BaseFilmController**: Sistema de renderizado con rotación de muerte y partículas
- **ReplayKeyframes**: Lógica inteligente que evita bucles infinitos

#### 🔧 Personalización:

La duración de la animación se puede modificar cambiando el valor `20` en:
- Métodos `update()` de StubEntity y MCEntity (límite del deathTime)
- Cálculo de rotación en BaseFilmController (`/20F` en la fórmula de ángulo)

