# Clank - A high-performance SCE-RT Medius server

This project is an Open Source implementation of the
proprietary SCE-RT Medius server stack used in many PlayStation 2
and PlayStation 3 multiplayer titles. The project was originally
intended for *Ratchet & Clank: Up Your Arsenal* for the
[UYA Online](https://uyaonline.com/) project. However we decided
to redesign it to work with many other Medius titles as well.

Our server aims to be feature rich, modular, stable, and **fast**.

Currently this server supports emulating Medius v1.5 - v1.8, however we plan on
adding support for Medius v1.9 and v1.10 (2.10) soon.

Join our [R&C Online Discord community](https://discord.gg/mUQzqGu) for more information.


## Features

Server features that are complete will be checked, features that are still in progress or planned are un-checked.

- [x] Modular design.
- [x] Inbound handlers for incoming packets.
- [x] Oubound handlers for outgoing packets.
- [x] Medius packet handling (Medius structs).
- [x] gRPC back-end communication between servers.
- [x] Emulates Medius Authentication Server (MAS).
- [x] Emulates NAT Server (NAT).
- [x] Emulates Medius Lobby Server (MLS).
- [x] Emulates DME Server (DME).
- [ ] Emulates Medius Universe Information Server (MUIS).
- [ ] SCE-RT encryption/decryption. (Using [medius-crypto](https://github.com/hashsploit/medius-crypto)).
- [x] SCE-RT packet frames.
- [ ] Lua sandbox.
- [ ] Lua scripting API.
- [ ] Database integration (MySQL/MariaDB).
- [x] Simulated database mode.
- [ ] Server operator chat commands.
- [ ] Support for `MEDIUS_MEMORY_POKE` and `MEDIUS_MEMORY_PEEK`.
- [x] Chat.
- [x] Custom Channels and Locations support.
- [x] Configurable player server operators.
- [x] Non-blocking async handling of incoming data.


## Game titles tested so far
The table below shows game titles that have been tested with Clank.
The focus of the table below is based on a title's ability to get from login to getting in a game world.
- **SUCCESS** = Able to get past MAS into MLS, create a game world on DME(P2S2P) or MAS(P2P) and be active in the game world.
- **PARTIAL** = Able to get past MAS into MLS.
- **INCOMPLETE** = Missing components or planned features to get past MAS.
- **UNKNOWN** = The title cannot be tested at the moment. See the description column for more info.

| Name                                                        | Status     | Notes                                          |
|-------------------------------------------------------------|------------|------------------------------------------------|
| Amplitude NTSC-U (2003)                                     | PARTIAL    | Lobby mostly works, MAS is missing P2P.        |
| Jak X: Combat Racing NTSC-U (2005)                          | INCOMPLETE | Missing native MUIS component.                 |
| Jak X: Combat Racing PAL (2005)                             | INCOMPLETE | Missing native MUIS component.                 |
| Killzone NTSC-U (2004)                                      | SUCCESS    | Missing native MUIS component.                 |
| Ratchet & Clank: Up Your Arsenal NTSC-U USA (2004)          | SUCCESS    |                                                |
| Ratchet & Clank: Up Your Arsenal PAL (2004)                 | UNKNOWN    | [medius-crypto](https://github.com/hashsploit/medius-crypto) not implemented yet.|
| Ratchet & Clank: Up Your Arsenal NTSC-K Korean (2004)       | SUCCESS    |                                                |
| Ratchet & Clank: Up Your Arsenal NTSC-U Public Beta (2004)  | PARTIAL    | Malformed payload for `CreateGame1`.           |
| Ratchet & Clank: Up Your Arsenal NTSC-U Prototype (2004)    | PARTIAL    | Malformed payload for `JoinGame`.              |
| Ratchet: Deadlocked NTSC-U (2005)                           | INCOMPLETE | Missing native MUIS component.                 |
| Ratchet: Deadlocked PAL (2005)                              | UNKNOWN    | [medius-crypto](https://github.com/hashsploit/medius-crypto) not implemented yet.|
| Syphon Filter: The Omega Strain NTSC-U (2004)               | PARTIAL    | Lobby mostly works, MAS is missing P2P.        |


## How to build/compile and run Clank

To build clank you will need `maven` and Java 8+ (optionally `graalvm` to compile the
executable jar to a native linux binary using `native-image`)

In order to run Clank you will need to create a JSON configuration file, there are
"example" configuration files located in `config/`, that can be used as a starting
place. For example, make a copy of `config/mas.json.example` named `config/mas.json`.

You can then use `./launch.sh config/mas.json` to run a Clank MAS server.

1. Run `./build.sh` to build Clank (requires maven).
2. Copy an example configuration file `config/mas.json.example` -> `config/mas.json`.
3. Run `./launch.sh config/mas.json`.

If you cloned the repo and are pulling from time-to-time, make note that
any changes to the `src/main/proto/` files will require the protocol buffers
to be recompiled, this can easily be run by running `./compile-protobufs.sh`.


## Clank components

This server supports emulating the following components:
- The **MUIS (Medius Universe Information Server)** is the entrypoint for several
  games that may have "multiverses", this is generally used before MAS. This server
  will reply with an `ip address` to a MAS server. This server is also sometimes
  used for P2P (peer-to-peer) games.
- The **MAS (Medius Authentication Server)** is where players initially login using
  an existing profile and get a `session token` and `ip address` that is then
  used to login to the MLS.
- The **MLS (Medius Lobby Server)** is where a majority of players reside when they
  are not in the game world, chatting, looking for a game, managing clans, adding
  buddies, or looking at stats.
- The **NAT Server (Network Address Translation)** is a server generally used for
  P2P games, however it is seen used in P2S2P (client/server) titles to return
  the client's public IP address to themselves.
- The **DME Server (Distributed Memory Engine)** is the game server, responsible
  for hosting P2S2P (client/server) games. This is generally used in the "staging"
  and "in game" modes in most titles.

You can read more about these components [here](https://wiki.hashsploit.net/PlayStation_2#Medius).

## Configuration

You can read more about the JSON configuration files [here](config/README.md).



## Console commands

Each component of Clank has different built-in commands as
they differ per context and implementation, however you can
always use the `help` command to view all commands available in
the current component being run. Some commands have
`[optional]` and or `<required>` parameters.

For *all* servers:
- `exit` - Shutdown the server emulator.
- `help [command]` - Show all commands, or show information about a specific command.
- `version` - Show the current version of the server emulator.

For *MAS*, *MLS* and *MUIS* servers:
- `broadcast <severity> <message>` - Broadcast a message to all the players connected.


## Lua plugin API

Under construction.


## About the SCE-RT / RTIME / Medius Protocol

SCE-RT is the underlying protocol of Medius titles for the PlayStation 2 and PlayStation 3.
SCE-RT was developed by the acquisition of RTIME Inc. by Sony Computer Entertainment
America for their underlying game networking engine for PlayStation 2 games.

RTIME (1997):
[https://web.archive.org/web/19970720232216/http://www.rtimeinc.com/html/welcome.html](https://web.archive.org/web/19970720232216/http://www.rtimeinc.com/html/welcome.html)

SCE-RT (2001-2006):
[https://web.archive.org/web/20060619133250/http://www.sce-rt.scea.com/](https://web.archive.org/web/20060619133250/http://www.sce-rt.scea.com/)

Read more about reverse-engineering the SCE-RT/Medius protocol here:
[https://wiki.hashsploit.net/PlayStation_2#Medius](https://wiki.hashsploit.net/PlayStation_2#Medius)


## Special thanks

A huge thanks to the following people in the PlayStation 2/PlayStation 3 community that made this project possible.

- **hashsploit** - reverse engineer, developer, documentation, UYA community.
- **FourBolt** - developer, UYA community.
- **Dnawrkshp** - reverse engineer, developer, modder, DL community.
- **No23** - medius broker, dnas broker, reverse engineer, developer.
- **Darkscorpius** - medius broker, reverse engineer, developer.
- **Badger41** - reverse engineer, developer, modder, DL community.
- **tn1** - reverse engineer, documentation.
- **atomic83** - medius broker, developer, documentation.
- **1UP** - medius broker, developer, SOCOM community.
- **K3rber0s** - developer, SOCOM community.
- **JoeyWolf** - tester, Jak X community.
- **Shanzenos** - medius broker, documentation.
- **Foas** - documentation, R&C community.
- **TheLastRar** - developer, tester, providing patches for CLR_DEV9.
- **Thief** - developer, documentation.
- **Derrik ∞ GHz** - medius broker, reverse engineer, developer.
- **Megalia1** - medius broker, documentation.
- **Cristian** - DNAS/DNS host, documentation.
- **shad** - developer, documentation.
