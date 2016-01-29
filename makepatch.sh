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
	FILE=`java makepatch chop $2`
        echo "$FILE"
	CLEAN=`find eclipse/Clean/src/main/java | grep $FILE`
	CAULD=`find eclipse/cauldron/src/main/java | grep $FILE`
	PATCH=`find patches/ | grep -i $FILE`
	if [[ -z "$PATCH" ]]
	then
		PATCH="patches/$FILE.patch"
		mkdir -p `java makepatch dir $FILE`
		touch $PATCH
	fi
	echo "Diff patch of ${redf}$CLEAN${reset} and ${gref}$CAULD${reset} below."
	git diff --minimal --no-index --no-prefix $CLEAN $CAULD
elif [ $1 == "make" ]
then
	echo "${bluf}CREATING PATCH${reset}"
	FILE=`java makepatch chop $2`
	CLEAN=`find eclipse/Clean/src/main/java | grep $FILE`
	CAULD=`find eclipse/cauldron/src/main/java | grep $FILE`
	PATCH=`find patches/ | grep -i $FILE`
	if [[ -z "$PATCH" ]]
	then
		PATCH="patches/$FILE.patch"
		mkdir -p `java makepatch dir $FILE`
		touch $PATCH
	fi
	git diff --minimal --no-prefix --no-index  $CLEAN $CAULD > $PATCH
	java makepatch $PATCH
	echo "Diff of ${redf}$CLEAN${reset} and ${gref}$CAULD${reset} written to ${yelf}$PATCH${reset}"
else
	echo "${bluf}CREATING PATCH${reset}"
	FILE=`java makepatch chop $1`
	CLEAN=`find eclipse/Clean/src/main/java | grep $FILE`
	CAULD=`find eclipse/cauldron/src/main/java | grep $FILE`
	PATCH=`find patches/ | grep -i $FILE`
	if [[ -z "$PATCH" ]]
	then
		PATCH="patches/$FILE.patch"
		mkdir -p `java makepatch dir $FILE`
		touch $PATCH
	fi
	git diff --minimal --no-prefix  --no-index $CLEAN $CAULD > $PATCH
	java makepatch $PATCH
	echo "Diff of ${redf}$CLEAN${reset} and ${gref}$CAULD${reset} written to ${yelf}$PATCH${reset}"
fi
