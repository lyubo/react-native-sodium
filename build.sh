#!/bin/bash

sigfile=`ls -1 libsodium-*.tar.gz.sig`
srcfile=`basename $sigfile .sig`
srcdir=`basename $srcfile .tar.gz`

# --------------------------
# Download and verify source
# --------------------------
[ -f $srcfile ] || curl https://download.libsodium.org/libsodium/releases/$srcfile > $srcfile
gpg --no-default-keyring --keyring `pwd`/trusted.gpg --verify $sigfile $srcfile || exit 1


# --------------------------
# Extract sources
# --------------------------
[ -e $srcdir ] && rm -Rf $srcdir
tar -xzf $srcfile
cd $srcdir


# --------------------------
# iOS build
# --------------------------
dist-build/ios.sh


# --------------------------
# Android build
# --------------------------
dist-build/android-arm.sh
dist-build/android-armv7-a.sh
dist-build/android-armv8-a.sh
dist-build/android-build.sh
dist-build/android-mips32.sh
dist-build/android-mips64.sh
dist-build/android-x86.sh
dist-build/android-x86_64.sh
cd ..


# --------------------------
# Move compiled libraries
# --------------------------
mkdir -p libsodium
rm -Rf libsodium/*

mv $srcdir/libsodium-android-* libsodium/
mv $srcdir/libsodium-ios libsodium/


# --------------------------
# Cleanup
# --------------------------
rm -Rf $srcdir
rm  $srcfile
