# StarvingValley

## Project structure

The project is organized into three main modules: `core` for the core game logic, `android` for Android-specific functionality, and `assets` for game resources such as sprites, maps, and textures. The `core` module implements the main gameplay mechanics and follows the Model-View-Controller (MVC) pattern, with its codebase divided into `models`, `views`, `controllers`, `config`, and `utils`. The `config` package contains shared constants, while `utils` provides utility classes for shared functionality.

`models` manage the game state, logic, and update loop. `views` are responsible for rendering and listening to user input. `controllers` act as intermediaries, translating user interactions from the `views` into game events handled by the `models`.

The `models` directory is further structured using an Entity-Component-System (ECS) architecture:
- `components` store data and state
- `entities` represent game objects composed of components, created using factories
- `systems` operate on entities and update their components
- `events` implement a publish-subscribe pattern for decoupled interaction between systems
- `interfaces` define contracts for data access and callbacks
- `types` contain enums and utility data classes
- `dto` holds data transfer objects used for syncing game state with Firebase


### Project tree

<details open>

```
.
│   .editorconfig
│   .gitattributes
│   .gitignore
│   build.gradle
│   gradle.properties
│   gradlew
│   gradlew.bat
│   local.properties
│   Makefile
│   my-release-key.jks
│   README.md
│   settings.gradle
│
├───android
│   │   AndroidManifest.xml
│   │   build.gradle
│   │   google-services.json
│   │
│   └───src/main/java/io/github/StarvingValley/android
|           AndroidLauncher.java
│           DeviceIdManager.java
│           FirebaseRepository.java
│
├───assets
│   │
│   └───...         # Sprites, maps, textures, and other game assets
├───core
│   │   build.gradle
│   │
│   └───src/main/java/io/github/StarvingValley
│       ├───config
│       │       Config.java
│       │
│       ├───controllers
│       │       FarmController.java
│       │       GameMenuController.java
│       │       InputEventController.java
│       │       JoystickController.java
│       │       StarvingValley.java
│       │       VillageController.java
│       │       VisitFarmController.java
│       │       WorldMapController.java
│       │
│       ├───models
│       │   │   Mappers.java
│       │   │
│       │   ├───components
│       │   │       ActiveWorldEntityComponent.java
│       │   │       AnimationComponent.java
│       │   │       AttackComponent.java
│       │   │       BuildableComponent.java
│       │   │       BuildPreviewComponent.java
│       │   │       ButtonComponent.java
│       │   │       CameraComponent.java
│       │   │       CameraFollowComponent.java
│       │   │       ClickableComponent.java
│       │   │       ClickedComponent.java
│       │   │       CollidableComponent.java
│       │   │       CropTypeComponent.java
│       │   │       CurrentScreenComponent.java
│       │   │       DamageComponent.java
│       │   │       DragEndComponent.java
│       │   │       DraggableComponent.java
│       │   │       DraggingComponent.java
│       │   │       DropComponent.java
│       │   │       DurabilityComponent.java
│       │   │       EconomyComponent.java
│       │   │       EnvironmentCollidableComponent.java
│       │   │       FoodItemComponent.java
│       │   │       GrowthStageComponent.java
│       │   │       HarvestingComponent.java
│       │   │       HiddenComponent.java
│       │   │       HotbarComponent.java
│       │   │       HudComponent.java
│       │   │       HungerComponent.java
│       │   │       InputComponent.java
│       │   │       InventoryComponent.java
│       │   │       InventoryItemComponent.java
│       │   │       InventorySelectedItemComponent.java
│       │   │       InventorySlotComponent.java
│       │   │       InventoryToggleButtonComponent.java
│       │   │       MapRenderComponent.java
│       │   │       PartOfHotbarComponent.java
│       │   │       PickupButtonComponent.java
│       │   │       PickupComponent.java
│       │   │       PlayerComponent.java
│       │   │       PositionComponent.java
│       │   │       PrefabTypeComponent.java
│       │   │       PulseAlphaComponent.java
│       │   │       SelectedHotbarEntryComponent.java
│       │   │       SizeComponent.java
│       │   │       SpeedComponent.java
│       │   │       SpriteComponent.java
│       │   │       SyncComponent.java
│       │   │       SyncDeletionRequestComponent.java
│       │   │       TextComponent.java
│       │   │       TiledMapComponent.java
│       │   │       TileOccupierComponent.java
│       │   │       TimeToGrowComponent.java
│       │   │       TradeableComponent.java
│       │   │       TradingComponent.java
│       │   │       UnsyncedComponent.java
│       │   │       VelocityComponent.java
│       │   │       WorldLayerComponent.java
│       │   │       WorldMapFarmComponent.java
│       │   │
│       │   ├───dto
│       │   │       SyncEntity.java
│       │   │
│       │   ├───entities
│       │   │       BuildPreviewFactory.java
│       │   │       CameraFactory.java
│       │   │       CropFactory.java
│       │   │       EntityFactoryRegistry.java
│       │   │       FoodItemFactory.java
│       │   │       HudFactory.java
│       │   │       InventoryFactory.java
│       │   │       MapFactory.java
│       │   │       PlayerFactory.java
│       │   │       SeedFactory.java
│       │   │       SoilFactory.java
│       │   │       TraderFactory.java
│       │   │       WallFactory.java
│       │   │       WorldMapUserFactory.java
│       │   │
│       │   ├───events
│       │   │       AddItemToInventoryEvent.java
│       │   │       AttackEndedEvent.java
│       │   │       DragEndEvent.java
│       │   │       DragStartEvent.java
│       │   │       EatingButtonPressedEvent.java
│       │   │       EntityAddedEvent.java
│       │   │       EntityEvent.java
│       │   │       EntityPlacedEvent.java
│       │   │       EntityRemovedEvent.java
│       │   │       EntityUpdatedEvent.java
│       │   │       EventBus.java
│       │   │       GameMenuCloseEvent.java
│       │   │       GameMenuOpenEvent.java
│       │   │       InventoryCloseEvent.java
│       │   │       InventoryOpenEvent.java
│       │   │       NotificationEvent.java
│       │   │       PlayerAttackedEntityEvent.java
│       │   │       RemoveItemFromInventoryEvent.java
│       │   │       RespawnEvent.java
│       │   │       ScreenTransitionEvent.java
│       │   │       StealingEvent.java
│       │   │       SyncIdEvent.java
│       │   │       TapEvent.java
│       │   │       TileInputEvent.java
│       │   │       WorldMapFarmClickEvent.java
│       │   │
│       │   ├───interfaces
│       │   │       AuthCallback.java
│       │   │       EntityDataCallback.java
│       │   │       Event.java
│       │   │       PlayerDataRepository.java
│       │   │       PushCallback.java
│       │   │       UserIdsCallback.java
│       │   │
│       │   ├───systems
│       │   │       ActionAnimationSystem.java
│       │   │       AlphaPulseSystem.java
│       │   │       AnimationSystem.java
│       │   │       AttackTimerSystem.java
│       │   │       BuildGridRenderSystem.java
│       │   │       BuildPlacementSystem.java
│       │   │       BuildPreviewSystem.java
│       │   │       CameraSystem.java
│       │   │       CropGrowthSystem.java
│       │   │       DamageSystem.java
│       │   │       DurabilityRenderSystem.java
│       │   │       EatingSystem.java
│       │   │       EnvironmentCollisionSystem.java
│       │   │       EventCleanupSystem.java
│       │   │       FarmToVillageTransitionSystem.java
│       │   │       FirebaseSyncSystem.java
│       │   │       HotbarItemClickSystem.java
│       │   │       HUDButtonPressHandlingSystem.java
│       │   │       HUDButtonPressSystem.java
│       │   │       HudRenderSystem.java
│       │   │       HungerRenderSystem.java
│       │   │       HungerSystem.java
│       │   │       InputCleanupSystem.java
│       │   │       InputSystem.java
│       │   │       InventoryDragSystem.java
│       │   │       InventoryOpenSystem.java
│       │   │       InventorySystem.java
│       │   │       MapRenderSystem.java
│       │   │       MovementSystem.java
│       │   │       PickupButtonSystem.java
│       │   │       PickupSystem.java
│       │   │       RenderSystem.java
│       │   │       RespawnSystem.java
│       │   │       SpriteSystem.java
│       │   │       StealingSystem.java
│       │   │       SyncMarkingSystem.java
│       │   │       TraderClickSystem.java
│       │   │       TradingSystem.java
│       │   │       VelocitySystem.java
│       │   │       WorldMapTransitionSystem.java
│       │   │
│       │   └───types
│       │           ButtonType.java
│       │           GameContext.java
│       │           InventoryInfo.java
│       │           InventoryType.java
│       │           ItemStack.java
│       │           ItemTrade.java
│       │           PrefabType.java
│       │           ScreenType.java
│       │           WorldLayer.java
│       │
│       ├───utils
│       │       AnimationFactory.java
│       │       AnimationUtils.java
│       │       Assets.java
│       │       BuildUtils.java
│       │       EntitySerializer.java
│       │       EventDebugger.java
│       │       InventoryUtils.java
│       │       MapUtils.java
│       │       PlacementRules.java
│       │       ScreenUtils.java
│       │       TextureUtils.java
│       │       TileUtils.java
│       │
│       └───views
│               EventDebugOverlay.java
│               FarmView.java
│               FilteringInputMultiplexer.java
│               GameMenuView.java
│               InputEventAdapter.java
│               JoystickOverlay.java
│               NotificationOverlay.java
│               VillageView.java
│               VisitFarmView.java
│               WorldMapView.java
│
└───gradle/wrapper
        gradle-wrapper.jar
        gradle-wrapper.properties

```

