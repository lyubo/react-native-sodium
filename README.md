# react-native-soduim

Sodium library is build from source (you should not trust a binary build when dealing  with cryptography)
Source code is downloaded and verified before compilation

General prerequisites
gpg (macports, homebrew)

macOS compilation prerequisites
XCode
libtool (macports, homebrew)
autoconf (macports, homebrew)
automake (macports, homebrew)


Android prerequisites
Android Studio
SDK,
NDK,
CMake,
LLDB
Environment variables

npm install react-native-sodium@https://github.com/lyubo/react-native-sodium.git
react-native link react-native-sodium
react-native run-ios or react-native run-android
