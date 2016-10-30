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

NSString * const ESODIUM = @"ESODIUM";
NSString * const ERR_BAD_KEY = @"BAD_KEY";
NSString * const ERR_BAD_MAC = @"BAD_MAC";
NSString * const ERR_BAD_MSG = @"BAD_MSG";
NSString * const ERR_BAD_NONCE = @"BAD_NONCE";
NSString * const ERR_FAILURE = @"FAILURE";

RCT_EXPORT_MODULE();

+ (void) initialize
{
    [super initialize];
    isInitialized = sodium_init() != -1;
}


// *****************************************************************************
// * Sodium constants
// *****************************************************************************
- (NSDictionary *)constantsToExport
{
  return @{
    @"crypto_auth_KEYBYTES": @crypto_auth_KEYBYTES,
    @"crypto_auth_BYTES": @crypto_auth_BYTES,
    @"crypto_box_PUBLICKEYBYTES": @crypto_box_PUBLICKEYBYTES,
    @"crypto_box_SECRETKEYBYTES": @crypto_box_SECRETKEYBYTES,
    @"crypto_box_NONCEBYTES": @crypto_box_NONCEBYTES,
    @"crypto_box_MACBYTES": @crypto_box_MACBYTES,
    @"crypto_box_ZEROBYTES": @crypto_box_ZEROBYTES
  };
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
  unsigned char *buf = (unsigned char *) sodium_malloc((u_int32_t)size);
  if (buf == NULL)
    reject(ESODIUM,ERR_FAILURE,nil);
  else {
    randombytes_buf(buf,(u_int32_t)size);
    resolve([[NSData dataWithBytesNoCopy:buf length:size freeWhenDone:NO]  base64EncodedStringWithOptions:0]);
    sodium_free(buf);
  }
}

RCT_EXPORT_METHOD(randombytes_close:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
{
  int result = randombytes_close();
  if (result == 0) resolve(0); else reject(ESODIUM,ERR_FAILURE,nil);
}

RCT_EXPORT_METHOD(randombytes_stir:(RCTPromiseResolveBlock)resolve reject:(__unused RCTPromiseRejectBlock)reject)
{
  randombytes_stir();
  resolve(0);
}


// ***************************************************************************
// * Secret-key cryptography - authentication
// ***************************************************************************
RCT_EXPORT_METHOD(crypto_auth:(NSString*)in k:(NSString*)k resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
{
  unsigned char out[crypto_auth_BYTES];

  const NSData *din = [[NSData alloc] initWithBase64EncodedString:in options:0];
  const NSData *dk = [[NSData alloc] initWithBase64EncodedString:k options:0];
  if (!din || !dk) reject(ESODIUM,ERR_FAILURE,nil);
  else if (dk.length != crypto_auth_KEYBYTES) reject(ESODIUM,ERR_BAD_KEY,nil);
  else {
    crypto_auth(out, [din bytes], (unsigned long long) din.length, [dk bytes]);
    resolve([[NSData dataWithBytesNoCopy:out length:sizeof(out) freeWhenDone:NO]  base64EncodedStringWithOptions:0]);
  }
}

RCT_EXPORT_METHOD(crypto_auth_verify:(NSString*)h in:(NSString*)in k:(NSString*)k resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
{
  const NSData *dh = [[NSData alloc] initWithBase64EncodedString:h options:0];
  const NSData *din = [[NSData alloc] initWithBase64EncodedString:in options:0];
  const NSData *dk = [[NSData alloc] initWithBase64EncodedString:k options:0];
  if (!dh || !din || !dk) reject(ESODIUM,ERR_FAILURE,nil);
  else if (dk.length != crypto_auth_KEYBYTES) reject(ESODIUM,ERR_BAD_KEY,nil);
  else if (dh.length != crypto_auth_BYTES) reject(ESODIUM,ERR_BAD_MAC,nil);
  else {
    int result = crypto_auth_verify([dh bytes], [din bytes], (unsigned long long) din.length, [dk bytes]);
    resolve(@(result));
  }
}

// *****************************************************************************
// * Public-key cryptography - authenticated encryption
// *****************************************************************************
RCT_EXPORT_METHOD(crypto_box_keypair:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
{
  unsigned char pk[crypto_box_PUBLICKEYBYTES],sk[crypto_box_SECRETKEYBYTES];
  if ( crypto_box_keypair(pk,sk) == 0) {
    NSString *pk64 = [[NSData dataWithBytesNoCopy:pk length:sizeof(pk) freeWhenDone:NO]  base64EncodedStringWithOptions:0];
    NSString *sk64 = [[NSData dataWithBytesNoCopy:sk length:sizeof(sk) freeWhenDone:NO]  base64EncodedStringWithOptions:0];
    if (!pk64 || !sk64) reject(ESODIUM,ERR_FAILURE,nil); else resolve(@{@"pk":pk64, @"sk":sk64});
  }
  else
    reject(ESODIUM,ERR_FAILURE,nil);
}

RCT_EXPORT_METHOD(crypto_box_easy:(NSString*)m n:(NSString*)n pk:(NSString*)pk sk:(NSString*)sk resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
{
  const NSData *dm = [[NSData alloc] initWithBase64EncodedString:m options:0];
  const NSData *dn = [[NSData alloc] initWithBase64EncodedString:n options:0];
  const NSData *dpk = [[NSData alloc] initWithBase64EncodedString:pk options:0];
  const NSData *dsk = [[NSData alloc] initWithBase64EncodedString:sk options:0];
  if (!dm || !dn || !dpk || !dsk) reject(ESODIUM,ERR_FAILURE,nil);
  else {
    unsigned long clen = crypto_box_MACBYTES + dm.length;
    unsigned char *dc = (unsigned char *) sodium_malloc(clen);
    if (dc == NULL) reject(ESODIUM,ERR_FAILURE,nil);
    else {
      int result = crypto_box_easy(dc,[dm bytes], dm.length, [dn bytes], [dpk bytes], [dsk bytes]);
      if (result != 0)
        reject(ESODIUM,ERR_FAILURE,nil);
      else
        resolve([[NSData dataWithBytesNoCopy:dc length:clen freeWhenDone:NO]  base64EncodedStringWithOptions:0]);
      sodium_free(dc);
    }
  }
}

RCT_EXPORT_METHOD(crypto_box_open_easy:(NSString*)c n:(NSString*)n pk:(NSString*)pk sk:(NSString*)sk resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
{
  const NSData *dc = [[NSData alloc] initWithBase64EncodedString:c options:0];
  const NSData *dn = [[NSData alloc] initWithBase64EncodedString:n options:0];
  const NSData *dpk = [[NSData alloc] initWithBase64EncodedString:pk options:0];
  const NSData *dsk = [[NSData alloc] initWithBase64EncodedString:sk options:0];
  if (!dc || !dn || !dpk || !dsk) reject(ESODIUM,ERR_FAILURE,nil);
  else {
    unsigned long mlen = dc.length - crypto_box_MACBYTES;
    unsigned char *dm = (unsigned char *) sodium_malloc(mlen);
    if (dm == NULL) reject(ESODIUM,ERR_FAILURE,nil);
    else {
      int result = crypto_box_open_easy(dm, [dc bytes], dc.length, [dn bytes], [dpk bytes], [dsk bytes]);
      if (result != 0)
        reject(ESODIUM,ERR_FAILURE,nil);
      else
        resolve([[NSData dataWithBytesNoCopy:dm length:mlen freeWhenDone:NO]  base64EncodedStringWithOptions:0]);
      sodium_free(dm);
    }
  }
}

@end
