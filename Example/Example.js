import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  View,NativeModules
} from 'react-native';

import Sodium from 'react-native-sodium'

export default  class Example extends Component {

  state: {
    sodiumVersion: string,
    sodiumError: string
   }

  constructor(props) {
    super(props);
    this.state = {sodiumVersion: "n/a", sodiumError:""}
  }

  componentWillMount() {
    Sodium.sodium_version_string()
      .then((data) => this.setState({sodiumVersion: data}))
      .catch((error) => this.setState({sodiumError: error}))
  }

  render() {
    // Sodium.crypto_box_keypair()
    //   .then(({publicKey,secretKey}) => console.log("pk ->",publicKey,"\nsk ->",secretKey))
    //   .catch((error) => console.log(error))
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to React Native!
        </Text>
        <Text style={styles.instructions}>
          Sodium Version: {this.state.sodiumVersion}
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
