// @flow

import React, { Component } from 'react'
import {
  StyleSheet,
  Text,
  ScrollView,View,TouchableHighlight,NativeModules
} from 'react-native'

import Sodium from 'react-native-sodium'

class TestResult extends Component {
    render() {
      const text = (this.props.result == null) ? "?" :(this.props.result ? "Pass":"Fail")
      const style = {color:(this.props.result == null ? "black" : (this.props.result ? "green":"red"))}
      return (
        <View style={styles.testContainer}>
          <Text style={styles.testLabel}>{this.props.name}:</Text>
          <Text style={[styles.testResult,style]}>{text}</Text>
        </View>
      );
    }

  }

export default  class Example extends Component {

  state: {
    sodium_version_string: string,
    randombytes_random: number,
    randombytes_uniform: number,
    randombytes_buf: string,
    crypto_auth: number,
    crypto_auth_verify: number,
    crypto_box_keypair: {pk:string, sk: string},
   }

  constructor(props) {
    super(props)
    this.state = {
      sodium_version_string: "n/a",
      crypto_box_keypair:{},
      sodiumError:""}
  }


  _handleError(error) {
    console.log(error)
    this.setState({sodiumError: error})
  }

  _testAuth1() {
    const k = "SmVmZQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="
    const c = "d2hhdCBkbyB5YSB3YW50IGZvciBub3RoaW5nPw=="
    const ta = "Fkt6e/z4GeLjlfvnO1bgo4e9ZCIugx/WECcM1+olBVQ="

    this.setState({crypto_auth:null,crypto_auth_verify:null})

    Sodium.crypto_auth(c,k).then((a) => {
      this.setState({crypto_auth:(a === ta)})
      Sodium.crypto_auth_verify(ta,c,k)
        .then((r) => this.setState({crypto_auth_verify:(r == 0)}))
        .catch((error) => {
          this.setState({crypto_auth_verify:false})
          this._handleError(error)
        })
    }).catch((error) => {
      this.setState({crypto_auth_verify:false})
      this._handleError(error)
    })
  }

  _testSodium() {
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

    // Secret key - authentication
    this._testAuth1()

    // Public-key cryptography - authenticated encryption
    Sodium.crypto_box_keypair()
      .then(({pk,sk}) => this.setState({crypto_box_keypair:{pk,sk}}))
  }

  componentWillMount() {
     this._testSodium()
  }

  render() {
    return (
      <ScrollView style={{flex:1}}>
        <TouchableHighlight onPress={() => this._testSodium()}>
          <Text style={styles.welcome}>
            Salted React Native!
          </Text>
        </TouchableHighlight>
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
        <TestResult name = "crypto_auth" result={this.state.crypto_auth}/>
        <TestResult name = "crypto_auth_verify" result={this.state.crypto_auth_verify}/>
        <Text style={styles.instructions}>
          crypto_box_keypair: {"\n\t"}pk: {this.state.crypto_box_keypair.pk}{"\n\t"}sk: {this.state.crypto_box_keypair.sk}
        </Text>
      </ScrollView>
    )
  }

}

const styles = StyleSheet.create({
  container: {
    flex: 1,
   //justifyContent: 'center',
    //alignItems: 'center',
    backgroundColor: '#F5FCFF',
    padding:5
  },

  testContainer: {
    flex: 1,
    flexDirection:'row',
    padding:5
  },

  testLabel: {
    flex:4,
    textAlign: 'left',
    color: '#333333',
  },

  testResult: {
    flex:1,
    textAlign: 'center',
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
})
