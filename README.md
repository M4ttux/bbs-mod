# BBS mod

BBS mod is a Minecraft mod for Fabric 1.20.4 and 1.20.1 (works on Forge as well) for creating animations within Minecraft. It has more features than that, but overall its main task is to facilitate making animated content within Minecraft. For more information, see BBS mod's [Modrinth](https://modrinth.com/mod/bbs-mod/) or [CurseForge](https://www.curseforge.com/minecraft/mc-mods/bbs-mod) pages.

This repository is the source code of BBS mod. The `1.20.4` code is in the `master` branch, and `1.20.1` in `1.20.1` branch, which is usually just a merge of the `master` branch.

If you'd like to contribute to BBS mod code-wise, I'm not looking for contributions. **Please fork the repository, and make your own version**.

See `LICENSE.md` for information about the license.

## Fork Roadmap

- ✅ **Implement a "/kill" button for the Actor in the "Damage" keyframe/tracker.**

- ✅ **Implement a new keyframe/tracker called "Shadow".**

- ✅ **Implement custom inventory selection panel for BBS actor forms.**

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

### Shadow Keyframe/Tracker System

A comprehensive shadow visibility control system has been implemented, allowing precise control over entity shadow rendering through keyframes.

#### ✨ Features:

- **👤 Shadow Toggle**: Control entity shadow visibility with True/False values
- **🎯 Smart UI Detection**: Automatically detects boolean-like keyframes (0/1 values)
- **🔄 Real-time Updates**: Shadow visibility changes instantly during playback
- **🎨 Visual Consistency**: Magenta color matching the Actions tracker
- **⚡ Performance Optimized**: Minimal rendering overhead

#### 🎮 Usage:

1. Create a **"Shadow"** keyframe on your actor
2. Set value to **1** (True) to show shadow or **0** (False) to hide shadow
3. The UI will automatically display as a True/False toggle
4. Shadow visibility updates in real-time during animation playback

#### ⚙️ Technical Implementation:

- **IEntity Interface**: Methods `isShadowVisible()`, `setShadowVisible()`, `getShadowVisibility()`, `setShadowVisibility()`
- **UIDoubleKeyframeFactory**: Smart detection for boolean-like keyframes with specialized toggle interface
- **BaseFilmController**: Integrated shadow rendering logic with keyframe-based control
- **FilmControllerContext**: Centralized shadow visibility management
- **ReplayKeyframes**: Shadow channel support with proper keyframe interpolation

#### 🔧 Customization:

- **Toggle Detection**: Modify detection logic in UIDoubleKeyframeFactory for different boolean patterns
- **Visual Style**: Change tracker color by modifying the COLORS mapping in UIReplaysEditor
- **Default Behavior**: Adjust default shadow visibility in entity constructors

---

### Custom Inventory Selection Panel

A custom inventory selection panel has been implemented for BBS actor forms, providing an intuitive interface for selecting items from the player's inventory without relying on Minecraft's native inventory screens.

#### ✨ Features:

- **📦 Custom UI Panel**: Independent inventory interface with clean, modern design
- **� Complete Inventory Access**: Shows both hotbar (slots 0-8) and main inventory (slots 9-35)
- **🚫 Empty Option**: Dedicated "Clear Selection" option for removing items
- **� Visual Feedback**: Hover effects and clear visual distinction between empty and filled slots
- **⚡ Instant Selection**: Click any item to immediately select it and close the panel
- **🔄 Real-time Updates**: Reflects current player inventory state accurately

#### 🎮 Usage:

1. In any BBS actor form with an item selection field
2. Click the **player icon button** next to the item field
3. A custom inventory panel will open showing your current inventory
4. Click **"Empty"** to clear the selection, or click any item to select it
5. The panel closes automatically after selection

#### ⚙️ Technical Implementation:

- **UIPlayerInventoryPanel**: Custom overlay panel with mathematical layout system
- **UIItemStack Integration**: Player icon button for seamless inventory access
- **Mathematical Positioning**: Precise slot grid layout with consistent spacing
- **Custom Slot Rendering**: Optimized item display with hover effects
- **Inventory Synchronization**: Real-time player inventory access and updates

#### 🔧 Customization:

- **Panel Size**: Modify `PANEL_WIDTH` constant for different panel widths
- **Visual Theme**: Adjust color constants for different visual styles
- **Layout Spacing**: Change `SLOT_SPACING` and `SECTION_SPACING` for different layouts
- **Slot Appearance**: Customize slot colors and hover effects in slot rendering methods

