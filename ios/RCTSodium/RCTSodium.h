//
//  RCTSodium.h
//
//  Created by Lyubomir Ivanov on 9/25/16.
//  Copyright © 2016 Lyubomir Ivanov. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface RCTSodium : NSObject <RCTBridgeModule>

- (void) sodium_version_string:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject;

- (void) randombytes_random:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject;
- (void) randombytes_uniform:(NSUInteger)upper_bound resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject;
- (void) randombytes_buf:(NSUInteger)size resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject;
- (void) randombytes_close;
- (void) randombytes_stir;

- (void) crypto_auth:(NSString*)in k:(NSString*)k resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject;
- (void) crypto_auth_verify:(NSString*)h in:(NSString*)in k:(NSString*)k resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject;

- (void) crypto_box_keypair:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject;

@end
