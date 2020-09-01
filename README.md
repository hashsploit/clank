# Clank - A Ratchet & Clank 3 Server Emulator

The primary focus for this server emulator is for [UYA Online](https://uyaonline.com/),
however this server emulator can be expanded on to work for other games that use
SCE-RT/Medius as well.

By emulating the SCE-RT (RTIME) Medius server stack (which is normally
divided into 4-8 servers) we are able to communicate with PS2 clients.

This server aims to be feature rich, modular, and fast, therefore some
components of Medius are merged together.

Join the [UYA Online Discord](https://discord.gg/mUQzqGu) server for updates.


## Features

Emulator features that are complete will be checked, features that are still in progress or planned are un-checked.

- [x] Modular design.
- [x] Inbound handlers for incoming packets.
- [ ] Oubound handlers for outgoing packets.
- [ ] SCE-RT encryption/decryption.
- [ ] SCE-RT packet splitting.
- [ ] SCE-RT app packet handling (structs).
- [ ] Lua sandbox.
- [ ] Lua scripting API.
- [ ] Send server "system messages" to clients.
- [ ] Emulates Medius Authentication Server (MAS).
- [ ] Emulates Medius Lobby Server (MLS).
- [ ] Configurable EULA screen.
- [ ] Configurable Announcements screen.
- [ ] Database and API integration.
- [ ] Server operator chat commands.
- [ ] Configurable player server operators.
- [ ] MPS+DME integrated server.
- [ ] DME Proxy integration for P2P titles anti-cheat and in-game modifications.
- [x] Discord Webhook message queue.
- [x] Non-blocking async handling of incoming data. 


## Clank Components

This server emulator is divided into 2 services:
- **The Medius Authentication Server (MAS)** is where players initially login using
  an existing profile and get a `session token` and `ip address` that is then
  used to login to the Medius Lobby Server.
- **The Medius Lobby Server (MLS)** is where a majority of players reside when they
  are not in game, chatting, looking for a game, managing clans, or looking at
  stats.

You can read more about these components [here](https://wiki.hashsploit.net/PlayStation_2#Medius).


### MAS (Medius Authentication Server)

The server emulator will act as an authentication server for handling user logins.


### MLS (Medius Lobby Server)

OpenMedius will act as a lobby server for handling out-of game
events, learderboards, chat rooms, game creation, clans and more.


### MPS (Medius Proxy Server)

Used for communicating with DME servers (P2S) / session hosts (P2P).

UYA is a P2S (Peer-to-server) title, therefore clank will integrate the DME server into MPS directly.


### DME Proxy (Anti-Cheat extension for P2P titles)

Due to the vulnerabilities of P2P (Peer-to-peer), this server has
the capability of running a P2S2P (Peer-to-server-to-peer) service
to validate that players (DME session clients) and hosts (DME
session masters) are not cheating.

This service must be enabled in the MLS configuration before it can
be ran.

How it works:
- The MLS will spoof other client's addresses as UDP hole-punching
  is being configured.
- Once the game has started the DME Proxy will begin forwarding
  packets to the correct players and filtering through pre-
  deterimined thresholds on a per-packet basis.
- If a regular player (DME session client) is caught cheating the
  DME Proxy will signal to the MLS of the player cheating, invalidate the player session (disconnecting from game), and finally ban the player.
- If a host-player (DME session master) is caught cheating, the DME
  Proxy will signal to the MLS of the host-player cheating, prematurely end the game, and ban the host-player.


## Commands

Each component of OpenMedius has different built-in commands as
they differ per context and implementation, however you can
always use the `help` command to view all commands available in
the current component being run.

- `exit` - Shutdown the server emulator.
- `help [command]` - Show all commands, or show information about a specific command.
- `clients` - Show all currently connected clients.


### Lua API

Under construction.


## About the SCE-RT/RTIME Protocol

![SCE-RT](sce-rt.png)

SCE-RT is the underlying protocol of Medius titles for the PlayStation 2 and PlayStation 3.
SCE-RT was developed by the acquisition of RTIME Inc. by Sony Computer Entertainment
America for their underlying game networking engine for PlayStation 2 games.

RTIME (1997):
[https://web.archive.org/web/19970720232216/http://www.rtimeinc.com/html/welcome.html](https://web.archive.org/web/19970720232216/http://www.rtimeinc.com/html/welcome.html)

SCE-RT (2001-2006):
[https://web.archive.org/web/20060619133250/http://www.sce-rt.scea.com/](https://web.archive.org/web/20060619133250/http://www.sce-rt.scea.com/)

Read more about reverse-engineering the SCE-RT/Medius protocol here:
[https://wiki.hashsploit.net/PlayStation_2#Medius](https://wiki.hashsploit.net/PlayStation_2#Medius)