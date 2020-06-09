# react-native-sodium

Precompiled binaries of [libsodium](https://libsodium.org) will be linked by default.
Optionally, you can choose to compile libsodium by yourself (run __npm&nbsp;run&nbsp;rebuild__ in package directory). Source code will be downloaded and verified before compilation.

### Source compilation
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
2. npx react-native run-ios or npx react-native run-android

### Help
See [example application](https://github.com/lyubo/react-native-sodium-example).
