#!/bin/bash

action=$1

set -e

relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root/../..

case $action in
	new )
		from_branch=$2
		new_branch=$3
		if [ $(git status -s | wc -l) != 0 ] 
		then
			echo 'aborting due to local modifications'
			git status
			exit 1
		fi
		git pull
		git checkout $from_branch
		git checkout -b $new_branch
		git push --set-upstream origin $new_branch
		;;
esac
