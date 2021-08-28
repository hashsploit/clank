## Clank general configuration

- The **mode** is a *string* and must one of the following Emulation Modes.
- The **log_level** is a *string* and must be one of the following: `FINEST` > `FINER` > `FINE` > `INFO` > `WARN` > `SEVERE`.
- The **address** is a *string* and can be set to a valid IPv4 address or `null` to use the default interface's address.
  This is the address in which the server will bind to and listen on.
- The **port** is an *integer* and must be valid port from 1025 to 65535,
  this is the port in which the server will bind to and listen on.

### Emulation Modes

| Name                               | Description                                                                    |
|------------------------------------|--------------------------------------------------------------------------------|
| MEDIUS_UNIVERSE_INFORMATION_SERVER | Run an indepedent [MUIS](https://wiki.hashsploit.net/PlayStation_2#Medius_Universe_Information_Server_.28MUIS.29) server.                                              |
| MEDIUS_AUTHENTICATION_SERVER       | Run an indepedent [MAS](https://wiki.hashsploit.net/PlayStation_2#Medius_Authentication_Server_.28MAS.29) server.                                              |
| MEDIUS_LOBBY_SERVER                | Run an indepedent [MLS](https://wiki.hashsploit.net/PlayStation_2#Medius_Lobby_Server_.28MLS.29) server.                                              |
| DME_SERVER                         | Run an indepedent [DME server](https://wiki.hashsploit.net/PlayStation_2#DME_Game_Server_.28Distributed_Memory_Engine.29).                                              |
| NAT_SERVER                         | Run an indepedent [NAT server](https://wiki.hashsploit.net/PlayStation_2#Medius_Network_Address_Translation_.28NAT.29).                                              |
| MONOLITH                           | Run all the above components in a single process (useful for development/debugging purposes). |


### MAS configuration (config/mas.json.example)

The component is an authentication server for handling player logins.

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
| database                 | object  | See **Database Object** below or `null` to use simulated-db mode.              |
| system_message           | object  | See **System Message Object** below.                                           |


**MLS Object:**

| Name                     | Type    | Description                                                                      |
|--------------------------|---------|----------------------------------------------------------------------------------|
| address                  | string  | IPv4 address of the MLS server, or `null` to use the current public IPv4 Address.|
| port                     | integer | Port of the MLS server.                                                          |


**NAT Object:**

| Name                     | Type    | Description                                                                      |
|--------------------------|---------|----------------------------------------------------------------------------------|
| address                  | string  | IPv4 address of the NAT server, or `null` to use the current public IPv4 Address.|
| port                     | integer | Port of the NAT server.                                                          |


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







### MLS configuration (config/mls.json.example)

The component is an authentication server for handling player logins.

| Name                     | Type    | Description                                                                    |
|--------------------------|---------|--------------------------------------------------------------------------------|
| encryption               | boolean | Enabled if this server should enable and enforce/require SCE-RT encryption.    |
| capacity                 | integer | Maximum number of concurrent clients this server can support.                  |
| timeout                  | integer | Time in milliseconds of how long a client can idle not sending a echo heartbeat before being automatically disconnected.|
| parent_threads           | integer | Number of threads this server should use for accepting connections into the event-loop.|
| child_threads            | integer | Number of threads to use for processing requests in the event-loop.            |
| application_id           | integer | This is the App Id of the game to support.                                     |
| dme                      | object  | See **DME object** below or `null` if using MAS P2P.                           |
| nat                      | object  | See **NAT Object** above.                                                      |
| rpc_server               | object  | See **RPC Server Object** below.                                               |
| whitelist                | object  | See **Whitelist Object** above.                                                |
| policy                   | string  | The Policy/EULA to send back to the player.                                    |
| announcements            | array   | An array of *string* announcements to send to the player.                      |
| operators                | array   | An array of *string* usernames that will have server operator privileges.      |
| chat                     | object  | See **Chat Object** below.                                                     |
| channels                 | array   | See **Channels Array** below.                                                  |
| locations                | array   | See **Locations Array** below.                                                 |
| database                 | object  | See **Database Object** above or `null` to use simulated-db mode.              |
| system_message           | object  | See **System Message Object** above.                                           |


**DME Object:**

| Name                     | Type    | Description                                                                      |
|--------------------------|---------|----------------------------------------------------------------------------------|
| address                  | string  | IPv4 address of the MLS server, or `null` to use the current public IPv4 Address.|
| port                     | integer | Port of the MLS server.                                                          |


**RPC Server Object:**

| Name                     | Type    | Description                                                                      |
|--------------------------|---------|----------------------------------------------------------------------------------|
| address                  | string  | IPv4 address of the RPC server, or `null` to use the current public IPv4 Address.|
| port                     | integer | Port of the RPC server.                                                          |
| encryption               | object  | Details that must be filled out if you want to enable TLS over RPC.              |


**Chat Object:**

| Name                     | Type    | Description                                                                      |
|--------------------------|---------|----------------------------------------------------------------------------------|
| enabled                  | boolean | If chat should be enabled or disabled.                                           |
| blacklist                | string  | Refer to a text file that has a list of blacklisted words separated by new-lines.|
| command_prefix           | string  | Chat command prefix.                                                             |
| strip_special_chars      | boolean | If enabled, this will strip non ASCII characters from non-operators              |


**Channels Array:**

Each channel object must consist of the following fields:

| Name                     | Type    | Description                                                                      |
|--------------------------|---------|----------------------------------------------------------------------------------|
| id                       | integer | Numerical ID of the Medius Channel Id.                                           |
| name                     | string  | Name of the channel.                                                             |
| capacity                 | integer | Capacity of the channel. Valid values can be `1` to `256`.                       |


**Locations Array:**

Each location object must consist of the following fields:

| Name                     | Type    | Description                                                                      |
|--------------------------|---------|----------------------------------------------------------------------------------|
| id                       | integer | Numerical ID of the Medius Location Id.                                          |
| name                     | string  | Name of the Location.                                                            |


