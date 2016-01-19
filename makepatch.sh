#!/bin/sh
redf=$(tput setaf 1)
bluf=$(tput setaf 6)
gref=$(tput setaf 2)
yelf=$(tput setaf 3)
reset=$(tput op)
if [ -z $@ ]
then
	echo "${redf}Please provide a file to generate.${reset}"
elif [ $1 == "echo" ]
then
	echo "${bluf}CREATING PATCH${reset}"
	FILE=`basename $2`
	CLEAN=`find eclipse/Clean/src/main/java | grep $FILE`
	CAULD=`find eclipse/cauldron/src/main/java | grep $FILE`
	PATCH=`find patches/ | grep -i $FILE`
	git diff --no-index --text -w $CLEAN $CAULD
	echo "Diff patch of ${redf}$CLEAN${reset} and ${gref}$CAULD${reset} above."
elif [ $1 == "make" ]
then
	echo "${bluf}CREATING PATCH${reset}"
	FILE=`basename $2`
	CLEAN=`find eclipse/Clean/src/main/java | grep $FILE`
	CAULD=`find eclipse/cauldron/src/main/java | grep $FILE`
	PATCH=`find patches/ | grep -i $FILE`
	git diff --no-index --text -w $CLEAN $CAULD > $PATCH
	java makepatch.class $PATCH
	echo "Diff of ${redf}$CLEAN${reset} and ${gref}$CAULD${reset} written to ${yelf}$PATCH${reset}"
else
	echo "${bluf}CREATING PATCH${reset}"
	FILE=`basename $1`
	CLEAN=`find eclipse/Clean/src/main/java | grep $FILE`
	CAULD=`find eclipse/cauldron/src/main/java | grep $FILE`
	PATCH=`find patches/ | grep -i $FILE`
	git diff --no-prefix --no-index --text -w $CLEAN $CAULD > $PATCH
	java makepatch.class $PATCH
	echo "Diff of ${redf}$CLEAN${reset} and ${gref}$CAULD${reset} written to ${yelf}$PATCH${reset}"
fi
