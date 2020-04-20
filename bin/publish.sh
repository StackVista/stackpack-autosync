#! /bin/bash

set -e

POSITIONAL=()
while [[ $# -gt 0 ]]
do
key="$1"

case $key in
    -min|--min-stackstate-version)
    MIN_STACKSTATE_VERSION="$2"
    shift # past argument
    shift # past value
    ;;
    -max|--max-stackstate-version)
    MAX_STACKSTATE_VERSION="$2"
    shift # past argument
    shift # past value
    ;;
    *)    # unknown option
    POSITIONAL+=("$1") # save it in an array for later
    shift # past argument
    ;;
esac
done
set -- "${POSITIONAL[@]}" # restore positional parameters

function comment {
  echo ">> $1"
}

function exitOnError {
  if [ $? -eq 0 ]; then
    comment "OK"
  else
    comment "FAILED"
    exit 1
  fi
}

if [ "$#" -ne 1 ]; then
    echo "Expected 1 argument:"
    echo "  publish.sh <publish_version>"
    exit -1
fi

stackpack=$PWD
publish_version=$1

version_file="${stackpack}/version.sbt"

comment "*******************************************************************"
comment "This script will create a new release for the stackpack-autosync from master."
comment "Attention!!!"
comment "Attention!!! Your local master will be replaced with the latest origin master."
comment "Attention!!!"
comment "*******************************************************************"


read -r -p "Are you sure? [y/N] " response
echo ""
if [[ "$response" =~ ^([yY][eE][sS]|[yY])+$ ]]
then
  comment "Making release ..."
else
  comment "Cancelled"
  exit 0
fi

comment "Stashing changes"
git stash
exitOnError

comment "Fetching changes"
git fetch --all
exitOnError

comment "Checking out master"
git checkout master
exitOnError

comment "Resetting master"
git reset --hard origin/master
exitOnError

comment "Bump to version $publish_version"
new_version="lastReleasedStackPackVersion := \"$publish_version\""
grep -q "lastReleasedStackPackVersion" $version_file &&
    sed -i'' -e "s/lastReleasedStackPackVersion.*/$new_version/" $version_file || echo -e "$new_version" >> $version_file

# set or replace the min supported stackstate version
if [ -n "$MIN_STACKSTATE_VERSION" ]; then
  comment "Setting minSupportedStackStateVersion to $MIN_STACKSTATE_VERSION"
  min_version="minSupportedStackStateVersion := \"$MIN_STACKSTATE_VERSION\""
  grep -q "minSupportedStackStateVersion" $version_file &&
    sed -i'' -e "s/minSupportedStackStateVersion.*/$min_version/" $version_file || echo -e "\n$min_version" >> $version_file
fi

# set or replace the max supported stackstate version
if [ -n "$MAX_STACKSTATE_VERSION" ]; then
  comment "Setting maxSupportedStackStateVersion to $MAX_STACKSTATE_VERSION"
  max_version="maxSupportedStackStateVersion := \"$MAX_STACKSTATE_VERSION\""
  grep -q "maxSupportedStackStateVersion" $version_file &&
    sed -i'' -e "s/maxSupportedStackStateVersion.*/$max_version/" $version_file || echo -e "\n$max_version" >> $version_file
fi

# clear out all empty lines
sed -i'' -e '/^$/d'  $version_file

git add "${version_file}"
git commit -m "$stackpack StackPack version $publish_version" --no-verify
exitOnError
comment "Committed new version $publish_version"


tag_name="$publish_version"
git tag "$tag_name"
exitOnError
comment "Tag $tag_name created"

comment "Push changes to master"
git push origin master
exitOnError

git push origin "$tag_name"
exitOnError

comment "Check release pipeline running on  https://github.com/StackVista/stackpack-autosync/actions"

comment "Be aware that the pipeline needs to be finished before the release is finished and can be used."
