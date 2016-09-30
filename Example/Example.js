import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  View,NativeModules
} from 'react-native';

import Sodium from 'react-native-sodium'

export default  class Example extends Component {
  render() {
    Sodium.sodium_version_string()
      .then((data) => console.log(data))
      .catch((error) => console.log(error))
    Sodium.crypto_box_keypair()
      .then(({publicKey,secretKey}) => console.log("pk ->",publicKey,"\nsk ->",secretKey))
      .catch((error) => console.log(error))
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to React Native!
        </Text>
        <Text style={styles.instructions}>
          To get started, edit index.android.js
        </Text>
        <Text style={styles.instructions}>
          Double tap R on your keyboard to reload,{'\n'}
          Shake or press menu button for dev menu
        </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