</details>

## Compiling and Running

You can run the game on an Android device by either compiling the app yourself in Android Studio or downloading the prebuilt APK from the GitHub releases.

### Download from Latest Release

You can get the latest APK from the [releases page](https://github.com/StarvingStudents/StarvingValley/releases/latest). Download the file `starving-valley.apk`. If your browser warns you that the file might be dangerous, choose "Download anyway".

### Compile with Android Studio

1. Open the project in Android Studio
2. In the top menu, go to `Build` -> `Build App Bundle(s) / APK(s)` -> `Build APK(s)`.
3. When the build is complete, Android Studio will show a notification. Click "locate" to find the generated APK. The APK will be located in  `android/build/outputs/apk/debug`.

### Installation

Once you have the APK file on your Android device:
1. Tap the APK file
2. When prompted, select **Install**. 
   - If your device blocks the installation, follow the on-screen instructions to allow installation from unknown sources.
3. After installation, you can launch the game like any other app.

### Run in Android Emulator

1. Open Android Studio and start the Android Emulator.
   - If no emulator is set up, go to `Tools` -> `Device Manager` to create one.
2. Click **`Run 'Android'`** in the top menu or press `F5`.

## Deployment
We created a Makefile for taking the friction out of building and deploying. To use it: 
1. Make sure your ANDROID_HOME environment variable points to your SDK
2. Run `make all` to build the game
3. Run `make install-apk` to install the game on your android emulator.
4. In the future you will also be able to deploy the app to Play Store directly by running `make publish-apk`
 

## Credits

Assets from: Sprout Lands by Cup Nooble
https://cupnooble.itch.io/sprout-lands-asset-pack