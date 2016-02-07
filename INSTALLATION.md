#Installing Thermos

###Switching from KCauldron to Thermos
Your server setup requires some changes to work with Thermos:

1. You must delete your "libraries" folder and replace it with the one that comes in the [Thermos Server ZIP](https://github.com/TCPR/ThermosServer/archive/master.zip).
See below for a script that can automatically do this

2. Delete the "bin" folder, Thermos does not use it.

3. Run this command to get the latest Thermos version : `wget $(curl -s https://api.github.com/repos/TCPR/Thermos/releases/latest | grep 'jar' | cut -d\" -f4) `

4. Move the JAR to Thermos.jar

5. Edit your server launch script so that the Java line looks like this:
```bash
java -d64 -server -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:+UseStringDeduplication -Xms4G -Xmx4G -XX:hashCode=5 -Dfile.encoding=UTF-8 -jar Thermos.jar nogui
```

6. You're good to go!

##Linux Scripts

###First Install Script (creates directory "Thermos")
Use this script to install the latest version of Thermos!

```bash
wget https://github.com/TCPR/ThermosServer/archive/master.zip
mv ThermosServer-master Thermos
cd Thermos
wget $(curl -s https://api.github.com/repos/TCPR/Thermos/releases/latest | grep 'jar' | cut -d\" -f4) 
mv Thermos-1.7.10-1614.*-server-*.jar Thermos.jar
cd ..
```

###Updating Thermos Script (must be in your server directory)
With this, you can quickly download the latest version of Thermos and backup your current version!
```bash
wget $(curl -s https://api.github.com/repos/TCPR/Thermos/releases/latest | grep 'jar' | cut -d\" -f4) 
mv Thermos.jar Thermos_backup_`date +%b_%d_%Y_%T`.jar
mv Thermos-1.7.10-1614.*-server-*.jar Thermos.jar
```

