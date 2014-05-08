<center>![Dice](http://www.easymfne.net/images/dice.png)</center>

<center>[Source](https://github.com/EasyMFnE/Dice) |
[Change Log](https://github.com/EasyMFnE/Dice/blob/master/CHANGES.log) |
[Feature Request](https://github.com/EasyMFnE/Dice/issues) |
[Bug Report](https://github.com/EasyMFnE/Dice/issues) |
[Donate](https://www.paypal.com/cgi-bin/webscr?hosted_button_id=457RX2KYUDY5G&item_name=Dice&cmd=_s-xclick)</center>

<center>**Latest Release:** v1.0 for Bukkit 1.7+</center>

## About ##

Dice is a very small Bukkit plugin designed to add an ability for players to roll dice, and is useful for role-playing.  Players can roll one die or many dice, with any number of sides.  The amount of players receiving dice-roll messages can be configured to match nearly any need.

## Features ##

* Players can roll dice (default and custom)
* Default numbers of dice and sides can be set in the configuration
* Result message can be global or limited by world/range

## Installation ##

1. Download Dice jar file.
2. Move/copy to your server's `plugins` folder.
3. Restart your server.
4. [**Optional**] Grant specific user permissions (see below).

## Permissions ##

* `dice.all` - Grant all permission nodes. (Default: `op`)
* `dice.reload` - Allow user to reload the plugin's configuration. (Default: `false`)
* `dice.roll` - Allow user to use the `/roll` command and default dice. (Default: `false`)
* `dice.roll.broadcast` - Broadcast the results of a player's rolls. (Default: `false`)
* `dice.roll.multiple` - Allow user to roll a custom number of dice. (Default: `false`)
* `dice.roll.multiple` - Allow user to roll custom-sided dice. (Default: `false`)

## Commands ##

Dice has only one command, `/roll`

* `/roll` - Roll the default dice
* `/roll <help,?>` - Show usage information
* `/roll reload` - Reload configuration from disk
* `/roll <count>` - Roll a custom number of default dice
* `/roll d<sides>` - Roll a default amount of custom-sided dice
* `/roll <count> d<sides>` - Roll custom number of custom-sided dice (parameter order does not matter)

## Configuration ##

At startup, the plugin will create a default configuration file if none exists.  This file is saved as `config.yml` and is located in `<server_folder>/plugins/Dice`.  Configuration nodes and expected values:

    default:
      sides: (integer >= 2)
      count: (integer >= 1)
    maximum:
      sides: (integer >= default.sides)
      count: (integer >= default.count)
    logging: (boolean, log rolls to console)
    broadcast:
      crossworld: (boolean, broadcast to all worlds)
      range: (integer > 0, broadcast range in blocks. -1 disables)
    messages:
      private: (String, sent to player if not broadcasting)
      broadcast: (String, sent to all players in range)

The `messages` configuration nodes will replace the following tags:

* `{PLAYER}` (The name of the player who rolled)
* `{RESULT}` (The result of the roll, as a list)
* `{COUNT}` (The number of dice rolled)
* `{SIDES}` (The number of sides per die)
* `{TOTAL}` (The total value of the roll)

## Bugs/Requests ##

This plugin is continually tested to ensure that it is performing correctly, but sometimes bugs can sneak in.  If you have found a bug with the plugin, or if you have a feature request, please [create an issue on Github](https://github.com/EasyMFnE/Dice/issues).

## Donations ##

Donating is a great way to thank the developer if you find the plugin useful for your server, and encourages work on more 100% free and open-source plugins.  If you would like to donate (any amount), there is an easily accessible link in the top right corner of this page.  Thank you!

## Privacy ##

This plugin utilizes Hidendra's **Plugin-Metrics** system.  You may opt out of this service by editing your configuration located in `plugins/Plugin Metrics`.  The following anonymous data is collected and sent to [mcstats.org](http://mcstats.org):

* A unique identifier
* The server's version of Java
* Whether the server is in online or offline mode
* The plugin's version
* The server's version
* The OS version, name, and architecture
* The number of CPU cores
* The number of online players
* The Metrics version

## License ##

This plugin is released as a free and open-source project under the [GNU General Public License version 3 (GPLv3)](http://www.gnu.org/copyleft/gpl.html).  To learn more about what this means, click that link or [read about it on Wikipedia](http://en.wikipedia.org/wiki/GNU_General_Public_License).
