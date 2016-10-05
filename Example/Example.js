// @flow

import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  ScrollView,View,NativeModules
} from 'react-native';

import Sodium from 'react-native-sodium'

export default  class Example extends Component {

  state: {
    sodium_version_string: string,
    randombytes_random: number,
    randombytes_uniform: number,
    randombytes_buf: string,
    crypto_box_keypair: {pk:string, sk: string},
    sodiumError: string
   }

  constructor(props) {
    super(props);
    this.state = {
      sodium_version_string: "n/a",
      crypto_box_keypair:{},
      sodiumError:""}
  }

  componentWillMount() {
    Sodium.sodium_version_string()
      .then((version) => this.setState({sodium_version_string: version}))
      .catch((error) => this.setState({sodiumError: error}))

    // Random data generation
    Sodium.randombytes_random()
      .then((value) => this.setState({randombytes_random: value}))

    Sodium.randombytes_uniform(10)
      .then((value) => this.setState({randombytes_uniform:value}))

    Sodium.randombytes_buf(10)
      .then((value) => this.setState({randombytes_buf:value}))

    Sodium.randombytes_close()
    Sodium.randombytes_stir()

    // Public-key cryptography - authenticated encryption
    Sodium.crypto_box_keypair()
      .then(({pk,sk}) => this.setState({crypto_box_keypair:{pk,sk}}))

  }

  render() {
    return (
      <ScrollView style={styles.container}>
        <Text style={styles.welcome}>
          Salted React Native!
        </Text>
        <Text style={styles.instructions}>
          sodium_version_string: {this.state.sodium_version_string}
        </Text>
        <Text style={styles.instructions}>
          randombytes_random: {this.state.randombytes_random}
        </Text>
        <Text style={styles.instructions}>
          randombytes_uniform: {this.state.randombytes_uniform}
        </Text>
        <Text style={styles.instructions}>
          randombytes_buf: {this.state.randombytes_buf}
        </Text>
        <Text style={styles.instructions}>
          crypto_box_keypair: {"\n\tpk:" + this.state.crypto_box_keypair.pk + "\n\tsk:" + this.state.crypto_box_keypair.sk}
        </Text>
      </ScrollView>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
//    justifyContent: 'center',
//    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'left',
    color: '#333333',
    marginBottom: 5,
  },
});
