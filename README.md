# SimplePushEvents Plugin

## Description
This Spigot plugin, for use with the latest Minecraft version, automatically sends notifications to a push service (ntfy.sh), when players join, leave, execute specific commands, do certain things or the server starts/shuts down.
This helps server owners and players stay informed about server activities in real-time.


## Ideas
If you have ideas for more events, open an issue.


## Java Version
Compatible with Minecraft 1.20.6 and requires Java 21.


## Features
- Notifies when the server starts up or shuts down.
- Sends push notifications when
    - a player joins or leaves the server.
    - a player unlocks an advancement.
    - a player creates a portal.
    - a player dies (including the death message).
    - executes `/spe [message content]` for custom notifications (with custom permissions).
- Change the message format or disable/enable them entirely.
- Easy integration with the ntfy.sh push service.
- Quick setup with minimal configuration required.
- Checks and notifies for specific player commands:
    - `/op`
    - `/deop`
    - `/ban`
    - `/banip`
    - `/pardon`
    - `/pardonip`
    - `/whitelist`


## Setup
To set up the SimplePushEvents plugin, follow these steps:

1. **Install the Plugin**: Download and place the SimplePushEvents plugin in your Spigot Minecraft server's plugins directory.

2. **Configure the Plugin**: On the first run, the plugin will generate a configuration file (`options.yml`) in the `plugins/simplepushevents` directory. Edit this file to customize your messages and settings.

3. **Download the ntfy.sh App**: Visit the [ntfy.sh](https://ntfy.sh/) website and download the ntfy app from the Play Store or App Store. The ntfy.sh service is completely free to use.

4. **Subscribe to the Push Channel**: Open the ntfy app and subscribe to the topic specified in the `donottouch.pushchannel` configuration setting. This will allow you to receive notifications sent by the plugin.

5. **Restart/Reload Server**: Restart or reload your Spigot Minecraft server to apply the configuration changes.

6. **Receive Notifications**: The plugin will now automatically send notifications according to your configured settings whenever players join, leave, or the server starts up or shuts down.


## Plugin Configuration
Before deploying the plugin, ensure you configure the following parameters in the `options.yml` file:

```yaml
donottouch:
  configexists: true
  pushchannel: 7fc647dd98
messages:
  general:
    title: 'Minecraft Server:'
    startup:
      status: true
      content: The server is online now!
    poweroff:
      status: true
      content: The server is shutting down!
    chatcommand:
      status: true
      content: '[%PLAYER%] wrote: %MESSAGE%'
      permission: spe.usechatcommand
  player:
    advancement:
      status: true
      content: 'The player %PLAYER% unlocked the advancement: %NAME%'
    join:
      status: true
      content: The player %PLAYER% joined!
    leave:
      status: true
      content: The player %PLAYER% left!
    count:
      status: true
      content: 'Players online right now: %ONLINE%/%MAX%'
    death:
      status: true
      content: 'The Player %PLAYER% died: %MESSAGE%'
    portalcreation:
      status: true
      content: The Player %PLAYER% created a portal at %LOCATION%
    command:
      disableall: false
      op:
        status: true
        content: The player %PLAYER% executed /op for %TARGET% !
      deop:
        status: true
        content: The player %PLAYER% executed /deop for %TARGET% !
      ban:
        status: true
        content: The player %PLAYER% executed /ban for %TARGET% !
      banip:
        status: true
        content: The player %PLAYER% executed /ban-ip for %TARGET% !
      pardon:
        status: true
        content: The player %PLAYER% executed /pardon for %TARGET% !
      pardonip:
        status: true
        content: The player %PLAYER% executed /pardon-ip for %TARGET% !
      whitelist:
        status: true
        content: 'The player %PLAYER% used a whitelist command: %CONTENT%'
```


## Note
Ensure the ntfy.sh push service is accessible from your server, as this plugin depends on it to send notifications. The service is free and only requires you to download the app and subscribe to the specific push channel topic.


## Disclaimer
This plugin is provided as-is without any warranty. The developer holds no responsibility for any issues or damages arising from its usage.


##### Enjoy real-time notifications with the SimplePushEvents plugin for your Spigot Minecraft server!     