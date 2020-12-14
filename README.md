# Clank - A high-performance SCE-RT Medius server emulator.

This project is an Open Source rewrite and implementation of the
proprietary SCE-RT Medius server stack used in many PlayStation 2:tm:
multiplayer titles. The project was originally intended for
*Ratchet & Clank: Up Your Arsenal* for the [UYA Online](https://uyaonline.com/)
project. However we have decided to redesign it to work for many other
Medius titles.

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
- [x] Emulates Medius Universe Information Server (MUIS).
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



## Clank Components

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

### General configuration

- The **mode** is a *string* and must be either `MEDIUS_UNIVERSE_INFORMATION_SERVER`, `MEDIUS_AUTHENTICATION_SERVER`, `MEDIUS_LOBBY_SERVER`, `NAT_SERVER` or `DME_SERVER`.
- The **log_level** is a *string* and must be one of the following: `FINEST` > `FINER` > `FINE` > `INFO` > `WARN` > `SEVERE`.
- The **address** is a *string* and can be set to a valid IPv4 address or `null` to use the default interface's address. This is the address in which the server will bind to and listen on.
- The **port** is an *integer* and must be valid port from 1025 to 65535, this is the port in which the server will bind to and listen on.

### MAS configuration

The component is an authentication server for handling player logins.

#### **config/mas.conf:**

| Name                     | Type    | Description                                                                    |
|--------------------------|---------|--------------------------------------------------------------------------------|
| encryption               | boolean | Enabled if this server should enable and enforce/require SCE-RT encryption.    |
| capacity                 | integer | Maximum number of concurrent clients this server can support.                  |
| timeout                  | integer | Time in milliseconds of how long a client can idle not sending a echo heartbeat before being automatically disconnected.|
| parent_threads           | integer | Number of threads this server should use for accepting connections into the event-loop.|
| child_threads            | integer | Number of threads to use for processing requests in the event-loop.            |
| max_login_attempts       | integer | Maximum number of failed login attempts from a single IP address before getting blocked for a set number of time.|
| application_id           | integer | This is the App Id of the game to support.                                     |
| mls                      | object  | See **MLS object** below.                                                      |
| nat                      | object  | See **NAT Object** below.                                                      |
| whitelist                | object  | See **Whitelist Object** below.                                                |
| policy                   | string  | The Policy/EULA to send back to the player.                                    |
| announcements            | array   | An array of *string* announcements to send to the player.                      |
| database                 | object  | See **Database Object** below.                                                 |
| system_message           | object  | See **System Message Object** below.                                           |


**MLS Object:**

| Name                     | Type    | Description                                                                      |
|--------------------------|---------|----------------------------------------------------------------------------------|
| address                  | string  | IPv4 address of the MLS server, or `null` to use the current public IPv4 Address.|
| capacity                 | integer | Port of the MLS server.                                                          |


**NAT Object:**

| Name                     | Type    | Description                                                                      |
|--------------------------|---------|----------------------------------------------------------------------------------|
| address                  | string  | IPv4 address of the NAT server, or `null` to use the current public IPv4 Address.|
| capacity                 | integer | Port of the NAT server.                                                          |


**Whitelist Object:**

| Name                     | Type    | Description                                                                         |
|--------------------------|---------|-------------------------------------------------------------------------------------|
| enabled                  | boolean | Enable if you want to only allow the following whitelisted usernames in the server. |
| players                  | array   | An array of *string* usernames that are allowed to authenticate. All other players will not be allowed to authenticate if **enabled** is set to `true`.|


**Database Object:**

| Name     | Type    | Description                                           |
|----------|---------|-------------------------------------------------------|
| host     | string  | The hostname or address of the MySQL/MariaDB server.  |
| database | string  | The database name to use within the DBMS.             |
| username | string  | The username to use to authenticate with the DBMS.    |
| password | string  | The password to use to authenticate with the DBMS.    |


**System Message Object:**

| Name     | Type    | Description                                                                          |
|----------|---------|--------------------------------------------------------------------------------------|
| enabled  | boolean | If set to true, this will send an automatic system message to the player on connect. |
| severity | integer | The message severity. Valid values are `0` to `255`.                                 |
| message  | string  | The message to send. The message may not exceed `1000` characters.                   |







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
