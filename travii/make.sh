mkdir release

# 1448
cd forge
git checkout b3a1548cfaac77e9756168a8887270f8181487ad
cd ..
git pull origin master
./gradlew --parallel -PofficialBuild clean setupCauldron jar
cp build/distributions/Thermos*server* release/

# 1492
cd forge
git checkout c308a47124394f025bc2ba553646ecf8b6c31ba2
cd ..
git pull origin master
./gradlew --parallel -PofficialBuild clean setupCauldron jar
cp build/distributions/Thermos*server* release/

# 1558
cd forge
git checkout aa8eaf2b286e809146b7faf4e59ce801a40eab9b
cd ..
git pull origin master
./gradlew --parallel -PofficialBuild clean setupCauldron jar
cp build/distributions/Thermos*server* release

# 1614
cd forge
git checkout c6aa04325e8a80af88755adb2339a402e7e90ebb
cd ..
git pull origin master
./gradlew --parallel -PofficialBuild clean setupCauldron jar packageBundle
cp build/distributions/Thermos*server* release
cd build/distributions/
unzip Thermos*bundle*zip
rm -r bin/unknown
mv bin libraries
zip -r libraries.zip libraries
cp libraries.zip ../../release/
