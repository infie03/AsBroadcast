
## Overview

AsBroadcast is a lightweight, versatile Minecraft plugin that enhances server communication with customizable broadcast messages in various formats.

| Feature | Description |
|---------|-------------|
| Multiple Message Types | Send messages as chat, title, subtitle, or actionbar |
| Combined Title/Subtitle | Send both title and subtitle simultaneously |
| Tab Completion | Easy command usage with intelligent suggestions |
| Version Compatibility | Works with Minecraft 1.18.x to 1.21.x |
| Color Support | Full support for legacy color codes (&) |
| Permission System | Granular control over command access |

## Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/as <type> <target> <time> <message>` | Send a broadcast message | asbroadcast.use |
| `/asadmin <time> <title> \|\| <subtitle>` | Send title and subtitle together | asbroadcast.admin |
| `/asreload` | Reload the plugin configuration | asbroadcast.reload |

### Message Types
- **chat**: Regular chat message
- **title**: Large centered text
- **subtitle**: Smaller text below title
- **actionbar**: Message above hotbar

### Time Format
Specify display duration for titles/subtitles/actionbar:
- `10s` - 10 seconds
- `1m` - 1 minute
- `2h` - 2 hours

## Configuration

```yaml
# AsBroadcast Configuration

# Debug mode - enables additional console logging
debug: false

# Enable tab completion for commands
tab-completion: true

# Enable welcome message for operators
welcome-message:
  enabled: true
  messages:
    - "&aWelcome to the server!"
    - "&7Use &e/as&7 to broadcast messages"

# Default display durations (milliseconds)
durations:
  default: 5000  # Default display time if not specified
  fade-in: 500   # Title fade in time
  fade-out: 500  # Title fade out time

# Messages
messages:
  errors:
    no-permission: "&cYou don't have permission to use this command."
    player-not-found: "&cPlayer not found: %player%"
    no-players: "&cNo players to send message to."
    invalid-type: "&cInvalid message type: %type%"
  success:
    message-sent: "&aMessage sent successfully!"
```

## Examples

```
/as chat all Hello everyone!
/as title all 10s &bWelcome to the server!
/as subtitle player1 5s &eEnjoy your stay!
/as actionbar all 3s &6Server restarting soon!
/asadmin 10s &bWelcome to the server! || &eEnjoy your stay!
```

## Compatibility

AsBroadcast is designed to work across multiple Minecraft versions with robust error handling:

| Version | Compatibility |
|---------|--------------|
| 1.18.x | ✅ Full support |
| 1.19.x | ✅ Full support |
| 1.20.x | ✅ Full support |
| 1.21.x | ✅ Full support |

## Installation

1. Download the latest release
2. Place the JAR file in your server's plugins folder
3. Restart your server or use `/reload`
4. Edit the configuration in `plugins/AsBroadcast/config.yml` if needed
5. Use `/asreload` to apply configuration changes

## Permissions

- `asbroadcast.use` - Access to the `/as` command
- `asbroadcast.admin` - Access to the `/asadmin` command
- `asbroadcast.reload` - Access to the `/asreload` command

## Support

For issues, feature requests, or questions, please open an in [Discord](https://discord.gg/6NXA8BWGbh).

---

*AsBroadcast - Simple and effective server communication*

##Note:
> AsBroadcast is open source for showcase purposes only. The code is available on [GitHub](https://github.com/infie03/AsBroadcast) to demonstrate my development skills and plugin design. While you're welcome to explore the code, full reuse, modification, or redistribution is not permitted without explicit permission.

If you're interested in collaboration or usage beyond viewing, feel free to reach out via [Discord](https://discord.gg/6NXA8BWGbh).
