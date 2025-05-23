# AsBroadcast

A simple Minecraft plugin that allows you to broadcast messages in various formats (chat, title, subtitle, actionbar) to players.

## Features

- Send messages to all players or specific players
- Support for different message types:
  - Chat messages
  - Title messages
  - Subtitle messages
  - Actionbar messages
- Combined title and subtitle messages with a single command
- Custom display duration
- Tab completion for easier command usage
- Support for Minecraft versions 1.18.x to 1.21.x
- Legacy color code support with '&' format
- Welcome message for server operators
- Fully customizable via config.yml

## Usage

### Basic Broadcast Command

The plugin adds a command `/as` with the following syntax:

```
/as <target> <type> <time> <message>
```

#### Parameters

- `target`: The target player(s) to receive the message
  - `all`: Send to all online players
  - `player_name`: Send to a specific player
- `type`: The message type
  - `chat`: Regular chat message
  - `title`: Title message (appears in the center of the screen)
  - `subtitle`: Subtitle message (appears below the title)
  - `actionbar`: Actionbar message (appears above the hotbar)
- `time`: The display duration
  - Format: `<number><unit>` where unit is:
    - `s`: Seconds (e.g., `10s`)
    - `m`: Minutes (e.g., `5m`)
    - `h`: Hours (e.g., `1h`)
  - Note: For chat messages, this parameter is ignored
- `message`: The message content (supports '&' color codes)

#### Examples

```
/as all chat 10s &aHello everyone!
/as all title 5s &b&lWelcome to the server!
/as all subtitle 5s &eEnjoy your stay!
/as all actionbar 10s &6Special event starting soon!
/as Steve chat 5s &cPrivate message to Steve
```

### Admin Broadcast Command

The plugin also provides an admin command `/asadmin` that allows sending both title and subtitle messages at the same time:

```
/asadmin <time> <title> || <subtitle>
```

#### Parameters

- `time`: The display duration (same format as the basic command)
- `title`: The title message (appears in the center of the screen)
- `subtitle`: The subtitle message (appears below the title)

Note: The title and subtitle must be separated by the `||` symbol.

#### Examples

```
/asadmin 10s &b&lWelcome to the server! || &eEnjoy your stay!
/asadmin 5s &c&lImportant Announcement || &7Please read the rules
/asadmin 30s &6&lSpecial Event || &dStarting in 5 minutes
```

### Reload Command

To reload the plugin configuration, use:

```
/asreload
```

## Configuration

The plugin is fully customizable through the `config.yml` file. Here are the available options:

### Messages

```yaml
messages:
  # Prefix for plugin messages
  prefix: "&8[&bAsBroadcast&8] &r"
  
  # Error messages
  errors:
    no-permission: "&cYou don't have permission to use this command."
    player-not-found: "&cPlayer not found: %player%"
    no-players: "&cNo players to send message to."
    invalid-type: "&cInvalid message type: %type%"
    usage: "&cUsage: /as <target> <type> <time> <message>"
  
  # Success messages
  success:
    message-sent: "&aMessage sent successfully!"
    config-reloaded: "&aConfiguration reloaded successfully!"
```

### Welcome Message

```yaml
# Welcome message for server operators (admins)
welcome-message:
  # Enable or disable the welcome message for ops
  enabled: true
```

### Command Settings

```yaml
command:
  # The command name (default: as)
  name: "as"
  
  # Command aliases
  aliases:
    - "broadcast"
    - "bc"
```

### Default Settings

```yaml
defaults:
  # Default time duration if not specified (in seconds)
  duration: 5
  
  # Default fade-in time for titles/subtitles (in milliseconds)
  fade-in: 500
  
  # Default fade-out time for titles/subtitles (in milliseconds)
  fade-out: 500
```

### Tab Completion

```yaml
tab-completion:
  # Enable or disable tab completion
  enabled: true
  
  # Time suggestions for tab completion
  time-suggestions:
    - "5s"
    - "10s"
    - "30s"
    - "1m"
    - "5m"
    - "10m"
```

### Debug Mode

```yaml
# Debug mode (prints additional information to console)
debug: false
```

## Color Codes

The plugin uses legacy color codes with the '&' symbol. Here are some examples:

- `&a` - Light green
- `&b` - Aqua
- `&c` - Red
- `&d` - Light purple
- `&e` - Yellow
- `&f` - White
- `&0` to `&9` - Various colors
- `&l` - Bold
- `&n` - Underline
- `&o` - Italic
- `&k` - Obfuscated/Magic
- `&r` - Reset

## Permissions

- `asbroadcast.use`: Permission to use the `/as` command
- `asbroadcast.admin`: Permission to use the `/asadmin` command
- `asbroadcast.reload`: Permission to use the `/asreload` command

## Installation

1. Download the latest release from the GitHub releases page
2. Place the JAR file in your server's `plugins` folder
3. Restart your server or use a plugin manager to load the plugin

## Building from Source

1. Clone the repository
2. Run `mvn clean package`
3. The compiled JAR will be in the `target` folder

## Requirements

- Java 17 or higher
- Minecraft server running Paper or compatible fork (1.18.x to 1.21.x) 