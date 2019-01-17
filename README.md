# react-native-sodium

Precompiled binaries of [libsodium](https://libsodium.org) will be linked by default.
Optionally, you can choose to compile libsodium by yourself (run __npm&nbsp;run&nbsp;rebuild__ in package directory). Source code will be downloaded and verified before compilation.

### Source compilation
###### General prerequisites
* gpg (macports, homebrew)

###### MacOS prerequisites
* libtool (macports, homebrew)
* autoconf (macports, homebrew)
* automake (macports, homebrew)


###### Android prerequisites
* Android NDK
* CMake
* LLDB

### Usage

1. npm install react-native-sodium
2. react-native link react-native-sodium
3. react-native run-ios or react-native run-android

### Help
See [example application](https://www.npmjs.com/package/react-native-sodium-example).