---

## Hoja de Ruta del Fork

- ✅ **Implementar en el keyframe/tracker de "Damage" un boton para hacer "/kill" al Actor.**

- ✅ **Implementar un nuevo keyframe/tracker llamado "Shadow".**

- ✅ **Implementar panel de selección de inventario personalizado para formularios de actores BBS.**

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

---

### Sistema de Keyframe/Tracker Shadow

Se ha implementado un sistema completo de control de visibilidad de sombras, permitiendo control preciso sobre el renderizado de sombras de entidades a través de keyframes.

#### ✨ Características:

- **👤 Toggle de Sombra**: Controla la visibilidad de la sombra de entidad con valores Verdadero/Falso
- **🎯 Detección UI Inteligente**: Detecta automáticamente keyframes tipo booleano (valores 0/1)
- **🔄 Actualizaciones en Tiempo Real**: La visibilidad de la sombra cambia instantáneamente durante la reproducción
- **🎨 Consistencia Visual**: Color magenta que coincide con el tracker Actions
- **⚡ Optimizado en Rendimiento**: Sobrecarga mínima en el renderizado

#### 🎮 Uso:

1. Crea un keyframe de **"Shadow"** en tu actor
2. Establece el valor en **1** (Verdadero) para mostrar sombra o **0** (Falso) para ocultarla
3. La UI mostrará automáticamente un toggle Verdadero/Falso
4. La visibilidad de la sombra se actualiza en tiempo real durante la reproducción de animación

#### ⚙️ Implementación Técnica:

- **IEntity Interface**: Métodos `isShadowVisible()`, `setShadowVisible()`, `getShadowVisibility()`, `setShadowVisibility()`
- **UIDoubleKeyframeFactory**: Detección inteligente para keyframes tipo booleano con interfaz de toggle especializada
- **BaseFilmController**: Lógica de renderizado de sombras integrada con control basado en keyframes
- **FilmControllerContext**: Gestión centralizada de visibilidad de sombras
- **ReplayKeyframes**: Soporte de canal shadow con interpolación adecuada de keyframes

---

### Panel de Selección de Inventario Personalizado

Se ha implementado un panel de selección de inventario personalizado para los formularios de actores BBS, proporcionando una interfaz intuitiva para seleccionar elementos del inventario del jugador sin depender de las pantallas nativas de inventario de Minecraft.

#### ✨ Características:

- **📦 Panel UI Personalizado**: Interfaz de inventario independiente con diseño limpio y moderno
- **🎒 Acceso Completo al Inventario**: Muestra tanto la hotbar (slots 0-8) como el inventario principal (slots 9-35)
- **🚫 Opción Vacía**: Opción dedicada "Limpiar Selección" para remover elementos
- **🎨 Retroalimentación Visual**: Efectos hover y distinción visual clara entre slots vacíos y llenos
- **⚡ Selección Instantánea**: Haz clic en cualquier elemento para seleccionarlo inmediatamente y cerrar el panel
- **🔄 Actualizaciones en Tiempo Real**: Refleja con precisión el estado actual del inventario del jugador

#### 🎮 Uso:

1. En cualquier formulario de actor BBS con un campo de selección de elemento
2. Haz clic en el **botón de icono de jugador** junto al campo de elemento
3. Se abrirá un panel de inventario personalizado mostrando tu inventario actual
4. Haz clic en **"Vacío"** para limpiar la selección, o haz clic en cualquier elemento para seleccionarlo
5. El panel se cierra automáticamente después de la selección

#### ⚙️ Implementación Técnica:

- **UIPlayerInventoryPanel**: Panel overlay personalizado con sistema de layout matemático
- **Integración UIItemStack**: Botón de icono de jugador para acceso seamless al inventario
- **Posicionamiento Matemático**: Layout de grid de slots preciso con espaciado consistente
- **Renderizado de Slots Personalizado**: Visualización optimizada de elementos con efectos hover
- **Sincronización de Inventario**: Acceso y actualizaciones en tiempo real del inventario del jugador

#### 🔧 Personalización:

- **Tamaño del Panel**: Modifica la constante `PANEL_WIDTH` para diferentes anchos de panel
- **Tema Visual**: Ajusta las constantes de color para diferentes estilos visuales
- **Espaciado del Layout**: Cambia `SLOT_SPACING` y `SECTION_SPACING` para diferentes layouts
- **Apariencia de Slots**: Personaliza colores de slots y efectos hover en métodos de renderizado de slots
