# Echo Client
[![Release](https://img.shields.io/badge/release-1.0.0-blue)](https://github.com/Naqsu/Echo-Client)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-brightgreen)](https://www.minecraft.net/)
[![Fabric](https://img.shields.io/badge/Loader-Fabric-lightgrey)](https://fabricmc.net/)

**Advanced Minecraft Utility Client with Integrated Web Interface**
Echo is a modern Fabric-based client designed for version 1.20.1, featuring a unique hybrid architecture. 
It combines high-performance Java core logic with a flexible, web-based graphical user interface powered by MCEF.

This client is intended for educational purposes and server security auditing. 
The developers are not responsible for any misuse or bans resulting from the use of this software on public servers.

-----

# Installation

To build and run Echo Client from source, follow the steps below.

1. **Clone the repository**
```bash
git clone https://github.com/Naqsu/Echo-Client.git
cd Echo-Client
```

2. **Setup the environment**
Ensure you have the MCEF (Minecraft Chromium Embedded Framework) runtime installed in your Minecraft instance, as it is required to render the WebGUI.

3. **Build the project**
Use the Gradle wrapper to compile the client:
```bash
./gradlew build
```

4. **Run the client**
You can launch the client directly via Gradle for testing:
```bash
./gradlew runClient
```

> [!IMPORTANT]
> For the client to work properly, you need Java 17+ and the Fabric Loader for Minecraft 1.20.1.

-----

# Overview

Echo Client stands out by utilizing a Web-to-Java bridge, allowing developers to create stunning interfaces using standard web technologies like HTML5, CSS3, and JavaScript, while maintaining the raw power of Java for game manipulation.

The client features a robust Event Bus system, a modular configuration manager, and a dedicated HotSwap service that allows for real-time GUI updates without restarting the game.

The default keybind for the Web ClickGUI is **Right Shift**.

-----

# Categories

The client logic is divided into several specialized categories. Each module within these categories can be fully customized through the interface or via the command system.

## Combat
Advanced modules designed for combat performance. This includes highly configurable logic for rotation handling and packet-based interactions to ensure maximum efficiency during engagements.

## Movement
Modules that manipulate player velocity and positioning. Includes Flight, Speed, and Sprint. The Flight module supports multiple modes, including Motion-based and Creative-style flight, with adjustable speed parameters.

## Visual
This category manages the rendering of the client. It handles the WebClickGUI injection and 3D world rendering. It also includes the shader pipeline used for visual enhancements and glow effects.

## HUD
The Head-Up Display category manages on-screen information. Echo features a modular HUD system where elements like the Watermark and ArrayList can be positioned and styled independently. The Watermark supports dynamic data such as real-time clocks and versioning.

## Player
Focuses on local player automation, including automated inventory management, chat utilities, and rotation spoofing.

## Scripts
Echo includes a specialized Script Engine. This allows users to load custom logic via JavaScript or Lua, effectively extending the client's functionality without modifying the core JAR file.

-----

# Core Systems

## Event System
The client uses a priority-based Event Bus. Listeners can be registered with different priority levels:
- MONITOR (Highest)
- HIGH
- MEDIUM
- LOW
- LOWEST

## Config Manager
All settings, module states, and keybinds are saved in a structured JSON format. Users can create multiple presets and switch between them instantly using the `.config load <name>` command.

## HotSwap Service
For UI developers, Echo provides a HotSwap service that watches for changes in the `/echo/html/` directory. Any change saved in your IDE will be reflected in the game immediately after a page reload (F5).

## Command System
The client features a command processor triggered by the `.` prefix. 
Available commands include:
- `.bind <module> <key>` - Assigns a module to a specific key.
- `.config <save/load> <name>` - Manages client profiles.
- `.help` - Displays the full list of available commands.
