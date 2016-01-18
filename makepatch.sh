#!/bin/sh
redf=$(tput setaf 1)
bluf=$(tput setaf 6)
gref=$(tput setaf 2)
yelf=$(tput setaf 3)
reset=$(tput op)
echo "${bluf}CREATING PATCH${reset}"
FILE=`basename $@`
CLEAN=`find eclipse/Clean/src/main/java | grep $FILE`
CAULD=`find eclipse/cauldron/src/main/java | grep $FILE`
PATCH=`find patches/ | grep -i $FILE`
git diff --no-index $CLEAN $CAULD > $PATCH
echo "Diff of ${redf}$CLEAN${reset} and ${gref}$CAULD${reset} written to ${yelf}$PATCH${reset}"


