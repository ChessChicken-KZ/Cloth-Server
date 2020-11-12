## Cloth-Server
A Modded server  for  minecraft alpha 1.2.6 back-porting modern minecraft server features to alpha 1.2.6

#### Features
 - give command now takes string names like `stone` instead of numeric ids like `1`
 - giveid command exists to take ids if you desire
 - tpcord command teleports you to a given set of cordinates
 - time set command from modern minecraft
 - Seed support! -  server.properties has a `seed` property that  takes a number value
 - seed command which tells you the world seed (random if no seed set in server.properties)
 - Other misc server.properties params that let you mess with world gen (see added server parameters section)
#### Roadmap
 - plugin support of some  kind - with docs
 - MOTD message when a player joins
 - expose noise gen params to settings file, watch chaos ensue
 - blockmappings.properties to let you customize string name mappings
 - death messages
 - more fine control over crop trampling
#### Added Server Parameters
 - seed - takes integer, governs world seed
 - OctaveMultiplierA - int, Unknown effects on noise octaves
 - OctaveMultiplierB - int, Unknown effects on noise octaves
 - OctaveMultiplierC - int, Unknown effects on noise octaves
 
 
#### How To Build
 - clone github repository
 - open repository as Intellij Idea(tm) project
 - run the `MinecraftServer` configuration
 - enjoy!

## Why?

Why not?
