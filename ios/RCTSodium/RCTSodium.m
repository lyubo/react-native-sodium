//
//  RCTSodium.m
//  RCTSodium
//
//  Created by Lyubomir Ivanov on 9/25/16.
//  Copyright © 2016 Lyubomir Ivanov. All rights reserved.
//
#import "RCTBridgeModule.h"
#import "RCTUtils.h"
#import "sodium.h"

#import "RCTSodium.h"

@implementation RCTSodium

static bool isInitialized;

const NSString *ERR_BAD_KEYSIZE = @"BAD_KEYSIZE";
const NSString *ERR_BAD_AUTHENTICATOR = @"BAD_AUTHENTICATOR";

RCT_EXPORT_MODULE();

+ (void) initialize
{
    [super initialize];
    isInitialized = sodium_init() != -1;
}


NSException* sodiumException(const NSString *message) {
  return [NSException exceptionWithName:message reason:@"" userInfo:nil];
}

// *****************************************************************************
// * Sodium-specific functions
// *****************************************************************************
RCT_EXPORT_METHOD(sodium_version_string:(RCTPromiseResolveBlock)resolve reject:(__unused RCTPromiseRejectBlock)reject)
{
    resolve(@(sodium_version_string()));
}


// *****************************************************************************
// * Random data generation
// *****************************************************************************
RCT_EXPORT_METHOD(randombytes_random:(RCTPromiseResolveBlock)resolve reject:(__unused RCTPromiseRejectBlock)reject)
{
    resolve(@(randombytes_random()));
}

RCT_EXPORT_METHOD(randombytes_uniform:(NSUInteger)upper_bound resolve:(RCTPromiseResolveBlock)resolve reject:(__unused RCTPromiseRejectBlock)reject)
{
    resolve(@(randombytes_uniform((uint32_t)upper_bound)));
}

RCT_EXPORT_METHOD(randombytes_buf:(NSUInteger)size resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
{
    unsigned char buf[(u_int32_t)size];
    randombytes_buf(buf,(u_int32_t)size);
    resolve([[NSData dataWithBytesNoCopy:buf length:sizeof(buf) freeWhenDone:NO]  base64EncodedStringWithOptions:0]);
}

RCT_EXPORT_METHOD(randombytes_close)
{
    randombytes_close();
}

RCT_EXPORT_METHOD(randombytes_stir)
{
    randombytes_stir();
}


// ***************************************************************************
// * Secret-key cryptography - authentication
// ***************************************************************************
RCT_EXPORT_METHOD(crypto_auth:(NSString*)in k:(NSString*)k resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
{
  unsigned char out[crypto_auth_BYTES];

  @try {
    const NSData *din = [[NSData alloc] initWithBase64EncodedString:in options:0];
    const NSData *dk = [[NSData alloc] initWithBase64EncodedString:k options:0];
    if (dk.length != crypto_auth_KEYBYTES) @throw sodiumException(ERR_BAD_KEYSIZE);
    crypto_auth(out, [din bytes], (unsigned long long) din.length, [dk bytes]);
    resolve([[NSData dataWithBytesNoCopy:out length:sizeof(out) freeWhenDone:NO]  base64EncodedStringWithOptions:0]);
  }
  @catch(NSException *e) {
    reject(RCTErrorUnspecified,e.name,RCTErrorWithMessage(e.reason));
  }
}

RCT_EXPORT_METHOD(crypto_auth_verify:(NSString*)h in:(NSString*)in k:(NSString*)k resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
{
  @try {
    const NSData *dh = [[NSData alloc] initWithBase64EncodedString:h options:0];
    const NSData *din = [[NSData alloc] initWithBase64EncodedString:in options:0];
    const NSData *dk = [[NSData alloc] initWithBase64EncodedString:k options:0];
    if (dk.length != crypto_auth_KEYBYTES) @throw sodiumException(ERR_BAD_KEYSIZE);
    if (dh.length != crypto_auth_BYTES) @throw sodiumException(ERR_BAD_AUTHENTICATOR);
    int result = crypto_auth_verify([dh bytes], [din bytes], (unsigned long long) din.length, [dk bytes]);
    resolve(@(result));
  }
  @catch(NSException *e) {
    reject(RCTErrorUnspecified,e.name,RCTErrorWithMessage(e.reason));
  }
}

// *****************************************************************************
// * Public-key cryptography - authenticated encryption
// *****************************************************************************
RCT_EXPORT_METHOD(crypto_box_keypair:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
{
    unsigned char pk[crypto_box_PUBLICKEYBYTES],sk[crypto_box_PUBLICKEYBYTES];
    int result = crypto_box_keypair(pk,sk);
    if ( result == 0) {
        NSString *pk64 = [[NSData dataWithBytesNoCopy:pk length:sizeof(pk) freeWhenDone:NO]  base64EncodedStringWithOptions:0];
        NSString *sk64 = [[NSData dataWithBytesNoCopy:sk length:sizeof(sk) freeWhenDone:NO]  base64EncodedStringWithOptions:0];
        resolve(@{@"pk":pk64, @"sk":sk64});
    }
    else
        reject(RCTErrorUnspecified, nil, RCTErrorWithMessage(@"Error")); //TODO: return result
}

@end
