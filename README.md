# Cauldron 
### Continuation of KCauldron minecraft server

This github was created due to the previous developer for KCauldron being missing for weeks - sometimes months - at a time.  When the dev was missing his gitlab would go offline making it "feel" like the project was abandoned completely.

This repo was based on KCauldron 1492 Build 152.  Some extra patches were added and cherry picked in order to improve and fix up some of the small little flaws that were inherent in the 152 build.

## Downloads
You can download the pre-built packages from https://github.com/spannerman79/KCauldron/releases

## Build Requirements
* Java 8 JDK
* `JAVA_HOME` defined on your OS

## Building Cauldron
* Checkout project
  * You can use IDE or clone from console:
  `git clone https://github.com/spannerman79/KCauldron.git`
* Init submodules
  * Since this project uses two other projects we need to download them as well
  `git submodule update --init --recursive`
* Start build process
  * This process downloads minecraft and apply patches
  * If you have gradle integration in IDE - you can still use gui
  `./gradlew setupCauldron jar`

## Updating sources
If you're once checkout source - you not need to do it again
* Update sources
  * `git pull origin master`
* Re apply patches & build binaries
  * `./gradlew clean setupCauldron jar`